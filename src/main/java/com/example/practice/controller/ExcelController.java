package com.example.practice.controller;
import com.example.practice.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
@Slf4j
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/download-excel")
    public ResponseEntity<String> downloadExcel() throws IOException {
        log.info("Request received for download excel.");
       String excelContent = excelService.generateExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
    }
}
