package com.example.dondeestaelpequenonicolas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.example.dondeestaelpequenonicolas.R;
import com.example.dondeestaelpequenonicolas.TouchImageView;

import java.util.Arrays;
// http://grepcode.com/file/repo1.maven.org/maven2/org.robolectric/android-all/4.2.2_r1.2-robolectric-0/android/widget/ImageView.java
//Source code for ImageView
public class FindNicolas extends Activity {
    //TODO This should be taken from the json
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
    float originalWidth;
    float originalHeight;
    float originalRatio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        images = ((Images) intent.getSerializableExtra("images")).getImages();
        level = intent.getIntExtra("level", 0);

        setAttributes(images[level]);
        setContentView(R.layout.activity_find_nicolas);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlaceholderFragment placeholderFragment = new PlaceholderFragment();
        fragmentTransaction.add(R.id.parentNicolas, placeholderFragment, "StartLevel");
        fragmentTransaction.commit();
        final TouchImageView img =(TouchImageView)this.findViewById(R.id.img);

        final Bitmap nicoBitMap = setupRedCircleBitmap(img);

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final float phoneWidth = img.getWidth();
                final float phoneHeight = img.getHeight();
                final float phoneRatio = phoneHeight/phoneWidth;

                //the Image fits in the x Axis
                final float factor = phoneRatio >= originalRatio ? phoneWidth/originalWidth : phoneHeight/originalHeight;

                final float nicoX = factor * originalNicoX;
                final float nicoY = factor * originalNicoY;
                final float radius = factor * originalRadius;
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();

                float[] absoluteCoordinates = new float[2];
                float[] relativeCoordinates = new float[2];
                absoluteCoordinates = new float[] {motionEvent.getX(),motionEvent.getY()};
                relativeCoordinates = eventToRelative(absoluteCoordinates,img);
                Log.d("AbsoluteCoordinates",Arrays.toString(absoluteCoordinates));
                Log.d("RelativeCoordinates", Arrays.toString(relativeCoordinates));
                if (distance(relativeCoordinates, nicoX, nicoY, radius)) {
                    Log.d("ConditionsSatisfied","idem");
                    //TODO
                    img.setImageBitmap(nicoBitMap);
                }

                return true;
            }
        });
    }

    public void startRound(View view){
        final FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag("StartLevel");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();

        final TouchImageView img =(TouchImageView)this.findViewById(R.id.img);
        img.setVisibility(View.VISIBLE);
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

    private void setAttributes(final Image image) {
        originalNicoX = image.getOriginalNicoX();
        originalNicoY = image.getOriginalNicoY();
        originalRadius = image.getOriginalRadius();
        comment = image.getComment();
        name = image.getName();
    }

    private Bitmap setupRedCircleBitmap(TouchImageView img) {
        final int imID = getResources().getIdentifier(name,"drawable",getPackageName());
        img.setImageResource(imID);
        Drawable nicoDrawable=getResources().getDrawable(imID);
        //Resource Bitmaps are immutable
        final Bitmap nicoBitMapOriginal = ((BitmapDrawable) nicoDrawable).getBitmap();
        Log.d("nicoBitMapOriginal parameters", "Width: " + Float.toString(nicoBitMapOriginal.getWidth()) + ", Height: " + Float.toString(nicoBitMapOriginal.getHeight()));
        final Bitmap nicoBitMap = nicoBitMapOriginal.copy(nicoBitMapOriginal.getConfig(),true);
        Log.d("nicoBitMap parameters", "Width: " + Float.toString(nicoBitMap.getWidth()) + ", Height: " + Float.toString(nicoBitMap.getHeight()));
        originalWidth = img.getDrawable().getIntrinsicWidth();
        originalHeight = img.getDrawable().getIntrinsicHeight();
        originalRatio = originalHeight/originalWidth;
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        Canvas canvas = new Canvas(nicoBitMap);
        Log.d("Original parameters", "X: " + Float.toString(originalNicoX) + ", Y: " + Float.toString(originalNicoY) + ", Radius: " + Float.toString(originalRadius));
        Log.d("Canvas parameters", "Width: " + Float.toString(canvas.getWidth()) + ", Height: " + Float.toString(canvas.getHeight()));
        canvas.drawCircle(originalNicoX,originalNicoY,originalRadius,paint);
        return nicoBitMap;
    }

    private void nicolasEncontrado(float coordX, float coordY){

    }
    private boolean distance(final float[] relCoord, final float nicoX, final float nicoY, final float radius) {
        Log.d("Parameters", "X: " + Float.toString(nicoX) + ", Y: " + Float.toString(nicoY) + ", Radius: " + Float.toString(radius));
        Log.d("RelativeCoordinates", Float.toString((relCoord[0] - nicoX) * (relCoord[0] - nicoX) + (relCoord[1] - nicoY) * (relCoord[1] - nicoY)));
        return (relCoord[0]-nicoX) * (relCoord[0]-nicoX) + (relCoord[1]-nicoY) * (relCoord[1]-nicoY) < radius*radius;
    }
    private float[] eventToRelative(float[] absoluteCoordinates, TouchImageView img) {
        final float[] m = new float[9];
        final Matrix matrix = img.getImageMatrix();
        matrix.getValues(m);
        final float transX = m[Matrix.MTRANS_X];
        final float transY = m[Matrix.MTRANS_Y];
        final float finalX = (absoluteCoordinates[0] - transX)/img.getCurrentZoom();
        final float finalY = (absoluteCoordinates[1] - transY)/img.getCurrentZoom();
        return new float[]{finalX, finalY};

    }
    // To be used with Nicolas
    private float[] relativeToEvent(float[] relativeCoordinates, TouchImageView img){
        float[] m = new float[9];
        Matrix matrix = img.getImageMatrix();
        matrix.getValues(m);

        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        float finalX = (relativeCoordinates[0]*img.getCurrentZoom() + transX);
        float finalY = (relativeCoordinates[1]*img.getCurrentZoom() + transY);
        return new float[]{finalX, finalY};

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

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }
        public void setLevelDescription(String levelDescription){

        }
    }

}
