package com.example.doan2.repository;

import com.example.doan2.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DangKyRepository extends JpaRepository<DangKy,Integer> {
    List<DangKy> findBySinhVien(SinhVien sinhVien);
    // lấy ra những sinh viên cùng đề tài với nhau trong kỳ đó và năm học đó , cùng giảng viên hướng dẫn
    @Query(value = "select k.sinhVien from DangKy k where k.ky= :ky and k.deTai=:deTai and k.giangVien=:giangVien")
    List<SinhVien> getSVbyKyHocNamHoc(Ky ky, DeTai deTai, GiangVien giangVien);

   // lấy ra những đề tài thuộc kỳ đó
   @Query(value = "select distinct k.deTai from DangKy k join DeTai d on k.deTai.id = d.id join ChuyenMon c on d.chuyenMon.id = c.id where k.ky=:ky  and c.tenChuyenMon = :tenChuyenMon")
    List<DeTai> getDeTaibyKy(Ky ky, String tenChuyenMon);
   // lấy ra trạng thái của đăng ký trong kỳ đó , đề tài đó , giảng viên đó
    @Query(value = "select distinct k.trangThai from DangKy k where k.ky=:ky and k.giangVien=:giangVien and k.deTai=:deTai")
    String gettrangThai(Ky ky,GiangVien giangVien , DeTai deTai);
    // lấy ra những GVHD của đề tài đó , kỳ đó , vì 1 đề tài đó có thể có nhiều giảng viên hướng dẫn nên sẽ để list GiangVien
   @Query(value = "select k.giangVien from DangKy k where k.deTai=:deTai and k.ky=:ky")
    List<GiangVien> giangViens(DeTai deTai, Ky ky);

   // lấy ra đăng ký theo đề tài , sinh viên , kỳ học để cập nhật trạng thái
    @Query(value = "from DangKy k where k.deTai= :deTai and k.sinhVien = :sinhVien and k.ky= :ky")
    DangKy getDangKyByDeTaiSinhVienKy(DeTai deTai, SinhVien sinhVien, Ky ky);
}
