package com.redvelvet.ocr_text_extractor.fileOperator;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;

import java.io.File;

/**
 * Created by abhishek on 9/5/18.
 */

public class PdfTextExtractor {

    public void getText(File file){
        try{
            String parsedText="";
            PdfReader reader = new PdfReader(file.toString());
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+ com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage(reader,i+1).trim()+"\n";
            }
            System.out.println(parsedText);
            reader.close();
        }
        catch (Exception e){
            Log.e("pdf text extractor",""+e);
        }
    }
}
