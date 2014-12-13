package com.dondeestanicolas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.dondeestanicolas.R;

// http://grepcode.com/file/repo1.maven.org/maven2/org.robolectric/android-all/4.2.2_r1.2-robolectric-0/android/widget/ImageView.java
//Source code for ImageView
public class FindNicolas extends Activity {

/*    private float originalNicoX=612;

    private float originalNicoY=398;
    private float originalRadius=25;
*/
    private float originalNicoX;
    private float originalNicoY;
    private float originalRadius;
    //private String name;
    //private String comment;
    private Image[] images;
    private Image image;
    private int level;
    private float originalWidth;
    private float originalHeight;
    private float originalRatio;
    private Bitmap nicoBitMapOriginal;
    private Bitmap nicoBitMap;
    private boolean nicoFound=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        images = ((Images) intent.getSerializableExtra("images")).getImages();
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        level=settings.getInt("level",0);
        //level = intent.getIntExtra("level", 0);
        image=images[level];
        setAttributes(images[level]);
        setContentView(R.layout.activity_find_nicolas);

        final TouchImageView img =(TouchImageView)this.findViewById(R.id.img);
        if(nicoBitMapOriginal!=null){
            nicoBitMapOriginal.recycle();
            Log.d("it was not empty","Original");
            nicoBitMapOriginal=null;
        }
        if(nicoBitMap!=null){
            Log.d("it was not empty","Modified");
            nicoBitMap.recycle();
            nicoBitMap=null;
        }
        if(savedInstanceState!=null){
            nicoFound=savedInstanceState.getBoolean("nicoFound");
        }
        if(nicoFound){
            View view =findViewById(R.id.LevelButton);
            view.setVisibility(View.VISIBLE);
        }
        setupRedCircleBitmap(img);

