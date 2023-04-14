package com.bats.lite.aop.config;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

@Service
public class ExcelUtils {

    public Font createHeaderFont(Workbook workbook) {
        var headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        return headerFont;
    }

    public CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(createHeaderFont(workbook));

        headerCellStyle.setFillBackgroundColor(IndexedColors.SEA_GREEN.getIndex());
        headerCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return headerCellStyle;
    }
}
