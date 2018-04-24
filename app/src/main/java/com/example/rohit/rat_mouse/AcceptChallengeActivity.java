package com.example.rohit.rat_mouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AcceptChallengeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_challenge);

        mScannerView = (ZXingScannerView)findViewById(R.id.camera_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("QR result", rawResult.getText()); // Prints scan results
        Toast.makeText(this,rawResult.getText(),Toast.LENGTH_LONG).show();
        Log.v("QR result", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
//        Intent intent = new Intent(AcceptChallengeActivity.this, MainActivity.class);
//        intent.putExtra("code","http://"+rawResult.getText());
//        startActivity(intent);
//        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
