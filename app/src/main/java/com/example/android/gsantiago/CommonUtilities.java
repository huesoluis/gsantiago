package com.example.android.gsantiago;

import android.content.Context;
import android.content.Intent;
import static com.example.android.gsantiago.ServerUtilities.URL;

/**
 * Created by LHueso on 08/04/2015.
 */
public final class CommonUtilities {



        // give your server registration url here
        static final String SERVER_URL = "http://"+URL+"/gcm_server_php/register.php";

        // Google project id
        static final String SENDER_ID = "";

        /**
         * Tag used on log messages.
         */
        static final String TAG = "GCMD";

        static final String DISPLAY_MESSAGE_ACTION =
                "com.example.android.gsantiago.DISPLAY_MESSAGE";

        static final String EXTRA_MESSAGE = "message";

        /**
         * Notifies UI to display a message.
         * <p>
         * This method is defined in the common helper because it's used both by
         * the UI and the background service.
         *
         * @param context application's context.
         * @param message message to be displayed.
         */
        static void displayMessage(Context context, String message) {
            Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
            intent.putExtra(EXTRA_MESSAGE, message);
            context.sendBroadcast(intent);
        }
    }


