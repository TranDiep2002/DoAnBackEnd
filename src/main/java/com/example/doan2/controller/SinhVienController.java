package com.example.doan2.controller;

import com.example.doan2.dto.sinhvien.SinhVienDTO;
import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import com.example.doan2.repository.SinhVienRepository;
import com.example.doan2.repository.TaiKhoanRepository;
import com.example.doan2.req.sinhVienReq.SinhVien_InsertReq;
import com.example.doan2.req.sinhVienReq.SinhVien_UpdateReq;
import com.example.doan2.service.MailService;
import com.example.doan2.service.SinhVienImportService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
// cấp quyền truy cập
// không thì sẽ gặp phải lỗi này
//Access to XMLHttpRequest at 'http://localhost:8080/account' from origin 'http://localhost:63342' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
@CrossOrigin(value = "*")
@Slf4j
public class SinhVienController {
    private SinhVienImportService sinhVienImportService;
    @Autowired
    private SinhVienRepository sinhVienRepo;
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MailService mailService;

    public SinhVienController(SinhVienImportService sinhVienImportService) {
        this.sinhVienImportService = sinhVienImportService;
    }


    @GetMapping("getallSinhVien")
    public ResponseEntity<?> getAllSinhVien(){
        List<SinhVien> sinhViens = sinhVienRepo.findByActiveFlag(false);
        List<SinhVienDTO> sinhvienDTOs= modelMapper.map(sinhViens,new TypeToken<List<SinhVienDTO>>(){}.getType());
        return ResponseEntity.ok(sinhvienDTOs);
    }
    @GetMapping("getSinhVienbyID/{id}")
    public  ResponseEntity<?> getSinhVienbyID(@PathVariable Integer id){
        Optional<SinhVien> opsinhVien= sinhVienRepo.findById(id);
        if(opsinhVien.isPresent()){
            SinhVien sinhVien = opsinhVien.get();
            SinhVienDTO sinhVienDTO = modelMapper.map(sinhVien,new TypeToken<SinhVienDTO>(){}.getType());
            return ResponseEntity.ok(sinhVienDTO);
        }
        return  ResponseEntity.ok("Không tìm thấy sinh viên có id ");
    }

    // chức năng tìm kiếm bằng tên
    @GetMapping("getSinhVienbyHoTen/{hoTen}")
    public ResponseEntity<?> getSinhVienbyHoTen(@PathVariable String hoTen){
        List<SinhVien> sinhVienList= sinhVienRepo.getSinhVienbyhoTen(hoTen);
        if(sinhVienList.size()!=0){
            List<SinhVienDTO> sinhvienDTOs= modelMapper.map(sinhVienList,new TypeToken<List<SinhVienDTO>>(){}.getType());
            return ResponseEntity.ok(sinhvienDTOs);
        }
        return ResponseEntity.ok(sinhVienList);
    }

    // chức năng lọc theo chuyên ngành
    @GetMapping("getSinhVienbyChuyenNganh/{nganh}")
    public ResponseEntity<?> getSinhVienbyChuyenNganh(@PathVariable String nganh){
        List<SinhVien> sinhVienList = sinhVienRepo.findByNganh(nganh);
        if(sinhVienList.size()!=0){
            List<SinhVienDTO> sinhvienDTOs= modelMapper.map(sinhVienList,new TypeToken<List<SinhVienDTO>>(){}.getType());
            return ResponseEntity.ok(sinhvienDTOs);
        }
        return ResponseEntity.ok(sinhVienList);
    }


