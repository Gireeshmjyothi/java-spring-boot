package com.example.practice.controller;
import com.example.practice.dto.ResponseDto;
import com.example.practice.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
@Slf4j
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/download-excel")
    public ResponseDto<String> downloadExcel(){
        log.info("Received request to generate excel sheet.");
        return excelService.generateExcel();
    }
}
