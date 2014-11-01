package com.example.dondeestaelpequenonicolas;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Created by gabi on 01.11.14.
 */
public class JSONQuestionProvider {


    private Image[] images;
    public JSONQuestionProvider(Context myContext)//, String getString(R.string.questions_file))
    {
        String fileName = "Nico.json";
        try
        {
            InputStreamReader myStream = new InputStreamReader
                    (myContext.getAssets().open(fileName));
            parseImages(myStream);
            myStream.close();
            return;
        }
        catch (IOException localIOException)
        {
            throw new RuntimeException(localIOException);
        }
    }
    private void parseImages(InputStreamReader myStream)
    {
        Gson gson = new Gson();
        this.images =
                gson.fromJson(myStream, Image[].class);
    }
    public Image[] getImages(){
        return images;
    }

}
