package com.szbb.pro.ui.activity.vip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.szbb.pro.R;
import com.szbb.pro.WebViewLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.tools.AppTools;


public class WebViewActivity extends BaseAty {
    private WebViewLayout detailLayout;
    private String url = "";

    private WebView webView;
    private ProgressBar progressBar;

    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailLayout = (WebViewLayout) viewDataBinding;
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(title))
            AppTools.removeSingleActivity(this);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(title);
        webView = detailLayout.webView;
        progressBar = detailLayout.progressBar;
    }

    @Override
    protected void initEvents() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);

        detailLayout.webView.loadUrl(url);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onClick(int id, View view) {

    }
}
