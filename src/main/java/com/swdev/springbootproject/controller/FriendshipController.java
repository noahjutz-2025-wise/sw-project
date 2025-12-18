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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    assert currentCbUser != null;
    List<CbUser> searchResults = new ArrayList<>();

    if (!searchEmail.isEmpty()) {
      searchResults.addAll(
          cbUserRepository.findByEmailContainingIgnoreCase(searchEmail.trim()).stream()
              .filter(
                  cbUser -> {
                    if (!cbUser.getId().equals(currentCbUser.getId())) {
                      return friendshipRepository
                          .findByCbUser1_IdOrCbUser2_Id(
                              currentCbUser.getId(), currentCbUser.getId())
                          .stream()
                          .noneMatch(
                              f ->
                                  f.getCbUser1().getId().equals(currentCbUser.getId())
                                      || f.getCbUser2().getId().equals(currentCbUser.getId()));
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
  public ResponseEntity<@NonNull String> addFriend(@PathVariable Long friendId, Authentication authentication, RedirectAttributes redirectAttributes) {
    HttpHeaders headers = new HttpHeaders();
    CbUser currentCbUser = (CbUser) authentication.getPrincipal();

    assert currentCbUser != null;

    if (currentCbUser.getId().equals(friendId)) {
      return ResponseEntity.badRequest().build();
    }

    boolean befriended = friendshipRepository.findByCbUser1_IdOrCbUser2_Id(currentCbUser.getId(), currentCbUser.getId())
        .stream()
        .anyMatch(f ->
            f.getCbUser1().getId().equals(friendId) || f.getCbUser2().getId().equals(friendId));

    if (befriended) {
      return ResponseEntity.badRequest().build();
    }

    Optional<@NonNull CbUser> cbUser = cbUserRepository.findById(friendId);
    if (cbUser.isPresent()) {
      friendshipRepository.save(Friendship.builder().cbUser1(currentCbUser).cbUser2(cbUser.get()).build());
    } else {
      return ResponseEntity.badRequest().build();
    }
    redirectAttributes.addFlashAttribute("message", "Freund erfolgreich hinzugefügt!");
    headers.add("Location", "/user/profile");
    return new ResponseEntity<@NonNull String>(headers, HttpStatus.FOUND);
  }
}
