package com.example.wlbreath.zhihudaily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wlbreath.zhihudaily.bean.WelcomePageBean;
import com.example.wlbreath.zhihudaily.presenter.WelcomePagePresenter;
import com.example.wlbreath.zhihudaily.view.IWelcomePageView;

public class WelcomeActivity extends AppCompatActivity implements IWelcomePageView {
    private final String TAG = "welcome activity";

    private final long ANIMATION_DURATION = 2000;
    private final long LIFE_DURATION = ANIMATION_DURATION;

    private ImageView mIvBg;
    private TextView mTvTitle;

    private WelcomePagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mIvBg = (ImageView) findViewById(R.id.iv_background);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mPresenter = new WelcomePagePresenter(getApplicationContext(), this);

        init();
    }

    private void init() {
        WelcomePageBean bean = mPresenter.getWelcomePageBean();

        updateTitle(bean.getTitle());
        updateBackground(bean.getBackground());

        toMainActivity(LIFE_DURATION);
    }


    public void updateTitle(String title) {
        if (title != null) {
            mTvTitle.setText(title);
        }
    }

    @Override
    public void updateBackground(Bitmap bg) {
        if (bg != null) {
            mIvBg.setImageBitmap(bg);
            animateBackground(ANIMATION_DURATION);
        }
    }

    @Override
    public void toMainActivity(long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                Log.d(TAG, "finish welcome activity");
                WelcomeActivity.this.finish();
            }

        }, LIFE_DURATION);
    }

    private void animateBackground(long duration) {
        ScaleAnimation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        mIvBg.startAnimation(animation);
    }
}
