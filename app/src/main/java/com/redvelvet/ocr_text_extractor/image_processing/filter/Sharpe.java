package com.redvelvet.ocr_text_extractor.image_processing.filter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by abhishek on 27/4/18.
 */

public class Sharpe {

    Bitmap input_img;
    public Sharpe(Bitmap input_img){
        this.input_img = input_img;
    }

    /**
     * border are sharpen
     * Gaussian blur is used present in opencv
     */
    public Bitmap getGaussianSharpedImage(){

        Mat src = new Morphology_operation().bitmap2Mat(input_img);

        Mat dest = new Mat();

        Imgproc.GaussianBlur(src,dest,new Size(0,0),10);
        Core.addWeighted(src, 1.5, dest, -0.5, 0, dest);

        return new Morphology_operation().mat2Bitmap(dest);
    }

    public Bitmap getLaplaceSharpedImage(){
        int kernelSize = 0;
        double max_Value = 255;
        Mat src = new Morphology_operation().bitmap2Mat(input_img);

        Mat med = new Mat();
        Mat dest = new Mat();

        Log.e("Src type",""+src.type());

        Imgproc.medianBlur(src,med,5);

        return new Morphology_operation().mat2Bitmap(dest);
    }

}
