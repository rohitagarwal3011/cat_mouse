package com.example.rohit.rat_mouse;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class WebActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener ,EasyPermissions.PermissionCallbacks {


    WebView webView;
    ImageButton up;
    ImageButton down;
    ImageButton right;
    ImageButton left;

    ImageButton up2;
    ImageButton down2;
    ImageButton right2;
    ImageButton left2;

    String user_type;

    DatabaseReference cat_status;
    DatabaseReference rat_status;
    DatabaseReference audience_count;
    DatabaseReference cat_pause;
    DatabaseReference rat_pause;
    DatabaseReference gameover;
    DatabaseReference life_count;
    DatabaseReference timer;

    String id;

    ValueEventListener cat_status_listener;
    ValueEventListener rat_status_listener;
    ValueEventListener cat_pause_Listener;
    ValueEventListener rat_pause_Listener;
    ValueEventListener gameoverListener;
    ValueEventListener life_countListener;
    ValueEventListener timerListener;


    Boolean cat_pause_status = false;
    Boolean rat_pause_status = false;
    @BindView(R.id.flOverlay)
    FrameLayout flOverlay;
    @BindView(R.id.btEnd)
    Button btEnd;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.refresh)
    ImageButton refresh;
    @BindView(R.id.tvUserType)
    TextView tvUserType;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String[] PERM_CAMERA = {Manifest.permission.CAMERA,android.Manifest.permission.RECORD_AUDIO};

    private Boolean isPermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        webView = (WebView) findViewById(R.id.webview);
        up = (ImageButton) findViewById(R.id.up);
        down = (ImageButton) findViewById(R.id.down);
        right = (ImageButton) findViewById(R.id.right);
        left = (ImageButton) findViewById(R.id.left);

        up2 = (ImageButton) findViewById(R.id.up2);
        down2 = (ImageButton) findViewById(R.id.down2);
        right2 = (ImageButton) findViewById(R.id.right2);
        left2 = (ImageButton) findViewById(R.id.left2);

        btEnd.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);

        id = getIntent().getStringExtra("code");

        FirebaseSetup();
        setListeners();
        gameover.setValue(false);
        cat_pause.setValue(false);
        rat_pause.setValue(false);
        //up.setOnClickListener(this);
//        down.setOnClickListener(this);
//        right.setOnClickListener(this);
//        left.setOnClickListener(this);

