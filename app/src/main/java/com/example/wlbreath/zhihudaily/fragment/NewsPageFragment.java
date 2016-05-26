package com.example.wlbreath.zhihudaily.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.StoryActivity;
import com.example.wlbreath.zhihudaily.bean.NewsBean;
import com.example.wlbreath.zhihudaily.bean.TopNewsBean;
import com.example.wlbreath.zhihudaily.component.MySlide;
import com.example.wlbreath.zhihudaily.presenter.NewsPagePresenter;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.example.wlbreath.zhihudaily.view.INewsPageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wlbreath on 16/3/6.
 */
public class NewsPageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, INewsPageView {
    private final String TAG = "HOMEPAGEFRAGMENT";

    private SwipeRefreshLayout mSrl;
    private LinearLayoutManager mLlm;
    private RecyclerView mRvStoryContainer;
    private FrameLayout mFlContentArea;
    private FrameLayout mFlErrorPage;
    private TextView mTvErrorPageReloadBtn;

    private NewsAdapter mNewsAdapter;

    private NewsPagePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mSrl.setColorSchemeColors(getResources().getColor(R.color.blue));
        mSrl.setOnRefreshListener(this);

        mLlm = new LinearLayoutManager(getContext());

        mRvStoryContainer = (RecyclerView) view.findViewById(R.id.rv_story_container);
        mRvStoryContainer.setLayoutManager(mLlm);
        mRvStoryContainer.addItemDecoration(new MyRecyclerItemDecoration());
        mRvStoryContainer.addOnScrollListener(new MyRVOnScrollListener());

        mFlContentArea = (FrameLayout) view.findViewById(R.id.fl_content_area);
        mFlErrorPage = (FrameLayout) view.findViewById(R.id.fl_error_page);

        mTvErrorPageReloadBtn = (TextView) mFlErrorPage.findViewById(R.id.tv_reload_btn);


        mNewsAdapter = new NewsAdapter(getContext());
        mRvStoryContainer.setAdapter(mNewsAdapter);

        mPresenter = new NewsPagePresenter(getContext(), this);

        init();

        return view;
    }

    private boolean isRecycleViewScrollToBottom() {
        return mLlm.findLastVisibleItemPosition() == mNewsAdapter.getItemCount() - 1;
    }

    private void init() {
        initErrorPage();
        initLatestNews();
    }

    private void initErrorPage() {
        mTvErrorPageReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPresenter.isOnline()) {
                    showErrorMessage(mFlErrorPage, getResources().getString(R.string.offline));
                    return;
                }
                initLatestNews();
            }
        });
    }

    private void initLatestNews() {
        mPresenter.loadLatestNews();
    }

