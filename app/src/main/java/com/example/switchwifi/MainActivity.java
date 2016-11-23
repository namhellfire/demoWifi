package com.example.switchwifi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final String TAG = "MAIN ACTIVITY";

    WifiManager wifiManager;
    Handler handler;
    Runnable runnable;
    AlertDialog alertDialog;
    WifiInfo wifiInfo;
    BroadcastReceiver receiverRSSIChanged;
    BroadcastReceiver recieverNetworkChanged;
    boolean isSwitchWifi = false;
    //AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiverRSSIChanged = this.myRssiChangeReceiver;
        recieverNetworkChanged = this.NetworkChangedState;

        this.registerReceiver(receiverRSSIChanged,
                new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

//        CreateAlertDialog(this).show();
        alertDialog = CreateAlertDialog(this).create();
//        if (!alertDialog.isShowing()) {
//            alertDialog.show();
//        }
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                CheckSignalWifi();
//                handler.postDelayed(this, 1000);
//            }
//        };
        //handler.postDelayed(runnable, 500);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onResume() {
        super.onResume();
        turnGPSOn();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //handler.removeCallbacks(runnable);
        unregisterReceiver(receiverRSSIChanged);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AutoConnectWifi();
                } else {
                    //do somethings

                }
                break;
        }
    }

    private void turnGPSOn() {
        int locationMode;
        try {
            locationMode = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (locationMode != Settings.Secure.LOCATION_MODE_OFF) {

            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } catch (Settings.SettingNotFoundException e) {
            String provider = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
    }

    public AlertDialog.Builder CreateAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to switch wifi ?")
                .setPositiveButton("Auto", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                        } else {
                            AutoConnectWifi();
                        }

                    }
                })
                .setNegativeButton("Manual", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        //startActivityForResult(intent, 0);
                        //startActivity(intent);

                        showListWifi(MainActivity.this);
                        dialog.dismiss();
                    }
                });
        return builder;
        //builder.create();
        //builder.show();
    }

    private BroadcastReceiver myRssiChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (wifiManager == null) {
                wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
            }

            wifiInfo = wifiManager.getConnectionInfo();

            int newRssi = wifiInfo.getRssi();
            Toast.makeText(context, "new Rssi = " + newRssi, Toast.LENGTH_SHORT);
            Log.e(TAG, "new Rssi = " + newRssi);
            if (newRssi <= -70) {
                //showDialog(this);
                unregisterReceiver(receiverRSSIChanged);
                if (alertDialog == null) {
                    alertDialog = CreateAlertDialog(MainActivity.this).create();
                    alertDialog.show();
                } else {
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }

            }
        }
    };

    BroadcastReceiver NetworkChangedState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "changed network ");

        }
    };


    public void showListWifi(final Context context) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.select_dialog_item);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        //do somethings
        final List<WifiConfiguration> wificonfig = wifiManager.getConfiguredNetworks();
        List<ScanResult> scanResults = new ArrayList<ScanResult>();
        scanResults = wifiManager.getScanResults();
        for (int i = 0; i < scanResults.size(); i++) {
            for (int j = 0; j < wificonfig.size(); j++) {
                if (scanResults.get(i).SSID.toString().trim().equals(wificonfig.get(j).SSID.replace("\"", "").toString().trim())) {
                    arrayAdapter.add(scanResults.get(i).SSID.toString());
                    break;
                }
            }
        }

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String SSIDWifi = arrayAdapter.getItem(which);
                        for (int i = 0; i < wificonfig.size(); i++) {
                            if (wificonfig.get(i).SSID.toString().equals("\"" + SSIDWifi + "\"")) {
                                isSwitchWifi = wifiManager.enableNetwork(wificonfig.get(i).networkId, true);
                                if (isSwitchWifi) {
                                    SystemClock.sleep(2000);
                                    registerReceiver(receiverRSSIChanged,
                                            new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
                                }
                            }
                        }
                    }
                });
        builderSingle.show();
    }

    private void CheckSignalWifi() {
        if (wifiManager == null) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        }

        wifiInfo = wifiManager.getConnectionInfo();

        int newRssi = wifiInfo.getRssi();
        Log.e(TAG, "new Rssi = " + newRssi);
        if (newRssi <= -70) {
            //showDialog(this);
            if (alertDialog == null) {
                alertDialog = CreateAlertDialog(MainActivity.this).create();
                alertDialog.show();
            } else {
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            }

        }
    }

    private void AutoConnectWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        //do somethings
        List<WifiConfiguration> wificonfig = new ArrayList<WifiConfiguration>();
        wificonfig = wifiManager.getConfiguredNetworks();
        List<ScanResult> scanResults = new ArrayList<ScanResult>();
        scanResults = wifiManager.getScanResults();

        long signalMax = wifiInfo.getRssi();
        int IDNetworkStrong = 0;
        for (int i = 0; i < wificonfig.size(); i++) {
            for (int j = 0; j < scanResults.size(); j++) {
                if (wificonfig.get(i).SSID.toString().equals("\"" + scanResults.get(j).SSID.toString() + "\"")) {
                    if (signalMax < scanResults.get(j).level) {
                        signalMax = scanResults.get(j).level;
                        IDNetworkStrong = wificonfig.get(i).networkId;
                        break;
                    }
                }
            }
        }
        Log.e(TAG, "signal max strong :" + signalMax + " of wifi :" + IDNetworkStrong);
        isSwitchWifi = wifiManager.enableNetwork(IDNetworkStrong, true);
        if (isSwitchWifi) {
            SystemClock.sleep(2000);
            registerReceiver(receiverRSSIChanged,
                    new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        }
    }
}


//        Excellent >-50 dBm
//
//        Good -50 to -60 dBm
//
//        Fair -60 to -70 dBm
//
//        Weak < -70 dBm