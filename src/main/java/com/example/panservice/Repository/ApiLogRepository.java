package com.example.panservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.panservice.Entity.ApiLog;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

}