package com.example.doan2.dto.thongBao;


import lombok.Data;

import java.util.Date;

@Data
public class ThongBaoDTO {
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String noiDungThongBao;
    private String tenKy;
    private String namHoc;
}
