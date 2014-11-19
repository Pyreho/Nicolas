package com.example.dondeestaelpequenonicolas;

import java.io.Serializable;

/**
 * Created by gabi on 01.11.14.
 */
public class Image implements Serializable {
    private String name;
    private float originalNicoX;
    private float originalNicoY;
    private float originalRadius;
    private float realHeight;
    private String comment;

    public String getName(){
        return name;
    }
    public float getOriginalNicoX(){
        return originalNicoX;
    }
    public float getOriginalNicoY(){
        return originalNicoY;
    }
    public float getOriginalRadius(){
        return originalRadius;
    }
    public float getRealHeight(){return realHeight;}
    public String getComment(){
        return comment;
    }


}