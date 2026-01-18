package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbMovieToMediaDtoConverter;
import com.swdev.springbootproject.entity.BookmarkStatus;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Friendship;
import com.swdev.springbootproject.model.Friend;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.FriendshipRepository;
import com.swdev.springbootproject.repository.MovieBookmarkRepository;
import com.swdev.springbootproject.service.CertifiedBangerService;
import com.swdev.springbootproject.service.EmailService;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfileController {

  private final FriendshipRepository friendshipRepository;
  private final CbUserRepository cbUserRepository;
  private final EmailService emailService;
  private final MovieBookmarkRepository movieBookmarkRepository;
  private final TMDBService tmdbService;
  private final TmdbMovieToMediaDtoConverter tmdbMovieToMediaDtoConverter;
  private final CertifiedBangerService certifiedBangerService;

  @GetMapping("/user/profile")
  public String profile(Model model, Authentication authentication) {

    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    CbUser updatedUser =
        cbUserRepository
            .findById(currentCbUser.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    model.addAttribute("name", updatedUser.getName());
    model.addAttribute("email", updatedUser.getEmail());
    model.addAttribute("verified", updatedUser.isVerified());

    List<Friendship> friendships =
        friendshipRepository.findBySender_IdOrReceiver_Id(
            currentCbUser.getId(), currentCbUser.getId());

    List<Friendship> receivedRequests =
        friendships.stream()
            .filter(
                friendship ->
                    !friendship.isAccepted()
                        && friendship.getReceiver().getId().equals(currentCbUser.getId()))
            .toList();

    List<Friendship> pendingRequests =
        friendships.stream()
            .filter(
                friendship ->
                    !friendship.isAccepted()
                        && friendship.getSender().getId().equals(currentCbUser.getId()))
            .toList();

    List<Friendship> acceptedRequests =
        friendships.stream().filter(Friendship::isAccepted).toList();

    List<Friend> friendsMapped =
        acceptedRequests.stream()
            .map(
                f -> {
                  CbUser friend =
                      f.getSender().getId().equals(currentCbUser.getId())
                          ? f.getReceiver()
                          : f.getSender();
                  return Friend.builder().friendshipId(f.getId()).friend(friend).build();
                })
            .toList();

    model.addAttribute("friends", friendsMapped);
    model.addAttribute("receivedRequests", receivedRequests);
    model.addAttribute("pendingRequests", pendingRequests);
    return "user/profile";
  }

  @GetMapping("/user/profile/edit")
  public String editProfile(Model model, Authentication authentication) {
    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    CbUser updatedUser =
        cbUserRepository
            .findById(currentCbUser.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    model.addAttribute("user", updatedUser);
    return "user/profile-edit";
  }

  @PostMapping("/user/profile/edit")
  public String updateProfile(
      @RequestParam("name") String name,
      @RequestParam("email") String email,
      Authentication authentication,
      RedirectAttributes redirectAttributes) {
    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    if (email.isBlank() || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    CbUser cbUser =
        cbUserRepository
            .findById(currentCbUser.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

    String oldEmail = cbUser.getEmail();
    boolean emailUpdated = !oldEmail.equals(email);

    cbUser.setName(name.trim());

    if (emailUpdated) {
      cbUser.setEmail(email.trim());
      cbUser.setVerified(false);
      emailService.sendVerificationEmail(cbUser);
      redirectAttributes.addFlashAttribute("success", "Profil erfolgreich aktualisiert!");
    }

    cbUserRepository.save(cbUser);
    redirectAttributes.addFlashAttribute("success", "Profil erfolgreich aktualisiert!");
    return "redirect:/user/profile";
  }

  @GetMapping("/user/{userId}/watchlist")
  public String getFriendWatchlist(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "WATCH_LATER") BookmarkStatus bookmarkStatus,
      Model model,
      Authentication authentication) {
    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    CbUser friendCbUser =
        cbUserRepository
            .findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if (friendshipRepository
            .findBySender_IdAndReceiver_Id(currentCbUser.getId(), friendCbUser.getId())
            .filter(Friendship::isAccepted)
            .isEmpty()
        && friendshipRepository
            .findBySender_IdAndReceiver_Id(friendCbUser.getId(), currentCbUser.getId())
            .filter(Friendship::isAccepted)
            .isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    List<BookmarkStatus> watchlistBookmarks =
        List.of(BookmarkStatus.WATCH_LATER, BookmarkStatus.IN_PROGRESS, BookmarkStatus.DONE);

    if (!watchlistBookmarks.contains(bookmarkStatus)) {
      bookmarkStatus = BookmarkStatus.WATCH_LATER;
    }

    List<MediaDto> watchlistMovies =
        movieBookmarkRepository.findByUserAndStatus(friendCbUser, bookmarkStatus).stream()
            .map(bookmark -> tmdbService.getMovieDetails(bookmark.getMovie().getId()))
            .filter(Objects::nonNull)
            .map(tmdbMovieToMediaDtoConverter::convert)
            .toList();

    certifiedBangerService.applyCertifiedBangerFlag(watchlistMovies);
    model.addAttribute("friend", friendCbUser);
    model.addAttribute("movies", watchlistMovies);
    model.addAttribute("bookmarkStatus", bookmarkStatus);
    return "user/friend-watchlist";
  }
}
