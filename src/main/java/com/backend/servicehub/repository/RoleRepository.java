package com.backend.servicehub.repository;

import com.backend.servicehub.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r " +
            "WHERE COALESCE(:search, '') = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "ORDER BY r.id DESC")
    Page<Role> filterRole(
            @Param("search") String search,
            Pageable pageable);

    boolean existsByName(String name);
}
