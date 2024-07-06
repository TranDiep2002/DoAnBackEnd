package com.example.doan2.controller;

import com.example.doan2.dto.giangvien.GiangVienDTO;
import com.example.doan2.dto.sinhvien.SinhVienDTO;
import com.example.doan2.entity.BoMon;
import com.example.doan2.entity.GiangVien;
import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import com.example.doan2.repository.BoMonRepository;
import com.example.doan2.repository.GiangVienRepository;
import com.example.doan2.repository.TaiKhoanRepository;
import com.example.doan2.req.giangVienReq.GiangVien_InserReq;
import com.example.doan2.req.giangVienReq.GiangVien_UpdateReq;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*")
@Slf4j
public class GiangVienController {
    @Autowired
    private GiangVienRepository giangVienRepo;
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;
   @Autowired
    private ModelMapper modelMapper;
   @Autowired
   private BoMonRepository boMonRepo;

   @GetMapping("getallGiangVien")
    public ResponseEntity<?> getallGiangVien(){
       List<GiangVien> giangVienList= giangVienRepo.findByActiveFlag(false);
       List<GiangVienDTO> giangVienDTOS= modelMapper.map(giangVienList,new TypeToken<List<GiangVienDTO>>(){}.getType());
       return ResponseEntity.ok(giangVienDTOS);
   }
    @GetMapping("getGiangVienbyID/{id}")
    public  ResponseEntity<?> getSinhVienbyID(@PathVariable Integer id){
        Optional<GiangVien> opGiangVien= giangVienRepo.findById(id);
        if(opGiangVien.isPresent()){
            GiangVien giangVien = opGiangVien.get();
            GiangVienDTO giangVienDTO= modelMapper.map(giangVien, new TypeToken<GiangVienDTO>(){}.getType());
            return ResponseEntity.ok(giangVienDTO);
        }
        return  ResponseEntity.ok("Không tìm thấy giảng viên có id ");
    }

    // lấy ra bộ môn của giảng viên để duyệt đề tài
    @GetMapping("getBoMonbyGiangVien/{maGV}")
    public ResponseEntity<?> getBoMonbyGiangVien(@PathVariable String maGV){
       BoMon boMon = giangVienRepo.getBoMonbyMaGiangVien(maGV);
       return ResponseEntity.ok(boMon.getTenBoMon());
    }
    // lấy ra vai trò của giảng viên để duyệt đề tài
    @GetMapping("getVaiTro/{maGV}")
    public ResponseEntity<?> getVaiTro(@PathVariable String maGV){
       GiangVien giangVien = giangVienRepo.findByMaGV(maGV);
       return  ResponseEntity.ok(giangVien.getVaiTro());
    }
    @GetMapping("getGiangVienbyHoTen/{hoTen}")
    public ResponseEntity<?> getGiangVienbyHoTen(@PathVariable String hoTen){
       List<GiangVien> giangVienList = giangVienRepo.getGiangVienbyhoTen(hoTen);
        List<GiangVienDTO> giangVienDTOS= modelMapper.map(giangVienList,new TypeToken<List<GiangVienDTO>>(){}.getType());
        return ResponseEntity.ok(giangVienDTOS);
    }

    @GetMapping("getGiangVienbyBoMon/{boMon}")
    public ResponseEntity<?> getGiangVienbyBoMon(@PathVariable String boMon){
       BoMon boMon1 = boMonRepo.findByTenBoMon(boMon);
        List<GiangVien> giangVienList = giangVienRepo.findByBoMon(boMon1);
        List<GiangVienDTO> giangVienDTOS= modelMapper.map(giangVienList,new TypeToken<List<GiangVienDTO>>(){}.getType());
        return ResponseEntity.ok(giangVienDTOS);
    }

