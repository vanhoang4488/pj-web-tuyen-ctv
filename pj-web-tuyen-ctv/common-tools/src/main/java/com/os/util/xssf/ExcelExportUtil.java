package com.os.util.xssf;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.*;

import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

public abstract class ExcelExportUtil {

    public static XSSFWorkbook genericExcel(String sheetName, List<String[]> dataList){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        String[] headerNames = dataList.get(0);
        CTTable ctTable = createTable(sheet, dataList.size(), headerNames.length);

        setTableHeader(ctTable, headerNames);

        addTableData(workbook, sheetName, dataList);
        return workbook;
    }

    private static CTTable createTable(XSSFSheet sheet, int rows, int cols){
        XSSFTable table = sheet.createTable(null);

        // cấu hình bảng
        CTTable ctTable = table.getCTTable();
        ctTable.setId(1);
        String bottomRight = String.valueOf((char) (64 + cols)) + rows;
        ctTable.setRef("A1:" + bottomRight);
        ctTable.setTotalsRowShown(true);

        CTTableStyleInfo styleInfo = ctTable.addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);
        return ctTable;
    }

    /**
     * Cài đặt cấu hình header
     * @param ctTable
     * @param headerNames
     */
    private static void setTableHeader(CTTable ctTable, String[] headerNames){
        CTTableColumns headers = ctTable.addNewTableColumns();
        CTAutoFilter autoFilter = ctTable.addNewAutoFilter();
        headers.setCount(headerNames.length);
        for (int i = 1; i <= headerNames.length; i++){
            CTTableColumn header = headers.addNewTableColumn();
            header.setId(i);
            header.setName("Column" + i);
            // add table heading drop-down
            CTFilterColumn filterColumn = autoFilter.addNewFilterColumn();
            filterColumn.setColId(i);
            filterColumn.setShowButton(true);
        }
    }

    private static void addTableData(XSSFWorkbook workbook, String sheetName, List<String[]> dataList) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        XSSFCellStyle style = getCellStyle(workbook);
        IntStream.range(0, dataList.size()).forEach(index -> {
            XSSFRow row = sheet.createRow(index);
            String[] cellValues = dataList.get(index);
            genExcelCell(row, cellValues, style);
        });
    }

    private static void genExcelCell(XSSFRow row, String[] cellValues, XSSFCellStyle style){
        for(int i = 0; i < cellValues.length; i++){
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(cellValues[i]);
            cell.setCellStyle(style);
        }
    }

    private static XSSFCellStyle getCellStyle(XSSFWorkbook workbook){
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
//        font.setFontHeightInPoints((short ) 14);
//        font.setFontName("Times New Roman");
        style.setFont(font);
        return style;
    }
}
