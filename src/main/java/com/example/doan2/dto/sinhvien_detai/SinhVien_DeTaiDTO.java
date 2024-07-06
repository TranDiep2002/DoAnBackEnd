package com.example.doan2.dto.sinhvien_detai;

import lombok.Data;

import java.util.Date;

@Data
public class SinhVien_DeTaiDTO {
    private Integer id;
    private String vaiTroSinhVien;
    private String vaiTroGiangVien;
    private Date ngayTao;
    private Date ngayCapNhat;
    private String hoTenGV;
    private String tenDeTai;
    private String tenKy;
    private String tenNamHoc;


}
