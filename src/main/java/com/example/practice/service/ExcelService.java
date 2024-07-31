package com.example.practice.service;

import com.example.practice.entity.User;
import com.example.practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService {

    private final UserRepository userRepository;

    public String generateExcel() throws IOException {
        log.info("generating excel sheet..");
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        String[] header = { "ID", "FirstName", "LastName", "Email" , "Phone"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }

        int rowNum = 1;
        int pageSize = 100;
        boolean hasMoreData = true;
        int pageNumber = 0;

        while (hasMoreData) {
            Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
            hasMoreData = userPage.hasNext();
            pageNumber++;

            for (User user : userPage.getContent()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(user.getId()));
                row.createCell(1).setCellValue(user.getFirstName());
                row.createCell(2).setCellValue(user.getLastName());
                row.createCell(3).setCellValue(user.getEmail());
                row.createCell(4).setCellValue(user.getPhoneNumber());
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.dispose();
        workbook.close();

        byte[] excelBytes = outputStream.toByteArray();
        String base64Encoded = Base64.getEncoder().encodeToString(excelBytes);
        log.info("Length of  byte code {}",excelBytes.length);
        log.info("Length of base64 code {}", base64Encoded.length());
        return base64Encoded;
    }
}
