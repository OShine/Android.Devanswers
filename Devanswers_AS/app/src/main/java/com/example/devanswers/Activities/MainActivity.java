package com.example.devanswers.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.devanswers.Fragments.CopyrightFragment;
import com.example.devanswers.HttpManager.HttpManager;
import com.example.devanswers.HttpManager.ICompleteHandler;
import com.example.devanswers.HttpManager.IFailureHandler;
import com.example.devanswers.InternetManager.InternetManager;
import com.example.devanswers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    private InternetManager _internetManager;
    private HttpManager _httpManager;

    private CopyrightFragment _copyrightFragment;

    private ImageButton _requestAnswer;
    private ImageButton _shareButton;

    private ImageView _answerLoading;
    private TextView _textAnswer;

    private int _offsetShareButton;

    private String _url;
    private String _suffix;
    private String _devAnswer;

    private RelativeLayout _background;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

         _background = (RelativeLayout) findViewById(R.id.main_layout);

        _internetManager = new InternetManager(getApplicationContext());
        _httpManager = new HttpManager();

        _requestAnswer = (ImageButton) findViewById(R.id.request_answer_ImageButton);
        _requestAnswer.setOnClickListener(this);

        _shareButton = (ImageButton) findViewById(R.id.share_button_ImageButton);
        _shareButton.setOnClickListener(this);


        _answerLoading = (ImageView) findViewById(R.id.answer_loading_ImageView);

        _textAnswer = (TextView) findViewById(R.id.text_answer_TextView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-CondBold.ttf");
        _textAnswer.setTypeface(custom_font);

        _copyrightFragment = (CopyrightFragment) getSupportFragmentManager().findFragmentById(R.id.copyright_fragment);

        getSupportFragmentManager().beginTransaction()
                .commit();

        _shareButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        _offsetShareButton = (int)(_shareButton.getMeasuredWidth() * 0.9);
        deactivateShare();

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

            case R.id.share_button_ImageButton: {

                shareIt();

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
                        parseWebPage(webPageContent);
                        _textAnswer.setText(_devAnswer.toUpperCase());
                        ChangeBackground();
                        if (_shareButton.isClickable() != true)
                            activateShare();

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

    private void parseWebPage(String text) {

        String openingTag = "initial = ";
        String closingTag = "},";
        int openingTagIndex = text.indexOf(openingTag);
        int closingTagIndex = text.indexOf(closingTag);
        String subString = text.substring(openingTagIndex + openingTag.length(), closingTagIndex + 1);
        try {
            JSONObject jsonObject = new JSONObject(subString);
            _suffix = jsonObject.getString("link");
            _devAnswer = Html.fromHtml(jsonObject.getString("text")).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void ChangeBackground() {

        Random rnd = new Random();
        int red = rnd.nextInt(175);
        int green = rnd.nextInt(175);
        int blue = rnd.nextInt(175);
        int color = Color.argb(255, red, green, blue);
        _background.setBackgroundColor(color);

    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        CharSequence savedText = _textAnswer.getText();
        savedInstanceState.putCharSequence("savedText", savedText);

        int color = Color.TRANSPARENT;
        Drawable background = _background.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        savedInstanceState.putInt("savedColor", color);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence savedRestoreText = savedInstanceState.getCharSequence("savedText");
        _textAnswer.setText(savedRestoreText);

        int savedColor = savedInstanceState.getInt("savedColor");
        _background.setBackgroundColor(savedColor);

    }

    private void shareIt() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DevAnswers");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Девелопер ответил:\n" +
                            _devAnswer  + ".\n" + "Оригинальная цитата доступна здесь: " + _url + "a/" +_suffix);
        startActivity(Intent.createChooser(sharingIntent, "Поделиться с помощью..."));

    }

    private void activateShare() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(_offsetShareButton, 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                _shareButton.setTranslationX(value);
            }
        });
        valueAnimator.start();
        _shareButton.setClickable(true);

    }

    private void deactivateShare() {

        _shareButton.setTranslationX(_offsetShareButton);
        _shareButton.setClickable(false);

    }


}

