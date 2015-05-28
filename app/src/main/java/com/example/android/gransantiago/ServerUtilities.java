package com.example.android.gransantiago;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Random;




public final class ServerUtilities {
    private static final int MAX_ATTEMPTS = 5;
    public static final String IP = "10.2.100.7";
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    // url de registro
    static final String REGISTER_URL = "http://"+IP+"/gcm_server_php/register.php";
    //url para cargar guardias
    static final String GUARDIAS_URL = "http://www.iessantiagohernandez.com/XmlGuardia/guardias.xml";

    // Google project id
    static final String SENDER_ID = "";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMD";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.example.android.gransantiago.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Register this account/device pair within the server.
     *
     */
    static Boolean register(Context context, String numprofesor,String dni, String regid, String alta) {
        Boolean r=false;
        Log.i(TAG, "entrando en funcion register");
        Log.i("unreg","entrando en register");


        String serverUrl = REGISTER_URL;
        String params = "regid="+regid+"&dni="+dni+"&numprofesor="+numprofesor+"&alta="+alta;

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                Log.i("unreg","posteando");

                r=postfinal(serverUrl, params);
                String message = context.getString(R.string.server_registered);

                return r;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }

            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        // Co    mmonUtilities.displayMessage(context, message);
    return r;

    }



    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */

    private static Boolean postfinal(String endpoint, String params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        Log.i(TAG, "entrando en postfinal");
        Log.i("unreg","entrando en postfinal");

         HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Log.i("unreg","buscando respuesta");

        Log.d("respuesta", response.toString());
        return true;
    }

}

