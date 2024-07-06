package com.example.doan2.service;

import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import com.example.doan2.repository.SinhVienRepository;
import com.example.doan2.repository.TaiKhoanRepository;
import com.example.doan2.until.DateUntil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class SinhVienImportService {
    private ExcelUploadSVService excelUploadSVService;
    @Autowired
    private DateUntil dateUntil;
    @Autowired
    private MailService mailService;
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;
    @Autowired
    private SinhVienRepository sinhVienRepo;

    public void saveSinhVienToDatabase(MultipartFile file , String sheetName){
        if(ExcelUploadSVService.isValidExcelFile(file)){
            try {
                List<SinhVien> sinhVienList= ExcelUploadSVService.getSinhVienFromExcel(file.getInputStream(),sheetName);
                sinhVienList.forEach(sinhVien -> {
                    sinhVien.setNgayTao(dateUntil.getDateNow());
                    sinhVien.setNgayCapNhat(dateUntil.getDateNow());
                    sinhVien.setActiveFlag(false);
                    byte[] array = new byte[7]; // length is bounded by 7
                    new Random().nextBytes(array);
                    String generatedString = new String(array, Charset.forName("UTF-8"));
                    TaiKhoan taiKhoan = new TaiKhoan();
                    taiKhoan.setSinhVien(sinhVien);
                    taiKhoan.setMaUser(sinhVien.getMaSV());
                    taiKhoan.setPassWord(generatedString);
                    taiKhoan.setLoaiTaiKhoan("SINHVIEN");
                    taiKhoan.setNgayTao(dateUntil.getDateNow());
                    taiKhoan.setActiveFlag(false);
                    taiKhoanRepo.save(taiKhoan);
                    mailService.senMail(sinhVien.getEmail(),"Mật khẩu của bạn là :"+taiKhoan.getPassWord());
                    sinhVienRepo.save(sinhVien);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
