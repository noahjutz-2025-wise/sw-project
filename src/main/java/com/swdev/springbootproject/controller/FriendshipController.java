package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Friendship;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.FriendshipRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class FriendshipController {

  private final CbUserRepository cbUserRepository;
  private final FriendshipRepository friendshipRepository;

  @GetMapping("/friends/search")
  public String searchFriends(Model model) {

    model.addAttribute("searchEmail", "");
    model.addAttribute("searchResult", List.of());
    return "user/friend-search";
  }

  @PostMapping("/friends/search")
  public String searchFriends(
      @RequestParam("searchEmail") String searchEmail, Model model, Authentication authentication) {

    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    List<CbUser> searchResults = new ArrayList<>();

    if (!searchEmail.isEmpty()) {
      searchResults.addAll(
          cbUserRepository.findByEmailContainingIgnoreCase(searchEmail.trim()).stream()
              .filter(
                  cbUser -> {
                    if (!cbUser.getId().equals(currentCbUser.getId())) {
                      return !friendshipRepository.existsBySender_IdAndReceiver_Id(
                              cbUser.getId(), currentCbUser.getId())
                          && !friendshipRepository.existsBySender_IdAndReceiver_Id(
                              currentCbUser.getId(), cbUser.getId());
                    }
                    return false;
                  })
              .toList());
    }
    model.addAttribute("searchEmail", searchEmail);
    model.addAttribute("searchResults", searchResults);
    return "user/friend-search";
  }

  @PostMapping("/friends/add/{friendId}")
  public ResponseEntity<@NonNull String> addFriend(
      @PathVariable Long friendId,
      Authentication authentication,
      RedirectAttributes redirectAttributes) {
    HttpHeaders headers = new HttpHeaders();
    CbUser currentCbUser = (CbUser) authentication.getPrincipal();

    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    if (currentCbUser.getId().equals(friendId)) {
      return ResponseEntity.badRequest().build();
    }

    boolean befriended =
        friendshipRepository
            .findBySender_IdOrReceiver_Id(currentCbUser.getId(), currentCbUser.getId())
            .stream()
            .anyMatch(
                f ->
                    f.getSender().getId().equals(friendId)
                        || f.getReceiver().getId().equals(friendId));

    if (befriended) {
      return ResponseEntity.badRequest().build();
    }

    Optional<@NonNull CbUser> cbUser = cbUserRepository.findById(friendId);
    if (cbUser.isPresent()) {
      friendshipRepository.save(
          Friendship.builder().sender(currentCbUser).receiver(cbUser.get()).build());
    } else {
      return ResponseEntity.badRequest().build();
    }
    redirectAttributes.addFlashAttribute("message", "Freundschaftsanfrage gesendet!");
    headers.add("Location", "/user/profile");
    return new ResponseEntity<@NonNull String>(headers, HttpStatus.FOUND);
  }

  @PostMapping("/friends/accept/{friendshipId}")
  public String acceptFriendship(
      @PathVariable Long friendshipId,
      Authentication authentication,
      RedirectAttributes redirectAttributes) {
    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);

    if (friendship.isPresent()) {
      Friendship f = friendship.get();

      if (f.getReceiver().getId().equals(currentCbUser.getId()) && !f.isAccepted()) {
        f.setAccepted(true);
        friendshipRepository.save(f);
        redirectAttributes.addFlashAttribute("message", "Freundschaftsanfrage akzeptiert!");
      }
    }
    return "redirect:/user/profile";
  }

  @DeleteMapping("/friends/decline/{friendshipId}")
  public ResponseEntity<@NonNull Void> declineFriendship(
      @PathVariable Long friendshipId, Authentication authentication) {

    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
    if (friendship.isPresent()) {
      Friendship f = friendship.get();

      if (f.getReceiver().getId().equals(currentCbUser.getId()) && !f.isAccepted()) {
        friendshipRepository.delete(f);
        return ResponseEntity.ok().build();
      }
    }

    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/friends/cancel/{friendshipId}")
  public ResponseEntity<@NonNull Void> cancelFriendship(
      @PathVariable Long friendshipId, Authentication authentication) {

    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
    if (friendship.isPresent()) {
      Friendship f = friendship.get();

      if (f.getSender().getId().equals(currentCbUser.getId()) && !f.isAccepted()) {
        friendshipRepository.delete(f);
        return ResponseEntity.ok().build();
      }
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/friends/delete/{friendshipId}")
  public ResponseEntity<@NonNull Void> deleteFriendship(
      @PathVariable Long friendshipId, Authentication authentication) {

    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    if (currentCbUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);

    if (friendship.isPresent()) {
      Friendship f = friendship.get();
      if (f.isAccepted()
          && (f.getSender().getId().equals(currentCbUser.getId())
              || f.getReceiver().getId().equals(currentCbUser.getId()))) {
        friendshipRepository.delete(f);
        return ResponseEntity.ok().build();
      }
    }
    return ResponseEntity.ok().build();
  }
}
