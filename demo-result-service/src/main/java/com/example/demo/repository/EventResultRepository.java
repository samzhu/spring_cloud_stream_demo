package com.example.demo.repository;

import com.example.demo.entity.EventResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource
public interface EventResultRepository extends JpaRepository<EventResult, String>, JpaSpecificationExecutor<EventResult> {
}
