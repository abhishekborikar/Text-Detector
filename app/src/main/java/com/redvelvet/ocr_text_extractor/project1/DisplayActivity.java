package com.redvelvet.ocr_text_extractor.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redvelvet.ocr_text_extractor.image_processing.ExtractAction.ResponseListener;
import com.redvelvet.ocr_text_extractor.image_processing.ExtractAction.TextExtract;
import com.redvelvet.ocr_text_extractor.image_processing.filter.BrightNContrast;
import com.redvelvet.ocr_text_extractor.image_processing.filter.Rgb2grayscale;
import com.redvelvet.ocr_text_extractor.translate.TranslateText;

import java.io.File;
import java.io.FileWriter;

public class DisplayActivity extends AppCompatActivity {

    ImageView display;
    EditText display_text;
    TextView textView;
    String extractedText;
    String translatedText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        display = findViewById(R.id.display);
        display_text = findViewById(R.id.display_text);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //get the bitmap image by decompressing the byte array
        byte[] byte_img = getIntent().getByteArrayExtra("byteArrayImage");
        Bitmap rgb = BitmapFactory.decodeByteArray(byte_img,0,byte_img.length);

        //step 1: convert rgb image to grayscale
        Bitmap grayscale = new Rgb2grayscale().getGrayScale(rgb);

//        new Deskew().getResult(grayscale);

        //step 2
//        Bitmap desk = new Deskew().getResult(grayscale);

        //step3
        Bitmap bright = new BrightNContrast().controlBrightness(grayscale);

        //step4
//        Bitmap sharp = new Sharpe(grayscale).getGaussianSharpedImage();

        //step5
        //Bitmap la_sharp = new Sharpe(sharp).getLaplaceSharpedImage();

        //getString from bitmap
        extractedText = new TextExtract(grayscale).getText();
        display_text.setText(extractedText);
        display.setImageBitmap(bright);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save:{
                saveDoc();
                Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();
                break;
            }
//            case R.id.translate:{
//
//                String trndtxt = getResult();
//                Log.e("translated text",""+trndtxt);
//                Toast.makeText(this, ""+trndtxt, Toast.LENGTH_SHORT).show();
//
//                break;
//            }
        }
        return true;
    }

    private void saveDoc(){
        final File file = new File(Environment.getExternalStorageDirectory()+"/TextDetector");
        if(!file.exists()){
            file.mkdir();
        }

        final EditText input = new EditText(DisplayActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        //take name as input
        new AlertDialog.Builder(DisplayActivity.this)
                .setTitle("File Name")
                .setView(input)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String filename = null;
                        File newFile = null;
                        if(input.getText()==null)
                            input.setText("sample");
                        filename = input.getText().toString();

                        if(filename.contains(".")){
                            filename = filename.split(".")[0];
                        }

                        //make folder
                        newFile = new File("/"+file+"/"+filename);
                        if(!newFile.exists())
                            newFile.mkdir();

                        StringBuilder builder = new StringBuilder(filename);
                        builder.append("-eng.txt");

                        File saveFile = new File(newFile,builder.toString());
                        try {
                            FileWriter writer = new FileWriter(saveFile);
                            writer.append(extractedText);
                            writer.flush();
                            writer.close();
                            Log.e("file status","saved "+filename);
                        }
                        catch(Exception e){

                            Log.e("save file",e.getMessage());
                        }

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public String getResult(){

        final TranslateText translate = new TranslateText(extractedText,"en","fr");
        translate.execute();
        translate.setOnResponseListener(
                new ResponseListener() {
                    @Override
                    public void onResponseReceive(String data) {
                        translatedText  = data;
                    }
                }
        );

        return translatedText;
    }


}
