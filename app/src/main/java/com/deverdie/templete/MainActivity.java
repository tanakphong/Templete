package com.deverdie.templete;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;


public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private Button mButton2;
    private TextView mTextView;
//    private boolean enabled = true;
    private ConstraintLayout mMainLayout;
    private FrameLayout frameLayout;
    private View myView;

    private int count = 0;
    private long startMillis=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        HUDView mView = new HUDView(this);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.RIGHT | Gravity.TOP;
//        params.setTitle("Load Average");
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        wm.addView(mView, params);

        hideSystemUI();
        blockTouch();
//        unblockTouch();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myView = inflater.inflate(R.layout.activity_invisible, null);
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == event.ACTION_DOWN) {
                    Log.d("dlg", "touch me");

                    //get system current milliseconds
                    long time= System.currentTimeMillis();

                    //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                    if (startMillis==0 || (time-startMillis> 2000) ) {
                        startMillis=time;
                        count=1;
                    }
                    //it is not the first, and it has been  less than 3 seconds since the first
                    else{ //  time-startMillis< 3000
                        count++;
                    }

                    if (count==5) {
                        //do whatever you need
                        wm.removeView(myView);
                        unblockTouch();
                    }
                }
                return false;
            }
        });
        Button btn5 = (Button) myView.findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dlg", "Click");
                wm.removeView(myView);
                unblockTouch();
            }
        });

// Add layout to window manager
        wm.addView(myView, params);

//        wm.removeView(myView);

//        ClickFragment cl = new ClickFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new ClickFragment());
        fragmentTransaction.commit();
        mMainLayout = (ConstraintLayout) findViewById(R.id.mainlayout);
        mMainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i("dlg", "mMainLayout.onTouch: "+motionEvent.toString());
                return false;
            }
        });
//        frameLayout = (FrameLayout) findViewById(R.id.fragment);
        mButton = (Button) findViewById(R.id.button);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Unblock", Toast.LENGTH_SHORT).show();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("dlg", "mButton2 onClick: Block");
                wm.addView(myView, params);
                blockTouch();
//                Toast.makeText(MainActivity.this, "Block", Toast.LENGTH_SHORT).show();
            }
        });

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        );
        mTextView = (TextView) findViewById(R.id.textView);
    }

    private void unblockTouch() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void blockTouch() {
        getWindow().setFlags(FLAG_NOT_TOUCHABLE,FLAG_NOT_TOUCHABLE);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        char unicodeChar = (char) event.getUnicodeChar();
//        mTextView.setText(mTextView.getText().toString() + String.valueOf(unicodeChar));
//        return true;
//    }
//    public void enable(boolean b) {
//        enabled = b;
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mMainLayout.dispatchTouchEvent(ev);
//        super.dispatchTouchEvent(ev);
//        Toast.makeText(this, "dispatchTouchEvent", Toast.LENGTH_SHORT).show();
        return true;
//        return enabled ?
//                super.dispatchTouchEvent(ev) :
//                true;
//        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            //
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(this, "mTextView.getText().toString():" + mTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    mTextView.setText("");
                } else {
//                    char unicodeChar = (char) event.getUnicodeChar();
                    mTextView.setText(mTextView.getText().toString() + String.valueOf((char) event.getUnicodeChar()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        // ATTENTION: GET THE X,Y OF EVENT FROM THE PARAMETER
//        // THEN CHECK IF THAT IS INSIDE YOUR DESIRED AREA
//
//
//        Toast.makeText(getApplicationContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
//        return true;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("dlg", "onTouchEvent: "+event.toString());
//
////        Toast.makeText(this, "onTouchEvent", Toast.LENGTH_SHORT).show();
//        return false;
//    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
}
