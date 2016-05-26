package com.example.wlbreath.zhihudaily;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wlbreath.zhihudaily.bean.RecommenderBean;
import com.example.wlbreath.zhihudaily.bean.StoryBean;
import com.example.wlbreath.zhihudaily.bean.StoryExtraInfoBean;
import com.example.wlbreath.zhihudaily.presenter.StoryViewPresenter;
import com.example.wlbreath.zhihudaily.view.IStoryView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.List;


public class StoryActivity extends AppCompatActivity implements IStoryView, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "storyActivity";

    private long mId;

    private SwipeRefreshLayout mSrl;
    private FrameLayout mFlErrorPage;
    private TextView mTvErrorPageReloadBtn;
    private FrameLayout mFlHeader;
    private SimpleDraweeView mSdvHeaderImage;
    private TextView mTvHeaderTitle;
    private TextView mTvHeaderImageSource;
    private HorizontalScrollView mHsvRecommenderItem;
    private LinearLayout mLlRecommenderContainer;
    private WebView mWebView;
    private LinearLayout mLlFavoriteBtn;
    private TextView mTvNumberFavorite;
    private LinearLayout mLlCommentBtn;
    private TextView mTvNumberComment;
    private LinearLayout mLlStarBtn;

    private StoryViewPresenter mPresenter;

    private StoryBean mStory;
    private StoryExtraInfoBean mStoryExtraInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mId = getIntent().getLongExtra("id", -1);
        if (mId == -1) {
            Log.e(TAG, "current story id is invalid");
            return;
        }

        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        mSrl.setColorSchemeColors(getApplicationContext().getResources().getColor(R.color.blue));
        mSrl.setOnRefreshListener(this);

        mFlErrorPage = (FrameLayout) findViewById(R.id.fl_error_page);
        mTvErrorPageReloadBtn = (TextView) mFlErrorPage.findViewById(R.id.tv_reload_btn);

        mFlHeader = (FrameLayout) findViewById(R.id.fl_header);
        mSdvHeaderImage = (SimpleDraweeView) findViewById(R.id.sdv_header_image);
        mTvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        mTvHeaderImageSource = (TextView) findViewById(R.id.tv_header_image_source);

        mHsvRecommenderItem = (HorizontalScrollView) findViewById(R.id.hsv_recommender_item);
        mLlRecommenderContainer = (LinearLayout) findViewById(R.id.ll_recommender_container);

        mWebView = (WebView) findViewById(R.id.webView);

        mPresenter = new StoryViewPresenter(getApplicationContext(), this);

        initView();

        update();
    }

    private void initView() {
        initActionBar();
        initWebView();
        initErrorPage();
    }

    private void update() {
        mPresenter.loadStory(mId);
        mPresenter.loadStoryExtraInfo(mId);
    }


    private void initWebView() {
        // config the webview
        WebView webView = getmWebView();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setDefaultTextEncodingName("UTF-8");
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.d(TAG, "actionbar is null, can not init favorite button");
            return;
        }

        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        // 下面的代码是用来初始化actionbar中的自定义view的
        View view = View.inflate(this, R.layout.activity_story_actionbar_custome_items, null);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(view, layoutParams);

        mTvNumberFavorite = (TextView) view.findViewById(R.id.tv_number_favorite);
        mLlFavoriteBtn = (LinearLayout) view.findViewById(R.id.ll_favorite_btn);
        mLlFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/24  
            }
        });

        mTvNumberComment = (TextView) view.findViewById(R.id.tv_number_comment);
        mLlCommentBtn = (LinearLayout) view.findViewById(R.id.ll_comment_btn);
        mLlCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStoryCommentActivity();
            }
        });

        mLlStarBtn = (LinearLayout) view.findViewById(R.id.ll_star_btn);
        mLlStarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/24  
            }
        });
    }

    private void initErrorPage() {
        mTvErrorPageReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void updateHeader(String title, String image, String imageSource) {
        ViewGroup header = getHeader();

        if (image == null) {
            header.setVisibility(View.GONE);

        } else {
            header.setVisibility(View.VISIBLE);

            updateHeaderTitle(title);
            updateHeaderImage(image);
            updateHeaderImageSource(imageSource);
        }
    }

    private void updateHeaderTitle(String title) {
        if (title == null) {
            title = "";
        }

        TextView textView = getHeaderTitle();
        if (textView != null) {
            textView.setText(title);
        }
    }

    private void updateHeaderImage(String image) {
        if (image == null) {
            Log.e(TAG, "header image is null");
            return;
        }

        SimpleDraweeView simpleDraweeView = getHeaderImage();
        if (simpleDraweeView != null) {
            simpleDraweeView.setImageURI(Uri.parse(image));
        }
    }

    private void updateHeaderImageSource(String imageSource) {
        if (imageSource == null) {
            imageSource = "";
        }

        TextView textView = getHeaderImageSource();
        if (textView != null) {
            textView.setText(imageSource);
        }
    }

    private void updateRecommender(List<RecommenderBean> recommenders) {
        ViewGroup recommenderItem = getRecommenderItem();

//        List<RecommenderBean> recommenders = storyInfo.getRecommenders();
        if (recommenders == null || recommenders.size() == 0) {
            recommenderItem.setVisibility(View.GONE);
            return;
        }

        recommenderItem.setVisibility(View.VISIBLE);

        ViewGroup recommenderContainer = getRecommenderContainer();
        recommenderContainer.removeAllViews();

        for (RecommenderBean recommender : recommenders) {
            String imageUrl = recommender.getAvatar();
            if (imageUrl == null) {
                continue;
            }

            View view = getNewRecommenderView(recommenderContainer);
            SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
            sdv.setImageURI(Uri.parse(imageUrl));
            recommenderContainer.addView(view);
        }
    }

    private void updateWebView(String shareUrl, String body, List<String> css, List<String> js) {
        if (body == null && shareUrl == null) {
            displayErrorPage(true);
            displayContentArea(false);
            return;
        }

        if (body != null) {
            String html = "<!doctype html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='utf-8'>" +
                    "<style>.headline .img-place-holder{height:0 !important;}</style>";

            if (css != null) {
                for (String cssUrl : css) {
                    html += "<link rel='stylesheet' type='text/css' href='" + cssUrl + "'>";
                }
            }
            html += "</head>" +
                    "<body>";
            html += body;

            if (js != null) {
                for (String jsUrl : js) {
                    html += "<script type='text/javascript' src='" + jsUrl + "'></script>";
                }
            }
            html += "</body>" +
                    "</html>";

            Log.d(TAG, html);

            mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);

        } else {
            mWebView.setWebViewClient(new WebViewClient() {
                private String url;

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (this.url == null) {
                        this.url = url;
                    }

                    if (this.url.equals(url)) {
                        view.loadUrl(url);

                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        view.getContext().startActivity(browserIntent);
                    }

                    return true;
                }
            });
            mWebView.loadUrl(shareUrl);
        }

    }

    private View getNewRecommenderView(ViewGroup parent) {
        return getInflater().inflate(R.layout.activity_story_recommender, parent, false);
    }

    private LayoutInflater getInflater() {
        return LayoutInflater.from(getApplicationContext());
    }

    @Override
    public void displayErrorPage(boolean show) {
        ViewGroup errorPage = getErrorPage();
        if (show) {
            errorPage.setVisibility(View.VISIBLE);
        } else {
            errorPage.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayContentArea(boolean show) {
        ViewGroup contentArea = getContentArea();
        if (show) {
            contentArea.setVisibility(View.VISIBLE);
        } else {
            contentArea.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewGroup getErrorPage() {
        if (mFlErrorPage == null) {
            mFlErrorPage = (FrameLayout) findViewById(R.id.fl_error_page);
        }
        return mFlErrorPage;
    }

    @Override
    public void showErrorMessage(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));

        snackbar.show();
    }

    @Override
    public void startRefreshAnimation() {
        mSrl.post(new Runnable() {
            @Override
            public void run() {
                mSrl.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopRefreshAnimation() {
        mSrl.post(new Runnable() {
            @Override
            public void run() {
                mSrl.setRefreshing(false);
            }
        });
    }

    @Override
    public void updateStory(StoryBean story) {
        mStory = story;

        updateHeader(story.getTitle(), story.getImage(), story.getImageSource());
        updateRecommender(story.getRecommenders());
        updateWebView(story.getShare_url(), story.getBody(), story.getCss(), story.getJs());
    }

    @Override
    public void updateStoryExtraInfo(StoryExtraInfoBean extraInfo) {
        mStoryExtraInfo = extraInfo;

        updateFavoriteNumber(extraInfo.getPopularity());
        updateCommentNumber(extraInfo.getShort_comments() + extraInfo.getLong_comments());
    }

    @Override
    public ViewGroup getContentArea() {
        if (mSrl == null) {
            mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        }

        return mSrl;
    }

    private ViewGroup getHeader() {
        if (mFlHeader == null) {
            mFlHeader = (FrameLayout) findViewById(R.id.fl_header);
        }
        return mFlHeader;
    }

    private SimpleDraweeView getHeaderImage() {
        if (mSdvHeaderImage == null) {
            mSdvHeaderImage = (SimpleDraweeView) findViewById(R.id.sdv_header_image);
        }
        return mSdvHeaderImage;
    }

    private TextView getHeaderImageSource() {
        if (mTvHeaderImageSource == null) {
            mTvHeaderImageSource = (TextView) findViewById(R.id.tv_header_image_source);
        }
        return mTvHeaderImageSource;
    }

    private TextView getHeaderTitle() {
        if (mTvHeaderTitle == null) {
            mTvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        }
        return mTvHeaderTitle;
    }

    private WebView getmWebView() {
        if (mWebView == null) {
            mWebView = (WebView) findViewById(R.id.webView);
        }
        return mWebView;
    }

    private ViewGroup getRecommenderItem() {
        if (mHsvRecommenderItem == null) {
            mHsvRecommenderItem = (HorizontalScrollView) findViewById(R.id.hsv_recommender_item);
        }
        return mHsvRecommenderItem;
    }

    private ViewGroup getRecommenderContainer() {
        if (mLlRecommenderContainer == null) {
            mLlRecommenderContainer = (LinearLayout) findViewById(R.id.ll_recommender_container);
        }
        return mLlRecommenderContainer;
    }

    private TextView getNumberComment() {
        return mTvNumberComment;
    }

    private TextView getNumberFavoriate() {
        return mTvNumberFavorite;
    }

    private void loadStoryCommentActivity() {
        Intent intent = new Intent(this, StoryCommentActivity.class);

        intent.putExtra("storyId", mId);
        if (mStoryExtraInfo != null) {
            intent.putExtra("longCommentCount", mStoryExtraInfo.getLong_comments());
            intent.putExtra("shortCommentCount", mStoryExtraInfo.getShort_comments());
        }

        startActivity(intent);
    }

    private void updateFavoriteNumber(int number) {
        String numberStr = "";
        if (number > 1000) {
            DecimalFormat df = new DecimalFormat("0.0");
            numberStr = df.format(number / 1000) + "k";
        } else {
            numberStr = String.valueOf(number);
        }

        getNumberFavoriate().setText(String.valueOf(numberStr));
    }

    private void updateCommentNumber(int number) {
        String numberStr = "";
        if (number > 1000) {
            DecimalFormat df = new DecimalFormat("0.0");
            numberStr = df.format(number / 1000.0) + "K";
        } else {
            numberStr = String.valueOf(number);
        }

        getNumberComment().setText(String.valueOf(numberStr));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        update();
    }
}
