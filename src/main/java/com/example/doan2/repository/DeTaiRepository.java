package com.example.doan2.repository;

import com.example.doan2.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeTaiRepository extends JpaRepository<DeTai, Integer> {
    DeTai findByTenDeTai(String tenDeTai);
}
