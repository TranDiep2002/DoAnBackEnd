//package com.example.doan2.controller;
//
//import com.example.doan2.entity.Ky;
//import com.example.doan2.entity.SinhVien;
//import com.example.doan2.entity.SinhVien_DeTai;
//import com.example.doan2.repository.KyRepository;
//import com.example.doan2.repository.SinhVienRepository;
//import com.example.doan2.repository.SinhVien_DeTaiRepository;
//import com.example.doan2.until.FileDownloadUntil;
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@RestController
//@CrossOrigin
//public class DeCuongController {
//
//    @Autowired
//    private SinhVien_DeTaiRepository sinhVien_deTaiRepo;
//
//    @Autowired
//    private SinhVienRepository sinhVienRepo;
//
//    @Autowired
//    private KyRepository kyRepo;
//    // Đọc thư mục gốc để lưu file từ cấu hình
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Autowired
//    private FileDownloadUntil fileDownloadUntil;
//    @PostMapping("/uploadFile/{maSV}")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String maSV) {
//        // Lấy tên file
//        String fileName = file.getOriginalFilename();
//        try {
//            Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//            SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//            SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//            String tenDeTai = sinhVien_deTai.getDeTai().getTenDeTai();
//            Integer maDeTai = sinhVien_deTai.getDeTai().getId();
//            // Tạo đường dẫn thư mục con
//            Path folderPath = Paths.get(uploadDir, "DeTai"+maDeTai );
//
//            // Kiểm tra và tạo thư mục nếu chưa tồn tại
//            if (!Files.exists(folderPath)) {
//                Files.createDirectories(folderPath);
//            }
//
//            // Tạo đường dẫn lưu file trong thư mục con
//            Path filePath = folderPath.resolve(fileName);
//
//            // Lưu file vào thư mục con
//            Files.write(filePath, file.getBytes());
//
//            return ResponseEntity.ok("File uploaded successfully to folder: " + filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("File upload failed: " + fileName);
//        }
//    }
////    public List<File> listFiles(String uploadDir, String maSV) throws IOException {
////        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
////        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
////        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
////        Integer maDT = sinhVien_deTai.getDeTai().getId();
////        Path dirPath = Paths.get(uploadDir, "DeTai"+ maDT);
////
////        // Try-with-resources to ensure the stream is closed
////        try (Stream<Path> paths = Files.list(dirPath)) {
////            return paths
////                    .filter(Files::isRegularFile) // filter only regular files (not directories or other types)
////                    .map(Path::toFile) // convert Path to File
////                    .collect(Collectors.toList());
////        }
////    }
////
////    @GetMapping("getFilebyMaSV/{maSV}")
////    public ResponseEntity<?> getFilebyMaSV(@PathVariable String maSV) throws IOException {
////        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
////        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
////        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
////        Integer maDT = sinhVien_deTai.getId();
////        List<File> files = listFiles(uploadDir,maSV);
////        String content = new String(Files.readAllBytes(files.get(0).toPath()));
////        return ResponseEntity.ok(content);
////    }
//    @GetMapping("/getFileName/{maSV}")
//    public ResponseEntity<?> getFileName(@PathVariable String maSV) {
//
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        System.out.println("File[] listFiles(): ");
//        File dir = new File(uploadDir+"/DeTai"+maDT);
//        File[] children = dir.listFiles();
//        for (File file : children) {
//            System.out.println(file.getAbsolutePath());
//        }
//
//        System.out.println();
//
//        System.out.println("String[] list(): ");
//        String[] paths = dir.list();
//        if(paths[0].equals(maSV+".docx")){
//            return ResponseEntity.ok(paths[0]);
//            }else
//                if(paths.length>1){
//                    if(paths[1].equals(maSV+".docx")){
//                        return ResponseEntity.ok(paths[1]);
//                        }else {
//                                return ResponseEntity.ok("");
//                            }
//                }else
//                    if(paths.length>2){
//                        if(paths[2].equals(maSV+".docx")){
//                            return ResponseEntity.ok(paths[2]);
//                        }else {
//                            return ResponseEntity.ok("");
//                        }
//                    }else
//                        if(paths.length>3){
//                            if(paths[3].equals(maSV+".docx")){
//                                return ResponseEntity.ok(paths[3]);
//                            }else {
//                                return ResponseEntity.ok("");
//                            }
//                        }
//                        else
//                            if(paths.length>4){
//                                if(paths[4].equals(maSV+".docx")){
//                                    return ResponseEntity.ok(paths[4]);
//                                }else {
//                                    return ResponseEntity.ok("");
//                                }
//                            }else {
//                                return ResponseEntity.ok("");
//                            }
//
//    }
//    @GetMapping("/download/{maSV}")
//    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String maSV) throws IOException {
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky, sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        File dir = new File(uploadDir + "/DeTai" + maDT);
//        String[] paths = dir.list();
//        if (paths == null || paths.length == 0) {
//            throw new FileNotFoundException("File not found");
//        }
//
//        File file = new File(uploadDir + "/DeTai" + maDT + "/" + paths[0]);
//
//
//        byte[] data = FileUtils.readFileToByteArray(file);
//        ByteArrayResource resource = new ByteArrayResource(data);
//
//        String encodedFilename = URLEncoder.encode(paths[0], StandardCharsets.UTF_8.toString()).replace("+", "%20");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(data.length)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
////        @GetMapping("/download/{maSV}")
////        public ResponseEntity<?> downloadFile(@PathVariable String maSV) throws IOException {
////            Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
////            SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
////            SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky, sinhVien);
////            Integer maDT = sinhVien_deTai.getDeTai().getId();
////            File dir = new File(uploadDir + "/DeTai" + maDT);
////            String[] paths = dir.list();
////            if (paths == null || paths.length == 0) {
////                throw new FileNotFoundException("File not found");
////            }
////
////            File file = new File(uploadDir + "/DeTai" + maDT + "/" + maSV+".docx");
////            if (!file.isFile()) {
////                HttpHeaders headers = new HttpHeaders();
////                headers.setContentType(MediaType.TEXT_PLAIN);
////                return new ResponseEntity<>("Sinh vien chua nop bai", headers, HttpStatus.NOT_FOUND);
////            }
////
////            byte[] data = FileUtils.readFileToByteArray(file);
////            ByteArrayResource resource = new ByteArrayResource(data);
////
////            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");
////
////            HttpHeaders headers = new HttpHeaders();
////            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
////
////            return ResponseEntity.ok()
////                    .headers(headers)
////                    .contentLength(data.length)
////                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
////                    .body(resource);
////        }
//
//
//    @GetMapping("/testUpload")
//    public String test() {
//        return "Test upload endpoint is working";
//    }
//}

