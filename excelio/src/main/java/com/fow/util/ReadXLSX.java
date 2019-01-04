package com.fow.util;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.TOC;

import javax.annotation.Resource;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReadXLSX {
    public static void main(String[] args) {

        File excelFile = new File("E:/SGY插值/50结果.xlsx"); //替换你文档地址
        File toFile=new File("E:/SGY插值/50结果.txt");
        ReadXLSX read=new ReadXLSX();
        try {
            List<List<String>> oneSheet=read.readOneSheet(excelFile,0);
            System.out.println("size:"+oneSheet.size());
            read.writeToTxt(toFile,oneSheet,"-> Trace # ");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
//        FileWriter fw=null;
//        BufferedWriter bw=null;
//        try {
//            if(!toFile.exists())
//                toFile.createNewFile();
//            fw=new FileWriter(toFile);
//            bw=new BufferedWriter(fw);
//            for(int i=0;i<10;i++)
//            {
//                bw.write("         "+i+"      "+i*10);
//                bw.newLine();
//                bw.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//
//            try {
//                if(bw!=null)
//                    bw.close();
//                if(fw!=null)
//                    fw.close();
//            }catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
    }
    public List<List<String>> readOneSheet(File excelFile,int sheetNum) throws Exception
    {
        List<List<String>> result=new LinkedList<List<String>>();
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(new FileInputStream(excelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numberOfSheets = wb.getNumberOfSheets();
        if(sheetNum>numberOfSheets||sheetNum<0)
        {
            throw new Exception("sheet number error");
        }
        XSSFSheet sheet = wb.getSheetAt(sheetNum);
        int columnNum = 0;
        if (sheet.getRow(0) != null) {
            columnNum = sheet.getRow(0).getLastCellNum()
                    - sheet.getRow(0).getFirstCellNum();
        }
        if(columnNum>0) {
            for (Row row : sheet) {
                String[] singleRow = new String[columnNum];
                List<String> columnList=new ArrayList<String>(columnNum);
                int n = 0;
                for (int i = 0; i < columnNum; i++) {
                    Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            columnList.add("");
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            columnList.add(Boolean.toString(cell.getBooleanCellValue()));
                            break;
                        // 数值
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = null;
                                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                                        .getBuiltinFormat("h:mm")) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else {// 日期
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                }
                                Date date = cell.getDateCellValue();
                                columnList.add(sdf.format(date));
                            } else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                String temp = cell.getStringCellValue();
                                // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                                if (temp.indexOf(".") > -1) {
                                    columnList.add( String.valueOf(new Double(temp)).trim());
                                } else {
                                    columnList.add(temp.trim());
                                }
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            columnList.add(cell.getStringCellValue().trim());
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            singleRow[n] = "";
                            columnList.add("");
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String temp = cell.getStringCellValue();
                            // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                            if (temp.indexOf(".") > -1) {
                                temp = String.valueOf(new Double(temp))
                                        .trim();
                                Double cny = Double.parseDouble(temp);//6.2041
                                DecimalFormat df = new DecimalFormat("0.00");
                                String CNY = df.format(cny);
                                columnList.add(CNY);
                            } else {
                                columnList.add(temp.trim());
                            }
                            break;
                        default:
                            columnList.add("");
                            break;
                    }
                }
                result.add(columnList);
            }
        }
        return result;
    }

    public int writeToTxt(File toFile,List<List<String>> data,String columnPrefix) throws IOException {
        if(toFile.isDirectory())
        {
            throw new IOException("该File为一个文件夹");
        }
        if(!toFile.exists())
        {
            toFile.createNewFile();
        }
        FileWriter fw=new FileWriter(toFile);
        BufferedWriter bw=new BufferedWriter(fw);
        int n=0;
        for(List<String> column:data)
        {
            int i=0;
            bw.write(columnPrefix+n);
            bw.newLine();
            bw.flush();
            for(String str:column)
            {
                bw.write( "         "+i+"      "+str);
                bw.newLine();
                bw.flush();
                i++;
            }
            n++;
        }
        if (bw!=null)
            bw.close();
        if(fw!=null)
            fw.close();
        return 1;
    }
    //    public void readAll(File excelFile)
