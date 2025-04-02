package com.example.aplicatieandroidip;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
    BluetoothAdapter mBluetoothAdapter;

    /*
     * Verifica si "asculta" starile device-ului
     * */

    private final BroadcastReceiver mBroadcastReceiverToggleBT = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state)
                {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        Toast.makeText(context, "Bluetooth is now OFF", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE ON");
                        Toast.makeText(context, "Bluetooth is now ON", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiverToggleDiscoverability = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED))
            {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode)
                {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "onReceive: Discoverability Enabled");
                        Toast.makeText(context, "Discoverability Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive: Discoverability Disabled. Able to receive connections");
                        Toast.makeText(context, "Discoverability Disabled. Able to receive connections.", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive: Discoverability Disabled. Not able to receive connections");
                        Toast.makeText(context, "Discoverability Disabled. Not able to receive connections", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // Checks if device is capable of BT (usually is)

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: CALLED");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiverToggleBT); // Unregisters the Broadcast Receiver
        unregisterReceiver(mBroadcastReceiverToggleDiscoverability);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public void toggleBT(View v){
        if(mBluetoothAdapter == null)
        {
            Log.d(TAG, "toggleBT: Device not capable of Bluetooth");
        }
        if(!mBluetoothAdapter.isEnabled())
        {
            Log.d(TAG, "toggleBT: Enabling BT");
            Intent enableBTIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiverToggleBT, BTIntent); //Tells the receiver the state of the device
        }
        if(mBluetoothAdapter.isEnabled())
        {
            Log.d(TAG, "toggleBT: Disabling BT");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiverToggleBT, BTIntent);
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    public void toggleDiscoverability(View v)
    {
        Log.d(TAG, "toggleDiscoverability: Making device discoverable for 60 seconds");

        Intent enableDiscoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        enableDiscoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
        startActivity(enableDiscoverableIntent);

        IntentFilter discoverableIntent = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiverToggleDiscoverability, discoverableIntent);
    }
}