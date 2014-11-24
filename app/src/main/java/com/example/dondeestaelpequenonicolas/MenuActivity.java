package com.example.dondeestaelpequenonicolas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MenuActivity extends Activity {
    private Images images;
    private int welcomeMessageNumber=R.integer.first_message;
    //private int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        JSONQuestionProvider jsonQuestionProvider=new JSONQuestionProvider(this);
        images=new Images(jsonQuestionProvider.getImages());
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        //level=settings.getInt("level",0);
        if(settings.getBoolean("FirstTime",true)){
            welcomeMessage();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
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
        /*Log.d("level",Integer.toString(level));
        if(level==images.getImages().length){
        View levelsCompletedView=this.findViewById(R.id.LevelsCompleted);
        levelsCompletedView.setVisibility(View.VISIBLE);
        }
        else {*/
            Intent intent = new Intent(this, FindNicolas.class);
            intent.putExtra("images", this.images);
            //intent.putExtra("level",0);
            startActivity(intent);
        //}
    }
    public void eraseInformation(View view){

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage("¿Quieres Reiniciar la Partida?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                eraseInformation();
            }

        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){

            }});
        AlertDialog dialog=builder.create();
        dialog.show();

            }
    private void eraseInformation(){
        SharedPreferences settings=getSharedPreferences("UserInfo",0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putInt("level",0);
        editor.commit();
        //level=0;

    }
    public void showHelp(View view){
        welcomeMessageNumber=R.integer.first_message;
        showHelp();
    }
    public void moreHelp(View view){
        if(welcomeMessageNumber==R.integer.first_message){
            welcomeMessageNumber=R.integer.second_message;
        }
        else{
            welcomeMessageNumber=R.integer.end_message;
        }
        showHelp();
    }
    public void showHelp(){
        TextView welcomeMessageView=(TextView) this.findViewById(R.id.welcome_message_text);
        View welcomeMessageLayout= this.findViewById(R.id.welcome_message_layout);
        Log.d("message",Integer.toString(welcomeMessageNumber));
        if(welcomeMessageNumber==R.integer.first_message){
            welcomeMessageView.setText(R.string.welcome_message_first);
            welcomeMessageLayout.setVisibility(View.VISIBLE);
        }
        else if(welcomeMessageNumber==R.integer.second_message){
            welcomeMessageView.setText(R.string.welcome_message_second);
        }
        else {
            welcomeMessageLayout.setVisibility(View.GONE);
        }

    }
    private void welcomeMessage(){
        SharedPreferences settings=getSharedPreferences("UserInfo",0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("FirstTime", false);
        editor.commit();
        showHelp();

    }

}


