package com.example.dondeestaelpequenonicolas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MenuActivity extends Activity {
    private Images images;
    private int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        JSONQuestionProvider jsonQuestionProvider=new JSONQuestionProvider(this);
        images=new Images(jsonQuestionProvider.getImages());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
    public void startFirstRound(View view){
        Intent intent=new Intent(this,FindNicolas.class);
        intent.putExtra("images",this.images);
        intent.putExtra("level",0);
        startActivity(intent);

    }
    public void launchSettings(View view){

    }
}
