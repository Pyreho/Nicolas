package com.example.dondeestaelpequenonicolas;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.dondeestaelpequenonicolas.R;

public class FindNicolas extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_nicolas);
        TouchImageView img =(TouchImageView)findViewById(R.id.img);
        int imID= getResources().getIdentifier("quincem2","drawable",getPackageName());
        img.setImageResource(imID);
        long originalWidth = img.getDrawable().getIntrinsicWidth();
        long originalHeight = img.getDrawable().getIntrinsicHeight();




        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float displayedWidth = view.getMeasuredWidth();
                float displayedHeight = view.getMeasuredHeight();
                float coordX=motionEvent.getX();
                float coordY= motionEvent.getY();
                float originX=view.getLeft();
                float originY=view.getRight();
                Log.d("Value of X",String.valueOf(coordX));
                Log.d("Value of Y",String.valueOf(coordY));
                Log.d("OriginX", String.valueOf(originX));
                Log.d("OriginY", String.valueOf(originY));
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.find_nicolas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
