package com.example.devanswers.Fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.devanswers.R;

public class CopyrightFragment extends Fragment implements View.OnClickListener {
    private ImageButton _showCopyright;

    private RelativeLayout _rootCopy;

    //private int _rootHeight;
    //private int _buttonHeight;
    private int _offsetCopy;

    public CopyrightFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.copyright_fragment, container, false);
        _showCopyright = (ImageButton) view.findViewById(R.id.show_copyright_ImageButton);
        _showCopyright.setOnClickListener(this);
        _rootCopy = (RelativeLayout) view.findViewById(R.id.rootCopy_RelativeLayout);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        _showCopyright.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int showCopyrightHeight = _showCopyright.getMeasuredHeight();

        _rootCopy.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int rootHeight = _rootCopy.getMeasuredHeight();

        _offsetCopy = rootHeight - showCopyrightHeight;
        _rootCopy.setTranslationY(_offsetCopy);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_copyright_ImageButton:
                if (_rootCopy.getTranslationY() == 0)
                    animate(0, _offsetCopy);
                else
                    animate(_offsetCopy, 0);
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
                _rootCopy.setTranslationY(value);
            }
        });
        valueAnimator.start();
    }

}
