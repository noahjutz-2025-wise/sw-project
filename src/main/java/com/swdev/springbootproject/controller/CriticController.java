package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.CertifiedBanger;
import com.swdev.springbootproject.entity.CertifyMovieRequest;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import com.swdev.springbootproject.repository.CertifyMovieRequestRepository;
import com.swdev.springbootproject.service.CriticService;
import com.swdev.springbootproject.service.TMDBService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final TMDBService tmdbService;

  @GetMapping
  public String getUsers(Model model) {
    model.addAttribute("users", criticService.findAllCbUsers());
    List<CertifyMovieRequest> pendingRequests =
        certifyMovieRequestRepository.findByStatusOrderByIdDesc(
            CertifyMovieRequest.RequestStatus.PENDING);
    model.addAttribute(
        "pendingRequests",
        certifyMovieRequestRepository.findByStatusOrderByIdDesc(
            CertifyMovieRequest.RequestStatus.PENDING));
    addMovieNames(pendingRequests, model);
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

  private void addMovieNames(List<CertifyMovieRequest> requests, Model model) {
    Map<Long, String> movieNames = new HashMap<>();
    for (CertifyMovieRequest request : requests) {
      try {
        TmdbMovie movie = tmdbService.getMovieDetails(request.getMovieId());
        movieNames.put(request.getMovieId(), movie.getTitle());
      } catch (Exception e) {
        movieNames.put(request.getMovieId(), "Unknown Title");
      }
    }
    model.addAttribute("movieNames", movieNames);
  }
}