   @PostMapping("insertGiangVien")
    public ResponseEntity<?> insertGiangVien(@RequestBody GiangVien_InserReq giangVien_inserReq){
       GiangVien giangVien1 = giangVienRepo.findByMaGV(giangVien_inserReq.getMaGV());
       BoMon boMon = boMonRepo.findByTenBoMon(giangVien_inserReq.getTenBoMon());
       if(boMon==null){
           return ResponseEntity.ok("Tên bộ môn không tồn tại");
       }
       if(giangVien1!=null){
           return ResponseEntity.ok("Giảng viên đã tồn tại, không thể thêm");
       }
       GiangVien giangVien = new GiangVien();
       // Lấy thời gian hiện tại
       LocalDateTime now = LocalDateTime.now();

       // Định dạng thời gian
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
       String formattedDateTime = now.format(formatter);
       System.out.println("Thời gian hiện tại: " + formattedDateTime);

       // Chuyển đổi LocalDateTime sang Date
       Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
       BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
       String encodePass = passwordEncoder.encode(giangVien_inserReq.getPassWord());
       giangVien.setMaGV(giangVien_inserReq.getMaGV());
       giangVien.setHoTen(giangVien_inserReq.getHoTen());
       giangVien.setEmail(giangVien_inserReq.getEmail());
       giangVien.setSoDT(giangVien_inserReq.getSoDT());
       giangVien.setVaiTro(giangVien_inserReq.getVaiTro());
       giangVien.setNgayTao(date);
       giangVien.setNgayCapNhat(date);
       giangVien.setActiveFlag(false);
       giangVien.setBoMon(boMon);
       giangVien.setGioiTinh(giangVien_inserReq.getGioiTinh());
       giangVien.setDonViCongTac(giangVien_inserReq.getDonViCongTac());
       giangVien.setHocVi(giangVien_inserReq.getHocVi());
       giangVienRepo.save(giangVien);
       TaiKhoan taiKhoan = new TaiKhoan();
       taiKhoan.setMaUser(giangVien.getMaGV());
       taiKhoan.setPassWord(encodePass);
       taiKhoan.setLoaiTaiKhoan("GIANGVIEN");
       taiKhoan.setGiangVien(giangVien);
       taiKhoan.setNgayTao(date);
       taiKhoan.setActiveFlag(false);
       taiKhoanRepo.save(taiKhoan);
       return ResponseEntity.ok("thêm giảng viên thành công");
   }

   @PutMapping("updateGiangVien")
    public ResponseEntity<?> updateGiangVien(@RequestBody GiangVien_UpdateReq giangVien_updateReq){
       Optional<GiangVien> opGiangVien= giangVienRepo.findById(giangVien_updateReq.getId());
       TaiKhoan taiKhoan = taiKhoanRepo.findByGiangVien(opGiangVien.get());
       BoMon boMon = boMonRepo.findByTenBoMon(giangVien_updateReq.getTenBoMon());
       if(boMon==null){
           return ResponseEntity.ok("Tên bộ môn không tồn tại");
       }
       if(opGiangVien.isPresent()){
           // Lấy thời gian hiện tại
           LocalDateTime now = LocalDateTime.now();

           // Định dạng thời gian
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
           String formattedDateTime = now.format(formatter);
           System.out.println("Thời gian hiện tại: " + formattedDateTime);

           // Chuyển đổi LocalDateTime sang Date
           Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
           GiangVien giangVien = opGiangVien.get();
           giangVien.setHoTen(giangVien_updateReq.getHoTen());
           giangVien.setEmail(giangVien_updateReq.getEmail());
           giangVien.setMaGV(giangVien_updateReq.getMaGV());
           giangVien.setVaiTro(giangVien_updateReq.getVaiTro());
           giangVien.setSoDT(giangVien_updateReq.getSoDT());
           giangVien.setNgayCapNhat(date);
           giangVien.setBoMon(boMon);
           giangVien.setGioiTinh(giangVien_updateReq.getGioiTinh());
           giangVien.setDonViCongTac(giangVien_updateReq.getDonViCongTac());
           giangVien.setHocVi(giangVien_updateReq.getHocVi());
           giangVienRepo.save(giangVien);
           taiKhoan.setGiangVien(giangVien);
           taiKhoan.setMaUser(giangVien_updateReq.getMaGV());
           taiKhoanRepo.save(taiKhoan);
           return ResponseEntity.ok("cập nhật giảng viên thành công");
       }
       return ResponseEntity.ok("Không tìm thấy giảng viên để cập nhật");
   }

   @DeleteMapping("deleteGiangVien/{id}")
    public ResponseEntity<?> deleteGiangVien(@PathVariable Integer id){
       Optional<GiangVien> opGiangVien = giangVienRepo.findById(id);
       TaiKhoan taiKhoan = taiKhoanRepo.findByGiangVien(opGiangVien.get());
       if(opGiangVien.isPresent()){
           // Lấy thời gian hiện tại
           LocalDateTime now = LocalDateTime.now();

           // Định dạng thời gian
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
           String formattedDateTime = now.format(formatter);
           System.out.println("Thời gian hiện tại: " + formattedDateTime);

           // Chuyển đổi LocalDateTime sang Date
           Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
           opGiangVien.get().setNgayCapNhat(date);
           opGiangVien.get().setActiveFlag(true);
           if (taiKhoan!=null){
               taiKhoan.setActiveFlag(true);
               taiKhoanRepo.save(taiKhoan);
           }
           giangVienRepo.save(opGiangVien.get());
           return ResponseEntity.ok("Xóa giảng viên thành công");
       }
       return ResponseEntity.ok("không tìm thấy giảng vien để xóa");
   }

}
