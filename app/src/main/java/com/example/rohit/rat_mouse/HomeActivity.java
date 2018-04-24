package com.example.rohit.rat_mouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.btnCreate)
    Button btnCreate;
    @BindView(R.id.btnAccept)
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnCreate, R.id.btnAccept})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:

                Intent intent = new Intent(HomeActivity.this,CreateGameActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAccept:
                Intent intent1 = new Intent(HomeActivity.this,AcceptChallengeActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
