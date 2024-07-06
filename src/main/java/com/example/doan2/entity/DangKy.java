package com.example.doan2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class DangKy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String trangThai;
    private Date ngayTao;
    private Date ngayCapNhat;
    private Boolean activeFlag;

    @ManyToOne
    @JoinColumn(name = "sinh_vien_id")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "giang_vien_id")
    private GiangVien giangVien;

    @ManyToOne
    @JoinColumn(name = "loai_dang_ky_id")
    private LoaiDangKy loaiDangKy;

    @ManyToOne
    @JoinColumn(name = "de_tai_id")
    private DeTai deTai;

    @ManyToOne
    @JoinColumn(name = "ky_id")
    private Ky ky;

}
