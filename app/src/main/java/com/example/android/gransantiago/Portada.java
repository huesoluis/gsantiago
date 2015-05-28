package com.example.android.gransantiago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static com.example.android.gransantiago.ServerUtilities.GUARDIAS_URL;

public class Portada extends ActionBarActivity {


//variables globales
final String PROPERTY_NUMPROFESOR = "numprofesor";
    final String PROPERTY_DNI = "dni";
    final String PROPERTY_REGID = "regid";
    private ArrayList<Falta> faltas=new ArrayList<>();
    private ArrayList<Falta> rfaltas=new ArrayList<>();

    static final String TAG = "GS";
    Context context;
    private ProgressBar mProgressBar;
    Button btnCarga;
    ListView lView;
    AdaptadorFaltas adaptador;
    Menu menu;

    String numprofesor,regid,dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adaptador = new AdaptadorFaltas(this);

//Establecemos el menu superior con sus propiedades
        ActionBar sactionbar = getSupportActionBar();
        sactionbar.show();
        sactionbar.setLogo(R.drawable.sh);
        sactionbar.setDisplayUseLogoEnabled(true);
        sactionbar.setDisplayShowTitleEnabled(true);
        sactionbar.setHomeButtonEnabled(true);
        sactionbar.setBackgroundDrawable(new ColorDrawable(0xffff6666)); // set your desired color
     //   sactionbar.setDisplayHomeAsUpEnabled(true);
        sactionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bggg_main));

//layout de la portada
        setContentView(R.layout.portada);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        btnCarga = (Button) findViewById(R.id.button1);
        lView = (ListView) findViewById(R.id.LstGuardias);
        lView.setVisibility(View.GONE);

//asociamos adaptador al listview
        lView.setAdapter(adaptador);
        btnCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Portada.this, "Cargando guardias...", Toast.LENGTH_LONG).show();
                cargafaltas();
            }
        });


    }
//fin oncreate

    @Override
    protected void onStart() {
        super.onStart();
    }

//cuando volvemos a la actividad actual desde la notificación actualizamos el listado de faltas automáticamente
    @Override
    protected void onResume() {
        Intent i=getIntent();
        if(i !=null && i.getBooleanExtra("pi",false))
        {
        cargafaltas();
        }
        regid=getRegistrationId(context);
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//Metodo para lanzar la tarea asíncrona encargada de cargar las faltas del servidor externo eserver

    protected void cargafaltas() {
        final Context mContext=null;
        final     Cargarguardias cguardias = new Cargarguardias();
        cguardias.execute(GUARDIAS_URL);
        Thread thread1 = new Thread(){
            public void run(){
                try {
                    cguardias.get(10000, TimeUnit.MILLISECONDS);  //Establecemos el timeout

                } catch (Exception e) {
                    cguardias.cancel(true);
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            btnCarga.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(ProgressBar.GONE);

                            Toast.makeText(Portada.this, "Hay problemas con la carga, revisa tu red o contacta al administrador", Toast.LENGTH_LONG).show();
                         }
                    });
                }
            }
        };
        thread1.start();
    }

//metodo para lanzar actividad de preferencias cuando se llama desde el menu
    public boolean mpreferencias() {
        Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
        startActivity(i);
        return true;
    }

//metodo para lanzar actividad de registro
    public boolean registro() {
        Intent i = new Intent(getApplicationContext(), Registro.class);
        startActivityForResult(i, 1);
        return true;
    }
//metodo para cancelar el registro
    public boolean unregistro() {

        GoogleCloudMessaging gcm;
        Log.i("unreg","cancelando registro...");
        gcm = GoogleCloudMessaging.getInstance(context);
       //borrando preferencias
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        SharedPreferences prefslocal=getSharedPreferences("Registro", MODE_PRIVATE);

        regid=prefslocal.getString(PROPERTY_REGID,"vacio");
        dni=prefslocal.getString(PROPERTY_DNI,"vacio");
        numprofesor=prefslocal.getString(PROPERTY_NUMPROFESOR, "vacio");
        editor.clear();
        Log.i("unreg","cancelado en prefs");

//cancelar registro en gcm
        editor.commit();

        try {
            gcm.unregister();
                }catch(Exception e){}

//cancelar registro en servidor iserver
        Log.i("unreg","cancelando en iserver");

        CancelRegistroTask cr=new CancelRegistroTask();
        cr.execute();



        Log.i("unreg","cancelado con exito");

        return true;
    }

