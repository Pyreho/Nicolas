package com.example.dondeestaelpequenonicolas;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.dondeestaelpequenonicolas.R;
import com.example.dondeestaelpequenonicolas.TouchImageView;

public class FindNicolas extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_nicolas);
        final TouchImageView img =(TouchImageView)findViewById(R.id.img);
        int imID= getResources().getIdentifier("quincem2","drawable",getPackageName());
        img.setImageResource(imID);
        final long originalWidth = img.getDrawable().getIntrinsicWidth();
        final long originalHeight = img.getDrawable().getIntrinsicHeight();




        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float displayedWidth = view.getMeasuredWidth();
                float displayedHeight = view.getMeasuredHeight();
                float coordX = motionEvent.getX();
                float coordY = motionEvent.getY();
                float originX = view.getLeft();
                float originY = view.getTop();

                float left = img.getZoomedRect().left * img.getWidth();
                float top = img.getZoomedRect().top * img.getHeight();
                float[] m=new float[9];
                Matrix matrix = img.getImageMatrix();
                matrix.getValues(m);
                Log.d("matrix", String.valueOf(matrix));
                Log.d("Array m",String.valueOf(m));
                float origW = originalWidth;
                float origH = originalHeight;
                float transX = m[Matrix.MTRANS_X];
                float transY = m[Matrix.MTRANS_Y];
                //float finalX = ((coordX - transX) * origW) / getImageWidth();
                //float finalY = ((coordY - transY) * origH) / getImageHeight();
                float finalX = (coordX - transX)/img.getCurrentZoom();
                float finalY=(coordY - transY)/img.getCurrentZoom();
                float finalXAdrian= coordX / img.getCurrentZoom() + left;
                float finalYAdrian=coordY / img.getCurrentZoom() + top;
                Log.d("Value of X", String.valueOf(coordX));
                Log.d("Value of Y", String.valueOf(coordY));
                //Log.d("Left", String.valueOf(left));
                //Log.d("Top", String.valueOf(top));
                //Log.d("viewTop", String.valueOf(originY));
                //Log.d("RelativeY", String.valueOf(coordY / img.getCurrentZoom()));
                Log.d("Absolute X", String.valueOf(coordX / img.getCurrentZoom() + left));//Coordenadas absolutas incluso haciendo zoom
                Log.d("Absolute Y", String.valueOf((coordY ) / img.getCurrentZoom()+ top));
                Log.d("Absolute X2",String.valueOf(finalX));
                Log.d("Absolute Y2",String.valueOf(finalY));
                Log.d("RelX", String.valueOf(finalX/finalXAdrian));
                Log.d("RelY", String.valueOf(finalY/finalYAdrian));
                //Log.d("Zoom", String.valueOf(img.getCurrentZoom()));
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
