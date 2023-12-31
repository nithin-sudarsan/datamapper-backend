package com.example.datamapperdemo.controller;

import lombok.RequiredArgsConstructor;
import org.perfios.DataMapper;
import org.perfios.DataMapperImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("api/dataMapper")
@RequiredArgsConstructor
public class Controller {
    @PostMapping("/file")
    public String mapperDemo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("rules") MultipartFile rules,
            @RequestParam("format") String format
    ) {
        DataMapper dataMapper = new DataMapperImpl();
        String rulesFile = null;
        String inputFile = null;
        String transformed = null;
        try {
            byte[] bytes = file.getBytes();
            inputFile = new String(bytes, StandardCharsets.UTF_8);

            byte[] csv_bytes = rules.getBytes();
            rulesFile = new String(csv_bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Could not upload files: " + e.getMessage());
        }
        if (format.equals("JSON")) {
            transformed = dataMapper.getTransformedString(inputFile, rulesFile, DataMapperImpl.Extension.JSON);
        } else if (format.equals("XML")) {
            transformed = dataMapper.getTransformedString(inputFile, rulesFile, DataMapperImpl.Extension.XML);
        }
        System.out.println(transformed);
        return transformed;
    }
    @PostMapping("/str")
    public String mapperDemo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("rules") String rules,
            @RequestParam("format") String format
    ) {
        DataMapper dataMapper = new DataMapperImpl();
        String inputFile = null;
        String transformed = null;
        try {
            byte[] bytes = file.getBytes();
            inputFile = new String(bytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.out.println("Could not upload files: " + e.getMessage());
        }
        if (format.equals("JSON")) {
            transformed = dataMapper.getTransformedString(inputFile, rules, DataMapperImpl.Extension.JSON);
        } else if (format.equals("XML")) {
            transformed = dataMapper.getTransformedString(inputFile, rules, DataMapperImpl.Extension.XML);
        }
        System.out.println(transformed);
        return transformed;
    }
    @PostMapping("/mapstruct")
    public String generateMapStructInterface(@RequestParam("rules") MultipartFile rules){
        DataMapper dataMapper = new DataMapperImpl();
        try {
            File tempFile = File.createTempFile("temp", ".txt");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(rules.getBytes());
            }
            String mapperInterface = dataMapper.generateMapStructInterfaceString(tempFile);
            tempFile.delete();
            return mapperInterface;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

