package com.swdev.springbootproject.controller.rest;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Friendship;
import com.swdev.springbootproject.model.dto.FriendDto;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.FriendshipRepository;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// For development and testing, the REST API is public. but of course ideally there needs to be
// authentification
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FriendshipRestController {
  private final FriendshipRepository friendshipRepository;
  private final CbUserRepository cbUserRepository;

  @GetMapping("/users/{userId}/friends")
  public ResponseEntity<@NonNull List<FriendDto>> getFriends(@PathVariable Long userId) {

    List<FriendDto> friends =
        friendshipRepository.findBySender_IdOrReceiver_Id(userId, userId).stream()
            .filter(Friendship::isAccepted)
            .map(friendship -> toFriendDto(friendship, userId))
            .toList();

    return ResponseEntity.ok(friends);
  }

  @PostMapping("/users/friendship")
  public ResponseEntity<Void> addFriendship(@RequestBody Map<String, Long> body) {
    Long senderId = body.get("senderId");
    Long receiverId = body.get("receiverId");

    if (senderId == null || receiverId == null || senderId.equals(receiverId)) {
      return ResponseEntity.badRequest().build();
    }

    var sender = cbUserRepository.findById(senderId).orElse(null);
    var receiver = cbUserRepository.findById(receiverId).orElse(null);
    if (sender == null || receiver == null) {
      return ResponseEntity.badRequest().build();
    }

    boolean exists =
        friendshipRepository.existsBySender_IdAndReceiver_Id(senderId, receiverId)
            || friendshipRepository.existsBySender_IdAndReceiver_Id(receiverId, senderId);

    if (exists) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    friendshipRepository.save(
        Friendship.builder().sender(sender).receiver(receiver).accepted(false).build());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/users/friendship/{id}")
  public ResponseEntity<Void> deleteFriendship(@PathVariable Long id) {
    var friendship = friendshipRepository.findById(id).orElse(null);
    if (friendship == null) {
      return ResponseEntity.notFound().build();
    }
    friendshipRepository.delete(friendship);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/users/friendship/{id}/update")
  public ResponseEntity<Void> updateFriendship(@PathVariable Long id) {
    var friendship = friendshipRepository.findById(id).orElse(null);
    if (friendship == null) {
      return ResponseEntity.notFound().build();
    }

    friendship.setAccepted(true);
    friendshipRepository.save(friendship);
    return ResponseEntity.ok().build();
  }

  private FriendDto toFriendDto(Friendship friendship, Long currentUserId) {
    CbUser other =
        friendship.getSender().getId().equals(currentUserId)
            ? friendship.getReceiver()
            : friendship.getSender();

    return FriendDto.builder()
        .id(other.getId())
        .friendshipId(friendship.getId())
        .name(other.getName())
        .email(other.getEmail())
        .build();
  }
}
