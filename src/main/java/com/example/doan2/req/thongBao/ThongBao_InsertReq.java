package com.example.doan2.req.thongBao;

import lombok.Data;

import java.util.Date;

@Data
public class ThongBao_InsertReq {
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String noiDungThongBao;
}
