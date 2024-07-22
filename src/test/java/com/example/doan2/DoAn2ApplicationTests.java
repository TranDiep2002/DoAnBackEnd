package com.example.doan2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DoAn2ApplicationTests {

    @Test
    @Rollback(value = false)
    void testInsertDocument() throws IOException {
//        File file = new File("C:\\Users\\admin\\Documents\\layoutmain_android.docx");
//
//        DeCuong document = new DeCuong();
//        document.setName(file.getName());
//
//        byte[] bytes= Files.readAllBytes(file.toPath());
//        document.setContent(bytes);
//        Long fileSize =(long) bytes.length;
//        document.setSize(fileSize);
//
//        document.setUploadTime(new Date());
//
//        DeCuong saveDoc = documentRepo.save(document);
//
//        DeCuong exitsDoc = entityManager.find(DeCuong.class, saveDoc.getId());
//        assert(exitsDoc.getSize()).equals(fileSize);

    }


}
