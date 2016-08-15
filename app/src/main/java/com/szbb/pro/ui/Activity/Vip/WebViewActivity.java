package com.szbb.pro.ui.activity.vip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.szbb.pro.R;
import com.szbb.pro.WebViewLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.widget.VideoEnabledWebChromeClient;
import com.szbb.pro.widget.VideoEnabledWebView;


public class WebViewActivity extends BaseAty {
    private WebViewLayout detailLayout;
    private String url = "";

    private VideoEnabledWebView webView;
    private ProgressBar progressBar;

    private String title = "";
    private VideoEnabledWebChromeClient webChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailLayout = (WebViewLayout) viewDataBinding;
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(title)) {
            AppTools.removeSingleActivity(this);
        }
        LogTools.i(url);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(title);
        webView = detailLayout.webView;
        progressBar = detailLayout.progressBar;
    }

    @Override
    protected void initEvents() {
        webChromeClient = new VideoEnabledWebChromeClient(detailLayout.nonLayout,
                detailLayout.fullScreenLayout,
                null,
                webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                if (progress >= 99) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar
                LogTools.w(fullscreen);
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new InsideWebViewClient());
        webView.loadUrl(url);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onBackPressed() {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed()) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
