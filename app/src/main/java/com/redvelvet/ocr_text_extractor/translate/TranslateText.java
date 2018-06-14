package com.redvelvet.ocr_text_extractor.translate;

/**
 * Created by abhishek on 29/4/18.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.redvelvet.ocr_text_extractor.image_processing.ExtractAction.ResponseListener;

public class TranslateText extends AsyncTask<Void,Void,String>{

    final String KEY = "";

    String srcText;
    String srcLng;
    String trgtLng;
    String result = null;

    public TranslateText(String srcText, String srcLng, String trgtLng){
        this.srcText = srcText;
        this.srcLng = srcLng;
        this.trgtLng = trgtLng;
    }


    // interface to pass result string
    ResponseListener listener;
    public void setOnResponseListener(ResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            TranslateOptions options = TranslateOptions.newBuilder()
                    .setApiKey(KEY)
                    .build();

            Translate translate = options.getService();

            final Translation translation = translate.translate(
                    srcText,
                    Translate.TranslateOption.targetLanguage("fr")
            );

            result = translation.getTranslatedText();
        }
        catch(Exception e){
            Log.e("translate error ",""+e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("result data ",""+s);
      listener.onResponseReceive(s);
    }
}