//    加载指定日期的新闻
    public void initHistoryNews(final Date date) {
        mPresenter.loadHistoryNews(date);
    }

    private void loadStoryActivity(long id) {
        Intent intent = new Intent(getContext(), StoryActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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

    public void displayContentArea(boolean show) {
        FrameLayout contentArea = getContentArea();

        if (show) {
            contentArea.setVisibility(View.VISIBLE);
        } else {
            contentArea.setVisibility(View.GONE);
        }
    }

    public void displayErrorPage(boolean show) {
        FrameLayout errorPage = getErrorPage();

        if (show) {
            errorPage.setVisibility(View.VISIBLE);
        } else {
            errorPage.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateTopNews(Date date, List<TopNewsBean> news) {
        if (news == null) {
            return;
        }

        mNewsAdapter.updateTopNews(news);
    }

    @Override
    public void updateNews(LinkedHashMap<Date, List<NewsBean>> news) {
        if (news == null) {
            Log.e(TAG, "updateNews: arg date or news is null");
            return;
        }
        mNewsAdapter.updateNews(news);
    }

    @Override
    public void appendNews(Date date, List<NewsBean> news) {
        if (date == null || news == null) {
            Log.e(TAG, "appendNews: arg date or news is null");
            return;
        }

        mNewsAdapter.appendNews(date, news);
    }

    @Override
    public FrameLayout getContentArea() {
        return mFlContentArea;
    }

    @Override
    public FrameLayout getErrorPage() {
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
    public void onRefresh() {
        if (!NetworkUtils.isOnline(getContext())) {
            showErrorMessage(mRvStoryContainer, getResources().getString(R.string.offline));
            mSrl.setRefreshing(false);
            return;
        }

        mSrl.setRefreshing(false);
    }

    private class MyRVOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    if (isRecycleViewScrollToBottom()) {
                        Date date = new Date();

                        Map<Date, List<NewsBean>> stories = mNewsAdapter.getNews();
                        if (stories != null && stories.size() != 0) {
                            Set<Date> dateSet = stories.keySet();
                            date = (Date) dateSet.toArray()[dateSet.size() - 1];

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            cal.add(Calendar.HOUR_OF_DAY, -24);

                            date = cal.getTime();
                        }

                        initHistoryNews(date);
                    }
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int position = mLlm.findFirstVisibleItemPosition();


            Date date = mNewsAdapter.getDate(position);

            String title = getResources().getString(R.string.home);
            if (date != null) {
                title = mNewsAdapter.getDateString(date);
            }

            UpdateActionBarTitle tmp = (UpdateActionBarTitle) getActivity();
            tmp.updateActionBarTitle(title);
        }
    }

    private class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;

        private final int HEADER = 0;
        private final int DATE_TYPE_ITEM = 1;
        private final int NEWS_TYPE_ITEM = 2;

        private List<TopNewsBean> mTopNews;
        private Map<Date, List<NewsBean>> mNews;

        public NewsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;

            switch (viewType) {
                case HEADER:
                    viewHolder = getNewHeaderHolder(parent);
                    break;

                case DATE_TYPE_ITEM:
                    viewHolder = getNewDateHolder(parent);
                    break;

                case NEWS_TYPE_ITEM:
                    viewHolder = getNewStoryHolder(parent);
                    break;
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case HEADER:
                    bindHeaderHolder((HeaderHolder) holder, mTopNews);
                    break;

                case DATE_TYPE_ITEM:
                    bindDateView((DateHolder) holder, getDate(position));
                    break;

                case NEWS_TYPE_ITEM:
                    bindStoryHolder((StoryHolder) holder, getNews(position));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            int count = 0;

            if(mTopNews != null){
                count++;
            }

            if (mNews != null) {
                count += mNews.size();

                Set<Date> keys = mNews.keySet();
                for (Date key : keys) {
                    count += mNews.get(key).size();
                }
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER;
            }

            int index = 1;
            for (Date key : mNews.keySet()) {
                if (index == position) {
                    return DATE_TYPE_ITEM;
                }

                index += mNews.get(key).size() + 1;
            }

            return NEWS_TYPE_ITEM;
        }

        public void updateNews(Map<Date, List<NewsBean>> news) {
            this.mNews = news;
            notifyDataSetChanged();
        }

        public void appendNews(Date date, List<NewsBean> news) {
            if (this.mNews == null) {
                this.mNews = new HashMap<>();
            }

            this.mNews.put(date, news);
            notifyDataSetChanged();
        }

        public Map<Date, List<NewsBean>> getNews() {
            return this.mNews;
        }

        public void updateTopNews(List<TopNewsBean> news) {
            this.mTopNews = news;
            notifyItemChanged(0);
        }


        public NewsBean getNews(int position) {
            NewsBean story = null;

            int startIndex = 2;
            for (Date key : mNews.keySet()) {
                List<NewsBean> storyBeans = mNews.get(key);

                int endIndex = startIndex + storyBeans.size();
                if (position >= startIndex && position <= endIndex - 1) {
                    story = storyBeans.get(position - startIndex);
                    break;
                }

                startIndex = endIndex + 1;
            }

            return story;
        }

        // 获取当前界面中显示的第一条数据是数据那个日期的
        public Date getDate(int position) {
            if (position == 0 || mNews == null) {
                return null;
            }

            Date date = null;

            int index = 1;
            for (Date key : mNews.keySet()) {
                index += mNews.get(key).size();
                if (position <= index) {
                    date = key;
                    break;
                }

                index += 1;
            }

            return date;
        }

        private HeaderHolder getNewHeaderHolder(ViewGroup parent) {
            View view = getInflater().inflate(R.layout.fragment_home_header, parent, false);
            return new HeaderHolder(view);
        }

        private StoryHolder getNewStoryHolder(ViewGroup parent) {
            View view = getInflater().inflate(R.layout.fragment_home_news_item, parent, false);
            return new StoryHolder(view);
        }

        private DateHolder getNewDateHolder(ViewGroup parent) {
            View view = getInflater().inflate(R.layout.fragment_home_date_item, parent, false);
            return new DateHolder(view);
        }

        private TopStoryHolder getNewTopStoryHolder(ViewGroup parent) {
            return new TopStoryHolder(getInflater().inflate(R.layout.fragment_home_top_news_item, parent, false));
        }

        private HeaderHolder bindHeaderHolder(HeaderHolder headerHolder, List<TopNewsBean> topStories) {
            if (topStories == null || topStories.size() == 0) {
                headerHolder.slide.clearViews();

            } else {

                List<View> views = new ArrayList<>();
                for (final TopNewsBean story : topStories) {
                    TopStoryHolder topStoryHolder = getNewTopStoryHolder(headerHolder.slide);

                    topStoryHolder.tv_title.setText(story.getTitle());

                    topStoryHolder.sdv_image.setImageURI(Uri.parse(story.getImage()));
                    topStoryHolder.sdv_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadStoryActivity(story.getId());
                        }
                    });

                    views.add(topStoryHolder.itemView);
                }

                headerHolder.slide.updateViews(views);
            }
            return headerHolder;
        }

        private StoryHolder bindStoryHolder(StoryHolder viewHolder, NewsBean storyBean) {
            if (storyBean == null) {
                return viewHolder;
            }

            viewHolder.tv_title.setText(storyBean.getTitle());

            ImageView iv_multipic = viewHolder.iv_multipic;
            if (storyBean.isMultipic()) {
                iv_multipic.setVisibility(View.VISIBLE);
            } else {
                iv_multipic.setVisibility(View.GONE);
            }

            String imageUrl = null;
            List<String> images = storyBean.getImages();
            if (images != null && images.size() != 0) {
                imageUrl = images.get(0);
            }

            if (imageUrl != null) {
                viewHolder.sdv_image.setImageURI(Uri.parse(imageUrl));
            }

            return viewHolder;
        }

        private DateHolder bindDateView(DateHolder viewHolder, Date date) {
            if (date == null) {
                return viewHolder;
            }

            viewHolder.tv_date.setText(getDateString(date));
            return viewHolder;
        }

        private LayoutInflater getInflater() {
            return LayoutInflater.from(context);
        }

        private String getDateString(Date date) {
            final String[] day = {
                    "星期日",
                    "星期一",
                    "星期二",
                    "星期三",
                    "星期四",
                    "星期五",
                    "星期六"
            };

            String curtDateStr = parseDateString(new Date());

            if (curtDateStr.equals(parseDateString(date))) {
                return context.getResources().getString(R.string.today_news);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 ");
            String dateStr = dateFormat.format(date);


            return dateStr + " " + day[date.getDay()];
        }

        private String parseDateString(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return dateFormat.format(date);
        }

        private class HeaderHolder extends RecyclerView.ViewHolder {
            MySlide slide;

            List<TopStoryHolder> topStoryHolders;

            public HeaderHolder(View itemView) {
                super(itemView);

                slide = (MySlide) itemView;
            }
        }

        private class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_title;
            ImageView iv_multipic;
            SimpleDraweeView sdv_image;

            public StoryHolder(View viewItem) {
                super(viewItem);

                tv_title = (TextView) viewItem.findViewById(R.id.tv_title);
                iv_multipic = (ImageView) viewItem.findViewById(R.id.iv_multipic);
                sdv_image = (SimpleDraweeView) viewItem.findViewById(R.id.sdv_image);

                viewItem.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                Intent intent = new Intent(getActivity(), StoryActivity.class);
                intent.putExtra("id", getNews(position).getId());
                startActivity(intent);
            }
        }

        private class TopStoryHolder extends RecyclerView.ViewHolder {
            TextView tv_title;
            SimpleDraweeView sdv_image;

            public TopStoryHolder(View itemView) {
                super(itemView);

                tv_title = (TextView) itemView.findViewById(R.id.tv_title);
                sdv_image = (SimpleDraweeView) itemView.findViewById(R.id.sdv_image);
            }
        }

        private class DateHolder extends RecyclerView.ViewHolder {
            TextView tv_date;

            public DateHolder(View viewItem) {
                super(viewItem);

                tv_date = (TextView) viewItem.findViewById(R.id.tv_date);
            }
        }
    }

    private class MyRecyclerItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 30;
        }
    }

    public interface UpdateActionBarTitle {
        void updateActionBarTitle(String title);
    }
}