        //This is mainly to avoid setting the OnTouchListener if the image is not displayed and avoid using a recycled BitMap
        Log.d("beforelistener",Boolean.toString(!nicoFound));
        if(!nicoFound){
        Log.d("listener",Boolean.toString(!nicoFound));
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


                float[] absoluteCoordinates = new float[2];
                float[] relativeCoordinates = new float[2];
                absoluteCoordinates = new float[] {motionEvent.getX(),motionEvent.getY()};
                relativeCoordinates = eventToRelative(absoluteCoordinates,img);
                //Log.d("AbsoluteCoordinates",Arrays.toString(absoluteCoordinates));
                //Log.d("RelativeCoordinates", Arrays.toString(relativeCoordinates));
                if (distance(relativeCoordinates, nicoX, nicoY, radius)) {
                    //Log.d("ConditionsSatisfied","idem");


                    nicolasFound(nicoBitMap,img);
                }

                return true;
            }
        });}
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
        //getMenuInflater().inflate(R.menu.find_nicolas, menu);
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
    @Override
    public void onBackPressed(){
        backToMenu();
    }
    @Override
    public void onDestroy(){
        nicoBitMap.recycle();
        nicoBitMapOriginal.recycle();
        Log.d("Destroyed","destroyed");
        super.onDestroy();

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        savedInstance.putBoolean("nicoFound",nicoFound);

    }
    private void setAttributes(final Image image) {
        originalNicoX = image.getOriginalNicoX();
        originalNicoY = image.getOriginalNicoY();
        originalRadius = image.getOriginalRadius();
        //comment = image.getComment();
        //name = image.getName();
    }

    private void setupRedCircleBitmap(TouchImageView img) {
        final int imID = getResources().getIdentifier(image.getName(),"drawable",getPackageName());

        //Drawable nicoDrawable=getResources().getDrawable(imID);
        //Resource Bitmaps are immutable
        BitmapFactory.Options options = new BitmapFactory.Options();
        //Just to get the data. It deprecates RealHeight
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),imID,options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        //String imageType = options.outMimeType;
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth=size.x;
        int screenHeight=size.y;
        //Log.d("screenWidth---------------", Integer.toString(screenWidth));
        int maxMeasures=Math.max(screenHeight,screenWidth);
        int inSampleSize=calculateInSampleSize(imageWidth,imageHeight, maxMeasures,maxMeasures);
        options.inSampleSize=inSampleSize;
        //  you can use this to debug
        //options.inSampleSize=8;
        options.inJustDecodeBounds=false;
        options.inPurgeable=true;
        options.inInputShareable=true;
        int numberOfTries=0;
        boolean imageNotDisplayed=true;
        while(imageNotDisplayed && numberOfTries<2) {
            numberOfTries+=1;
            try{

                nicoBitMapOriginal = BitmapFactory.decodeResource(getResources(), imID, options);

                img.setImageBitmap(nicoBitMapOriginal);

                //final Bitmap nicoBitMapOriginal = ((BitmapDrawable) nicoDrawable).getBitmap();
                //Log.d("nicoBitMapOriginal parameters", "Width: " + Float.toString(nicoBitMapOriginal.getWidth()) + ", Height: " + Float.toString(nicoBitMapOriginal.getHeight()));
                nicoBitMap = nicoBitMapOriginal.copy(nicoBitMapOriginal.getConfig(), true);

                //Log.d("nicoBitMap parameters", "Width: " + Float.toString(nicoBitMap.getWidth()) + ", Height: " + Float.toString(nicoBitMap.getHeight()));
                //originalWidth = img.getDrawable().getIntrinsicWidth();
                //originalHeight = img.getDrawable().getIntrinsicHeight();
                //originalRatio = originalHeight/originalWidth;
                originalRatio = (float) img.getDrawable().getIntrinsicHeight() / img.getDrawable().getIntrinsicWidth();
                originalHeight = image.getRealHeight();
                originalWidth = originalHeight / originalRatio;
                final float drawingFactor = nicoBitMap.getHeight() / originalHeight;
                // Log.d("Heightscompared", "OriginalHeight: "+Float.toString(originalHeight)+"imageHeight: "+Float.toString(screenHeight));
                // Log.d("Widthscompared", "OriginalWidth: "+Float.toString(originalWidth)+"imageWidth: "+Float.toString(screenWidth));
                // Log.d("drawingFactor",Float.toString(drawingFactor));
                Paint paint = new Paint();

                paint.setStrokeWidth(image.getOriginalStroke() * drawingFactor);
                paint.setStrokeCap(Paint.Cap.BUTT);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.RED);
                Canvas canvas = new Canvas(nicoBitMap);
                //Log.d("Original parameters", "X: " + Float.toString(originalNicoX) + ", Y: " + Float.toString(originalNicoY) + ", Radius: " + Float.toString(originalRadius));
                //Log.d("Canvas parameters", "Width: " + Float.toString(canvas.getWidth()) + ", Height: " + Float.toString(canvas.getHeight()));
                canvas.drawCircle(originalNicoX * drawingFactor, originalNicoY * drawingFactor, originalRadius * drawingFactor, paint);
                //nicoBitMapOriginal.recycle();

                if (nicoFound) {
                    img.setImageBitmap(nicoBitMap);
                    nicoBitMapOriginal.recycle();
                }

                imageNotDisplayed=false;

            }
            catch (OutOfMemoryError E){
                nicoBitMap.recycle();
                nicoBitMapOriginal.recycle();
                imageNotDisplayed=true;
                inSampleSize*=2;
                options.inSampleSize=inSampleSize;
                Log.d("OutOfMemoryExceptionTryNumber",Integer.toString(numberOfTries));

            }

        }
        if(imageNotDisplayed){
            View view=this.findViewById(R.id.memory_out_of_range);
            view.setVisibility(View.VISIBLE);
            View button=this.findViewById(R.id.LevelButton);
            button.setVisibility(View.VISIBLE);
            nicoFound=true;
            img.setImageBitmap(null);
        }

    }


    private boolean distance(final float[] relCoord, final float nicoX, final float nicoY, final float radius) {
       // Log.d("Parameters", "X: " + Float.toString(nicoX) + ", Y: " + Float.toString(nicoY) + ", Radius: " + Float.toString(radius));
       // Log.d("RelativeCoordinates", Float.toString((relCoord[0] - nicoX) * (relCoord[0] - nicoX) + (relCoord[1] - nicoY) * (relCoord[1] - nicoY)));
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
    private void nicolasFound(Bitmap nicoBitMap, final TouchImageView img){
        img.setImageBitmap(nicoBitMap);
        nicoFound=true;
        nicoBitMapOriginal.recycle();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                img.resetZoom();
            }
            },800);


        //try{ Thread.sleep(1000);}catch (InterruptedException e){}
        final Button nextLevelButton=(Button) this.findViewById(R.id.LevelButton);
        handler.postDelayed(new Runnable() {
            public void run() {
                nextLevelButton.setVisibility(View.VISIBLE);
            }
        },1600);


    }
    public void nextLevel(View view){
        if(level<images.length){
            level+=1;
            SharedPreferences settings=getSharedPreferences("UserInfo",0);
            SharedPreferences.Editor editor=settings.edit();
            editor.putInt("level",level);
            editor.commit();

        if(level==images.length){
            View congratsView=this.findViewById(R.id.Congrats);
            congratsView.setVisibility(View.VISIBLE);
        }
        else{

            Intent intent = new Intent(this,LevelDescriptionActivity.class);
            intent.putExtra("images", new Images(images));
            TouchImageView img =(TouchImageView)this.findViewById(R.id.img);
            img.setImageDrawable(null);
            nicoBitMap.recycle();
            startActivity(intent);
            finish();
        }}
    }

    public void backToMenu(View view){
        backToMenu();
    }
    private void backToMenu(){
        Intent intent =new Intent(this,MenuActivity.class);
        TouchImageView img =(TouchImageView)this.findViewById(R.id.img);
        img.setImageDrawable(null);
        nicoBitMap.recycle();
        nicoBitMapOriginal.recycle();
        startActivity(intent);
        finish();
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

   /* public static class PlaceholderFragment extends Fragment {
        private String levelDescription="Nicolás";
        private String level= "0";
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            TextView levelDescriptionView=(TextView)rootView.findViewById(R.id.LevelDescription);
            levelDescriptionView.setText(levelDescription);
            TextView levelView=(TextView)rootView.findViewById(R.id.Level);
            levelView.setText(level);
            return rootView;
        }
        public void setLevelDescription(String levelDescription){
            this.levelDescription=levelDescription;
        }
        public void setLevel(int level) {
            this.level ="Misión " + Integer.toString(level);
        }

    }*/
    private static int calculateInSampleSize(
            int realWidth, int realHeight,  int reqWidth, int reqHeight) {
        // Raw height and width of image
        //final int height = options.outHeight;
        //final int width = options.outWidth;
        int inSampleSize = 1;

        if (realHeight > reqHeight || realWidth > reqWidth) {

            final int halfHeight = realHeight / 2;
            final int halfWidth = realWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
