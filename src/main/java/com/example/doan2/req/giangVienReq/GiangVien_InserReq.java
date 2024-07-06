package com.example.doan2.req.giangVienReq;

import lombok.Data;

import java.util.Date;

@Data
public class GiangVien_InserReq {
    private String maGV;
    private String hoTen;
    private String vaiTro;
    private String email;
    private String soDT;
    private String passWord;
    private String tenBoMon;
    private String gioiTinh;
    private String hocVi;
    private String donViCongTac;
}
