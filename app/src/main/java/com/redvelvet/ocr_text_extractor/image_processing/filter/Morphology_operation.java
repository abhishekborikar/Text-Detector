package com.redvelvet.ocr_text_extractor.image_processing.filter;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by abhishek on 27/4/18.
 */

public class Morphology_operation {

    Bitmap input_img;
    public Morphology_operation(Bitmap input_img){
        this.input_img = input_img;
    }

    public Morphology_operation(){

    }

//    public Bitmap dierode(){
//        //prepare source matrix
//        Mat src =  bitmap2Mat(input_img);
//
//        //prepare destination matrix
//        Mat _dilate = new Mat();
//        Mat _erode  = new Mat();
//
//        //prepare kernal matrix
//        Mat kernal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size((2*2)+1,(2*2)+1));
//
//        //apply dilate
//        Imgproc.dilate(src,_dilate,kernal);
//
//        //apply erodate
//        Imgproc.erode(_dilate,_erode,kernal);
//
//        //convert dest(Mat) to bitmap
//        Bitmap result = mat2Bitmap(_erode);
//
//        return result;
//    }

    //convert bitmap image to Matrix(opencv)
    public Mat bitmap2Mat(Bitmap image){
        Mat mat = new Mat();
        Bitmap bmp32 = image.copy(Bitmap.Config.ARGB_8888,true);
        Utils.bitmapToMat(bmp32,mat);

        return mat;
    }

    //convert Mat to bitmap image
    public Bitmap mat2Bitmap(Mat mat){
        Bitmap bitmap = null;
        try{
            bitmap = Bitmap.createBitmap(mat.width(),mat.height(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat,bitmap);
        }
        catch(Exception e){
            Log.e("opencv mat2Bitmap ",""+e);
        }

        return bitmap;
    }

}
