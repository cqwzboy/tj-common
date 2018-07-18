package com.tj.common.ocr;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	 File imageFile1 = new File("E:\\barcode\\2xo1.jpg");  
    	 File imageFile2 = new File("E:\\barcode\\4k98.jpg");  
    	 File imageFile3 = new File("E:\\barcode\\4spq.jpg");  
    	 File imageFile4 = new File("E:\\barcode\\7l89.jpg");  
    	 File imageFile5 = new File("E:\\barcode\\qp3u.jpg");  
    	 File imageFile6 = new File("22.jpg");  
    	 File imageFile7 = new File("E:\\barcode\\593a.png");  
         ITesseract instance = new Tesseract();  
//         URL url = ClassLoader.getSystemResource("E:\\barcode");  
//         String path = url.getPath().substring(1);  
//         instance.setDatapath("E:\\tessdata");  
         // 默认是英文（识别字母和数字），如果要识别中文(数字 + 中文），需要制定语言包  
//         instance.setLanguage("chi_sim");  
         instance.setLanguage("eng");  
         try{  
//             System.out.println(instance.doOCR(imageFile1));  
//             System.out.println(instance.doOCR(imageFile2));  
//             System.out.println(instance.doOCR(imageFile3));  
//             System.out.println(instance.doOCR(imageFile4));  
//             System.out.println(instance.doOCR(imageFile5));  
             System.out.println(instance.doOCR(imageFile6));  
//             System.out.println(instance.doOCR(imageFile7));  
         }catch(TesseractException e){  
             e.printStackTrace();
         }  
    }
}
