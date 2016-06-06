package edu.ucsb.cs.cs185.yan.finalproject185;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    DrawerLayout drawer;
    Boolean isForward;
    EditText edittext;
    Boolean isDragging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isForward = false;
        isDragging = false;

        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new myWebViewClient());
        edittext = (EditText) findViewById(R.id.urlField);

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
                    } else if (url.substring(0, 7).toLowerCase() != "http://" &&
                            url.substring(0, 8).toLowerCase() != "https://") {
                        url = "http://" + url;
                    }

                    webView.loadUrl(url);
                    return true;
                }
                return false;
            }
        });

        // Top buttons
        Button back_button = (Button) findViewById(R.id.buttonBackward);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //edittext.setText(webView.getUrl());
            }
        });

        Button forward_button = (Button) findViewById(R.id.buttonForward);
        forward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForwardPressed();
                //edittext.setText(webView.getUrl());
            }
        });

        Button refresh_button = (Button) findViewById(R.id.button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshPress();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(drawerListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // do what you need to with the event, and then...
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Toast.makeText(getApplicationContext(), "touchListener DOWN", Toast.LENGTH_SHORT).show();
                break;

            case MotionEvent.ACTION_MOVE:
                //Toast.makeText(getApplicationContext(), "touchListener MOVE", Toast.LENGTH_SHORT).show();
                //edittext.setText(event.getRawX()+"");
                break;

            case MotionEvent.ACTION_UP:
                //edittext.setText("up");
                float yPos = event.getRawY();
                float xPos = event.getRawX();

                if (isDragging) {
                    if (yPos >= 1300 && yPos <= 1420 && xPos >= 520) {
                        drawer.closeDrawer(Gravity.RIGHT);
                        onBackPressed();
                        //edittext.setText(webView.getUrl());
                    } else if (yPos > 1420 && yPos <= 1520 && xPos >= 520) {
                        drawer.closeDrawer(Gravity.RIGHT);
                        onForwardPressed();
                        //edittext.setText(webView.getUrl());
                    } else if (yPos > 1520 && yPos <= 1620 && xPos >= 520) {
                        drawer.closeDrawer(Gravity.RIGHT);
                        onRefreshPress();
                    } else if (yPos > 1620 && yPos <= 1750 && xPos >= 520) {
                        drawer.closeDrawer(Gravity.RIGHT);
                        onUrlPress();
                    } else {
                        //Toast.makeText(getApplicationContext(), "missed", Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(getApplicationContext(), "touchListener UP", Toast.LENGTH_SHORT).show();
                isDragging = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {
            switch(newState) {
                case DrawerLayout.STATE_SETTLING:
                    isDragging = false;
                    break;
                case DrawerLayout.STATE_IDLE:
                    isDragging = false;
                    break;
                case DrawerLayout.STATE_DRAGGING:
                    //Toast.makeText(getApplicationContext(), "STATE_DRAGGING", Toast.LENGTH_SHORT).show();
                    isDragging = true;
                    break;
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.btn_forward) {
            onForwardPressed();
        } else if (id == R.id.btn_refresh) {
            onRefreshPress();
        } else if (id == R.id.btn_url) {
            onUrlPress();
        }

        drawer.closeDrawer(Gravity.RIGHT);

        return true;
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            //addToBackStack(url);
            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            edittext.setText(url);
            webView.loadUrl(url);

            return true;
        }
    }

    private boolean onRefreshPress() {
        Toast.makeText(getApplicationContext(), R.string.btn_refresh, Toast.LENGTH_SHORT).show();
        webView.loadUrl(webView.getUrl());

        return true;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            Toast.makeText(getApplicationContext(), R.string.btn_back, Toast.LENGTH_SHORT).show();
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();
            edittext.setText(historyUrl);

            webView.goBack();

            return;
        }

        // Otherwise defer to system default behavior.
        return;
    }

    public void onForwardPressed() {
        if (webView.canGoForward()) {
            Toast.makeText(getApplicationContext(), R.string.btn_forward, Toast.LENGTH_SHORT).show();
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            String forwardUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() + 1).getUrl();
            edittext.setText(forwardUrl);
            webView.goForward();
        }
    }

    private boolean onUrlPress() {
        //Toast.makeText(getApplicationContext(), R.string.btn_url, Toast.LENGTH_SHORT).show();
        edittext.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);

        return true;
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
}
