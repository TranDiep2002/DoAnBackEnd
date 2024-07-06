package com.example.doan2.dto.dangKy;

import com.example.doan2.entity.SinhVien;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DangKyDTO {
    private Integer id;
    private String trangThai;
    private Date ngayTao;
    private Date ngayCapNhat;
    private List<String> tenSinhViens;
    private String hoTen;
    private String tenDeTai;
    private String moTaDeTai;
    private String tenKy;
    private String tenNamHoc;
}
