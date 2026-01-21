package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.CertifiedBanger;
import com.swdev.springbootproject.entity.CertifyMovieRequest;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import com.swdev.springbootproject.repository.CertifyMovieRequestRepository;
import com.swdev.springbootproject.service.CriticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/critics")
@PreAuthorize("hasAuthority('admin')")
public class CriticController {
  private final CriticService criticService;
  private final CertifyMovieRequestRepository certifyMovieRequestRepository;
  private final CertifiedBangerRepository certifiedBangerRepository;

  @GetMapping
  public String getUsers(Model model) {
    model.addAttribute("users", criticService.findAllCbUsers());
    model.addAttribute(
        "pendingRequests",
        certifyMovieRequestRepository.findByStatusOrderByIdDesc(
            CertifyMovieRequest.RequestStatus.PENDING));
    return "admin/critics";
  }

  @PostMapping("/{userId}/update")
  public String certifyCritic(
      @PathVariable Long userId, @RequestParam boolean certification, Model model) {
    CbUser cbUserUpdated = criticService.updateCertifiedCriticStatus(userId, certification);
    model.addAttribute("user", cbUserUpdated);
    return "fragments/critic-user-row :: userRow(user=${user})";
  }

  @PostMapping("/requests/{requestId}/approve")
  public String approveCriticRequest(@PathVariable Long requestId, Model model) {
    CertifyMovieRequest certifyMovieRequest =
        certifyMovieRequestRepository.findById(requestId).orElseThrow();
    certifyMovieRequest.setStatus(CertifyMovieRequest.RequestStatus.APPROVED);
    var request = certifyMovieRequestRepository.save(certifyMovieRequest);
    if (!certifiedBangerRepository.existsById(request.getMovieId())) {
      certifiedBangerRepository.save(new CertifiedBanger(request.getMovieId()));
    }

    model.addAttribute(
        "pendingRequests",
        certifyMovieRequestRepository.findByStatusOrderByIdDesc(
            CertifyMovieRequest.RequestStatus.PENDING));
    return "fragments/certification-requests :: requestsCard";
  }

  @PostMapping("/requests/{requestId}/reject")
  public String rejectCriticRequest(@PathVariable Long requestId, Model model) {
    CertifyMovieRequest certifyMovieRequest =
        certifyMovieRequestRepository.findById(requestId).orElseThrow();
    certifyMovieRequest.setStatus(CertifyMovieRequest.RequestStatus.REJECTED);
    certifyMovieRequestRepository.save(certifyMovieRequest);

    model.addAttribute(
        "pendingRequests",
        certifyMovieRequestRepository.findByStatusOrderByIdDesc(
            CertifyMovieRequest.RequestStatus.PENDING));
    return "fragments/certification-requests :: requestsCard";
  }
}
