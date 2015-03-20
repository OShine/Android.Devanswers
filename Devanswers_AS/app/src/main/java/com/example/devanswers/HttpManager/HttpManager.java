package com.example.devanswers.HttpManager;

import android.os.AsyncTask;

public class HttpManager
{
    public HttpManager()
    {

    }

    public void DownloadWebPage(String urlAddress, ICompleteHandler completeHandler, IFailureHandler failureHandler)
    {
        new WebPageDownloader(completeHandler, failureHandler).execute(urlAddress);
    }
}
