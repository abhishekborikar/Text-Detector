package com.redvelvet.ocr_text_extractor.project1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.redvelvet.ocr_text_extractor.structure.Hash;

import java.util.List;

/**
 * Created by abhishek on 1/5/18.
 */

public class CustomAdapter extends BaseAdapter {


    int imgFolder = R.drawable.folder;
    int imgFile = R.drawable.file;
    int imgPdf = R.drawable.pdf;
    Context mContext;
    List<Hash> files;

    public CustomAdapter(Context context,List<Hash> files){
        mContext = context;
        this.files = files;
    }


    @Override
    public int getCount() {
        if(files!=null)
        return files.size();

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_layout, null);
            TextView textView = grid.findViewById(R.id.gridtextView);
            ImageView imageView = grid.findViewById(R.id.gridimageView);

            //get fileName and flag
            Hash file = files.get(position);
            Log.e("file",""+file.getFileName());
            textView.setText(file.getFileName());

            //check flag and set icon
            int flag = file.getFlag();
            if(flag==1){
                imageView.setImageResource(imgFolder);
            }
            else if(flag == 2){
                imageView.setImageResource(imgFile);
            }
            else{
                imageView.setImageResource(imgPdf);
            }
        } else {
            grid = convertView;
        }

        return grid;
    }
}
