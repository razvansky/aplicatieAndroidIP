package com.example.aplicatieandroidip;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class bluetoothfrag extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "bluetoothfrag";

    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices;
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvDeviceList;
    public bluetoothfrag(){}
    // BroadcastReceiver-urile sunt identice, doar contextul se ia cu requireContext()
    private final BroadcastReceiver mBroadcastReceiverToggleBT = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "Bluetooth OFF", Toast.LENGTH_SHORT).show(); break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "Bluetooth ON", Toast.LENGTH_SHORT).show(); break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiverDiscoverBT = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getName() != null && !deviceExists(device)) {
                    mBTDevices.add(device);
                    if (mDeviceListAdapter == null) {
                        mDeviceListAdapter = new DeviceListAdapter(requireContext(), mBTDevices, R.layout.device_adapter_view);
                        lvDeviceList.setAdapter(mDeviceListAdapter);
                    } else {
                        mDeviceListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiverPairBT = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                int prevBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                if (bondState == BluetoothDevice.BOND_BONDED && prevBondState == BluetoothDevice.BOND_BONDING) {
                    launchManualControl(mDevice);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lvDeviceList = view.findViewById(R.id.lvDeviceList);
        mBTDevices = new ArrayList<>();

        lvDeviceList.setOnItemClickListener(this);

        requireActivity().registerReceiver(mBroadcastReceiverPairBT, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

        Button toggleButton = view.findViewById(R.id.btnToggleBT);
        toggleButton.setOnClickListener(this::toggleBT);

        Button refreshButton = view.findViewById(R.id.btnRefreshDevices);
        refreshButton.setOnClickListener(this::refreshDevices);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().unregisterReceiver(mBroadcastReceiverToggleBT);
        requireActivity().unregisterReceiver(mBroadcastReceiverDiscoverBT);
        requireActivity().unregisterReceiver(mBroadcastReceiverPairBT);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public void toggleBT(View v){
        if (mBluetoothAdapter == null) return;
        if (!mBluetoothAdapter.isEnabled()) {
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        } else {
            mBluetoothAdapter.disable();
        }
        requireActivity().registerReceiver(mBroadcastReceiverToggleBT, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void refreshDevices(View v){
        mBTDevices.clear();
        if (mDeviceListAdapter != null) mDeviceListAdapter.notifyDataSetChanged();
        discoverBT();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void discoverBT(){
        if (mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.isDiscovering()) mBluetoothAdapter.cancelDiscovery();
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            requireActivity().registerReceiver(mBroadcastReceiverDiscoverBT, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        } else {
            Toast.makeText(requireContext(), "Bluetooth nu este pornit", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBTPermissions(){
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED ||
                requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    private boolean deviceExists(BluetoothDevice device){
        for (BluetoothDevice bt : mBTDevices)
            if (bt.getAddress().equals(device.getAddress()))
                return true;
        return false;
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT})
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice selectedDevice = mBTDevices.get(position);
        if (selectedDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            launchManualControl(selectedDevice);
        } else {
            selectedDevice.createBond();
        }
    }

    private void launchManualControl(BluetoothDevice device) {
        // Intent intent = new Intent(requireContext(), ManualControlActivity.class);
        //intent.putExtra("device", device.getAddress());
        //startActivity(intent);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_intern,new controalefrag()).commit();
    }
}
