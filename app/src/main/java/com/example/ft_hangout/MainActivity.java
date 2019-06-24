package com.example.ft_hangout;


import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.ft_hangout.entity.Contacts;
import com.example.ft_hangout.fragments.ContactAddModifyFragment;
import com.example.ft_hangout.fragments.ContactDetailsFragment;
import com.example.ft_hangout.fragments.ContactsListFragment;
import com.example.ft_hangout.utils.ThemeUtil;
import com.example.ft_hangout.viewmodel.ContactsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import android.net.Uri;


import static com.example.ft_hangout.R.id.fragment_container;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ContactsListFragment listFragment;
    private FragmentTransaction fragmentTransaction;
    private ImageButton imgButton;
    private ContactsViewModel _contactsViewModel;
    private Contacts _contact;
    private String dateTime;
    private static final String DATETIME = "dateTime";
    SharedPreferences sharedPreferences;
    private static final String CHANNEL_ID = "FT_HANGOUT_CHANNEL";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    private final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001;

    private TelephonyManager _telephonyManager;
    private MyPhoneCallListener mListener;

    public boolean isPhoneActivated, isSMSActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionsState();

        if (findViewById(fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }
        listFragment = new ContactsListFragment();
        listFragment.getLifecycle();
        _contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);

        sharedPreferences = getBaseContext().getSharedPreferences(DATETIME, MODE_PRIVATE);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, listFragment, null).commit();


        // Create a telephony manager.
        _telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // Check to see if Telephony is enabled.
        if (isTelephonyEnabled()) {
            Log.d(TAG, getString(R.string.telephony_enabled));
            // Check for phone permission.
            //checkForPhonePermission();
            // Register the PhoneStateListener to monitor phone activity.
            mListener = new MyPhoneCallListener();
            _telephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
        } else {
            Toast.makeText(this,
                    R.string.telephony_not_enabled, Toast.LENGTH_LONG).show();
            Log.d(TAG, getString(R.string.telephony_not_enabled));
            // Disable the call button.
            //disableCallButton();
        }
    }

    /**
     * Checks whether the app has SMS permission.
     */
    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    /**
     * Checks whether the app has phone-calling permission.
     */
    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Permission already granted. Enable the call button.
        }
    }

    private void checkPermissionsState() {

        // CALL PHONE
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            isPhoneActivated = true;

        }

        // SEND SMS
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            isSMSActivated = true;
        }
    }

    /**
     * Checks whether Telephony is enabled.
     *
     * @return true if enabled, otherwise false
     */
    private boolean isTelephonyEnabled() {
        if (_telephonyManager != null) {
            if (_telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityFromFragment(fragment, intent, requestCode, options);
    }


    public void onClick(View v) {
        imgButton = (ImageButton) v;
        switch (imgButton.getId()) {
            case R.id.details:
                fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactDetailsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.sms:
                _contact = _contactsViewModel.getSelectedContact().getValue();

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                smsIntent.setType("vnd.android-dir/mms-sms");
                if (_contact.getNumobile() != null) {
                    smsIntent.putExtra("address", _contact.getNumobile());
                } else {
                    smsIntent.putExtra("address", "");
                    // sendSMS("","");

                }
                smsIntent.putExtra("sms_body", "");


                // If package resolves to an app, check for phone permission,
                // and send intent.
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    checkForSmsPermission();
                    if (isSMSActivated)
                        startActivity(smsIntent);
                } else {
                    Log.e(TAG, "Can't resolve app for ACTION_SEND Intent.");
                }
                //Toast.makeText(this, "Bouton SMS clické !", Toast.LENGTH_LONG).show();
                break;
            case R.id.phone:
                _contact = _contactsViewModel.getSelectedContact().getValue();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                // Set the data for the intent as the phone number.
                callIntent.setData(Uri.parse("tel:" + _contact.getNumobile()));
                // If package resolves to an app, check for phone permission,
                // and send intent.
                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    checkForPhonePermission();
                    if (isPhoneActivated)
                        startActivity(callIntent);
                } else {
                    Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
                }
                break;
        }
    }

    /**
     * Getters and setters
     *
     * @return
     */

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isPhoneActivated() {
        return isPhoneActivated;
    }

    public boolean isSMSActivated() {
        return isSMSActivated;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    isSMSActivated = true;
                    listFragment.getAdapter().notifyDataSetChanged();
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                }
                break;
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (permissions[0].equalsIgnoreCase(Manifest.permission.CALL_PHONE)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    isPhoneActivated = true;
                    listFragment.getAdapter().notifyDataSetChanged();
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                }
                break;
            case READ_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    ((ContactAddModifyFragment) frag).openPickImage();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    /**
     * Monitors and logs phone call activities, and shows the phone state
     * in a toast message.
     */
    private class MyPhoneCallListener extends PhoneStateListener {
        private boolean returningFromOffHook = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // Define a string for the message to use in a toast.
            String message = getString(R.string.phone_status);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // Incoming call is ringing (not used for outgoing call).
                    message = message +
                            getString(R.string.ringing) + incomingNumber;
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // Phone call is active -- off the hook.
                    message = message + getString(R.string.offhook);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    returningFromOffHook = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // Phone is idle before and after phone call.
                    // If running on version older than 19 (KitKat),
                    // restart activity when phone call ends.
                    message = message + getString(R.string.idle);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    /*if (returningFromOffHook) {
                        // No need to do anything if >= version KitKat.
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            Log.i(TAG, getString(R.string.restarting_app));
                            // Restart the app.
                            Intent intent = getPackageManager()
                                    .getLaunchIntentForPackage(getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }*/
                    break;
                default:
                    message = message + "Phone off";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        getDelegate().onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, dateTime, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateTime = getCurrentTimeStamp();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
        dateTime = getCurrentTimeStamp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}
