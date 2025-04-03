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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "BluetoothActivity";
    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices;
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvDeviceList;

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

    private final BroadcastReceiver mBroadcastReceiverDiscoverBT = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: Action Found.");

            if(action.equals(BluetoothDevice.ACTION_FOUND))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(device != null && !deviceExists(device) && device.getName() != null)
                {
                    mBTDevices.add(device);
                    Log.d(TAG, "onReceive: " + device.getName() + " " + device.getAddress());

                    // Preserve scroll position
                    int index = lvDeviceList.getFirstVisiblePosition();
                    View v = lvDeviceList.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();

                    if (mDeviceListAdapter == null) {
                        mDeviceListAdapter = new DeviceListAdapter(context, mBTDevices, R.layout.device_adapter_view);
                        lvDeviceList.setAdapter(mDeviceListAdapter);
                    } else {
                        mDeviceListAdapter.notifyDataSetChanged();
                    }

                    lvDeviceList.setSelectionFromTop(index, top);
                }

            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiverPairBT = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                switch (mDevice.getBondState())
                {
                    case BluetoothDevice.BOND_BONDED:
                        Log.d(TAG, "onReceive: BOND_BONDED");
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Log.d(TAG, "onReceive: BOND_BONDING");
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d(TAG, "onReceive: BOND_NONE");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();       // Checks if device is capable of BT (usually is)
        lvDeviceList = findViewById(R.id.lvDeviceList);
        mBTDevices = new ArrayList<>();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);      //This is broadcast only when there is an attempt at bonding
        registerReceiver(mBroadcastReceiverPairBT, filter);
        lvDeviceList.setOnItemClickListener(BluetoothActivity.this);        //Makes it so that an item can be selected
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: CALLED");
        super.onDestroy();

        // Unregisters the Broadcast Receivers
        unregisterReceiver(mBroadcastReceiverToggleBT);
        unregisterReceiver(mBroadcastReceiverToggleDiscoverability);
        unregisterReceiver(mBroadcastReceiverDiscoverBT);
        unregisterReceiver(mBroadcastReceiverPairBT);
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
    public void toggleDiscoverability(View v) {
        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "toggleDiscoverability: Making device discoverable for 60 seconds");

            Intent enableDiscoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            enableDiscoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
            startActivity(enableDiscoverableIntent);

            IntentFilter discoverableIntent = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            registerReceiver(mBroadcastReceiverToggleDiscoverability, discoverableIntent);
        }
        else {
            Log.d(TAG, "toggleDiscoverability: BLUETOOTH IS NOT ENABLED!");
            Toast.makeText(getBaseContext().getApplicationContext(), "Please Enable Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    ///TO DO: Deschide lista de discover intr-un fragment
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void discoverBT(View v)
    {
        Button btnRefreshDevices = findViewById(R.id.btnRefreshDevices);
        if(mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "discoverBT: Looking for unpaired devices.");
            btnRefreshDevices.setVisibility(View.VISIBLE);
            btnRefreshDevices.setEnabled(false);

            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, "discoverBT: Canceling discovery.");
            }

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiverDiscoverBT, discoverDevicesIntent);
            new android.os.Handler().postDelayed(() -> {
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "discoverBT: Stopping discovery after 30 seconds.");
                }
                btnRefreshDevices.setEnabled(true);  // Re-enable button after stopping discovery
            }, 30000);
        }
        else{
            Log.d(TAG, "toggleDiscoverability: BLUETOOTH IS NOT ENABLED!");
            Toast.makeText(getBaseContext().getApplicationContext(), "Please Enable Bluetooth", Toast.LENGTH_SHORT).show();
            btnRefreshDevices.setVisibility(View.VISIBLE);
            btnRefreshDevices.setEnabled(false);
        }
    }
    private void checkBTPermissions()               //Android 6.0+ needs to check some permissions in order to be able to discover other devices
    {
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COURSE_LOCATION");
        if(permissionCheck != 0)
        {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    private boolean deviceExists(BluetoothDevice device)            //Function for checking if the device is already in the ListView
    {
        for(BluetoothDevice BTDevice : mBTDevices)
        {
            if(BTDevice.getAddress().equals(device.getAddress())){
                return true;
            }
        }
        return false;
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT})
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();        //This eats a lot of memory if not cancelled
        Log.d(TAG, "onItemClick: Chose a device");
        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();

        Log.d(TAG, "onItemClick: deviceName: " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress: " + deviceAddress);

        Log.d(TAG, "Trying to pair with " + deviceName);
        mBTDevices.get(position).createBond();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void refreshDevices(View v)
    {
        Log.d(TAG, "refreshDevices: Refreshing available devices...");

        mBTDevices.clear();
        if(mDeviceListAdapter != null)
        {
            mDeviceListAdapter.notifyDataSetChanged();
        }

        discoverBT(v);
    }
}