//    {
//        List<List<String>> result=new LinkedList<List<String>>();
//        XSSFWorkbook wb = null;
//        try {
//            wb = new XSSFWorkbook(new FileInputStream(excelFile));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int numberOfSheets = wb.getNumberOfSheets();
//        String str = "";
//        for (int x = 0; x < numberOfSheets; x++) {
//            XSSFSheet sheet = wb.getSheetAt(x);
//            int columnNum = 0;
//            if (sheet.getRow(0) != null) {
//                columnNum = sheet.getRow(0).getLastCellNum()
//                        - sheet.getRow(0).getFirstCellNum();
//            }
//            if (columnNum > 0) {
//                for (Row row : sheet) {
//                    String[] singleRow = new String[columnNum];
//                    int n = 0;
//                    for (int i = 0; i < columnNum; i++) {
//                        Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
//                        switch (cell.getCellType()) {
//                            case Cell.CELL_TYPE_BLANK:
//                                singleRow[n] = "";
//                                if (cell == null || cell.equals("") || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
//                                    System.out.print("<Null>|");
//                                } else {
//                                    System.out.print(singleRow[n] + "|");
//                                }
//                                break;
//                            case Cell.CELL_TYPE_BOOLEAN:
//                                singleRow[n] = Boolean.toString(cell
//                                        .getBooleanCellValue());
//                                System.out.print(singleRow[n] + "|");
//                                break;
//                            // 数值
//                            case Cell.CELL_TYPE_NUMERIC:
//                                if (DateUtil.isCellDateFormatted(cell)) {
//                                    SimpleDateFormat sdf = null;
//                                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
//                                            .getBuiltinFormat("h:mm")) {
//                                        sdf = new SimpleDateFormat("HH:mm");
//                                    } else {// 日期
//                                        sdf = new SimpleDateFormat("yyyy-MM-dd");
//                                    }
//                                    Date date = cell.getDateCellValue();
//                                    System.out.print(sdf.format(date) + "|");
//                                } else {
//                                    cell.setCellType(Cell.CELL_TYPE_STRING);
//                                    String temp = cell.getStringCellValue();
//                                    // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
//                                    if (temp.indexOf(".") > -1) {
//                                        singleRow[n] = String.valueOf(new Double(temp))
//                                                .trim();
//                                        System.out.print(singleRow[n] + "|");
//                                    } else {
//                                        singleRow[n] = temp.trim();
//                                        System.out.print(singleRow[n] + "|");
//                                    }
//                                }
//                                break;
//                            case Cell.CELL_TYPE_STRING:
//                                singleRow[n] = cell.getStringCellValue().trim();
//                                System.out.print(singleRow[n] + "|");
//                                break;
//                            case Cell.CELL_TYPE_ERROR:
//                                singleRow[n] = "";
//                                System.out.print(singleRow[n] + "|");
//                                break;
//                            case Cell.CELL_TYPE_FORMULA:
//                                cell.setCellType(Cell.CELL_TYPE_STRING);
//                                String temp = cell.getStringCellValue();
//                                // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
//                                if (temp.indexOf(".") > -1) {
//                                    temp = String.valueOf(new Double(temp))
//                                            .trim();
//                                    Double cny = Double.parseDouble(temp);//6.2041
//                                    DecimalFormat df = new DecimalFormat("0.00");
//                                    String CNY = df.format(cny);
//                                    System.out.print(CNY + "|");
//                                } else {
//                                    singleRow[n] = temp.trim();
//                                    System.out.print(singleRow[n] + "|");
//
//                                }
//                            default:
//                                singleRow[n] = "";
//                                break;
//                        }
//                        n++;
//                    }
//                    System.out.println();
//                }
//
//                System.out.println("===========================================================Sheet分割线===========================================================");
//            }
//        }
//    }

}
