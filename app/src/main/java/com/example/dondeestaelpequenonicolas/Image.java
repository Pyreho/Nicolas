package com.example.dondeestaelpequenonicolas;

import java.io.Serializable;

/**
 * Created by gabi on 01.11.14.
 */
public class Image implements Serializable {
    public String name;
    public float originalNicoX;
    public float originalNicoY;
    public float originalRadius;
    public String comment;

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
    public String getComment(){
        return comment;
    }

}