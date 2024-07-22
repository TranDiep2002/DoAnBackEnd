package com.example.doan2.repository;

import com.example.doan2.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao,Integer> {
    @Query(value = "from ThongBao t order by t.id DESC ")
    List<ThongBao> getAllThongBao();

    @Query(value = "select t.ngayKetThuc from ThongBao t where t.noiDungThongBao = 'Nộp đề cương' ")
    Date GetNgayKetThuc();
}

