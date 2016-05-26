package com.example.wlbreath.zhihudaily.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlbreath on 16/3/22.
 */
public class MySlide extends FrameLayout {
    private final String TAG = "MySlide";

    private final int VIEW_PAGER_CHANGE_INTERVAL = 3000;

    private final int SELECTOR_RADIUS = DensityUtil.dip2px(getContext(), 6);

    private final int PAGE_CHANGD = 1;

    private ViewPager viewPager;
    private LinearLayout ll_selector_container;

    private MyPagerAdapter pagerAdapter;

    private List<View> views;
    private List<View> selectors;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAGE_CHANGD:
                    showNextStory();
                    break;
            }
        }
    };

    public MySlide(Context context) {
        super(context);

        init();
    }

    public MySlide(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        removeAllViews();
        addView(getLayout());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(getPagerAdapter());
        viewPager.addOnPageChangeListener(new MyOnPagerChangeListener());

        ll_selector_container = (LinearLayout) findViewById(R.id.ll_selector_container);
    }

    public void clearViews(){
        this.views = null;
        pagerAdapter.clearViews();
        updateSelectors(0);
    }

    public void updateViews(List<View> views) {
        if(views == null || views.size() == 0){
            clearViews();
            return;
        }

        // views是否发生变化，没有变化不更新
        if(this.views == views){
            return;
        }

        if(this.views != null && views != null && this.views.size() == views.size()){
            int count = 0;
            for(int i = 0, len = this.views.size(); i < len; ++i){
                if(this.views.get(i) == views.get(i)){
                    count++;
                }
            }

            if(count == views.size()){
                return;
            }
        }

        this.views = views;

        updatePagerAdapter(this.views);

        if (this.views == null) {
            updateSelectors(0);
        } else {
            updateSelectors(this.views.size());
        }

        startAnimate();
    }

    private void updatePagerAdapter(List<View> views) {
        getPagerAdapter().updateViews(views);
    }


    public ViewPager getViewPager() {
        return viewPager;
    }

    public void startAnimate() {
        // clear previous animate request
        handler.removeMessages(PAGE_CHANGD);

        // no page item, can not animate
        if (isEmpty()) {
            return;
        }

        handler.sendEmptyMessageDelayed(PAGE_CHANGD, VIEW_PAGER_CHANGE_INTERVAL);
    }

    public void stopAnimate() {
        handler.removeMessages(PAGE_CHANGD);
    }

    public boolean isEmpty() {
        return views == null || views.size() == 0;
    }

    public void showNextStory() {
        // no item, can not show next story
        if (isEmpty()) {
            return;
        }

        setStorySelected((viewPager.getCurrentItem() + 1) % views.size());
    }

    public void setStorySelected(int index) {
        if (isEmpty() || index >= views.size()) {
            return;
        }

        viewPager.setCurrentItem(index, true);
    }

    private void updateSelectors(int number) {
        ll_selector_container.removeAllViews();
        selectors = new ArrayList<View>();

        for (int i = 0; i < number; ++i) {
            View view = new View(getContext());
            ll_selector_container.addView(view);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.width = SELECTOR_RADIUS;
            lp.height = SELECTOR_RADIUS;
            lp.setMargins(SELECTOR_RADIUS, 0, 0, 0);
            view.setLayoutParams(lp);
            view.setBackgroundResource(R.drawable.home_page_slide_selector);
            view.setEnabled(i == 0);

            selectors.add(view);
        }
    }

    private void setSelectorSelected(int position) {
        if (selectors == null || selectors.size() == 0 || position >= selectors.size()) {
            return;
        }

        for (int i = 0, len = selectors.size(); i < len; ++i) {
            selectors.get(i).setEnabled(i == position);
        }
    }

    private View getLayout() {
        return getLayoutInflater().inflate(R.layout.component_myslide, this, false);
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    private MyPagerAdapter getPagerAdapter() {
        if (pagerAdapter == null) {
            pagerAdapter = new MyPagerAdapter();
        }
        return pagerAdapter;
    }

    private class MyOnPagerChangeListener implements ViewPager.OnPageChangeListener {
        private int prevPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setSelectorSelected(position);
            startAnimate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    prevPosition = viewPager.getCurrentItem();
                    stopAnimate();
                    break;

                case ViewPager.SCROLL_STATE_IDLE:
                    if (prevPosition == viewPager.getCurrentItem()) {
                        startAnimate();
                    }
                    prevPosition = -1;
                    break;
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> views;

        public void updateViews(List<View> views) {
            this.views = views;
            notifyDataSetChanged();
        }

        public void clearViews() {
            this.views = null;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            }
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
