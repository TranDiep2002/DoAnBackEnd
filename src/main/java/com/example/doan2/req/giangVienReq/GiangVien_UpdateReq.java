package com.example.doan2.req.giangVienReq;

import lombok.Data;

@Data
public class GiangVien_UpdateReq {
    private Integer id;
    private String maGV;
    private String hoTen;
    private String vaiTro;
    private String email;
    private String soDT;
    private String tenBoMon;
    private String gioiTinh;
    private String hocVi;
    private String donViCongTac;
}
