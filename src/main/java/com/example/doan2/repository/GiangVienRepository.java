package com.example.doan2.repository;

import com.example.doan2.entity.BoMon;
import com.example.doan2.entity.GiangVien;

import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiangVienRepository extends JpaRepository<GiangVien,Integer> {
    GiangVien findByMaGV(String maGV);
    List<GiangVien> findByActiveFlag(boolean activeFlag);
    @Override
    Optional<GiangVien> findById(Integer integer);
    void deleteById(Integer id);

    @Query(value = "from GiangVien g where g.id not in " +
            "(select gk2.giangVien.id  from GiangVien_Ky gk2 join Ky k on gk2.ky.id = k.id where gk2.soLuongSV>5 and k.tenKy=:tenKy and k.namHoc=:namHoc)")
    List<GiangVien> getGiangVienbySoLuongSV(String tenKy, String namHoc);

    @Query(value = "select g.boMon from GiangVien g where g.maGV = :maGV ")
    BoMon getBoMonbyMaGiangVien(String maGV);

    @Query(value = "from GiangVien v where v.hoTen like %:hoTen% ")
    List<GiangVien> getGiangVienbyhoTen(String hoTen);

    List<GiangVien> findByBoMon(BoMon boMon);
}
