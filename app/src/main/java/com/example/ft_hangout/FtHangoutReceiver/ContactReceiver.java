package com.example.ft_hangout.FtHangoutReceiver;

import android.annotation.TargetApi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.telephony.*;
import android.util.Log;
import android.widget.Toast;

import com.example.ft_hangout.R;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class ContactReceiver extends BroadcastReceiver {

    private static final String TAG = ContactReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the SMS message.
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");

        // Retrieve the SMS message received.
            if(bundle != null){

                    Object[] pdus = (Object[]) bundle.get(pdu_type);

                    if (pdus != null) {
                        // Check the Android version.
                        boolean isVersionM =
                                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                        // Fill the msgs array.
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            // Check Android version and use appropriate createFromPdu.
                            if (isVersionM) {
                                // If Android version M or newer:
                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                            } else {
                                // If Android version L or older:
                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            }
                            // Build the message to show.
                            strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                            strMessage += " :" + msgs[i].getMessageBody() + "\n";


                            // Log and display the SMS message.
                            Log.d(TAG, "onReceive: " + strMessage);
                            Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                        }
                    }

           /* intent = new Intent(this, ContactReceiver.class);
                intent.setAction(ACTION_SNOOZE);
                intent.putExtra(EXTRA_NOTIFICATION_ID, 0);
                PendingIntent snoozePendingIntent =
                        PendingIntent.getBroadcast(this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_sms_not)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.ic_snooze, getString(R.string.snooze),
                                snoozePendingIntent);*/


            }
        }
    }

}
