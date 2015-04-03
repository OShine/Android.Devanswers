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
    private InternetManager internetManager;
    private HttpManager httpManager;

    private CopyrightFragment copyrightFragment;

    private ImageButton requestAnswer;
    private ImageButton shareButton;

    private ImageView answerLoading;
    private TextView textAnswer;

    private int offsetShareButton;

    private String url;
    private String suffix;
    private String devAnswer;

    private RelativeLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

         background = (RelativeLayout) findViewById(R.id.main_layout);

        internetManager = new InternetManager(getApplicationContext());
        httpManager = new HttpManager();

        requestAnswer = (ImageButton) findViewById(R.id.request_answer_ImageButton);
        requestAnswer.setOnClickListener(this);

        shareButton = (ImageButton) findViewById(R.id.share_button_ImageButton);
        shareButton.setOnClickListener(this);


        answerLoading = (ImageView) findViewById(R.id.answer_loading_ImageView);

        textAnswer = (TextView) findViewById(R.id.text_answer_TextView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-CondBold.ttf");
        textAnswer.setTypeface(custom_font);

        copyrightFragment = (CopyrightFragment) getSupportFragmentManager().findFragmentById(R.id.copyright_fragment);

        getSupportFragmentManager().beginTransaction()
                .commit();

        shareButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        offsetShareButton = (int)(shareButton.getMeasuredWidth() * 0.9);
        deactivateShare();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.request_answer_ImageButton:
            if (internetManager.IsConnected() == true)
            {
                downloadPage();
            }
            break;

            case R.id.share_button_ImageButton: {

                shareIt();

            }
                break;

        }
    }

    public void downloadPage() {

        url = "http://devanswers.ru/";
        httpManager.DownloadWebPage(url,
                new ICompleteHandler()
                {
                    @Override
                    public void OnComplete(Object data)
                    {
                        String webPageContent = String.valueOf(data);
                        parseWebPage(webPageContent);
                        textAnswer.setText(devAnswer.toUpperCase());
                        changeBackground();
                        if (shareButton.isClickable() != true)
                            activateShare();

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

    private void parseWebPage(String text) {

        String openingTag = "initial = ";
        String closingTag = "},";
        int openingTagIndex = text.indexOf(openingTag);
        int closingTagIndex = text.indexOf(closingTag);
        String subString = text.substring(openingTagIndex + openingTag.length(), closingTagIndex + 1);
        try {
            JSONObject jsonObject = new JSONObject(subString);
            suffix = jsonObject.getString("link");
            devAnswer = Html.fromHtml(jsonObject.getString("text")).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void changeBackground() {

        Random rnd = new Random();
        int red = rnd.nextInt(175);
        int green = rnd.nextInt(175);
        int blue = rnd.nextInt(175);
        int color = Color.argb(255, red, green, blue);
        background.setBackgroundColor(color);

    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        CharSequence savedText = textAnswer.getText();
        savedInstanceState.putCharSequence("savedText", savedText);

        int color = Color.TRANSPARENT;
        Drawable background = this.background.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        savedInstanceState.putInt("savedColor", color);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence savedRestoreText = savedInstanceState.getCharSequence("savedText");
        textAnswer.setText(savedRestoreText);

        int savedColor = savedInstanceState.getInt("savedColor");
        background.setBackgroundColor(savedColor);

    }

    private void shareIt() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DevAnswers");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Девелопер ответил:\n" +
                devAnswer + ".\n" + "Оригинальная цитата доступна здесь: " + url + "a/" + suffix);
        startActivity(Intent.createChooser(sharingIntent, "Поделиться с помощью..."));

    }

    private void activateShare() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(offsetShareButton, 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                shareButton.setTranslationX(value);
            }
        });
        valueAnimator.start();
        shareButton.setClickable(true);

    }

    private void deactivateShare() {

        shareButton.setTranslationX(offsetShareButton);
        shareButton.setClickable(false);
    }
}

