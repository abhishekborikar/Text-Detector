package com.redvelvet.ocr_text_extractor.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.redvelvet.ocr_text_extractor.fileOperator.TextConverter;

import java.io.File;
import java.io.FileWriter;

public class EditorActivity extends AppCompatActivity {

    //widgets
    TextView tv;
    EditText et;
    ScrollView sv_tv,sv_et;

    File file;
    boolean hide = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        tv = findViewById(R.id.editor_tv);
        et = findViewById(R.id.editor_ed);
        sv_et = findViewById(R.id.sv_et);
        sv_tv = findViewById(R.id.sv_tv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showDoc();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_save,menu);
        if(hide){
            menu.findItem(R.id.save_txt).setVisible(false);
            menu.findItem(R.id.edit_txt).setVisible(true);
        }
        else{
            menu.findItem(R.id.save_txt).setVisible(true);
            menu.findItem(R.id.edit_txt).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_txt:{
                saveDoc();
                hide = true;
                invalidateOptionsMenu();
                break;
            }
            case R.id.edit_txt:{
                editView();
                hide = false;
                invalidateOptionsMenu();
                break;
            }
        }

        return true;
    }

    public void showDoc(){
        //change visibility
        sv_et.setVisibility(View.GONE);
        sv_tv.setVisibility(View.VISIBLE);

        Intent response = getIntent();
        String fileName = response.getStringExtra("fileName");

        file = new File(Environment.getExternalStorageDirectory()+"/TextDetector/"+fileName);

        //get the text from text file
        TextConverter textConverter = new TextConverter();
        String text = textConverter.getText(file);

        tv.setText(text);
    }

    public void editView(){
        //change visibility
        sv_et.setVisibility(View.VISIBLE);
        sv_tv.setVisibility(View.GONE);

        et.setText(tv.getText());
    }
    public void saveDoc(){

        String text = et.getText().toString();

        //delete the existing text from file
        try {
            //write the updated text to file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.append(text);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception e){
            Log.e("editor ",""+e);
        }
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        showDoc();
    }
}
