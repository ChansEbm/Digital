package com.szbb.pro.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.szbb.pro.tools.FileTools;

import java.io.File;

/**
 * Created by KenChan on 16/6/12.
 */
public class DownloadService extends IntentService {

    public DownloadService () {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService (String name) {
        super(name);
    }


    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent (Intent intent) {
        if (intent != null) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(
                    Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(intent.getStringExtra("uri")));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationUri(Uri.fromFile(new File(FileTools.getInstance()
                                                                     .getDownloadCacheDir() +
                                                            "szbb.apk")));
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_HIDDEN);
            request.allowScanningByMediaScanner();
            request.setAllowedOverRoaming(false);
            downloadManager.enqueue(request);
        }
    }
}
