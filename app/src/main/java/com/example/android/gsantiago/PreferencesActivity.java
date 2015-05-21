package com.example.android.gsantiago;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity
{
//    AlertDialog alertDialog;
    static String dni;
 static String np;
    public static final String PROPERTY_NP = "np";
    static  String regid;
    AlertDialog alertDialog;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        SharedPreferences prefs=getSharedPreferences("Registro",MODE_PRIVATE);
        dni = prefs.getString("dni", "No hay dni");
        np = prefs.getString(PROPERTY_NP, "No hay código");
        Log.i("gsh", "entrando en prefs, np=" + np);
        regid  = prefs.getString("regid", "No hay registro");
        if(!regid.equals("No hay registro")){regid="Registrado";}
/*
        AlertDialog.Builder build = new AlertDialog.Builder(this);
//setting some title text
        build.setTitle("SomeTitle");
//setting radiobuttons list
        build.setSingleChoiceItems(new String[]{"One", "Two"}, 0, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("dialog", "entrando en prefs");

                //Some behavior here
                Toast.makeText(getBaseContext(), "firing!!!", Toast.LENGTH_LONG).show();

            }
        });
        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Might be empty
            }
        });
        //creating dialog and showing
        AlertDialog dialog = build.create();
        dialog.show();
*/
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
            Log.i("prefs1", "entrando en prefs");
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
                    Log.i("prefs2", "entrando en prefs2");
                    df.show(getFragmentManager(), "activationDialog");
                    //Toast.makeText(getActivity(), "firing!!!", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            ep2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.i("prefs2", "entrando en prefs2");
                    df.show(getFragmentManager(), "activationDialog");
                    //Toast.makeText(getActivity(), "firing!!!", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            ep3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.i("prefs2", "entrando en prefs2");
                    df.show(getFragmentManager(), "activationDialog");
                    //Toast.makeText(getActivity(), "firing!!!", Toast.LENGTH_LONG).show();
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
                            // do something
                        }
                    })
                    .create();
        }
    }


}