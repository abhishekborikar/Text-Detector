package com.redvelvet.ocr_text_extractor.fileOperator;

import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by abhishek on 2/5/18.
 */

public class PdfConverter {

    /**
     *  Convert Text file to pdf
     *  @param file : path of file
     */


    public String getPdf(File file){

        try{

            //get text from text file
            String text = new TextConverter().getText(file);

            //prepare name for new fle
            String fileName = file.getName().split("\\.")[0];
            StringBuilder fileNameBuilder = new StringBuilder(fileName);
            fileNameBuilder.append("PDF.pdf");

            //---------------convert to pdf---------------
            Document document = new Document();
            PdfWriter.getInstance(document,new FileOutputStream(file.getParent()+"/"+fileNameBuilder.toString()));
            document.open();

            //font formatting
            float fntSize, lineSpacing;
            fntSize = 20f;
            lineSpacing = 30f;

            Paragraph para = new Paragraph(new Phrase(lineSpacing,text, FontFactory.getFont(FontFactory.COURIER,fntSize)));
            para.setAlignment(Element.ALIGN_LEFT);

            document.add(para);
            document.close();
            return file.getParent();
            //---------------------------------------------

        }
        catch (Exception e){
            Log.e("pdf converter",""+e);
        }
        return null;
    }
}
