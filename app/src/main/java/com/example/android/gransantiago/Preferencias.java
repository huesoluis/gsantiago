package com.example.android.gransantiago;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class Preferencias extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        builder.append("\n" + sharedPrefs.getBoolean("dni", false));
        builder.append("\n" + sharedPrefs.getString("np", "-1"));
        builder.append("\n" + sharedPrefs.getString("regid", "NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
        settingsTextView.setText(builder.toString());

    }

}

