    package com.redvelvet.ocr_text_extractor.image_processing.ExtractAction;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;


import java.io.File;
import java.util.prefs.Preferences;

    /**
 * Created by abhishek on 28/4/18.
 */

public class TextExtract {

    Bitmap input_img;
    TessBaseAPI tessBaseAPI;

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+"/tessdata";


    public TextExtract(Bitmap input_img){
        this.input_img = input_img;
    }

    public String getText() {
        try {
            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e) {
            Log.e("getText ", e.getMessage());
        }
        tessBaseAPI.init(DATA_PATH,"eng");
        tessBaseAPI.setImage(input_img);
        String retStr = "No result";
        try {
            retStr = tessBaseAPI.getUTF8Text();
        } catch (Exception e) {
            Log.e("getText 1", e.getMessage());
        }

        tessBaseAPI.end();

        return retStr;
    }

    public void makeFile(){

    }
}
