package com.manager.timezone.timezonemanagerserver.repository;

import com.manager.timezone.timezonemanagerserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByIsDeleted(boolean isDeleted);

    Optional<User> findByUidAndIsDeleted(UUID uid, boolean isDeleted);
}
