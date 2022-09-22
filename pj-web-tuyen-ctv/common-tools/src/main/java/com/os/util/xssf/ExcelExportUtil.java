package com.os.util.xssf;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

public abstract class ExcelExportUtil {

    public static XSSFWorkbook genExcelSheet(String sheetName, List<String> capNames){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFCellStyle style = workbook.createCellStyle();

        XSSFFont font = new XSSFFont();
        font.setFamily(FontFamily.ROMAN);
        style.setFont(font);

        genExcelRowHeader(sheet, workbook.getStylesSource(), capNames);
        return workbook;
    }

    private static void genExcelRowHeader(XSSFSheet sheet, StylesTable stylesTable, List<String> capNames){
        XSSFCellStyle headerStyle = new XSSFCellStyle(stylesTable);
        headerStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());

        XSSFFont font = new XSSFFont();
        font.setBold(true);
        headerStyle.setFont(font);

        XSSFRow row = sheet.createRow(1);
        row.setRowStyle(headerStyle);

        int cellPos = 1;
        for(String caption : capNames){
            XSSFCell cell = row.createCell(cellPos, CellType.STRING);
            cell.setCellValue(caption);
        }
    }

    /**
     * thêm lần lượt từng ô dữ liệu trong hàng.
     * @param sheet
     * @param cellValues
     */
    public static void addDataCell(XSSFSheet sheet, int rowPos, List<String> cellValues){
        XSSFRow row = sheet.createRow(rowPos);

        int cellPos = 1;
        for(String cellValue : cellValues){
            XSSFCell cell = row.createCell(cellPos, CellType.STRING);
            cell.setCellValue(cellValue);
        }
    }
}
