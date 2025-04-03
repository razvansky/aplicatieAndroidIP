package com.example.aplicatieandroidip;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicatieandroidip.NFC.NFCRead;

public class NFCActivity extends AppCompatActivity {

    private NFCRead.NfcReader nfcReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nfcReader = new NFCRead.NfcReader(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Start NFC reader mode when the activity is in the foreground
        if (nfcReader.isNfcEnabled()) {
            nfcReader.startNfcReaderMode();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop NFC reader mode when the activity is in the background
        nfcReader.stopNfcReaderMode();
    }


}