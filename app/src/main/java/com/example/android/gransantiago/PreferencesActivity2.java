package com.example.android.gransantiago;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class PreferencesActivity2 extends PreferenceActivity
{
    static String dni;
    static String np;
    public static final String PROPERTY_NP = "np";
    static  String regid;

    EditTextPreference ep1,ep2,ep3;
AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences("Registro", MODE_PRIVATE);
        dni = prefs.getString("dni", "No hay dni");
        np = prefs.getString(PROPERTY_NP, "No hay código");
        Log.i("gsh", "entrando en prefs, np=" + np);
        regid = prefs.getString("regid", "No hay registro");
        if (!regid.equals("No hay registro")) {
            regid = "Registrado";
        }


      //  alertDialog = new AlertDialog.Builder(getActivity().getBaseContext()).create();
        Log.i("prefs1", "entrando en prefs");

        addPreferencesFromResource(R.xml.preferences);
        ep1 = (EditTextPreference) getPreferenceScreen().findPreference("cprofesor");
        ep1.setSummary(np);
        ep1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i("prefs2", "entrando en prefs2");


                alertDialog.setTitle("Ande vas");

                // Setting Dialog Message
                alertDialog.setMessage("Quieeeeto!!!");

                alertDialog.show();
                return false;
            }
        });


        ep1.setEnabled(false);


        ep2 = (EditTextPreference) getPreferenceScreen().findPreference("registrado");
        ep2.setSummary(regid);
        ep2.setEnabled(false);

        ep3 = (EditTextPreference) getPreferenceScreen().findPreference("dni");
        ep3.setSummary(dni);
        ep3.setEnabled(false);


    }
        @Override
        public void onResume() {

            super.onResume();
        }






}