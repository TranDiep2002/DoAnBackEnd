package com.example.doan2.repository;

import com.example.doan2.dto.dangKy.DangKyEditDTO;
import com.example.doan2.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DangKyRepository extends JpaRepository<DangKy,Integer> {
    List<DangKy> findBySinhVien(SinhVien sinhVien);
    // lấy ra những sinh viên cùng đề tài với nhau trong kỳ đó và năm học đó , cùng giảng viên hướng dẫn
    @Query(value = "select k.sinhVien from DangKy k where k.ky= :ky and k.deTai=:deTai and k.giangVien=:giangVien")
    List<SinhVien> getSVbyKyHocNamHoc(Ky ky, DeTai deTai, GiangVien giangVien);

   // lấy ra những đề tài thuộc kỳ đó, chuyên môn đó để giảng viên bộ môn duyệt
   @Query(value = "select distinct k.deTai from DangKy k join DeTai d on k.deTai.id = d.id join ChuyenMon c on d.chuyenMon.id = c.id where k.ky=:ky  and c.tenChuyenMon = :tenChuyenMon")
    List<DeTai> getDeTaibyKy(Ky ky, String tenChuyenMon);

   // lấy ra những đề tài thuộc kỳ
   // lấy ra trạng thái của đăng ký trong kỳ đó , đề tài đó , giảng viên đó

    @Query(value = "select distinct k.deTai from DangKy k where k.ky=:ky ")
    List<DeTai> getDeTaibyKyGiaoVu(Ky ky);

    // lấy ra trạng thái của đề tài
    @Query(value = "select distinct k.trangThai from DangKy k where k.ky=:ky and k.giangVien=:giangVien and k.deTai=:deTai")
    String gettrangThai(Ky ky,GiangVien giangVien , DeTai deTai);
    // lấy ra những GVHD của đề tài đó , kỳ đó , vì 1 đề tài đó có thể có nhiều giảng viên hướng dẫn nên sẽ để list GiangVien
   @Query(value = "select k.giangVien from DangKy k where k.deTai=:deTai and k.ky=:ky")
    List<GiangVien> giangViens(DeTai deTai, Ky ky);

   // lấy ra đăng ký theo đề tài , sinh viên , kỳ học để cập nhật trạng thái
    @Query(value = "from DangKy k where k.deTai= :deTai and k.sinhVien = :sinhVien and k.ky= :ky")
    DangKy getDangKyByDeTaiSinhVienKy(DeTai deTai, SinhVien sinhVien, Ky ky);

    // cập nhật trạng thái đề tài
    @Transactional
    @Modifying
    @Query(value = "update DangKy k set k.trangThai ='Đã duyệt' where k.deTai = :deTai and k.giangVien =:giangVien")
    void UpDateTrangThaiDeTai(DeTai deTai, GiangVien giangVien);

    // cập nhật trạng thái đề tài
    @Transactional
    @Modifying
    @Query(value = "update DangKy k set k.trangThai ='Hủy' where k.deTai = :deTai and k.giangVien =:giangVien")
    void UpDateTrangThaiDeTaiHuy(DeTai deTai, GiangVien giangVien);

    // cập nhật ghi chú
    @Transactional
    @Modifying
    @Query(value = "update DangKy k set k.ghiChu=:ghichu where k.deTai = :deTai ")
    void UpdateGhiChu(DeTai deTai,String ghichu);

    // cập nhật giảng viên cho đề tài ( trong tường hợp hội đồng đề nghị đổi i=giảo viên hướng dẫn)
    @Transactional
    @Modifying
    @Query(value = "update DangKy k set k.giangVien =:giangVien where k.deTai=:deTai and k.ky=:ky")
    void UpdateGiangVienHD(GiangVien giangVien, DeTai deTai, Ky ky);

    // lấy ra trạng thái đăng ký
    @Query(value = "select distinct k.trangThai from DangKy k where  k.deTai = :deTai and k.giangVien =:giangVien ")
    String getTrangThai(DeTai deTai, GiangVien giangVien);

    // lấy ra đăng ký by đề tài và kỳ để cập nhật GVHD
    @Query("select distinct new com.example.doan2.dto.dangKy.DangKyEditDTO(k.giangVien.maGV, k.deTai.tenDeTai, k.deTai.moTa) from DangKy k where k.deTai = :deTai and k.ky = :ky")
    DangKyEditDTO getDangKybyDeTaiandKy(DeTai deTai, Ky ky);

    List<DangKy> findByDeTaiAndKy(DeTai deTai,Ky ky);
   // cập nhật giảng viên hướng dẫn xong thì phải trừ số lượng sinh viên của giảng viên cũ và thêm số lượng sinh viên của giảng viên mới
    // -- đầu tiền : đếm số lượng sinh viên trong đề tài đó , mô tả đó và kỳ đó
    @Query("select  count (k.sinhVien) from DangKy  k where k.deTai=:deTai and k.ky =:ky")
    Integer getSoLuongSVTrongNhom (DeTai deTai, Ky ky);
    // -- lấy ra giảng viên hướng dẫn hiện tại để trừ số lượng sinh viên
    @Query("select distinct k.giangVien from DangKy k where k.deTai = :deTai and k.ky = :ky")
    GiangVien getGiangVienToUpdateSoLuongSinhVien(DeTai deTai, Ky ky);

}
