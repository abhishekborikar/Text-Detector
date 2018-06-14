package com.redvelvet.ocr_text_extractor.fileOperator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by abhishek on 1/5/18.
 */

public class TextConverter {

    public String getText(File file){

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (Exception e) {
            //You'll need to add proper error handling here
            return null;
        }
        return text.toString();
    }
}
