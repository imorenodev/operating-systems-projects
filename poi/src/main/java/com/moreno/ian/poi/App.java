package com.moreno.ian.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    		FileInputStream fis = new FileInputStream(new File("kdp.xlsx"));
    		XSSFWorkbook  excel = new HSSFWorkbook(fis);
    }
}
