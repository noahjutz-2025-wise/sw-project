package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.CertifyMovieRequest;
import com.swdev.springbootproject.repository.CertifyMovieRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/certified")
public class CriticRequestController {
  private final CertifyMovieRequestRepository certifyMovieRequestRepository;

  @PostMapping("/request")
  @PreAuthorize("hasRole('CRITIC')")
  public String requestCertification(
      @RequestParam Long movieId,
      @AuthenticationPrincipal CbUser cbUser,
      RedirectAttributes redirectAttributes) {

    if (certifyMovieRequestRepository.existsByMovieIdAndCbUser_IdAndStatus(
        movieId, cbUser.getId(), CertifyMovieRequest.RequestStatus.PENDING)) {
      redirectAttributes.addFlashAttribute(
          "message", "Certification Request is already in progress");
      return "redirect:/app/movie/" + movieId;
    }

    CertifyMovieRequest request =
        CertifyMovieRequest.builder()
            .movieId(movieId)
            .cbUser(cbUser)
            .status(CertifyMovieRequest.RequestStatus.PENDING)
            .build();

    certifyMovieRequestRepository.save(request);
    return "redirect:/app/movie/" + movieId;
  }
}
