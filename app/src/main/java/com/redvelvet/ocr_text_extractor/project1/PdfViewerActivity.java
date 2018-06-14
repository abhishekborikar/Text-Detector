package com.redvelvet.ocr_text_extractor.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {

    ImageView img;
    TextView textView;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

//        img = (ImageView)findViewById(R.id.pdfView);
        scrollView = findViewById(R.id.sv_pdf);
        textView = findViewById(R.id.dispaly_tv);

        Intent response = getIntent();
        File file = (File)response.getExtras().get("pdf_file");

        showPdf(file);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void showPdf(File file){
        try {
//            Document document = new Document();
//
//            PdfWriter writer = PdfWriter.getInstance(document,
//                    new FileOutputStream(file.getAbsoluteFile()));
//            document.open();
//            PdfReader reader = new PdfReader(file.getAbsolutePath());
//            int n = reader.getNumberOfPages();
//            PdfImportedPage page;
//            // Go through all pages
//            for (int i = 0; i <= n; i++) {
//
//                page = writer.getImportedPage(reader, i);
//                Image instance = Image.getInstance(page);
//                View view = new View(PdfViewerActivity.this,Image);
//                // here you can show image on your phone
//                scrollView.addView(view);
//            }
//            document.close();


            //-----------------------testing-----------------------
            PdfReader reader = new PdfReader(file.toString());
            Toast.makeText(this, "reader "+reader, Toast.LENGTH_SHORT).show();
            String text = PdfTextExtractor.getTextFromPage(reader,0);
            Toast.makeText(this, "text "+text, Toast.LENGTH_SHORT).show();
            textView.setText(text);
            //-----------------------------------------------------
        }
        catch (Exception e){
            Log.e("pdf view ",""+e);
        }
    }
}
