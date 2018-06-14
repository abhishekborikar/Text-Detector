package com.redvelvet.ocr_text_extractor.project1;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.redvelvet.ocr_text_extractor.fileOperator.FileOperation;
import com.redvelvet.ocr_text_extractor.fileOperator.PdfConverter;
import com.redvelvet.ocr_text_extractor.structure.Hash;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //widgets
    BottomNavigationView navigation;
    GridView gridView;
    String adapter_address = null;
    String dir_name = null;

    //others
    Bitmap bitmap = null;

    //String
    private static final int REQUEST_CODE = 99;
    public static List<Hash> folders = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //======================test delete================================
//        Toast.makeText(this, ""+Environment.getRootDirectory().getPath(), Toast.LENGTH_LONG).show();
//
//        File file = new File(Environment.getRootDirectory().getPath());
//        if(file!=null){
//            File f[] = file.listFiles();
//            for (int i = 0; i < f.length; i++) {
//                Log.e("file "+i," "+f[i]);
//                if(f[i].toString().trim().equalsIgnoreCase("/system/app")){
//                    File fa[] = f[i].listFiles();
//                    for (int j = 0; j < fa.length; j++) {
//                        Log.e("file "+j," "+fa[j]);
//                    }
//                }
//            }
//        }




        //=================================================================

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1888);


        navigation = findViewById(R.id.navigation);
        gridView = findViewById(R.id.gridView);

        gridView.requestFocus();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //==============================================================================================
        File text_extractor = new File(Environment.getExternalStorageDirectory()+"/Text Extractor");
        if(!text_extractor.exists()){
            text_extractor.mkdir();


//            File tess = new File(Environment.getExternalStorageDirectory()+"/Text Extractor/.Tess");
//            if(!tess.exists()){
//                tess.mkdir();
//                File tessdata = new File(Environment.getExternalStorageDirectory()+"/Text Extractor/.Tess/tessdata");
//                if(!tessdata.exists()){
//                    tessdata.mkdir();
//                }
//            }
        }
        File tessdata = new File(Environment.getExternalStorageDirectory()+"/Text Extractor/tessdata");
        if(!tessdata.exists()){
            tessdata.mkdir();
        }
        //===============================================================================================

        //================================Grid View Handler============================================================
        //gridView adapter
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),new FileOperation().getAllFolders(null));
        gridView.setAdapter(adapter);
        adapter_address = gridView.getAdapter().toString();


        //--------gridview clicklistener------------
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int flag = folders.get(position).getFlag();
                String fileName = folders.get(position).getFileName();

                Log.e("grid adapter",""+gridView.getAdapter());
                Toast.makeText(MainActivity.this, ""+fileName, Toast.LENGTH_SHORT).show();

                if(flag==1){
                    //---------------------------Directory Operations-------------------------------
                    gridView.setAdapter(new CustomAdapter(MainActivity.this.getApplicationContext(),new FileOperation().getAllFolders(fileName)));
                    dir_name = fileName;
                    Log.e("grid adapter",""+gridView.getAdapter());

                }
                else if(flag==2){
                    //-------------------------Text File Operations------------------------------
                    Intent textIntent = new Intent(MainActivity.this,EditorActivity.class);
                    textIntent.putExtra("fileName",""+dir_name+"/"+fileName);
                    startActivity(textIntent);
                }
                else if(flag ==3){
                    //--------------------------Pdf File Operations--------------------------------
                    Intent pdfView = new Intent(Intent.ACTION_VIEW);
                    Toast.makeText(MainActivity.this, ""+fileName, Toast.LENGTH_SHORT).show();
                    File pdfFile = new File(Environment.getExternalStorageDirectory()+"/TextDetector/"+dir_name+"/"+fileName);
                    pdfView.setDataAndType(Uri.fromFile(pdfFile),"application/pdf");
                    startActivity(pdfView);

//                    Intent intent = new Intent(MainActivity.this,PdfViewerActivity.class);
//                    intent.putExtra("pdf_file",pdfFile);
//                    startActivity(intent);
                }


            }
        });

        //-----------------------------gridView longClick Listener-------------------------------------
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int flag = folders.get(position).getFlag();
                String fileName = folders.get(position).getFileName();

                    File file = new File(Environment.getExternalStorageDirectory()+"/TextDetector/"+dir_name+"/"+fileName);
                    notification(file,flag);


                return true;
            }
        });
        //-------------------------------------------------------------------------------------------------
        //================================================================================================================

        //--------------------------------Bottom navigation--------------------------------
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                gridView.requestFocus();
                switch (item.getItemId()) {
                    case R.id.navigation_camera: {

                        if(checkCameraHardware(getApplicationContext())){

                            //camera permission
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1888);

                            //scan library use camera
                            startScan(ScanConstants.OPEN_CAMERA);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Camera not present", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                    case R.id.navigation_gallery: {

                        startScan(ScanConstants.OPEN_MEDIA);
                        return true;
                    }
//                    case R.id.navigation_pdf: {
//                        Intent pdfIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                        pdfIntent.setType("application/pdf");
//                        startActivityForResult(pdfIntent,13);
//
//
//                        return true;
//                    }
                }
                return false;
            }
        });
        //--------------------------------------------------------------------------------------
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            try {
                Toast.makeText(this, ""+uri, Toast.LENGTH_LONG).show();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);

                //----------------------next activity--------------------------
                Intent display = new Intent(MainActivity.this,DisplayActivity.class);

                    //compress bitmap before passing to next intnet
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                display.putExtra("byteArrayImage",stream.toByteArray());
                startActivity(display);
                //--------------------------------------------------------------

            } catch (IOException e) {
                Log.e("onActivityResult ",""+e);
            }
        }
        else if(requestCode == 13 && resultCode == RESULT_OK){

        }


    }

    @Override
    public void onBackPressed() {
        String new_adapter_address = gridView.getAdapter().toString();
        if(!new_adapter_address.equalsIgnoreCase(adapter_address)){
            CustomAdapter adapter = new CustomAdapter(getApplicationContext(),new FileOperation().getAllFolders(null));
            gridView.setAdapter(adapter);
        }
    }

    private void startScan(int Preference){
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, Preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera
// no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void notification(final File file,int flag){

        final int pdf_id = 12;
        final int delete_id = 13;
        final int share_id = 14;

        //============================create layout for notification==================================
        final RadioGroup rg = new RadioGroup(MainActivity.this);

        RadioButton pdf = new RadioButton(MainActivity.this);
        pdf.setText("Convert to PDF");
        pdf.setTextColor(Color.BLACK);
        pdf.setPadding(12,7,7,3);
        pdf.setId(pdf_id);

        RadioButton dlt =  new RadioButton(MainActivity.this);
        dlt.setText("Delete");
        dlt.setTextColor(Color.BLACK);
        dlt.setPadding(12,5,5,7);
        dlt.setId(delete_id);

        RadioButton share =  new RadioButton(MainActivity.this);
        share.setText("Share");
        share.setTextColor(Color.BLACK) ;
        share.setPadding(12,5,5,7);
        share.setId(share_id);

        if(flag==2)
            rg.addView(pdf);
        if(flag==2 || flag==3)
            rg.addView(share);
        rg.addView(dlt);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        rg.setLayoutParams(lp);


        //---------------------------------Alert Dialog---------------------------------------------
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(rg)
                .create();
                dialog.show();
        //------------------------------------------------------------------------------------------


        //rg click listener
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case pdf_id:{
                        String path = new PdfConverter().getPdf(file);
                        if(path!=null){
                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(),new FileOperation().getAllFolders(null));
                            gridView.setAdapter(adapter);
                            gridView.invalidate();
                            Toast.makeText(MainActivity.this, "saved to : "+path, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case delete_id:{
                        Log.e("delete ",""+file);
                        if(file.exists()) {
                            file.delete();
                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(),new FileOperation().getAllFolders(null));
                            gridView.setAdapter(adapter);
                            gridView.invalidate();
                            Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "error deleting file", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case share_id:{
                        if(file.exists()){
                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                            File fileWithinMyDir = new File(file.getAbsolutePath());

                            intentShareFile.setType("application/pdf");
                            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));

                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                    "Sharing File...");
                            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                            startActivity(Intent.createChooser(intentShareFile, "Share File"));
                        }
                        else{
                            Toast.makeText(MainActivity.this, "can not share", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                dialog.dismiss();
            }
        });

        //========================================================================================
    }

    public void copyFile(){

    }

}
