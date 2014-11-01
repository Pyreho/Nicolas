package com.example.dondeestaelpequenonicolas;

import java.io.Serializable;

/**
 * Created by gabi on 01.11.14.
 */
public class Images implements Serializable{
    private Image[] images;
    public Images(Image[] inputImage){
        images=inputImage;
    }
    public Image[] getImages(){
        return images;
    }
}
