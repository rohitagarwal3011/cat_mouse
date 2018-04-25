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

    DatabaseReference session_node;
    DatabaseReference cat_status;
    DatabaseReference rat_status;
    DatabaseReference audience_count;

    ValueEventListener cat_status_listener;
    ValueEventListener rat_status_listener;

    Boolean cat = false, rat = false;
    @BindView(R.id.reset)
    Button reset;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        ButterKnife.bind(this);
        id= getIntent().getStringExtra("code");
        FirebaseSetup();
        CheckforUsers();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        FirebaseSetup();
//        CheckforUsers();
        addListeners();


    }

    @Override
    protected void onPause() {
        super.onPause();
        removeListeners();
    }

    @OnClick({R.id.btnCat, R.id.btnMouse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCat:

                if(!cat)
                {
                    cat_status.setValue(true);
                   // removeListeners();
                    Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
                    intent.putExtra("link", Constants.link);
                    intent.putExtra("type", "cat");
                    intent.putExtra("code",id);
                    startActivity(intent);
                }
//                cat_status.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Boolean status = (Boolean) dataSnapshot.getValue();
//                        if (!status) {
//                            cat_status.setValue(true);
//                            removeListeners();
//                            Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
//                            intent.putExtra("link", Constants.link);
//                            intent.putExtra("type", "cat");
//                            intent.putExtra("code",id);
//                            startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                break;

            case R.id.btnMouse:

                if(!rat)
                {
                    rat_status.setValue(true);
                   // removeListeners();
                    Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
                    intent.putExtra("link", Constants.link);
                    intent.putExtra("type", "rat");
                    intent.putExtra("code",id);
                    startActivity(intent);
                }
//                rat_status.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Boolean status = (Boolean) dataSnapshot.getValue();
//                        if (!status) {
//                            rat_status.setValue(true);
//                            removeListeners();
//                            Intent intent = new Intent(PlayerSelectActivity.this, WebActivity.class);
//                            intent.putExtra("link", Constants.link);
//                            intent.putExtra("type", "rat");
//                            intent.putExtra("code",id);
//                            startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                break;
        }
    }

    private void FirebaseSetup() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        session_node = database.getReference("connect").child(id);
        cat_status = database.getReference("connect").child(id).child("catStatus");
        rat_status = database.getReference("connect").child(id).child("ratStatus");
        audience_count = database.getReference("connect").child(id).child("audienceCount");
    }

    private void CheckforUsers() {
        cat_status_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean status = (Boolean) dataSnapshot.getValue();
                if (status) {
                    btnCat.setEnabled(false);
                    cat = true;
                } else {
                    btnCat.setEnabled(true);
                    cat = false;
                }
               // check_for_audience();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        cat_status.addValueEventListener(cat_status_listener);

        rat_status_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean status = (Boolean) dataSnapshot.getValue();
                if (status) {
                    btnMouse.setEnabled(false);
                    rat = true;
                } else {
                    btnMouse.setEnabled(true);
                    rat = false;
                }
                //check_for_audience();

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
            intent.putExtra("code",id);
            startActivity(intent);
        }
        else {
            addListeners();
        }
    }

    private void removeListeners() {
        cat_status.removeEventListener(cat_status_listener);
        rat_status.removeEventListener(rat_status_listener);
    }

    private void addListeners(){
        cat_status.addValueEventListener(cat_status_listener);
        rat_status.addValueEventListener(rat_status_listener);
    }

    @OnClick(R.id.reset)
    public void onViewClicked() {

        cat_status.setValue(false);
        rat_status.setValue(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        session_node.setValue(null);
        Intent intent = new Intent(PlayerSelectActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}
