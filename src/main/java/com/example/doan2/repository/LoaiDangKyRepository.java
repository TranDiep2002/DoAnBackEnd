package com.example.doan2.repository;

import com.example.doan2.entity.LoaiDangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoaiDangKyRepository extends JpaRepository<LoaiDangKy,Integer> {
    Optional<LoaiDangKy> findById(Integer id);
}
