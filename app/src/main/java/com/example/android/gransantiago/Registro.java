package com.example.android.gransantiago;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.android.gransantiago.ServerUtilities.REGISTER_URL;



public class Registro extends FragmentActivity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REGID = "regid";
    public static final String PROPERTY_DNI = "dni";
    public static final String PROPERTY_NUMPROFESOR = "numprofesor";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //ConnectionDetector cd;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
     String regid,numprofesor,dni,SENDER_ID = "880980506015";
       Context con;
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMD";
    AlertDialogManager alert =new AlertDialogManager();

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    // UI elements
    EditText txtNp;
    EditText txtDni;

    // Register button
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //mDisplay = (TextView) findViewById(R.id.display);
        Log.i(TAG, "oncreate");
        context = getApplicationContext();
        con=this.getWindow().getContext();
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);

           regid=getRegistrationId(context);
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        // Check if GCM configuration is set
        if (REGISTER_URL == null || SENDER_ID == null || REGISTER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            //alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
            //      "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
            return;
        }

        txtDni = (EditText) findViewById(R.id.txtDni);
        txtNp = (EditText) findViewById(R.id.txtNp);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtDni.setText("");
        txtNp.setText("");
/*
         * Click event on Register button
         * */
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dni = txtDni.getText().toString();
                numprofesor= txtNp.getText().toString();

// Comprobamos si se ha rellenado el formulario correctamente
                if(dni.trim().length() !=8 && numprofesor.trim().equals("")){
                    Toast.makeText(getApplicationContext(), "dni o número de profesor incorrecto, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    alert.showAlertDialog(Registro.this, "Error en los datos", "Inténtalo de nuevo", false);
                    return;
                }
                if (regid.isEmpty()) {
                     Toast.makeText(getApplicationContext(), "Iniciando registro...", Toast.LENGTH_SHORT).show();
                     RegistroTask tarea = new RegistroTask();
                     tarea.execute(Registro.this);
                    }else{
                    Toast.makeText(getApplicationContext(), "Ya estas registrado", Toast.LENGTH_LONG).show();
                             }

                finish();

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
// Check device for Play Services APK.
        checkPlayServices();


    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private  String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REGID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(Registro.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
// should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

//Tarea que realiza el registro en segundo plano
    private class RegistroTask extends AsyncTask<Activity,Integer,Boolean> {

        Boolean res;



        public RegistroTask(){


        }

        protected Boolean doInBackground(Activity... params) {
            try {
            //gcm.unregister();
                //registrando en google gcm
            regid =gcm.register(SENDER_ID);
//regid="provisional";
                Log.i(TAG, "ok registro");
            }catch(Exception e){            Log.i(TAG, "no regid");}
            Log.i(TAG, "enviando registro a iserver:"+regid);

            //registrando en el servidor iserver
          res=sendRegistrationIdToBackend();
            //registrando en preferencias
            Log.i(TAG, "resultado registro en iserver:"+res);
            Log.i("registronp", numprofesor);

            if(res) storeRegistrationId(context, regid, dni,numprofesor);

            return res;
        }


        protected void onPostExecute(Boolean result) {

            if(!result) {
                Toast t=new Toast(getApplicationContext());
                t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                t.makeText(getApplicationContext(), "Ha sido imposible realizar el registro\n Consulta al administrador en zizeog@gmail.com" +
                        "o revisa tu conexión\n", Toast.LENGTH_LONG).show();
            }
            else {

                setResult(RESULT_OK);
                Toast.makeText(getApplicationContext(), "Registro completado con éxito!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private boolean sendRegistrationIdToBackend() {

        boolean registro;
       registro=ServerUtilities.register(context, numprofesor, dni, regid,"alta");
        return registro;
    }
    private void storeRegistrationId(Context context, String regid, String dni, String numprofesor) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REGID, regid);
        editor.putString(PROPERTY_DNI, dni);
        editor.putString(PROPERTY_NUMPROFESOR, numprofesor);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

}
