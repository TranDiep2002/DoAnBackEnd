package com.example.doan2.dto.login;

import lombok.Data;

import java.util.Date;

@Data
public class TaiKhoanDTO {
    private Integer id;
    private String maUser;
    private String loaiTaiKhoan; // phân quyền là giảng viên hay sinh viên
    private String passWord;
    private Date ngayTao;
    private String hoTen;
}
