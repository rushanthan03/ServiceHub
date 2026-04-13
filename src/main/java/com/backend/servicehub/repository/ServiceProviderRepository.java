package com.backend.servicehub.repository;

import com.backend.servicehub.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

}
