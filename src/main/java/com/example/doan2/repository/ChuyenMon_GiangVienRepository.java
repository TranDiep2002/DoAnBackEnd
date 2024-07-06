package com.example.doan2.repository;

import com.example.doan2.entity.ChuyenMon_GiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuyenMon_GiangVienRepository extends JpaRepository<ChuyenMon_GiangVien,Integer> {
}
