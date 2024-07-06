package com.example.doan2.until;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DateUntil {
     public  Date getDateNow(){
        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();
        // Định dạng thời gian
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
         String formattedDateTime = now.format(formatter);
        System.out.println("Thời gian hiện tại: " + formattedDateTime);
        // Chuyển đổi LocalDateTime sang Date
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return  date;
    }

}