//metodo para recoger los resultados de la actividad registro
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)
           updatemenu(0);

    }

//metodo para ocultar o mostrar opciones de menu según si el usuario está o no registrado
    private void updatemenu(int op) {
        MenuItem ireg = menu.findItem(R.id.registrar);
        MenuItem iunreg = menu.findItem(R.id.unregistrar);
        if(op==0)
        {
            ireg.setVisible(false);
            ireg.setEnabled(false);
            iunreg.setVisible(true);
            iunreg.setEnabled(true);
        }
        else{
            iunreg.setVisible(false);
            iunreg.setEnabled(false);
            ireg.setVisible(true);
            ireg.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuportada, menu);
        this.menu=menu;
        if(!getRegistrationId(context).isEmpty())
        {
            MenuItem item = menu.findItem(R.id.registrar);
            item.setVisible(false);
            item.setEnabled(false);
        }
        else{
            MenuItem item = menu.findItem(R.id.unregistrar);
            item.setVisible(false);
            item.setEnabled(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.preferencias:
                mpreferencias();
                return true;
            case R.id.actualizar:
                actualizar();
                return true;
            case R.id.registrar:
               registro();
                return true;
            case R.id.unregistrar:
                unregistro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actualizar() {
        Cargarguardias cguardias = new Cargarguardias();
        cguardias.execute(GUARDIAS_URL);
    }

    //cargamos las guardias del servidor web (iserver) en segundo plano
    private class Cargarguardias extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            btnCarga.setVisibility(View.GONE);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        protected Boolean doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                RssParserDom3 saxparser =
                        new RssParserDom3(params[0]);

                rfaltas = saxparser.parse();

            } catch (Exception e) {
            }
     return true;
        }


        protected void onPostExecute(Boolean result) {
            Toast.makeText(Portada.this, "Guardias cargadas", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            lView.setVisibility(View.VISIBLE);

            faltas.addAll(rfaltas);

           adaptador.notifyDataSetChanged();
        }
    }


//clase adaptador para mostrar el listado de guardias
    class AdaptadorFaltas extends ArrayAdapter<Falta> {

        Activity context;

        AdaptadorFaltas(Activity context) {
            super(context, R.layout.listitem_falta, faltas);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.listitem_falta, null);

            TextView tsesion = (TextView)item.findViewById(R.id.sesion);
            tsesion.setText("Sesion: "+faltas.get(position).getSesion());

            TextView tpfalta = (TextView)item.findViewById(R.id.pfalta);
            tpfalta.setText(faltas.get(position).getProfesorFalta());

            TextView tpcubre = (TextView)item.findViewById(R.id.pcubre);
            tpcubre.setText(faltas.get(position).getProfesorCubre());

            TextView tasignatura = (TextView)item.findViewById(R.id.asignatura);
            tasignatura.setText(faltas.get(position).getAsignatura());


            if(tasignatura.getText().equals("a2")) item.setBackgroundColor(Color.GREEN);
            return(item);
        }
    }

    private  String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REGID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }
//Obtenemos las preferencias del fichero de preferencias
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(Registro.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
//clase asíncrona para cancelar el registro
  private class CancelRegistroTask extends AsyncTask<Void,Integer,Boolean> {

      private Boolean result=true;

      protected Boolean doInBackground(Void... params) {
            result = unregisterToBackend();
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                updatemenu(1);

                Toast.makeText(Portada.this, "Se ha dado de baja con éxito", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(Portada.this, "Hubo un error en la baja", Toast.LENGTH_SHORT).show();

              return;
        }

    }
    private Boolean unregisterToBackend() {
        Log.i("unreg", "desregistering device (regId = " + regid + ")"+numprofesor+dni+regid);
        Boolean res=ServerUtilities.register(context, numprofesor, dni, regid,"baja");
        Log.i("desreg", "fin de sendregtobackend"+res.toString());

        return res;
    }

}

