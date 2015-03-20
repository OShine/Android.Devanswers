package com.example.devanswers.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.devanswers.R;

public class MainActivity extends Activity
{
    private RelativeLayout _clickableArea;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }
}
