package com.example.devanswers.HttpManager;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebPageDownloader extends AsyncTask<String, Void, Void>
{
    //region [private fields]
    private ICompleteHandler _completeHandler;
    private IFailureHandler _failureHandler;

    private String _webPageContent;
    //endregion


    public WebPageDownloader()
    {

    }

    public WebPageDownloader(ICompleteHandler completeHandler, IFailureHandler failureHandler)
    {
        _completeHandler = completeHandler;
        _failureHandler = failureHandler;
    }


    //region [override methods]
    @Override
    protected Void doInBackground(String... params)
    {
        _webPageContent = DownloadWebPage(params[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        _completeHandler.OnComplete(_webPageContent);
    }
    //endregion


    //region [private methods]
    private String DownloadWebPage(String urlAddress)
    {
        StringBuffer response = new StringBuffer();
        try
        {
            try
            {
                URL url = new URL(urlAddress);

                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setReadTimeout(10000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoInput(true);
                httpConnection.connect();

                int responseCode = httpConnection.getResponseCode();
                if (responseCode == 200)
                {
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

                    String responseLine;

                    while ((responseLine = responseReader.readLine()) != null)
                    {
                        response.append(responseLine);
                    }

                    responseReader.close();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();

                cancel(true);

                _failureHandler.OnFailure();
            }
            catch (IOException e)
            {
                e.printStackTrace();

                cancel(true);

                _failureHandler.OnFailure();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            cancel(true);

            _failureHandler.OnFailure();
        }

        if (response.length() != 0)
        {
            return response.toString();
        }
        else
        {
            return null;
        }
    }
    //endregion


    //region [public methods]
    public void SetCompleteHandler(ICompleteHandler completeHandler)
    {
        _completeHandler = completeHandler;
    }

    public void SetFailureHandler(IFailureHandler failureHandler)
    {
        _failureHandler = failureHandler;
    }
    //endregion
}
