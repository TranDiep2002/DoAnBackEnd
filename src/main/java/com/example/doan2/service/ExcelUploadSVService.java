package com.example.doan2.service;

import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import com.example.doan2.until.DateUntil;
import org.apache.commons.math3.analysis.function.Sinh;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ExcelUploadSVService {

    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ;
    }

    public static List<SinhVien> getSinhVienFromExcel(InputStream inputStream, String sheetName){
        List<SinhVien> sinhVienList= new ArrayList<>();
        try {
            XSSFWorkbook workbook= new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            int rowIndex =0;
            for(Row row: sheet){
                if(rowIndex==0){
                    rowIndex++;
                    continue; // lệnh này là bỏ qua
                }
                Iterator<Cell> cellIterator= row.iterator();
                int cellIndex=0;
                SinhVien sinhVien = new SinhVien();
                while (cellIterator.hasNext()){
                    Cell cell= cellIterator.next();
                    switch (cellIndex){
                        case 0-> sinhVien.setId((int) cell.getNumericCellValue());
                        case 1-> sinhVien.setEmail(cell.getStringCellValue());
                        case 2-> sinhVien.setHoTen(cell.getStringCellValue());
                        case 3-> sinhVien.setMaSV(cell.getStringCellValue());
                        case 4-> sinhVien.setNganh(cell.getStringCellValue());
                        case 5-> sinhVien.setGioiTinh(cell.getStringCellValue());
                        case 6-> sinhVien.setLopHanhChinh(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                System.out.print("Sinh Viên từ file:"+ sinhVien.getHoTen());
            sinhVienList.add(sinhVien);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sinhVienList;
    }
}
