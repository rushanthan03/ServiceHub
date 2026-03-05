package com.backend.servicehub.repository;

import com.backend.servicehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "WHERE (COALESCE(:search, '') = '' OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:isActive IS NULL OR u.isActive = :isActive) " +
            "ORDER BY u.id DESC")
    Page<User> filterUser(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable);

    Optional<User> findByEmail(String normalizedEmail);
}
