package com.example.doan2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiangVien_Ky {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer soLuongSV = 0;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "giang_vien_id")
    private GiangVien giangVien;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ky_id")
    private Ky ky;
}
