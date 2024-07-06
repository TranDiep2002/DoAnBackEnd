package com.example.doan2.repository;

import com.example.doan2.entity.Ky;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KyRepository extends JpaRepository<Ky,Integer> {

    Ky findByTenKyAndAndNamHoc(String tenKy,String namHoc);
    // hai câu query này để đẩy lên frontend
    @Query(value = "select distinct k.tenKy from Ky k")
    List<String> getTenKy();


    @Query(value = "select distinct k.namHoc from Ky k")
    List<String> getNamHoc();

    @Transactional
    @Modifying
    @Query(value = "update Ky k set k.trangThai='Đã hoàn thành' ")
    void updateKy();

    // lấy ra kỳ học theo trạng thái
    Ky findByTrangThai(String trangThai);
}
