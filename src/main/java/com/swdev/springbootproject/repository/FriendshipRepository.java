package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.Friendship;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<@NonNull Friendship, @NonNull Long> {
  List<@NonNull Friendship> findBySender_IdOrReceiver_Id(
      @NonNull Long userId1, @NonNull Long userId2);

  boolean existsByCbUser1_IdAndCbUser2_Id(@NonNull Long userId1, @NonNull Long userId2);
}
