package com.example.devanswers.Fragments;

import android.animation.Animator;
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

public class DonateFragment extends Fragment implements View.OnClickListener {

    private ImageButton showDonate;

    private RelativeLayout root;

    private TextView donateText;
    private boolean donateOpened;
    private int animationDuration = 700;

    public boolean isOpened() {

        return donateOpened;
    }

    public void setOpened(boolean state){

        donateOpened = state;
        if (donateOpened)
            root.setTranslationX(0);
        else
            root.setTranslationX(donateFragmentOffset);
    }

    private int donateFragmentOffset;

    public DonateFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        donateOpened = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.donate_fragment, container, false);

        showDonate = (ImageButton) view.findViewById(R.id.show_donate_ImageButton);
        showDonate.setOnClickListener(this);

        donateText = (TextView) view.findViewById(R.id.donate_text_TextView);
        donateText.setText((getResources().getString(R.string.logo_text)).toUpperCase());
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-CondLight.ttf");
        donateText.setTypeface(font);

        root = (RelativeLayout) view.findViewById(R.id.donate_root_RelativeLayout);

//        showCopyright.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int showDonateHeight = showDonate.getLayoutParams().height;

        root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int rootHeight = root.getMeasuredHeight();

        donateFragmentOffset = - rootHeight + showDonateHeight/2;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!donateOpened)
            root.setTranslationX(donateFragmentOffset);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_donate_ImageButton: {
                toggle();
            }
            break;
        }
    }

    private void animate(int from, int to)
    {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                showDonate.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showDonate.setClickable(true);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(animationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                root.setTranslationX(value);
            }
        });
        valueAnimator.start();

    }

    public void toggle() {

        donateOpened = !donateOpened;
        if (!donateOpened)
            animate(0, donateFragmentOffset);
        else
           animate(donateFragmentOffset, 0);

    }

}
