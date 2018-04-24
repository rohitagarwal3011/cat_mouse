package com.example.rohit.rat_mouse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGameActivity extends AppCompatActivity {

    @BindView(R.id.btnGenerate)
    Button btnGenerate;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btnGenerate)
    public void onViewClicked() {

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.hatch);
        Bitmap qrCode = AwesomeQRCode.create(UUID.randomUUID().toString(), 800, 20, 0.3f, Color.BLACK, Color.WHITE,image, true, true);
        ivQRCode.setImageBitmap(qrCode);
    }
}