//package com.example.doan2.controller;
//
//import com.example.doan2.entity.Ky;
//import com.example.doan2.entity.SinhVien;
//import com.example.doan2.entity.SinhVien_DeTai;
//import com.example.doan2.repository.KyRepository;
//import com.example.doan2.repository.SinhVienRepository;
//import com.example.doan2.repository.SinhVien_DeTaiRepository;
//import com.example.doan2.until.FileDownloadUntil;
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@RestController
//@CrossOrigin
//public class DeCuongController {
//
//    @Autowired
//    private SinhVien_DeTaiRepository sinhVien_deTaiRepo;
//
//    @Autowired
//    private SinhVienRepository sinhVienRepo;
//
//    @Autowired
//    private KyRepository kyRepo;
//    // Đọc thư mục gốc để lưu file từ cấu hình
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Autowired
//    private FileDownloadUntil fileDownloadUntil;
//    @PostMapping("/uploadFile/{maSV}")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String maSV) {
//        // Lấy tên file
//        String fileName = file.getOriginalFilename();
//        try {
//            Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//            SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//            SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//            String tenDeTai = sinhVien_deTai.getDeTai().getTenDeTai();
//            Integer maDeTai = sinhVien_deTai.getDeTai().getId();
//            // Tạo đường dẫn thư mục con
//            Path folderPath = Paths.get(uploadDir, "DeTai"+maDeTai );
//
//            // Kiểm tra và tạo thư mục nếu chưa tồn tại
//            if (!Files.exists(folderPath)) {
//                Files.createDirectories(folderPath);
//            }
//
//            // Tạo đường dẫn lưu file trong thư mục con
//            Path filePath = folderPath.resolve(fileName);
//
//            // Lưu file vào thư mục con
//            Files.write(filePath, file.getBytes());
//
//            return ResponseEntity.ok("File uploaded successfully to folder: " + filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("File upload failed: " + fileName);
//        }
//    }
//    public List<File> listFiles(String uploadDir, String maSV) throws IOException {
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        Path dirPath = Paths.get(uploadDir, "DeTai"+ maDT);
//
//        // Try-with-resources to ensure the stream is closed
//        try (Stream<Path> paths = Files.list(dirPath)) {
//            return paths
//                    .filter(Files::isRegularFile) // filter only regular files (not directories or other types)
//                    .map(Path::toFile) // convert Path to File
//                    .collect(Collectors.toList());
//        }
//    }
//
//    @GetMapping("getFilebyMaSV/{maSV}")
//    public ResponseEntity<?> getFilebyMaSV(@PathVariable String maSV) throws IOException {
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getId();
//        List<File> files = listFiles(uploadDir,maSV);
//        String content = new String(Files.readAllBytes(files.get(0).toPath()));
//        return ResponseEntity.ok(content);
//    }
//    @GetMapping("/getFileName/{maSV}")
//    public ResponseEntity<?> getFileName(@PathVariable String maSV) {
//
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        System.out.println("File[] listFiles(): ");
//        File dir = new File(uploadDir+"/DeTai"+maDT);
//        File[] children = dir.listFiles();
//        for (File file : children) {
//            System.out.println(file.getAbsolutePath());
//        }
//
//        System.out.println();
//
//        System.out.println("String[] list(): ");
//        String[] paths = dir.list();
//        for (String path : paths) {
//            System.out.println(path);
//        }
//        return ResponseEntity.ok(paths[0]);
//    }
//    @GetMapping("/download/{maSV}")
//    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String maSV) throws IOException {
//
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        File dir = new File(uploadDir+"/DeTai"+maDT);
//        String[] paths = dir.list();
//        // Tạo một đối tượng File từ tên tập tin được yêu cầu.
//        File file = new File(uploadDir+"/DeTai"+maDT+"/" + paths[0]);
//
//        // Đọc dữ liệu từ File vào một mảng byte.
//        byte[] data = FileUtils.readFileToByteArray(file);
//
//        // Tạo một đối tượng ByteArrayResource từ mảng byte.
//        ByteArrayResource resource = new ByteArrayResource(data);
//
//        // Thiết lập các header để trình duyệt có thể tải xuống file.
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=" + paths[0]);
//
//        // Trả về đối tượng ResponseEntity bao lấy ByteArrayResource và các header tương ứng.
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(data.length)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
//
//    @GetMapping("/testUpload")
//    public String test() {
//        return "Test upload endpoint is working";
//    }
//}

