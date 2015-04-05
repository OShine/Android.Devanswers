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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.devanswers.ColorHelper;
import com.example.devanswers.DataBase.DataBase;
import com.example.devanswers.DataBase.DeveloperAnswerModel;
import com.example.devanswers.Fragments.CopyrightFragment;
import com.example.devanswers.HttpManager.HttpManager;
import com.example.devanswers.HttpManager.ICompleteHandler;
import com.example.devanswers.HttpManager.IFailureHandler;
import com.example.devanswers.InternetManager.InternetManager;
import com.example.devanswers.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    private InternetManager internetManager;
    private HttpManager httpManager;

    private CopyrightFragment copyrightFragment;

    private LinearLayout requestAnswer;
    private ImageButton shareButton;

    private ImageView steamImage;
    private TextView textAnswer;
    private TextView logo;

    private int offsetShareButton;

    private String url;

    private RelativeLayout background;

    private DataBase dataBaseHelper;

    private String suffix;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        dataBaseHelper = new DataBase(this);

        internetManager = new InternetManager(getApplicationContext());

        httpManager = new HttpManager();

        requestAnswer = (LinearLayout) findViewById(R.id.request_answer_LinearLayout);
        requestAnswer.setOnClickListener(this);

        shareButton = (ImageButton) findViewById(R.id.share_button_ImageButton);
        shareButton.setOnClickListener(this);

        steamImage = (ImageView) findViewById(R.id.answer_loading_ImageView);

        background = (RelativeLayout) findViewById(R.id.main_layout);

        logo = (TextView) findViewById(R.id.logo_TextView);
        logo.setText(getResources().getString(R.string.logo_text));
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        logo.setTypeface(font);

        textAnswer = (TextView) findViewById(R.id.text_answer_TextView);
        font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-CondBold.ttf");
        textAnswer.setTypeface(font);
        
        copyrightFragment = (CopyrightFragment) getSupportFragmentManager().findFragmentById(R.id.copyright_fragment);

        getSupportFragmentManager().beginTransaction()
                .commit();

        shareButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        offsetShareButton = (int) (shareButton.getLayoutParams().height* 0.9);
        deactivateShare();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.request_answer_LinearLayout:
                if (internetManager.IsConnected() == true)
                {
                    downloadPage();
                }
                else
                {
                    text = dataBaseHelper.getRandomDeveloperAnswer();

                    showDeveloperAnswer();

                    changeBackground();
                }
                break;

            case R.id.share_button_ImageButton:
                shareIt();
                break;
        }
    }

    public void downloadPage()
    {
        url = "http://devanswers.ru/";
        httpManager.DownloadWebPage(url,
                new ICompleteHandler()
                {
                    @Override
                    public void OnComplete(Object data)
                    {
                        parseWebPage(String.valueOf(data));

                        saveDeveloperAnswer();

                        showDeveloperAnswer();

                        changeBackground();
                    }
                },
                new IFailureHandler()
                {
                    @Override
                    public void OnFailure()
                    {
                        downloadPage();
                    }
                });
    }

    private void parseWebPage(String webPageData)
    {
        String openingTag = "initial = ";
        String closingTag = "},";

        int openingTagIndex = webPageData.indexOf(openingTag);
        int closingTagIndex = webPageData.indexOf(closingTag);

        String subString = webPageData.substring(openingTagIndex + openingTag.length(), closingTagIndex + 1);
        try
        {
            JSONObject jsonObject = new JSONObject(subString);
            suffix = jsonObject.getString("link");
            text = Html.fromHtml(jsonObject.getString("text")).toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void saveDeveloperAnswer()
    {
        dataBaseHelper.saveDeveloperAnswer(suffix, text);
    }

    private void showDeveloperAnswer()
    {
        textAnswer.setText(text);


        if (shareButton.isClickable() != true)
            activateShare();
    }


    private void changeBackground()
    {
        background.setBackgroundColor(ColorHelper.GetRandomColor());
    }

    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        CharSequence savedText = textAnswer.getText();
        savedInstanceState.putCharSequence("savedText", savedText);

        int color = Color.TRANSPARENT;
        Drawable background = this.background.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        savedInstanceState.putInt("savedColor", color);

        savedInstanceState.putFloat("translationShare", shareButton.getTranslationX());
        savedInstanceState.putBoolean("clickableShare", shareButton.isClickable());



    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence savedRestoreText = savedInstanceState.getCharSequence("savedText");
        textAnswer.setText(savedRestoreText);

        int savedColor = savedInstanceState.getInt("savedColor");
        background.setBackgroundColor(savedColor);

        shareButton.setTranslationX(savedInstanceState.getFloat("translationShare"));
        shareButton.setClickable(savedInstanceState.getBoolean("clickableShare"));



    }

    private void shareIt()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DevAnswers");
        if (internetManager.IsConnected() == true)
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Девелопер ответил:\n" + text + ".\n" + "Оригинальная цитата доступна здесь: " + url + "a/" + suffix);
        else
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Девелопер ответил:\n" + text + ".\n" + "Смешные и не очень смешные, но очень типичные ответы девелоперов ищите здесь: http://devanswers.ru/");
        startActivity(Intent.createChooser(sharingIntent, "Поделиться с помощью..."));
    }

    private void activateShare()
    {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(offsetShareButton, 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                Integer value = (Integer) animation.getAnimatedValue();
                shareButton.setTranslationX(value);
            }
        });
        valueAnimator.start();
        shareButton.setClickable(true);
    }

    private void deactivateShare()
    {
        shareButton.setTranslationX(offsetShareButton);
        shareButton.setClickable(false);
    }
}

