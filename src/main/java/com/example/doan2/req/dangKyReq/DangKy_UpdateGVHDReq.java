package com.example.doan2.req.dangKyReq;

import lombok.Data;

@Data
public class DangKy_UpdateGVHDReq {
    private String maGV; // lấy mã giảng viên để update tráng thái
    private String tenDeTai;
    private String moTaDeTai;
}
