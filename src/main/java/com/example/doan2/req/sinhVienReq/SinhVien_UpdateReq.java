package com.example.doan2.req.sinhVienReq;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.Date;

@Data
public class SinhVien_UpdateReq {
    private Integer id;
    private String hoTen;
    private String maSV;
    private String email;
    private String nganh;
    private String lopHanhChinh;
    private String gioiTinh;
}

