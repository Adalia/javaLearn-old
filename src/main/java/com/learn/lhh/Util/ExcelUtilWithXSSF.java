package com.learn.lhh.Util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ExcelUtilWithXSSF extends FileUtil {
    /**
     * @param file
     * @param sheetName
     * @param rowNum(行号）
     * @param cellNum（列号）
     * @return （指定单元格的值，string类型）
     */
    public static String getCellValue(String file, String sheetName, int rowNum, int cellNum) {
        System.out.println("getCellValue-----"+getOneSheet(file, sheetName));
        XSSFCell cell = getOneSheet(file, sheetName).getRow(rowNum).getCell(cellNum);
        System.out.println("getCellValue-----"+cell);
        String cellStringValue = getCellSringValue(cell);

        return cellStringValue;
    }

    //读取excel中指定行的数据
    public static Map<Integer, String> getOneLineData(String file, String sheetName, int rowNum) {
        Map<Integer, String> rowMap = new HashMap<Integer, String>();
        XSSFRow row = getOneSheet(file, sheetName).getRow(rowNum);
        int lastCell = row.getLastCellNum();
        for (int i = 0; i <= lastCell; i++) {
            String cellValue = getCellSringValue(row.getCell(i));
            rowMap.put(i, cellValue);
        }
        return rowMap;
    }

    //在指定的sheet中查找数据
    public static Map findValueFromSheet(String file, String sheetName, String value) {
        boolean flag = false;
        Map result = new HashMap();
        InputStream in = null;
        XSSFWorkbook wb = null;
        try{
            in = ClassLoader.class.getResourceAsStream(file);
            wb = new XSSFWorkbook(in);
        }catch (IOException e){
            System.out.println(e);
        }
        XSSFSheet sheet = wb.getSheet(sheetName);
        int rowNum = sheet.getLastRowNum();
        for (int r = 0; r <= rowNum; r++) {
            XSSFRow row = sheet.getRow(r);
            int cellNum = row.getLastCellNum();
            System.out.println(cellNum);
            for (int l = 0; l < cellNum; l++) {
                System.out.println("cellvalue:"+getCellSringValue(row.getCell(l)));
                if (getCellSringValue(row.getCell(l)).equals(value)) {
                    flag = true;
                    result.put("rowNum", r+1);
                    result.put("cellNum", l+1);
                    return result;
                }
            }
        }
        return result;
    }

    //在excel中查找数据
    public static Map findValueFromExcel(String file, String value) {
        boolean flag = false;
        Map result = new HashMap();
        InputStream in = null;
        XSSFWorkbook wb = null;
        try {
            in = ClassLoader.class.getResourceAsStream(file);
            wb = new XSSFWorkbook(in);
        }catch (IOException e){
            System.out.println(e);
        }
        int sheetsNum = wb.getNumberOfSheets();
        for (int i = 0; i < sheetsNum; i++) {
            XSSFSheet sheet = wb.getSheetAt(i);
            Map isFinded = findValueFromSheet(file, sheet.getSheetName(), value);
            System.out.println("findValueFromExcel------"+isFinded);
            if (!isFinded.isEmpty()) {
                result.put("sheetName", sheet.getSheetName());
                result.put("rowNum", isFinded.get("rowNum"));
                result.put("cellNum", isFinded.get("cellNum"));
//                if (isFinded.get("result").equals("true")) {
//                    flag = true;
//                    result.put("result", flag);
//                    result.put("sheetName", sheet.getSheetName());
//                    result.put("rowNum", isFinded.get("rowNum"));
//                    result.put("cellNum", isFinded.get("cellNum"));
//                }
                return result;  //?????为什么return放在这里才能跳出for循环
            }

        }
        try {

            in.close();
        }catch (IOException E){
            System.out.println(E);
        }

        return result;
    }

    /**
     * @param file(excel 文件的路径）
     * @param sheetName(取值的sheet)
     * @return (String, 根据单元格的类型返回单元格的String类型的值
     * @throws Exception
     */

    public static XSSFSheet getOneSheet(String file, String sheetName) {
        try {
            InputStream in = ClassLoader.class.getResourceAsStream(file);
            System.out.println("getOneSheet----"+in );
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheet(sheetName);
            in.close();
            return sheet;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /* 根据单元格的类型返回String值 */
    protected static String getCellSringValue(XSSFCell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING://字符串类型
                cellValue = cell.getStringCellValue();
                if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
                    cellValue = " ";
                break;
            case XSSFCell.CELL_TYPE_NUMERIC: //数值类型
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_FORMULA: //公式
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                cellValue = " ";
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }
}
