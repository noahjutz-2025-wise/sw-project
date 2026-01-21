package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import com.swdev.springbootproject.repository.PostRepository;
import com.swdev.springbootproject.service.CriticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasAuthority('admin')")
public class AdminDashboardController {
  private final CriticService criticService;
  private final PostRepository postRepository;
  private final CbUserRepository cbUserRepository;
  private final CertifiedBangerRepository certifiedBangerRepository;

  @GetMapping
  public String showAdminDashboard(Model model) {
    long countPosts = postRepository.count();
    long countUsers = cbUserRepository.count();
    long countCertifiedBangers = certifiedBangerRepository.count();

    model.addAttribute("users", criticService.findAllCbUsers());
    model.addAttribute("countPosts", countPosts);
    model.addAttribute("countUsers", countUsers);
    model.addAttribute("countCertifiedBangers", countCertifiedBangers);
    model.addAttribute("crud", "read");

    return "admin/dashboard";
  }

  /*REVIEW: These following code lines are DANGEROUS when banning users, because every other features relating
  those users could break at runtime, so use as your will

  @GetMapping("/{id}/ban")
  public String getBanConfirmation(@PathVariable Long id, Model model) {
    model.addAttribute("id", id);
    model.addAttribute("crud", "delete");
    return "admin/dashboard";
  }

  @DeleteMapping("/{id}/ban")
  public String banUser(@PathVariable Long id) {
    cbUserRepository.findById(id).ifPresent(cbUserRepository::delete);

    return "redirect:/admin/dashboard";
  }
  */
}
