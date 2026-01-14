package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CertifiedBanger;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/certified")
@PreAuthorize("hasAuthority('admin')")
public class CertifiedBangerController {
  private final CertifiedBangerRepository certifiedBangerRepository;

  @PostMapping("/add/{movieId}")
  public String mark(@PathVariable Long movieId, Model model) {
    if (!certifiedBangerRepository.existsById(movieId)) {
      certifiedBangerRepository.save(new CertifiedBanger(movieId));
    }

    model.addAttribute("movieId", movieId);
    model.addAttribute("certified", true);
    return "fragments/certify-btn :: certifyBtn";
  }

  @DeleteMapping("/remove/{movieId}")
  public String unmark(@PathVariable Long movieId, Model model) {
    if (certifiedBangerRepository.existsById(movieId)) {
      certifiedBangerRepository.deleteById(movieId);
    }

    model.addAttribute("movieId", movieId);
    model.addAttribute("certified", false);
    return "fragments/certify-btn :: certifyBtn";
  }
}