package com.example.doan2.controller;

import com.example.doan2.dto.deCuong.DeCuongDTO;
import com.example.doan2.dto.deTai.DeTaiDTO;
import com.example.doan2.dto.giangvien.GiangVienDTO;
import com.example.doan2.entity.*;
import com.example.doan2.repository.*;
import com.example.doan2.req.deCuongReq.DSDeTaiReq;
import com.example.doan2.until.DateUntil;
import com.example.doan2.until.FileDownloadUntil;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin
public class DeCuongController {

    @Autowired
    private SinhVien_DeTaiRepository sinhVien_deTaiRepo;

    @Autowired
    private SinhVienRepository sinhVienRepo;

    @Autowired
    private KyRepository kyRepo;

    @Autowired
    private TrangThaiDeCuongRepository trangThaiDeCuongRepo;

    @Autowired
    private ThongBaoRepository thongBaoRepo;

    @Autowired
    private DeTaiRepository deTaiRepo;

    @Autowired
    private DateUntil dateUntil;

    @Autowired
    private GiangVienRepository giangVienRepo;

    @Autowired
    private ModelMapper modelMapper;

    // Đọc thư mục gốc để lưu file từ cấu hình
    @Value("${file.upload-dir}")
    private String uploadDir;


    @Autowired
    private FileDownloadUntil fileDownloadUntil;
    @PostMapping("/uploadFile/{maSV}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String maSV) {
        // Lấy tên file
        String fileName = file.getOriginalFilename();
        try {
            Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
            SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
            SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
            String tenDeTai = sinhVien_deTai.getDeTai().getTenDeTai();
            Integer maDeTai = sinhVien_deTai.getDeTai().getId();
            // Tạo đường dẫn thư mục con
            Path folderPath = Paths.get(uploadDir, "DeTai"+maDeTai );

            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // Tạo đường dẫn lưu file trong thư mục con
            Path filePath = folderPath.resolve(fileName);

            // Lưu file vào thư mục con
            Files.write(filePath, file.getBytes());
            TrangThaiDeCuong trangThaiDeCuong = new TrangThaiDeCuong();
            trangThaiDeCuong.setTrangThai("Chờ duyệt");
            trangThaiDeCuong.setThoiGianNop( dateUntil.getDateNow());
            trangThaiDeCuong.setDeTai_Id(maDeTai);
            trangThaiDeCuong.setMaSV(sinhVien.getMaSV());
            trangThaiDeCuong.setKy_id(ky.getId());
            trangThaiDeCuongRepo.save(trangThaiDeCuong);

            return ResponseEntity.ok("File uploaded successfully to folder: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + fileName);
        }
    }


    //    public List<File> listFiles(String uploadDir, String maSV) throws IOException {
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        Path dirPath = Paths.get(uploadDir, "DeTai"+ maDT);
//
//        // Try-with-resources to ensure the stream is closed
//        try (Stream<Path> paths = Files.list(dirPath)) {
//            return paths
//                    .filter(Files::isRegularFile) // filter only regular files (not directories or other types)
//                    .map(Path::toFile) // convert Path to File
//                    .collect(Collectors.toList());
//        }
//    }
//
//    @GetMapping("getFilebyMaSV/{maSV}")
//    public ResponseEntity<?> getFilebyMaSV(@PathVariable String maSV) throws IOException {
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
//        Integer maDT = sinhVien_deTai.getId();
//        List<File> files = listFiles(uploadDir,maSV);
//        String content = new String(Files.readAllBytes(files.get(0).toPath()));
//        return ResponseEntity.ok(content);
//    }
    @GetMapping("/getFileName/{maSV}")
    public ResponseEntity<?> getFileName(@PathVariable String maSV) {

        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien);
        if(sinhVien_deTai==null){
            return ResponseEntity.ok("Sinh vien chua co nhom");
        }
        Integer maDT = sinhVien_deTai.getDeTai().getId();
        System.out.println("File[] listFiles(): ");
        File dir = new File(uploadDir+"/DeTai"+maDT);
        File[] children = dir.listFiles();
        for (File file : children) {
            System.out.println("các đường path con:"+file.getAbsolutePath());
        }

        System.out.println();

        System.out.println("String[] list(): ");
        String[] paths = dir.list();
        if(paths[0].equals(maSV+".docx")){
            return ResponseEntity.ok(paths[0]);
        }else
        if(paths.length>1){
            if(paths[1].equals(maSV+".docx")){
                return ResponseEntity.ok(paths[1]);
            }else {
                return ResponseEntity.ok("");
            }
        }else
        if(paths.length>2){
            if(paths[2].equals(maSV+".docx")){
                return ResponseEntity.ok(paths[2]);
            }else {
                return ResponseEntity.ok("");
            }
        }else
        if(paths.length>3){
            if(paths[3].equals(maSV+".docx")){
                return ResponseEntity.ok(paths[3]);
            }else {
                return ResponseEntity.ok("");
            }
        }
        else
        if(paths.length>4){
            if(paths[4].equals(maSV+".docx")){
                return ResponseEntity.ok(paths[4]);
            }else {
                return ResponseEntity.ok("");
            }
        }else {
            return ResponseEntity.ok("");
        }

    }

