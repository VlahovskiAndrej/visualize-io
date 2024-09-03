package com.finki.datavisualization.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileUploadController {

    @GetMapping("/visual")
    public String visual() {
        return "visual";
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                String label = row.getCell(0).getStringCellValue();
                labels.add(label);

                Cell dataCell = row.getCell(1);
                if (dataCell.getCellType() == CellType.NUMERIC) {
                    data.add((int) dataCell.getNumericCellValue());
                } else if (dataCell.getCellType() == CellType.STRING) {
                    try {
                        data.add(Integer.parseInt(dataCell.getStringCellValue()));
                    } catch (NumberFormatException e) {
                        data.add(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "result";
    }

//    @PostMapping("/upload")
//    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//            String line;
//            List<Map<String, Object>> chartData = new ArrayList<>();
//            // Skip the header line
//            String header = reader.readLine();
//            String[] headers = header.split(",");
//
//            // Parse the CSV file
//            while ((line = reader.readLine()) != null) {
//                String[] columns = line.split(",");
//                Map<String, Object> dataPoint = new HashMap<>();
//                dataPoint.put("label", columns[0]);  // Assuming first column "Bankrupt?" as label
//                dataPoint.put("value", Double.parseDouble(columns[1]));  // Visualize the "Operating Expense Rate"
//                chartData.add(dataPoint);
//            }
//
//            model.addAttribute("chartData", chartData);
//            model.addAttribute("headers", headers);  // Pass headers for potential dropdown or selection
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "visual";
//    }
}