    @PostMapping("insertSinhVien")
    public ResponseEntity<?> insertSinhVien(@RequestBody SinhVien_InsertReq sinhVien_insertReq){
        SinhVien sinhVien1 = sinhVienRepo.findByMaSV(sinhVien_insertReq.getMaSV());
        if(sinhVien1!=null){
            return ResponseEntity.ok("Sinh viên đã tồn tại, không thể thêm");

        }
        SinhVien sinhVien = new SinhVien();
        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String formattedDateTime = now.format(formatter);
        System.out.println("Thời gian hiện tại: " + formattedDateTime);

        // Chuyển đổi LocalDateTime sang Date
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        String encodePass = passwordEncoder.encode(sinhVien_insertReq.getPassWord());
        sinhVien.setMaSV(sinhVien_insertReq.getMaSV());
        sinhVien.setHoTen(sinhVien_insertReq.getHoTen());
        sinhVien.setEmail(sinhVien_insertReq.getEmail());
        sinhVien.setNganh(sinhVien_insertReq.getNganh());
        sinhVien.setLopHanhChinh(sinhVien_insertReq.getLopHanhChinh());
        sinhVien.setNgayTao(date);
        sinhVien.setNgayCapNhat(date);
        sinhVien.setActiveFlag(false);
        sinhVien.setGioiTinh(sinhVien_insertReq.getGioiTinh());
        sinhVienRepo.save(sinhVien);
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setSinhVien(sinhVien);
        taiKhoan.setMaUser(sinhVien_insertReq.getMaSV());
        taiKhoan.setPassWord(encodePass);
        taiKhoan.setLoaiTaiKhoan("SINHVIEN");
        taiKhoan.setNgayTao(date);
        taiKhoan.setActiveFlag(false);
        taiKhoanRepo.save(taiKhoan);
        mailService.senMail(sinhVien_insertReq.getEmail(),"Mật khẩu của bạn là :"+sinhVien_insertReq.getPassWord());
        return ResponseEntity.ok("thêm sinh viên thành công");
    }

    @PostMapping("uploadFileSinhVien/{sheetName}")
    public ResponseEntity<?> uploadSinhVienData(@RequestParam("file")MultipartFile file , @PathVariable String sheetName){
        this.sinhVienImportService.saveSinhVienToDatabase(file,sheetName);
        return ResponseEntity.ok(Map.of("Mesage","Sinh Vien data upload and save to db successfully"));
    }

    @PutMapping("updateSinhVien")
    public ResponseEntity<?> updateSinhVien(@RequestBody SinhVien_UpdateReq sinhVien_updateReq){
        Optional<SinhVien> opsinhVien = sinhVienRepo.findById(sinhVien_updateReq.getId());
        TaiKhoan taiKhoan = taiKhoanRepo.findBySinhVien(opsinhVien.get());
        if(opsinhVien.isPresent()){
            SinhVien sinhVien = opsinhVien.get();
            sinhVien.setHoTen(sinhVien_updateReq.getHoTen());
            sinhVien.setEmail(sinhVien_updateReq.getEmail());
            sinhVien.setMaSV(sinhVien_updateReq.getMaSV());
            sinhVien.setNganh(sinhVien_updateReq.getNganh());
            sinhVien.setLopHanhChinh(sinhVien.getLopHanhChinh());
            sinhVien.setGioiTinh(sinhVien_updateReq.getGioiTinh());
            // Lấy thời gian hiện tại
            LocalDateTime now = LocalDateTime.now();

            // Định dạng thời gian
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String formattedDateTime = now.format(formatter);
            System.out.println("Thời gian hiện tại: " + formattedDateTime);

            // Chuyển đổi LocalDateTime sang Date
            Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            sinhVien.setNgayCapNhat(date);
            sinhVienRepo.save(sinhVien);
            taiKhoan.setMaUser(sinhVien_updateReq.getMaSV());
            taiKhoanRepo.save(taiKhoan);
            return ResponseEntity.ok("Cập nhật sinh viên và tài khoản thành công");
        }
        return  ResponseEntity.ok(" không tìm thấy sinh viên để cập nhật");
    }
    @Transactional
    @DeleteMapping ("deleteSinhVien/{id}")
    public ResponseEntity<?> deleteSinhVien(@PathVariable Integer id){
        Optional<SinhVien> optionalSinhVien= sinhVienRepo.findById(id);
        if(optionalSinhVien.isPresent()){
            TaiKhoan taiKhoan = taiKhoanRepo.findByMaUser(optionalSinhVien.get().getMaSV());
            taiKhoan.setActiveFlag(true);
            taiKhoanRepo.save(taiKhoan);
            optionalSinhVien.get().setActiveFlag(true);
            sinhVienRepo.save(optionalSinhVien.get());
            return ResponseEntity.ok("Xóa sinh viên thành công");
        }
        return ResponseEntity.ok("không tìm thấy sinh viên để xóa");
    }

}
