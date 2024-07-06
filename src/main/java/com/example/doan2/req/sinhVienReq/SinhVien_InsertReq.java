package com.example.doan2.req.sinhVienReq;

import lombok.Data;

import java.util.Date;

@Data
public class SinhVien_InsertReq {
    private String hoTen;
    private String maSV;
    private String email;
    private String nganh;
    private String passWord;
    private String lopHanhChinh;
    private String gioiTinh;
}
