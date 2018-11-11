package com.learn.lhh.Util;
import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import jdk.internal.org.xml.sax.InputSource;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class ExcelUtilWithHSSF extends FileUtil {
    /**
     * @param file
     * @param sheetName
     * @param rowNum(行号）
     * @param cellNum（列号）
     * @return （指定单元格的值，string类型）
     */
    public static String getCellValue(String file, String sheetName, int rowNum, int cellNum) {

        HSSFCell cell = getOneSheet(file, sheetName).getRow(rowNum).getCell(cellNum);
        String cellStringValue = getCellSringValue(cell);

        return cellStringValue;
    }

    //读取excel中指定行的数据
    public static Map<Integer, String> getOneLineData(String file, String sheetName, int rowNum) {
        Map<Integer, String> rowMap = new HashMap<Integer, String>();
        HSSFRow row = getOneSheet(file, sheetName).getRow(rowNum);
        int lastCell = row.getLastCellNum();
        for (int i = 0; i < lastCell; i++) {
            String cellValue = getCellSringValue(row.getCell(i));
            rowMap.put(i, cellValue);
        }
        return rowMap;
    }

    //在指定的sheet中查找数据
    public static boolean findValueFromSheet(String file, String sheetName, String value) throws Exception {
        boolean flag = false;
        InputStream in = ClassLoader.class.getResourceAsStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(in);
        int sheetsNum = wb.getNumberOfSheets();
        for (int i = 0; i < sheetsNum; i++) {
            HSSFSheet sheet = wb.getSheet(sheetName);
            int rowNum = sheet.getLastRowNum();
            for (int r = 0; r < rowNum; r++) {
                HSSFRow row = sheet.getRow(r);
                int cellNum = row.getLastCellNum();
                for (int l = 0; l < cellNum; l++) {
                    if (getCellSringValue(row.getCell(l)).equals(value)) {
                        flag = true;
                    }
                }
            }

        }
        return flag;
    }

    //在excel中查找数据
    public static boolean findValueFromExcel(String file, String value) throws Exception {
        boolean flag = false;
        InputStream in = ClassLoader.class.getResourceAsStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(in);
        int sheetsNum = wb.getNumberOfSheets();
        for (int i = 0; i < sheetsNum; i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            if (findValueFromSheet(file, sheet.getSheetName(), value)) {
                flag = true;
                return flag;
            }
        }
        in.close();
        return flag;
    }

    /**
     * @param file(excel 文件的路径）
     * @param sheetName(取值的sheet)
     * @return (String, 根据单元格的类型返回单元格的String类型的值
     * @throws Exception
     */

    public static HSSFSheet getOneSheet(String file, String sheetName) {
        System.out.println(file);
        try {
            InputStream in = ClassLoader.class.getResourceAsStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(in);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet(sheetName);
            return sheet;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /* 根据单元格的类型返回String值 */
    protected static String getCellSringValue(HSSFCell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING://字符串类型
                cellValue = cell.getStringCellValue();
                if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
                    cellValue = " ";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC: //数值类型
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA: //公式
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = " ";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }
}
