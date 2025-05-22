package com.example.aplicatieandroidip;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayDeque;


public class ManualControlActivity extends AppCompatActivity implements ServiceConnection, SerialListener {

    private static final String TAG = "ManualControl";
    private enum Connected { False, Pending, True }
    private String deviceAddress;
    private SerialService service;
    private Connected connected = Connected.False;
    private boolean initialStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
        deviceAddress = getIntent().getStringExtra("device");
        Log.d(TAG, "Received address = " + deviceAddress);

        findViewById(R.id.btnUp).setOnClickListener(v -> send("F"));  // F = Forward
        findViewById(R.id.btnDown).setOnClickListener(v -> send("B"));     // B = Backward
        findViewById(R.id.btnLeft).setOnClickListener(v -> send("L"));     // L = Left
        findViewById(R.id.btnRight).setOnClickListener(v -> send("R"));    // R = Right

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        stopService(new Intent(this, SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if(service != null)
        {
            service.attach(this);
            Log.d(TAG, "Service Attached");
        }
        else {
            startService(new Intent(this, SerialService.class));
            bindService(new Intent(this, SerialService.class), this, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "Service bound");
        }
    }

    @Override
    public void onStop() {
        if(service != null && !this.isChangingConfigurations())
            service.detach();
        unbindService(this);
        Log.d(TAG, "Service unbound");
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(initialStart && service != null) {
            initialStart = false;
            Log.d(TAG, "Resuming");
            connect();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Log.d(TAG, "onService called");
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart) {
            initialStart = false;
            Log.d(TAG, "Service Attached");
            connect();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            SerialSocket socket = new SerialSocket(this.getApplicationContext(), device);
            service.connect(socket);
            connected = Connected.Pending;
            Log.d(TAG, "Service connecting...");
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
        Log.d(TAG, "Service disconnected");
    }

    private void send(String message) {
        if (connected != Connected.True) {
            // Optionally show a Toast to indicate failure
            return;
        }

        try {
            byte[] data = (message + "\n").getBytes();  // newline is optional
            service.write(data);
            Log.d(TAG, "Message sent" + data);
        } catch (Exception e) {
            onSerialIoError(e);  // fallback handling
        }
    }

    @Override
    public void onSerialConnect() {
        Log.d(TAG, "Service Connected");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        Log.e(TAG, "Service couldn't connect" + e.getMessage());
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        ArrayDeque<byte[]> datas = new ArrayDeque<>();
        datas.add(data);
    }

    public void onSerialRead(ArrayDeque<byte[]> datas) {
    }

    @Override
    public void onSerialIoError(Exception e) {
        Log.e(TAG, "Service couldn't connect" + e.getMessage());
        disconnect();
    }
}