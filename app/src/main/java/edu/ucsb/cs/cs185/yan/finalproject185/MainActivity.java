package edu.ucsb.cs.cs185.yan.finalproject185;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

//gesture libraries
import android.view.MotionEvent;
import android.gesture.Gesture;

import java.net.URL;
import java.security.Key;

//forward/back
import java.util.Deque;
import java.util.ArrayDeque;

import static android.view.GestureDetector.*;

public class MainActivity extends AppCompatActivity
        implements OnGestureListener, OnDoubleTapListener, NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    private GestureDetector GestureDetect;
    DrawerLayout drawer;
    Deque<String> back_stack = new ArrayDeque<>();
    Deque<String> forward_stack = new ArrayDeque<>();
    Boolean isForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize stacks for forward/back tracking
        //back_stack = new ArrayDeque<>();
        //forward_stack = new ArrayDeque<>();
        isForward = false;

        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new myWebViewClient());
        final EditText edittext = (EditText) findViewById(R.id.urlField);

        GestureDetect = new GestureDetector(this, this);
        GestureDetect.setOnDoubleTapListener(this);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String url = edittext.getText().toString();
                    if (url.length() <= 7) {
                        url = "http://" + url;
                    } else if (url.substring(0,7).toLowerCase() != "http://" &&
                            url.substring(0,8).toLowerCase() != "https://") {
                        url = "http://" + url;
                    }

                    addToBackStack(url);

                    //Toast.makeText(getApplicationContext(),edittext.getText(), Toast.LENGTH_SHORT).show();
                    //webView.setWebViewClient(new myWebViewClient());
                    webView.loadUrl(url);
                    return true;
                }
                return false;
            }
        });


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //ListView listView =  (ListView) findViewById(R.id.right_drawer);
        //listView.

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_back) {
            Toast.makeText(getApplicationContext(), R.string.btn_back, Toast.LENGTH_SHORT).show();
            onBackPress();
        } else if (id == R.id.btn_forward) {
            Toast.makeText(getApplicationContext(), R.string.btn_forward, Toast.LENGTH_SHORT).show();
            isForward = true;
            onForwardPress();
        } else if (id == R.id.btn_refresh) {
            Toast.makeText(getApplicationContext(), R.string.btn_refresh, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_url) {
            Toast.makeText(getApplicationContext(), R.string.btn_url, Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(Gravity.RIGHT);

        return true;
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            //addToBackStack(url);
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            webView.loadUrl(url);

            return true;
        }
    }

    private boolean addToBackStack(String url) {
        if (!isForward) {
            if (!forward_stack.isEmpty())
                forward_stack.clear();
        }
        isForward = false;

        back_stack.addFirst(url);

        return true;
    }

    private boolean onBackPress() {
        if (back_stack.isEmpty() || back_stack.size() == 1) {
            return false;
        } else {
            forward_stack.addFirst(back_stack.removeFirst());
            webView.loadUrl(back_stack.peekFirst());

            return true;
        }
    }

    private boolean onForwardPress() {
        if (forward_stack.isEmpty()) {
            return false;
        } else {
            back_stack.addFirst(forward_stack.peekFirst());
            webView.loadUrl(forward_stack.removeFirst());

            return true;
        }
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if((KeyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(KeyCode, event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GestureDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(getApplicationContext(), "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(getApplicationContext(), "onDoubleTap", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
