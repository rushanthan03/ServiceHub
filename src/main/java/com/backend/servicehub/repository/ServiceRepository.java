package com.backend.servicehub.repository;

import com.backend.servicehub.entity.Service;
import com.backend.servicehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("SELECT s FROM Service s " +
            "WHERE COALESCE(:search, '') = '' OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "ORDER BY s.id DESC")
    Page<User> filterService(
            @Param("search") String search,
            Pageable pageable);

}
