package com.redvelvet.ocr_text_extractor.fileOperator;

import android.os.Environment;
import android.util.Log;

import com.redvelvet.ocr_text_extractor.project1.MainActivity;
import com.redvelvet.ocr_text_extractor.structure.Hash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 2/5/18.
 */

public class FileOperation {

    /**
     * in Hash Class Structure flag is to indicate file format
     * 1 -> directory
     * 2 -> text file
     * 3 -> other than text file
     */
    public List<Hash> getAllFolders(String directory){
        File root = null;

        if(directory!=null){
            root = new File(Environment.getExternalStorageDirectory()+"/TextDetector/"+directory);
        }
        else{
            root = new File(Environment.getExternalStorageDirectory()+"/TextDetector");
        }

        List<Hash> result = new ArrayList<>();
        String filename = null;
        try {
            if (root.listFiles().length > 0) {
                for (File file : root.listFiles()) {
                    Hash hash = new Hash();
                    filename = file.getName();
                    if (file.isDirectory()) {
                        hash.setFlag(1);
                        hash.setFileName(filename);
                    } else {

                        if (filename.contains("txt")) {
                            hash.setFlag(2);
                            hash.setFileName(filename);
                        } else {
                            hash.setFlag(3);
                            hash.setFileName(filename);
                        }
                    }

                    result.add(hash);
                    Log.e("folder gt", "" + filename);
                    hash = null;
                    filename = null;
                }
            }
        }
        catch (Exception e){
            Log.e("file result",""+e);
            return null;
        }

        for (int i = 0; i < result.size(); i++) {
            Log.e("for loop",result.get(i).getFileName()+" "+result.get(i).getFlag());
        }

        if(result==null)
            return null;
        MainActivity.folders = result;

        //--------------------delete after testing-------------------------
        if(result!=null)
            Log.e("Folders ",""+result.get(0));
        else
            Log.e("Folders ","empty");
        //-----------------------------------------------------------------
        return result;
    }
}
