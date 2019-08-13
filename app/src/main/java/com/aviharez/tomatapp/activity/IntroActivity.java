package com.aviharez.tomatapp.activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.fragment.SampleSlide;
import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();


        addSlide(SampleSlide.newInstance(R.layout.fragment_slide1));
        addSlide(SampleSlide.newInstance(R.layout.fragment_slide2));
        addSlide(SampleSlide.newInstance(R.layout.fragment_slide3));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        setBarColor(Color.TRANSPARENT);
        setSeparatorColor(Color.TRANSPARENT);
        setIndicatorColor(Color.BLACK, Color.DKGRAY);
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
        setColorDoneText(Color.BLACK);
        setColorSkipButton(Color.BLACK);
        setNextArrowColor(Color.BLACK);
        setSkipText("Lewati");
        setDoneText("Mulai");

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