    // sửa bảng trạn thái để cương , thêm mã sinh viên , mã đề tài , đường path vào, khi nộp file thì lưu vào
    // lấy ra tất cả các tên file theo đề tài , mô tả
    @GetMapping("/getAllFileName")
    public ResponseEntity<?> getAllFileName(){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<DeTai>  deTaiList = sinhVien_deTaiRepo.getAllDeTaiByKy(ky);
        List<Integer> idList = new ArrayList<>();
        deTaiList.forEach(deTai -> {
            Integer id = deTai.getId();
            idList.add(id);
        });
        List<DeCuongDTO> deCuongDTOS = new ArrayList<>();
        idList.forEach(eachid -> {
            DeCuongDTO deCuongDTO = new DeCuongDTO();
            Optional<DeTai> deTai = deTaiRepo.findById(eachid);
            deCuongDTO.setTenDeTai(deTai.get().getTenDeTai());
            deCuongDTO.setMoTa(deTai.get().getMoTa());
            deCuongDTO.setNamHoc(ky.getNamHoc());
            deCuongDTO.setTenKy(ky.getTenKy());
            String trangThai = trangThaiDeCuongRepo.getTrangThaiDeCuong(deTai.get().getId(),ky.getId());
            deCuongDTO.setTrangThai(trangThai);
            File dir = new File(uploadDir+"/DeTai"+eachid);
            String[] paths = dir.list();
            if (paths!=null){
                deCuongDTO.setTenFile(List.of(paths));
            }else
                deCuongDTO.setTenFile(null);

            deCuongDTOS.add(deCuongDTO);
        });
        return ResponseEntity.ok(deCuongDTOS);
    }
//    @GetMapping("/download/{maSV}")
//    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String maSV) throws IOException {
//        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
//        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
//        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky, sinhVien);
//        Integer maDT = sinhVien_deTai.getDeTai().getId();
//        File dir = new File(uploadDir + "/DeTai" + maDT);
//        String[] paths = dir.list();
//        if (paths == null || paths.length == 0) {
//            throw new FileNotFoundException("File not found");
//        }
//
//        File file = new File(uploadDir + "/DeTai" + maDT + "/" + paths[0]);
//
//
//        byte[] data = FileUtils.readFileToByteArray(file);
//        ByteArrayResource resource = new ByteArrayResource(data);
//
//        String encodedFilename = URLEncoder.encode(paths[0], StandardCharsets.UTF_8.toString()).replace("+", "%20");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(data.length)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
        @GetMapping("/download/{maSV}")
        public ResponseEntity<?> downloadFile(@PathVariable String maSV) throws IOException {
            Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
            SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
            SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky, sinhVien);
            Integer maDT = sinhVien_deTai.getDeTai().getId();
            File dir = new File(uploadDir + "/DeTai" + maDT);
            String[] paths = dir.list();
            if (paths == null || paths.length == 0) {
                throw new FileNotFoundException("File not found");
            }

