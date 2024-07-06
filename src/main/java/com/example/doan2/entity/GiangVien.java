package com.example.doan2.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GiangVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String maGV;
    private String hoTen;
    private String vaiTro;
    private String email;
    private String soDT;
    private Date ngayTao;
    private Date ngayCapNhat;
    private String gioiTinh;
    private String hocVi;
    private String donViCongTac;
    private Boolean activeFlag;// khi xóa thì trạng thái thành true


    @OneToOne(mappedBy = "giangVien")
    @JsonBackReference // Sử dụng @JsonBackReference để tránh vòng lặp
    private TaiKhoan taiKhoans;

    @ManyToOne
    @JsonBackReference // Sử dụng @JsonBackReference để tránh vòng lặp
    @JoinColumn(name = "bomon_id")
    private BoMon boMon;

    @JsonManagedReference
    @OneToMany(mappedBy = "giangVien")
    List<DangKy> dangKyList;

    @JsonManagedReference
    @OneToMany(mappedBy = "giangVien")
    List<ChuyenMon_GiangVien> chuyenMon_giangViens;

    @JsonManagedReference
    @OneToMany(mappedBy = "giangVien")
    List<SinhVien_DeTai> sinhVien_deTais;
    // còn mối quan hệ hội đồng con và điểm thành phần đề tài

    @JsonManagedReference
    @OneToMany(mappedBy = "giangVien")
    List<GiangVien_Ky> giangVien_kies;

    // Thêm constructor phù hợp với các tham số trong câu lệnh @Query
    public GiangVien(Integer id, String maGV, String hoTen, String email, String gioiTinh, String hocVi, String soDT, String donViCongTac, Boolean activeFlag, Date ngayTao, Date ngayCapNhat, String vaiTro) {
        this.id = id;
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.hocVi = hocVi;
        this.soDT = soDT;
        this.donViCongTac = donViCongTac;
        this.activeFlag = activeFlag;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.vaiTro = vaiTro;
    }

}
