package com.example.dondeestaelpequenonicolas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.example.dondeestaelpequenonicolas.R;
import com.example.dondeestaelpequenonicolas.TouchImageView;

import java.util.Arrays;
// http://grepcode.com/file/repo1.maven.org/maven2/org.robolectric/android-all/4.2.2_r1.2-robolectric-0/android/widget/ImageView.java
//Source code for ImageView
public class FindNicolas extends Activity {
    //TODO This should be taken from the json
    private float nicoX;
    private float nicoY;
    private float radius;
/*    private float originalNicoX=612;

    private float originalNicoY=398;
    private float originalRadius=25;
*/
    private float originalNicoX;
    private float originalNicoY;
    private float originalRadius;
    private String name;
    private String comment;
    private Image[] images;
    private int level;
    float[] absoluteCoordinates;
    float[] relativeCoordinates;
    float originalWidth;
    float originalHeight;
    float originalRatio;
    float phoneWidth;
    float phoneHeight;
    float phoneRatio;
    float factor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        absoluteCoordinates=new float[2];
        relativeCoordinates=new float[2];
        Intent intent=getIntent();
        images=((Images)intent.getSerializableExtra("images")).getImages();
        level=intent.getIntExtra("level",0);

        setAttributes(images[level]);

        setContentView(R.layout.activity_find_nicolas);
        final TouchImageView img =(TouchImageView)findViewById(R.id.img);
        int imID= getResources().getIdentifier(name,"drawable",getPackageName());
        img.setImageResource(imID);
        Drawable nicoDrawable=getResources().getDrawable(imID);
        //Resource Bitmaps are immutable
        Bitmap nicoBitMapOriginal=((BitmapDrawable)nicoDrawable).getBitmap();
        final Bitmap nicoBitMap=nicoBitMapOriginal.copy(nicoBitMapOriginal.getConfig(),true);
        originalWidth = img.getDrawable().getIntrinsicWidth();
        originalHeight = img.getDrawable().getIntrinsicHeight();
        originalRatio=originalHeight/originalWidth;
        Paint paint =new Paint();
        paint.setStrokeWidth(1);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        Canvas canvas=new Canvas(nicoBitMap);
        canvas.drawCircle(originalNicoX,originalNicoY,originalRadius,paint);

        /*
        final long originalHeight = img.getDrawable().getIntrinsicHeight();
*/



        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Here we hide the ads when zooming
                Fragment fragment=getFragmentManager().findFragmentById(R.id.adFragment);
                if(img.getCurrentZoom()!=1){

                    if( fragment.isVisible())
                    {
                    FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.commit();
                    }


                }
                /*else{
                    if(fragment.isHidden()){
                        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                        fragmentTransaction.show(fragment);
                        fragmentTransaction.commit();
                    }

                }*/

                phoneWidth=img.getWidth();
                phoneHeight=img.getHeight();
                phoneRatio=phoneHeight/phoneWidth;
                //Log.d("measureWidth",Float.toString(measureWidth));
                Log.d("phoneWidth",Float.toString(phoneWidth));
                //the Image fits in the x Axis
                if(phoneRatio>=originalRatio){
                factor=phoneWidth/originalWidth;}
                else{
                    factor=phoneHeight/originalHeight;
                }
                nicoX=factor*originalNicoX;
                nicoY=factor*originalNicoY;
                radius=factor*originalRadius;
                WindowManager windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
                Display display=windowManager.getDefaultDisplay();
                //int rotation=display.getRotation();


                absoluteCoordinates=new float[] {motionEvent.getX(),motionEvent.getY()};
                relativeCoordinates=eventToRelative(absoluteCoordinates,img);
                Log.d("AbsoluteCoordinates",Arrays.toString(absoluteCoordinates));
                Log.d("RelativeCoordinates", Arrays.toString(relativeCoordinates));
                //Log.d("NewAbsoluteCoordinates",Arrays.toString(relativeToEvent(relativeCoordinates, img)));
                if(distance(relativeCoordinates)){
                    Log.d("ConditionsSatisfied","idem");
                    //TODO
                    img.setImageBitmap(nicoBitMap);
                }

                return true;

/*
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
*/


/*
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
                if(distance())
                //Log.d("Zoom", String.valueOf(img.getCurrentZoom()));

  */

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

    private void nicolasEncontrado(float coordX, float coordY){

    }
    private boolean distance(float[] relCoord){
        return (relCoord[0]-nicoX)*(relCoord[0]-nicoX)+(relCoord[1]-nicoY)*(relCoord[1]-nicoY)<radius*radius;
    }
    private float[] eventToRelative(float[] absoluteCoordinates, TouchImageView img){
        float[] m=new float[9];
        Matrix matrix = img.getImageMatrix();
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        float finalX = (absoluteCoordinates[0] - transX)/img.getCurrentZoom();
        float finalY=(absoluteCoordinates[1] - transY)/img.getCurrentZoom();
        return new float[]{finalX, finalY};

    }
    // To be used with Nicolas
    private float[] relativeToEvent(float[] relativeCoordinates, TouchImageView img){
        float[] m=new float[9];
        Matrix matrix = img.getImageMatrix();
        matrix.getValues(m);

        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        float finalX = (relativeCoordinates[0]*img.getCurrentZoom() + transX);
        float finalY=(relativeCoordinates[1]*img.getCurrentZoom() + transY);
        return new float[]{finalX, finalY };

    }
    private void setAttributes(Image image){
        originalNicoX=image.getOriginalNicoX();
        originalNicoY=image.getOriginalNicoY();
        originalRadius=image.getOriginalRadius();
        comment=image.getComment();
        name=image.getName();
    }
    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /** Called when leaving the activity */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /** Called when returning to the activity */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /** Called before the activity is destroyed */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }

    }

    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }
    }*/
}
