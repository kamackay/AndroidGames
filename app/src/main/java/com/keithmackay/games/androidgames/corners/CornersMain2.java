package com.keithmackay.games.androidgames.corners;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.keithmackay.games.androidgames.R;

public class CornersMain2 extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_corners_main2);
        webView = (WebView) findViewById(R.id.corners2_webview);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                webView.addJavascriptInterface(new JSInterface(this), "Android");
            webView.loadUrl("file:///android_asset/corners.html");
        }
    }

    private class JSInterface {
        Context mContext;

        public JSInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void saveBoard(String boardData) {
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
            edit.putString(mContext.getString(R.string.settings_corners_boardData), boardData);
            edit.apply();
        }

        @JavascriptInterface
        public void toast(String text) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public int getStressLevel() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            return prefs.getInt(mContext.getString(R.string.settings_stressLevel), CornersMain.StressLevel.Low.val());
        }
    }
}
