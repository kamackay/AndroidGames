package com.keithmackay.games.androidgames;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class CornersMain2 extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corners_main2);
        webView = (WebView) findViewById(R.id.corners2_webview);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JSInterface(this), "Android");
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
    }
}