//        up2.setOnClickListener(this);
//        down2.setOnClickListener(this);
//        right2.setOnClickListener(this);
//        left2.setOnClickListener(this);
        setclicks();

        setWebView();

        set_user_type();
    }

    private void set_user_type() {

        user_type = getIntent().getStringExtra("type");
        tvUserType.setText(user_type);
        if (user_type.equalsIgnoreCase("cat")) {
            Toast.makeText(this, "You are cat", Toast.LENGTH_LONG).show();
            up.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);

            up2.setVisibility(View.GONE);
            down2.setVisibility(View.GONE);
            right2.setVisibility(View.GONE);
            left2.setVisibility(View.GONE);
        } else if (user_type.equalsIgnoreCase("rat")) {
            Toast.makeText(this, "You are rat", Toast.LENGTH_LONG).show();
            up2.setVisibility(View.VISIBLE);
            down2.setVisibility(View.VISIBLE);
            left2.setVisibility(View.VISIBLE);
            right2.setVisibility(View.VISIBLE);

            up.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "You are audience", Toast.LENGTH_LONG).show();


        }
    }

    private void setWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("WebActivity", "onPermissionRequest");
                WebActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Log.d("WebActivity", request.getOrigin().toString());
                        // if(request.getOrigin().toString().equals(getIntent().getStringExtra("link")))
                        if (true) {
                            Log.d("WebActivity", "GRANTED");
                            request.grant(request.getResources());
                        } else {
                            Log.d("WebActivity", "DENIED");
                            request.deny();
                        }
                    }
                });
            }


        });
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, "JSInterface");
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if(hasCameraPermission()){
            isPermissionGranted=true;
            webView.loadUrl(getIntent().getStringExtra("link"));
            //myWebView.loadUrl(alertScript());
        }else{
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your camera so you can take pictures.",
                    REQUEST_CAMERA_PERMISSION,
                    PERM_CAMERA);
        }


    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(WebActivity.this, PERM_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        webView.loadUrl(getIntent().getStringExtra("link"));
        isPermissionGranted=true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        isPermissionGranted=false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.up:
//                webView.loadUrl("javascript:"+"moveUp(1);");
//                break;
//
//            case R.id.down:
//                webView.loadUrl("javascript:"+"moveDown(1);");
//                break;
//
//            case R.id.right:
//                webView.loadUrl("javascript:"+"moveRight(1);");
//                break;
//
//            case R.id.left:
//                webView.loadUrl("javascript:"+"moveLeft(1);");
//                break;


//            case R.id.up2:
//                webView.loadUrl("javascript:"+"moveDown(2);");
//                break;
//
//            case R.id.down2:
//                webView.loadUrl("javascript:"+"moveUp(2);");
//                break;
//
//            case R.id.right2:
//                webView.loadUrl("javascript:"+"moveLeft(2);");
//                break;
//
//            case R.id.left2:
//                webView.loadUrl("javascript:"+"moveRight(2);");
//                break;

        }
    }

    private void setclicks() {
        up.setOnTouchListener(this);
        down.setOnTouchListener(this);
        left.setOnTouchListener(this);
        right.setOnTouchListener(this);

        up2.setOnTouchListener(this);
        down2.setOnTouchListener(this);
        left2.setOnTouchListener(this);
        right2.setOnTouchListener(this);

        btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameover.setValue(true);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:" + "init();");
                webView.loadUrl("javascript:" + "setallfalse();");
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.up:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setUpKeyTrue();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setUpKeyFalse();");
                }
                break;

            case R.id.down:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setDownKeyTrue();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setDownKeyFalse();");
                }
                break;

            case R.id.right:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setRightKeyTrue();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setRightKeyFalse();");
                }
                break;

            case R.id.left:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setLeftKeyTrue();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setLeftKeyFalse();");
                }
                break;


            case R.id.up2:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setUpKey2True();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setUpKey2False();");
                }
                break;

            case R.id.down2:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setDownKey2True();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setDownKey2False();");
                }
                break;

            case R.id.right2:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setRightKey2True();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setRightKey2False();");
                }
                break;


            case R.id.left2:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.loadUrl("javascript:" + "setLeftKey2True();");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    webView.loadUrl("javascript:" + "setLeftKey2False();");
                }
                break;

        }
        return true;
    }

    @OnClick(R.id.back)
    public void onViewClicked() {

        finish();
    }

    @Override
    public void onBackPressed() {
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class JavaScriptInterface {
        private Activity activity;

        public JavaScriptInterface(Activity activiy) {
            this.activity = activiy;
        }

        @JavascriptInterface
        public void startVideo() {

            Toast.makeText(getApplicationContext(), "Rat got caught!!", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this.activity,MainActivity.class);
//            activity.startActivity(intent);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse(videoAddress), "video/3gpp"); // The Mime type can actually be determined from the file
//            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {

        removeListeners();
        if (user_type.equalsIgnoreCase("cat")) {
            cat_pause.setValue(false);
            cat_status.setValue(false);
            gameover.setValue(true);
        } else if (user_type.equalsIgnoreCase("rat")) {
            rat_pause.setValue(false);
            rat_status.setValue(false);
            gameover.setValue(true);
        } else {
            Toast.makeText(this, "You are audience", Toast.LENGTH_LONG).show();


        }
        super.onDestroy();

    }

    @Override
    protected void onPause() {

        if (user_type.equalsIgnoreCase("cat")) {
            cat_pause.setValue(true);
        } else if (user_type.equalsIgnoreCase("rat")) {
            rat_pause.setValue(true);
        } else {
            Toast.makeText(this, "You are audience", Toast.LENGTH_LONG).show();


        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user_type.equalsIgnoreCase("cat")) {
            cat_pause.setValue(false);
        } else if (user_type.equalsIgnoreCase("rat")) {
            rat_pause.setValue(false);
        } else {
            Toast.makeText(this, "You are audience", Toast.LENGTH_LONG).show();


        }
    }

    private void FirebaseSetup() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cat_status = database.getReference("connect").child(id).child("catStatus");
        rat_status = database.getReference("connect").child(id).child("ratStatus");
        audience_count = database.getReference("connect").child(id).child("audienceCount");
        cat_pause = database.getReference("connect").child(id).child("catPause");
        rat_pause = database.getReference("connect").child(id).child("ratPause");
        gameover = database.getReference("connect").child(id).child("gameover");

    }

    private void setListeners() {

        gameoverListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((Boolean) dataSnapshot.getValue()) {
                    flOverlay.setVisibility(View.VISIBLE);
                    tvMsg.setText("Game Ended");
                    hidebuttons();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        gameover.addValueEventListener(gameoverListener);

        cat_pause_Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cat_pause_status = (Boolean) dataSnapshot.getValue();
                check_for_pause();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        cat_pause.addValueEventListener(cat_pause_Listener);

        rat_pause_Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                rat_pause_status = (Boolean) dataSnapshot.getValue();
                check_for_pause();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        rat_pause.addValueEventListener(rat_pause_Listener);

        life_countListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        timerListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        cat_status_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        rat_status_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    private void removeListeners()
    {
        gameover.removeEventListener(gameoverListener);
        cat_pause.removeEventListener(cat_pause_Listener);
        rat_pause.removeEventListener(rat_pause_Listener);
        cat_status.removeEventListener(cat_status_listener);
        rat_status.removeEventListener(rat_status_listener);
        timer.removeEventListener(timerListener);
    }

    private void check_for_pause() {
        if (rat_pause_status || cat_pause_status) {
            flOverlay.setVisibility(View.VISIBLE);
            tvMsg.setText("Paused");
            if (user_type.equalsIgnoreCase("cat")) {
                //Toast.makeText(this, "You are cat", Toast.LENGTH_LONG).show();
                up.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                left.setVisibility(View.GONE);

                up2.setVisibility(View.GONE);
                down2.setVisibility(View.GONE);
                right2.setVisibility(View.GONE);
                left2.setVisibility(View.GONE);
            } else if (user_type.equalsIgnoreCase("rat")) {
                //Toast.makeText(this, "You are rat", Toast.LENGTH_LONG).show();
                up2.setVisibility(View.GONE);
                down2.setVisibility(View.GONE);
                right2.setVisibility(View.GONE);
                left2.setVisibility(View.GONE);
                up.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
            } else {
                //Toast.makeText(this, "You are audience", Toast.LENGTH_LONG).show();


            }
        } else {
            flOverlay.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
            btEnd.setVisibility(View.VISIBLE);

            if (user_type.equalsIgnoreCase("cat")) {
                //Toast.makeText(this, "You are cat", Toast.LENGTH_LONG).show();
                up.setVisibility(View.VISIBLE);
                down.setVisibility(View.VISIBLE);
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);

                up2.setVisibility(View.GONE);
                down2.setVisibility(View.GONE);
                right2.setVisibility(View.GONE);
                left2.setVisibility(View.GONE);
            } else if (user_type.equalsIgnoreCase("rat")) {
                //Toast.makeText(this, "You are rat", Toast.LENGTH_LONG).show();
                up2.setVisibility(View.VISIBLE);
                down2.setVisibility(View.VISIBLE);
                left2.setVisibility(View.VISIBLE);
                right2.setVisibility(View.VISIBLE);

                up.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
            } else {
                //Toast.makeText(this, "You are audience", Toast.LENGTH_LONG).show();


            }
        }
    }

    private void hidebuttons()
    {
        refresh.setVisibility(View.GONE);
        btEnd.setVisibility(View.GONE);
        up.setVisibility(View.GONE);
        down.setVisibility(View.GONE);
        right.setVisibility(View.GONE);
        left.setVisibility(View.GONE);
        up2.setVisibility(View.GONE);
        down2.setVisibility(View.GONE);
        right2.setVisibility(View.GONE);
        left2.setVisibility(View.GONE);
    }
}
