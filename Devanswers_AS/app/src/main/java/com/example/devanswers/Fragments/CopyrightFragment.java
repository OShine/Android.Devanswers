package com.example.devanswers.Fragments;

import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.devanswers.R;

public class CopyrightFragment extends Fragment implements View.OnClickListener {
    private ImageButton showCopyright;

    private RelativeLayout root;

    private TextView copyrightText;
    private boolean opened;

    public boolean isOpened() {

        return opened;
    }

    private int fragmentOffset;

    public CopyrightFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opened = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.copyright_fragment, container, false);

        showCopyright = (ImageButton) view.findViewById(R.id.show_copyright_ImageButton);
        showCopyright.setOnClickListener(this);
        
        copyrightText = (TextView) view.findViewById(R.id.copyright_text_TextView);
        copyrightText.setText((getResources().getString(R.string.copyright_text)).toUpperCase());
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-CondLight.ttf");
        copyrightText.setTypeface(font);

        root = (RelativeLayout) view.findViewById(R.id.copyright_root_RelativeLayout);

//        showCopyright.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int showCopyrightHeight = showCopyright.getLayoutParams().height;

        root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int rootHeight = root.getMeasuredHeight();

        fragmentOffset = rootHeight - showCopyrightHeight;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        root.setTranslationY(fragmentOffset);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_copyright_ImageButton:
                toggle();
                break;
        }
    }

    private void animate(int from, int to)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                root.setTranslationY(value);
            }
        });
        valueAnimator.start();
    }

    public void toggle() {

        opened = !opened;
        if (opened == true)
            animate(0, fragmentOffset);
        else
            animate(fragmentOffset, 0);
    }
}
