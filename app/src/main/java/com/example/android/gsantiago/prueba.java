package com.example.android.gsantiago;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;


public class prueba extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Activity ","onCreate");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Activity ","onStop");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("Activity ","onRestoreInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Activity ","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Activity ","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Activity ","onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Activity ","onSaveInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Activity ","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Activity ","onRestart");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}