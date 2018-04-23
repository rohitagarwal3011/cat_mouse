package com.example.rohit.rat_mouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerSelectActivity extends AppCompatActivity {


    @BindView(R.id.btnCat)
    Button btnCat;
    @BindView(R.id.btnMouse)
    Button btnMouse;

    DatabaseReference cat_status;
    DatabaseReference rat_status;
    DatabaseReference audience_count;

    ValueEventListener cat_status_listener;
    ValueEventListener rat_status_listener;

    Boolean cat = false, rat = false;
    @BindView(R.id.reset)
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        ButterKnife.bind(this);
        FirebaseSetup();
        CheckforUsers();

    }

    @OnClick({R.id.btnCat, R.id.btnMouse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCat:

                cat_status.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String status = (String) dataSnapshot.getValue();
                        if (status.equalsIgnoreCase("not_active")) {
                            cat_status.setValue("active");
                            removeListeners();
                            Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
                            intent.putExtra("link", Constants.link);
                            intent.putExtra("type", "cat");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case R.id.btnMouse:

                rat_status.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String status = (String) dataSnapshot.getValue();
                        if (status.equalsIgnoreCase("not_active")) {
                            rat_status.setValue("active");
                            removeListeners();
                            Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
                            intent.putExtra("link", Constants.link);
                            intent.putExtra("type", "rat");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }
    }

    private void FirebaseSetup() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cat_status = database.getReference("cat_status");
        rat_status = database.getReference("rat_status");
        audience_count = database.getReference("audience_count");
    }

    private void CheckforUsers() {
        cat_status_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = (String) dataSnapshot.getValue();
                if (status.equalsIgnoreCase("active")) {
                    btnCat.setEnabled(false);
                    cat = true;
                } else {
                    btnCat.setEnabled(true);
                    cat = false;
                }
                check_for_audience();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        cat_status.addValueEventListener(cat_status_listener);

        rat_status_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = (String) dataSnapshot.getValue();
                if (status.equalsIgnoreCase("active")) {
                    btnMouse.setEnabled(false);
                    rat = true;
                } else {
                    btnMouse.setEnabled(true);
                    rat = false;
                }
                check_for_audience();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        rat_status.addValueEventListener(rat_status_listener);


    }

    private void check_for_audience() {
        if (cat && rat) {
            removeListeners();
            Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
            intent.putExtra("link", Constants.link);
            intent.putExtra("type", "audience");
            startActivity(intent);
        }
    }

    private void removeListeners() {
        cat_status.removeEventListener(cat_status_listener);
        rat_status.removeEventListener(rat_status_listener);
    }

    @OnClick(R.id.reset)
    public void onViewClicked() {

        cat_status.setValue("not_active");
        rat_status.setValue("not_active");
    }
}
