package com.skillswap.skillswap_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {
    List<ConnectionRequest> findByRequesterId(Long requesterId);
    List<ConnectionRequest> findByReceiverId(Long receiverId);
    Optional<ConnectionRequest> findByRequesterIdAndReceiverId(Long requesterId, Long receiverId);
}
