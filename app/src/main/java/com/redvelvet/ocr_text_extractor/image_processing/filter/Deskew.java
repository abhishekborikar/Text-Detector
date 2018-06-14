package com.redvelvet.ocr_text_extractor.image_processing.filter;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.Skew;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 7/5/18.
 */

public class Deskew {

    public void getResult(Bitmap inputImg){

        Mat src = new Morphology_operation().bitmap2Mat(inputImg);
        Mat img_gray = new Mat();
        Imgproc.cvtColor(src,img_gray, Imgproc.COLOR_BGR2GRAY);

        Mat img_tresh_result = new Mat();
//        Imgproc.adaptiveThreshold(img_gray,img_tresh_result,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,15,40);
//        Imgproc.threshold(img_gray,img_tresh_result,100,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C);
//        return new Morphology_operation().mat2Bitmap(img_tresh_result);



        //Invert colors
        Core.bitwise_not(img_tresh_result,img_tresh_result);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));

        //erosion
        Mat ero = new Mat();
        Imgproc.erode(img_tresh_result,ero,element);

        //find white pixel
        Mat mWhite = Mat.zeros(ero.size(),ero.type());
        Core.findNonZero(ero,mWhite);

        MatOfPoint matOfPoint = new MatOfPoint(mWhite);

        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        matOfPoint.convertTo(matOfPoint2f, CvType.CV_32F);

        RotatedRect rotatedRect = Imgproc.minAreaRect(matOfPoint2f);

        Point[] vertices = new Point[4];
        rotatedRect.points(vertices);
        List<MatOfPoint> boxContours = new ArrayList<>();
        boxContours.add(new MatOfPoint(vertices));
        Imgproc.drawContours( ero, boxContours, 0, new Scalar(128, 128, 128), -1);
        double angle = rotatedRect.angle;
        if(angle < -45){
//            angle = -(90+angle);
            angle += 90;
        }
        else{
            angle = -angle;
        }
        Log.e("Angle ",angle+"");
        Mat deskew = deskew(ero,angle);
        Bitmap deskew_bitmap = new Morphology_operation().mat2Bitmap(deskew);
//        return deskew_bitmap;

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setImage(inputImg);
        Pix test = baseApi.getThresholdedImage();
        float a = Skew.findSkew(test);

        Log.e("skew angle",a+"");
    }

    public Mat deskew(Mat src, double angle) {
        Point center = new Point(src.width()/2, src.height()/2);
        Mat rotImage = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        //1.0 means 100 % scale
        Size size = new Size(src.width(), src.height());
        Imgproc.warpAffine(src, src, rotImage, size, Imgproc.INTER_CUBIC);
        return src;
    }
}
