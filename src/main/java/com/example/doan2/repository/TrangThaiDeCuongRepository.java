package com.example.doan2.repository;

import com.example.doan2.entity.GiangVien;
import com.example.doan2.entity.TrangThaiDeCuong;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrangThaiDeCuongRepository extends JpaRepository<TrangThaiDeCuong, Integer> {

    @Query(value = "select distinct t.trangThai from TrangThaiDeCuong t where t.deTai_Id=:deTai_Id and t.ky_id=:ky_id")
    String getTrangThaiDeCuong(Integer deTai_Id, Integer ky_id);


    @Query(value = "from TrangThaiDeCuong  t where t.maSV=:maSV and t.ky_id=:ky_id")
    TrangThaiDeCuong getTrangThaiDCbyMaSVKy(String maSV,Integer ky_id);

    // lấy ra những đề cương ở kỳ hiện tại với đề tài đó
    @Query(value = "from TrangThaiDeCuong t where t.deTai_Id=:deTai_Id and t.ky_id=:ky_id")
    List<TrangThaiDeCuong> getDCbyDeTaiKy(Integer deTai_Id, Integer ky_id);
    // lấy ra những đề tài chưa có giảng viên chấm
    @Query(value = "select distinct t.deTai_Id from TrangThaiDeCuong t where t.maGV ='' and  t.ky_id=:ky_id")
    List<Integer> deTaiIDListChuacoGVCham(Integer ky_id);

    // lấy ra những giảng viên chưa được phân công
    @Query(value = "from GiangVien  g where g.maGV not in (select distinct t.maGV from TrangThaiDeCuong t where t.ky_id =:ky_id)")
    List<GiangVien> getGVChuaPhanCongCham(Integer ky_id);

    // lấy ra nhwunxg trạng thái đề cương mà giảng viên đó , nếu trạng thái null , giảng viên đó chưa đc phân công -> setTranjg thái giảng viên
    @Query(value = "  from TrangThaiDeCuong t where t.maGV=:maGV and t.ky_id=:ky_id ")
    List<TrangThaiDeCuong>  getTrangThaiDeCuongbyMaGv(String maGV,Integer ky_id);






}
