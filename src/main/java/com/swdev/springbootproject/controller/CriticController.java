package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.service.CriticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/critics")
@PreAuthorize("hasAuthority('admin')")
public class CriticController {
  private final CriticService criticService;

  @GetMapping
  public String getUsers(Model model) {
    model.addAttribute("users", criticService.findAllCbUsers());
    return "admin/critics";
  }

  @PostMapping("/{userId}/update")
  public String certifyCritic(@PathVariable Long userId, @RequestParam boolean certification, Model model) {
    CbUser cbUserUpdated = criticService.updateCertifiedCriticStatus(userId, certification);
    model.addAttribute("user", cbUserUpdated);
    return "fragments/critic-user-row :: userRow(user=${user})";
  }
}
