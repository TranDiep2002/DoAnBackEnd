package com.example.doan2.req.taiKhoan;

import lombok.Data;

import java.util.Date;

@Data
public class TaiKhoan_UpdateReq {
    private String maUser;
    private String loaiTaiKhoan; // phân quyền là giảng viên hay sinh viên
    private String passWord;
    private Date ngayTao;
}
