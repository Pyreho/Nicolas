package com.dondeestanicolas;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dondeestanicolas.R;

public class LevelDescriptionActivity extends Activity {
    private Image[] images;
    private Image image;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        images = ((Images) intent.getSerializableExtra("images")).getImages();
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        level=settings.getInt("level",0);
        //level = intent.getIntExtra("level", 0);
        image=images[level];
        setContentView(R.layout.activity_level_description);
        setAttributes(image);
        final View view=this.findViewById(R.id.start_round_button);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {view.setVisibility(View.VISIBLE);

            }
        },600);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.level_description, menu);
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
    private void setAttributes(Image image){
        TextView levelDescriptionView=(TextView)this.findViewById(R.id.level_description);
        levelDescriptionView.setText(image.getComment());
        TextView levelView=(TextView)this.findViewById(R.id.level);
        levelView.setText("Misión "+Integer.toString(level+1));
    }

    public void startRound(View view){
        Intent intent=new Intent(this, FindNicolas.class);
        intent.putExtra("images", new Images(images));
        startActivity(intent);
        finish();
    }
    public void onBackPressed(){
        Intent intent =new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
