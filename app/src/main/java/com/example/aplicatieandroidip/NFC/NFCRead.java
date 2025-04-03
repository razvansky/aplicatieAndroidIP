package com.example.aplicatieandroidip.NFC;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NFCRead {

    public static class NfcReader {

        private NfcAdapter nfcAdapter;
        private boolean isNfcEnabled = false;
        private Activity activity;

        public NfcReader(Activity activity) {
            this.activity = activity;
            // Initialize the NFC adapter
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

            if (nfcAdapter == null) {
                // NFC is not supported on this device
                Toast.makeText(activity, "NFC not supported on this device", Toast.LENGTH_SHORT).show();
            }

            if (!nfcAdapter.isEnabled()) {
                // NFC is disabled, prompt user to enable NFC
                Toast.makeText(activity, "Please enable NFC in your settings", Toast.LENGTH_SHORT).show();
            } else {
                isNfcEnabled = true;
            }
        }

        public boolean isNfcEnabled() {
            return isNfcEnabled;
        }

        public void startNfcReaderMode() {
            if (isNfcEnabled) {
                // Enable NFC foreground dispatch system to receive NFC intents
                nfcAdapter.enableReaderMode(activity, (NfcAdapter.ReaderCallback) activity, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
            }
        }

        public void stopNfcReaderMode() {
            if (isNfcEnabled) {
                // Disable the NFC reader mode when not in use
                nfcAdapter.disableReaderMode(activity);
            }
        }

        public void onTagDiscovered(Tag tag) {
            // This method will be triggered when an NFC tag is discovered
            byte[] tagId = tag.getId();
            String tagIdString = bytesToHex(tagId);

            Log.d("RFIDScanner", "Tag ID: " + tagIdString);

            // Handle the discovered RFID tag, for example, display a message
            Toast.makeText(activity, "Scanned RFID Tag: " + tagIdString, Toast.LENGTH_SHORT).show();
        }
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}
