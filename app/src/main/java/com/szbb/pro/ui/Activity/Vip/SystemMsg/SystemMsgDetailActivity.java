package com.szbb.pro.ui.Activity.Vip.SystemMsg;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.szbb.pro.R;
import com.szbb.pro.SystemMsgDetailLayout;
import com.szbb.pro.base.BaseAty;

public class SystemMsgDetailActivity extends BaseAty {
    private SystemMsgDetailLayout detailLayout;
    private String url = "";

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailLayout = (SystemMsgDetailLayout) viewDataBinding;
        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_detail);
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
        return R.layout.activity_system_msg_detail;
    }

    @Override
    protected void onClick(int id, View view) {

    }
}
