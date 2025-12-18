package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Friendship;
import com.swdev.springbootproject.repository.FriendshipRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

  private final FriendshipRepository friendshipRepository;

  @GetMapping("/user/profile")
  public String profile(Model model, Authentication authentication) {

    CbUser currentCbUser = (CbUser) authentication.getPrincipal();
    assert currentCbUser != null;
    model.addAttribute("name", currentCbUser.getName());
    model.addAttribute("email", currentCbUser.getEmail());

    List<Friendship> friendships =
        friendshipRepository.findByCbUser1_IdOrCbUser2_Id(
            currentCbUser.getId(), currentCbUser.getId());
    List<CbUser> friendsMapped =
        friendships.stream()
            .map(
                f ->
                    f.getCbUser1().getId().equals(currentCbUser.getId())
                        ? f.getCbUser2()
                        : f.getCbUser1())
            .toList();

    model.addAttribute("friends", friendsMapped);

    return "/user/profile";
  }
}
