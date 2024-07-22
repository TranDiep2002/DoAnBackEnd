package com.example.doan2.req.dangKyReq;

import com.example.doan2.dto.sinhvien.SinhVienDTO;
import com.example.doan2.entity.SinhVien;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DangKyDeTaiReq {
    private String maSV1;
    private String vaiTroSV1;
//    private String maSV2;
    private String vaiTroSV2;
//    private String maSV3;
    private String vaiTroSV3;
//    private String maSV4;
    private String vaiTroSV4;
    private String maSV5;
    private String vaiTroSV5;
    private List<SinhVienDTO> sinhViens;// phải để sinh viên DTO
    private String maGV;
    private String tenDeTai;
    private String moTa;
    private String nganh;
}
