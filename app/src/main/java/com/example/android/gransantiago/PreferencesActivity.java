package com.example.android.gransantiago;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

public class PreferencesActivity extends PreferenceActivity
{
    static String dni;
    static String np;
    public static final String PROPERTY_NP = "np";
    static  String regid;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        SharedPreferences prefs=getSharedPreferences("Registro",MODE_PRIVATE);
        dni = prefs.getString("dni", "No hay dni");
        np = prefs.getString("numprofesor", "No hay código");
        regid  = prefs.getString("regid", "No hay registro");
        if(!regid.equals("No hay registro")){regid="Registrado";}
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {

        DialogFragment df;
        Preference ep1,ep2,ep3;
        PreferenceCategory pc;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            df=new SomeDialog();
            addPreferencesFromResource(R.xml.preferences);
            ep1=getPreferenceScreen().findPreference("cprofesor");
            ep1.setSummary(np);
            ep2=getPreferenceScreen().findPreference("registrado");
            ep2.setSummary(regid);
            ep3=getPreferenceScreen().findPreference("dni");
            ep3.setSummary(dni);
            ep1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    df.show(getFragmentManager(), "activationDialog");
                    return true;
                }
            });
            ep2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    df.show(getFragmentManager(), "activationDialog");
                    return true;
                }
            });
            ep3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    df.show(getFragmentManager(), "activationDialog");
                    return true;
                }
            });
        }


        @Override
        public void onResume() {
            super.onResume();
        }

    }
    public static class SomeDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Aviso")
                    .setMessage("No es posible modificar las preferencias. Para ello tendrás que registrarte de nuevo")
                    .setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
        }
    }


}