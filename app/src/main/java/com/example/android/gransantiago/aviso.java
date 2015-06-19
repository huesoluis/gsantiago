package com.example.android.gransantiago;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;


public class Aviso extends ActionBarActivity {
String msg;
    String msg2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Aviso.this);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (i != null) {
            msg2=extras.getString("mensaje");
            msg=i.getStringExtra("mensaje");

            Log.i("notificaciones", "recibido aviso:" + msg+msg2);
            // set title
            alertDialogBuilder.setTitle("Aviso importante");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Recibido:"+msg)
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            Aviso.this.finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {}


}
