package com.example.doan2.dto.deCuong;

import lombok.Data;

import java.util.List;

@Data
public class DeCuongDTO {
    private String tenDeTai;
    private String moTa;
    private  String namHoc;
    private String tenKy;
    private String trangThai;
    private List<String> tenFile;
}
