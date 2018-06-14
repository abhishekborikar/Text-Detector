package com.redvelvet.ocr_text_extractor.image_processing.filter;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by abhishek on 7/5/18.
 */

public class BrightNContrast {

    static double alpha = 1;
    static double beta = 25;
    public Bitmap controlBrightness(Bitmap input_img){

        Mat src = new Mat();
        Utils.bitmapToMat(input_img,src);
        Mat dest = new Mat();
        src.convertTo(dest,-1,alpha,beta);

        return new Morphology_operation().mat2Bitmap(dest);
    }
}
