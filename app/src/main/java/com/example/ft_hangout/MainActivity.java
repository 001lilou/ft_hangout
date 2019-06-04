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


import com.example.ft_hangout.Entity.Contacts;
import com.example.ft_hangout.FtHangoutFragment.ContactDetailsFragment;
import com.example.ft_hangout.FtHangoutFragment.ContactsListFragment;
import com.example.ft_hangout.Utils.ThemeUtil;
import com.example.ft_hangout.ViewModel.ContactsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForSmsPermission();
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
                if( _contact.getNumobile() != null)
                {
                    smsIntent.putExtra("address",_contact.getNumobile());
                }
                else
                {
                    smsIntent.putExtra("address","");
                   // sendSMS("","");

                }
                smsIntent.putExtra("sms_body","");
                startActivity(smsIntent);


                //Toast.makeText(this, "Bouton SMS clické !", Toast.LENGTH_LONG).show();
         /*   case R.id.phone:
                _contact = _contactsViewModel.getSelectedContact().getValue();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setType("vnd.android-dir/mms-sms");
                if( _contact.getNumobile() != null)
                {
                    Toast.makeText(this, "YESSSS", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Can't resolve app for ACTION_DIAL Intent.", Toast.LENGTH_LONG).show();
                }
                dialIntent.putExtra("sms_body","Body of Message");
                startActivity(dialIntent);*/
                break;
            default:
                return;
        }
    }
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.

                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
