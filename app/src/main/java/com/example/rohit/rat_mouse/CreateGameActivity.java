package com.example.rohit.rat_mouse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGameActivity extends AppCompatActivity {

    @BindView(R.id.btnGenerate)
    Button btnGenerate;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;

    DatabaseReference connect;
    String random_code;
    String status_child = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);
        setupFirebase();
    }


    @OnClick(R.id.btnGenerate)
    public void onViewClicked() {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.hatch);
        random_code = UUID.randomUUID().toString();
        Bitmap qrCode = AwesomeQRCode.create(random_code, 800, 20, 0.3f, Color.BLACK, Color.WHITE,image, true, true);
        ivQRCode.setImageBitmap(qrCode);
        create_new_session();
    }

    private void setupFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        connect = database.getReference("connect");
    }

    private void create_new_session()
    {
        Session session = new Session(random_code);
        connect.child(random_code).setValue(session);
        setupListener();
    }

    private void setupListener()
    {
        connect.child(random_code).child(status_child).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if((Boolean)dataSnapshot.getValue())
                {
                    proceed_to_player_selection();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void proceed_to_player_selection()
    {
        Intent intent = new Intent(CreateGameActivity.this,PlayerSelectActivity.class);
        intent.putExtra("code",random_code);
        startActivity(intent);

    }




}