            File file = new File(uploadDir + "/DeTai" + maDT + "/" + maSV+".docx");
            if (!file.isFile()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity<>("Sinh vien chua nop bai", headers, HttpStatus.NOT_FOUND);
            }

            byte[] data = FileUtils.readFileToByteArray(file);
            ByteArrayResource resource = new ByteArrayResource(data);

            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }

        // download file bằng tenfile cho giảng viên chấm
        @GetMapping("/downloadFilebyFileName/{tenDeTai}/{moTa}/{tenFile}")
        public ResponseEntity<?> downloadFilebyTenFile(@PathVariable String tenFile,@PathVariable String tenDeTai,@PathVariable String moTa) throws IOException {
            Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
            DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
            Integer maDT = deTai.getId();
            File dir = new File(uploadDir + "/DeTai" + maDT);
            String[] paths = dir.list();
            if (paths == null || paths.length == 0) {
                throw new FileNotFoundException("File not found");
            }

            File file = new File(uploadDir + "/DeTai" + maDT + "/" + tenFile);
            if (!file.isFile()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity<>("Sinh vien chua nop bai", headers, HttpStatus.NOT_FOUND);
            }

            byte[] data = FileUtils.readFileToByteArray(file);
            ByteArrayResource resource = new ByteArrayResource(data);

            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }


    // xóa file đề cương
    @DeleteMapping("XoaDeCuong/{maSV}")
    public ResponseEntity<?> XoaDeCuong(@PathVariable String maSV) throws FileNotFoundException {
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        SinhVien sinhVien = sinhVienRepo.findByMaSV(maSV);
        SinhVien_DeTai sinhVien_deTai = sinhVien_deTaiRepo.findByKyAndSinhVien(ky, sinhVien);
        Integer maDT = sinhVien_deTai.getDeTai().getId();
        File dir = new File(uploadDir + "/DeTai" + maDT);
        String[] paths = dir.list();
        if (paths == null || paths.length == 0) {
            throw new FileNotFoundException("File not found");
        }

        File file = new File(uploadDir + "/DeTai" + maDT + "/" + maSV+".docx");
        if (file.exists()){
            file.delete();
            return ResponseEntity.ok("Xóa file thành công");
        }
        return ResponseEntity.ok("Không có bài làm nào để xóa");
    }


    @GetMapping("/testUpload")
    public String test() {
        return "Test upload endpoint is working";
    }

    // lấy thời gian còn lại để nộp đề cương
    @GetMapping("/getNgayConLai")
    public ResponseEntity<?> getNgayConLai() {
        Date dateHan = thongBaoRepo.GetNgayKetThuc();
        Date now = dateUntil.getDateNow();

        long thoiGian = dateHan.getTime() - now.getTime(); // đơn vị là mili giây

        long giay = (thoiGian / 1000) % 60;
        long phut = (thoiGian / (60 * 1000)) % 60;
        long gio = (thoiGian / (60 * 60 * 1000)) % 24;
        long day = thoiGian / (24 * 60 * 60 * 1000);

        return ResponseEntity.ok(day + " ngày " + gio + " giờ " + phut + " phút " + giay + " giây");
    }
    // lấy ra hạn chót
    @GetMapping("/getHanChot")
    public ResponseEntity<?> getHanChot() throws ParseException {
        Date date = thongBaoRepo.GetNgayKetThuc();
        //set the format to use as a constructor argument
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        String d1= format.format(date);
        return ResponseEntity.ok(d1);
    }

    // lấy ra trạng thái đề cương theo mã sinh viên
    @GetMapping("/getTrangThaiDCbyMASV/{maSV}")
    public ResponseEntity<?> getTrangThaiDCbyMaSV(@PathVariable String maSV){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        TrangThaiDeCuong trangThaiDeCuong = trangThaiDeCuongRepo.getTrangThaiDCbyMaSVKy(maSV,ky.getId());
        if (trangThaiDeCuong==null){
            return ResponseEntity.ok("Sinh viên chưa nộp bài");
        }
        return ResponseEntity.ok(trangThaiDeCuong.getTrangThai());
    }

    // khi nhấn duyệt đề cương
    @PutMapping("/duyetDC/{tenDeTai}/{moTa}")
    public ResponseEntity<?> duyetDeCuong(@PathVariable String tenDeTai,@PathVariable String moTa){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<TrangThaiDeCuong> trangThaiDeCuong = trangThaiDeCuongRepo.getDCbyDeTaiKy(deTai.getId(),ky.getId());
        trangThaiDeCuong.forEach(trangThaiDeCuong1 -> {
            trangThaiDeCuong1.setTrangThai("Đã duyệt");
            trangThaiDeCuongRepo.save(trangThaiDeCuong1);
        });

        return ResponseEntity.ok("Duyệt đề cương thành công");
    }

    // khi nhấn nút hủy
    @PutMapping("/huyDC/{tenDeTai}/{moTa}")
    public ResponseEntity<?> huyDeCuong(@PathVariable String tenDeTai,@PathVariable String moTa){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<TrangThaiDeCuong> trangThaiDeCuong = trangThaiDeCuongRepo.getDCbyDeTaiKy(deTai.getId(),ky.getId());
        trangThaiDeCuong.forEach(trangThaiDeCuong1 -> {
            trangThaiDeCuong1.setTrangThai("Đã hủy");
            trangThaiDeCuongRepo.save(trangThaiDeCuong1);
        });

        return ResponseEntity.ok("Hủy đề cương thành công");
    }

    // nhấn nút yêu cầu chỉnh sửa
    @PutMapping("/yeuCauChinhSuaDC/{tenDeTai}/{moTa}")
    public ResponseEntity<?> yeuCauChinhSuaDc(@PathVariable String tenDeTai,@PathVariable String moTa){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<TrangThaiDeCuong> trangThaiDeCuong = trangThaiDeCuongRepo.getDCbyDeTaiKy(deTai.getId(),ky.getId());
        trangThaiDeCuong.forEach(trangThaiDeCuong1 -> {
            trangThaiDeCuong1.setTrangThai("Yêu cầu chỉnh sửa");
            trangThaiDeCuongRepo.save(trangThaiDeCuong1);
        });

        return ResponseEntity.ok("Yêu cầu chỉnh sửa đề cương thành công");
    }

    // thêm ghi chú
    @PutMapping("/themGhiChuDC/{tenDeTai}/{moTa}/{ghiChu}")
    public ResponseEntity<?> themGhiChuDC(@PathVariable String tenDeTai, @PathVariable String moTa,@PathVariable String ghiChu){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<TrangThaiDeCuong> trangThaiDeCuong = trangThaiDeCuongRepo.getDCbyDeTaiKy(deTai.getId(),ky.getId());
        trangThaiDeCuong.forEach(trangThaiDeCuong1 -> {
            trangThaiDeCuong1.setGhiChu(ghiChu);
            trangThaiDeCuongRepo.save(trangThaiDeCuong1);
        });
        return ResponseEntity.ok("Thêm ghi chú thành công");
    }
    // lấy ra những đề tài chưa có giảng viên chấm
    @GetMapping("/deTaiChuaCoGVCham")
    public ResponseEntity<?> getDeTaiChuaCoGVCham(){
        Ky ky= kyRepo.findByTrangThai("Đang tiến hành");
        List<Integer> deTai_ids= trangThaiDeCuongRepo.deTaiIDListChuacoGVCham(ky.getId());
        List<DeTaiDTO> deTaiDTOS = new ArrayList<>();
        for(Integer deTaiid:deTai_ids){
            Optional<DeTai> deTai = deTaiRepo.findById(deTaiid);
            DeTaiDTO deTaiDTO = new DeTaiDTO();
            deTaiDTO.setTenDeTai(deTai.get().getTenDeTai());
            deTaiDTO.setMoTa(deTai.get().getMoTa());
            deTaiDTOS.add(deTaiDTO);
        }
        return ResponseEntity.ok(deTaiDTOS);
    }
    // lấy ra trạng thái của giảng viên : giảng viên đó đã được phân công hay chưa
    @GetMapping("/getTrangThaiGV/{maGV}")
    public ResponseEntity<?> getTrangThaiGV(@PathVariable String maGV){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        GiangVien giangVien = giangVienRepo.findByMaGV(maGV);
        if (giangVien==null){
            return ResponseEntity.ok("Không timd thấy giảng viên");
        }
        List<TrangThaiDeCuong> trangThaiDeCuongs = trangThaiDeCuongRepo.getTrangThaiDeCuongbyMaGv(maGV,ky.getId());
        if (trangThaiDeCuongs==null){
            return ResponseEntity.ok("GV chưa được phân công");
        }
        return ResponseEntity.ok("GV đã được phân công");
    }
    // lấy ra tất cả giảng viên
    @GetMapping("/getALlGV")
    public ResponseEntity<?> getAllGV(){
        List<GiangVien> giangVienList = giangVienRepo.findAll();
        List<GiangVienDTO> giangVienDTOS = modelMapper.map(giangVienList, new TypeToken<List<GiangVienDTO>>(){}.getType());
        return ResponseEntity.ok(giangVienDTOS);
    }

    // cap nhat giang vien cham diem
    // lấy ra những đề cương chưa đc chấm điểm -> những đề tài và những giáng viên chưa đc phân công chấm điểm
    @PutMapping("/themGiangVienChamDiem")
    public ResponseEntity<?> capNhatGiangVienCham(@RequestBody DSDeTaiReq deTaiReq){
        List<DeTai> deTaiList = deTaiReq.getDeTaiList();
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        deTaiList.forEach(deTai -> {
            List<TrangThaiDeCuong> trangThaiDeCuong = trangThaiDeCuongRepo.getDCbyDeTaiKy(deTai.getId(),ky.getId());
            trangThaiDeCuong.forEach(trangThaiDeCuong1 -> {
                trangThaiDeCuong1.setMaGV(deTaiReq.getMaGV());
                trangThaiDeCuongRepo.save(trangThaiDeCuong1);
            });
        });
        return ResponseEntity.ok("Caaph nhật giảng viên chấm điểm thành công");
    }

}
