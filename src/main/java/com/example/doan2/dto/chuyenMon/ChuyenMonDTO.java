package com.example.doan2.dto.chuyenMon;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.Date;

@Data
public class ChuyenMonDTO {
    private Integer id;
    private String tenChuyenMon;
    private Date ngayTao;
    private Date ngayCapNhat;
}
