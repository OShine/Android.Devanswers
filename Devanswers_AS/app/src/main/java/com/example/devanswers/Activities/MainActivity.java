package com.example.devanswers.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devanswers.Fragments.CopyrightFragment;
import com.example.devanswers.Fragments.ShareFragment;
import com.example.devanswers.HttpManager.HttpManager;
import com.example.devanswers.HttpManager.ICompleteHandler;
import com.example.devanswers.HttpManager.IFailureHandler;
import com.example.devanswers.InternetManager.InternetManager;
import com.example.devanswers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;

public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    private InternetManager _internetManager;

    private HttpManager _httpManager;

    private ShareFragment _shareFragment;
    private CopyrightFragment _copyrightFragment;

    private ImageButton _requestAnswer;

    private ImageView _answerLoading;

    private TextView _textAnswer;

    private String _url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        _internetManager = new InternetManager(getApplicationContext());

        _httpManager = new HttpManager();

        _requestAnswer = (ImageButton) findViewById(R.id.request_answer_ImageButton);
        _requestAnswer.setOnClickListener(this);

        _answerLoading = (ImageView) findViewById(R.id.answer_loading_ImageView);

        _textAnswer = (TextView) findViewById(R.id.text_answer_TextView);

        _shareFragment = (ShareFragment) getSupportFragmentManager().findFragmentById(R.id.share_fragment);
        _copyrightFragment = (CopyrightFragment) getSupportFragmentManager().findFragmentById(R.id.copyright_fragment);

        getSupportFragmentManager().beginTransaction()
                .hide(_shareFragment)
                .hide(_copyrightFragment)
                .commit();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.request_answer_ImageButton:
                if (_internetManager.IsConnected() == true)
                {
                    DownloadPage();
                }
                break;
        }
    }

    public void DownloadPage() {

        _url = "http://devanswers.ru/";
        _httpManager.DownloadWebPage(_url,
                new ICompleteHandler()
                {
                    @Override
                    public void OnComplete(Object data)
                    {
                        String webPageContent = String.valueOf(data);

                        _textAnswer.setText(GetDevAnswerText(webPageContent));

                    }
                },
                new IFailureHandler()
                {
                    @Override
                    public void OnFailure()
                    {
                        DownloadPage();

                    }
                });
    }

    private String GetDevAnswerText(String text) {

        String openingTag = "initial = ";
        String closingTag = "},";
        int openingTagIndex = text.indexOf(openingTag);
        int closingTagIndex = text.indexOf(closingTag);
        String subString = text.substring(openingTagIndex + openingTag.length(), closingTagIndex + 1);
        String devAnswer = "";

        try {
            JSONObject jsonObject = new JSONObject(subString);
            devAnswer = jsonObject.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devAnswer;

    }
}

