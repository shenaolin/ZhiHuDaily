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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.StoryActivity;
import com.example.wlbreath.zhihudaily.bean.ThemeBean;
import com.example.wlbreath.zhihudaily.bean.ThemeCategoryBean;
import com.example.wlbreath.zhihudaily.bean.ThemeEditor;
import com.example.wlbreath.zhihudaily.bean.ThemeStory;
import com.example.wlbreath.zhihudaily.presenter.ThemePagePresenter;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.example.wlbreath.zhihudaily.view.IThemePageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlbreath on 16/3/6.
 */
public class ThemaPageFragment extends Fragment implements IThemePageView {
    private final String TAG = "ThemaPageFragment";

    private FrameLayout mFlErrorPager;
    private TextView mTvErrorPageReloadBtn;
    private SwipeRefreshLayout mSrl;
    private RecyclerView mRv;
    private LinearLayoutManager mLlm;

    private ThemeCategoryBean mThemeCategory;
    private ThemeAdaper mThemeAdaper;

    private ThemePagePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mThemeCategory = (ThemeCategoryBean) getArguments().getSerializable("themeCategory");
        mThemeAdaper = new ThemeAdaper(getContext());

        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        mFlErrorPager = (FrameLayout) view.findViewById(R.id.fl_error_page);

        mTvErrorPageReloadBtn = (TextView) mFlErrorPager.findViewById(R.id.tv_reload_btn);
        mTvErrorPageReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadTheme(mThemeCategory.getId());
            }
        });

        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mSrl.setColorSchemeColors(getResources().getColor(R.color.blue));
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTheme(mThemeCategory.getId());
            }
        });

        mLlm = new LinearLayoutManager(getContext());

        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mRv.setLayoutManager(mLlm);
        mRv.addItemDecoration(new MyRecyclerItemDecoration());
        mRv.setAdapter(mThemeAdaper);
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;

                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;

                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (isRecycleViewScrollToBottom()) {
                            ThemeStory story = mThemeAdaper.getLastThemeStory();
                            if (story != null) {
                                mPresenter.loadThemeStories(mThemeCategory.getId(), story.getId());
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mPresenter = new ThemePagePresenter(getContext(), this);

        init();

        return view;
    }

    public void displayErrorPage(boolean show) {
        if (mFlErrorPager == null) {
            return;
        }

        if (show) {
            mFlErrorPager.setVisibility(View.VISIBLE);
        } else {
            mFlErrorPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorMessage(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));

        snackbar.show();
    }

    @Override
    public void update(ThemeBean theme) {
        mThemeAdaper.updateTheme(theme);
    }

    @Override
    public void appendStories(List<ThemeStory> stories) {
        mThemeAdaper.appendThemeStories(stories);
    }

    @Override
    public void displayContentArea(boolean show) {
        if (mSrl == null) {
            return;
        }

        if (show) {
            mSrl.setVisibility(View.VISIBLE);
        } else {
            mSrl.setVisibility(View.GONE);
        }
    }

    private void init() {
        initErrorPage();

        mPresenter.loadTheme(mThemeCategory.getId());
    }

    private void initErrorPage() {
        mTvErrorPageReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isOnline(getContext())) {
                    showErrorMessage(mFlErrorPager,
                            getContext().getResources().getString(R.string.offline));
                    return;
                }

                mPresenter.loadTheme(mThemeCategory.getId());
            }
        });
    }

    private boolean isRecycleViewScrollToBottom() {
        return mLlm.findLastVisibleItemPosition() == mThemeAdaper.getItemCount() - 1;
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

    @Override
    public ViewGroup getContentArea() {
        return mRv;
    }

    @Override
    public ViewGroup getErrorPage() {
        return mFlErrorPager;
    }

    private class MyRecyclerItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = mRv.getChildAdapterPosition(view);
            if (position == 0 || position == 1) {
                outRect.bottom = 0;
            } else {
                outRect.bottom = 30;
            }
        }
    }

    private class ThemeAdaper extends RecyclerView.Adapter<android.support.v7.widget.RecyclerView.ViewHolder> {
        private final int HEADER_ITEM = 0;
        private final int EDITOR_ITEM = 1;
        private final int STORY_ITEM = 2;

        private Context context;


        private ThemeBean mTheme;

        public ThemeAdaper(Context context) {
            this.context = context;
        }

        public void updateTheme(ThemeBean theme) {
            this.mTheme = theme;
            notifyDataSetChanged();
        }

        public void appendThemeStories(List<ThemeStory> stories) {
            if (mTheme == null) {
                Log.e(TAG, "mtheme is null, can not update stories");
                return;
            }

            if (mTheme.getStories() == null) {
                mTheme.setStories(new ArrayList<ThemeStory>());
            }

            List<ThemeStory> tmpStories = mTheme.getStories();
            tmpStories.addAll(stories);

            mTheme.setStories(tmpStories);
            notifyDataSetChanged();
        }

        public ThemeStory getLastThemeStory() {
            if (mTheme == null || mTheme.getStories() == null || mTheme.getStories().size() == 0) {
                return null;
            }

            List<ThemeStory> stories = mTheme.getStories();

            return stories.get(stories.size() - 1);
        }

        @Override
        public int getItemCount() {
            if (mTheme == null) {
                return 0;
            }

            int count = 2;

            if (mTheme.getStories() != null) {
                count += mTheme.getStories().size();
            }

            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER_ITEM;
            }

            if (position == 1) {
                return EDITOR_ITEM;
            }

            return STORY_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType) {
                case HEADER_ITEM:
                    viewHolder = getNewHeaderHolder(parent);
                    break;

                case EDITOR_ITEM:
                    viewHolder = getNewEditorContainerHolder(parent);
                    break;

                case STORY_ITEM:
                    viewHolder = getNewStoryHolder(parent);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case HEADER_ITEM:
                    bindHeaderHolder((HeaderHolder) holder, mTheme.getDescription(), mTheme.getBackground());
                    break;

                case EDITOR_ITEM:
                    bindEditorHolder((EditorContainerHolder) holder, mTheme.getEditors());
                    break;

                case STORY_ITEM:
                    bindStoryHolder((StoryHolder) holder, position, getStory(position));
            }
        }

        private void bindStoryHolder(final StoryHolder holder, final int position, final ThemeStory story) {
            if (story == null) {
                return;
            }

            String title = story.getTitle();
            if (title != null) {
                holder.tv_title.setText(title);
            }

            List<String> images = story.getImages();
            if (images == null || images.size() == 0) {
                holder.sdv_image.setVisibility(View.GONE);

            } else {
                holder.sdv_image.setVisibility(View.VISIBLE);
                holder.sdv_image.setImageURI(Uri.parse(images.get(0)));
            }
        }

        private ThemeStory getStory(int position) {
            return mTheme.getStories().get(converToStoryPosition(position));
        }

        private int converToStoryPosition(int position) {
            return position - 2;
        }

        private void bindEditorHolder(EditorContainerHolder holder, List<ThemeEditor> editors) {
            holder.ll_editors_container.removeAllViews();

            if (editors == null || editors.size() == 0) {
                holder.itemView.setVisibility(View.GONE);

            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                for (ThemeEditor editor : editors) {
                    EditorHolder editorHolder = getNewEditorHolder(holder.ll_editors_container);
                    editorHolder.sdv_image.setImageURI(Uri.parse(editor.getAvatar()));
                    holder.ll_editors_container.addView(editorHolder.itemView);
                }
            }
        }

        public void bindHeaderHolder(final HeaderHolder headerHolder, final String title, final String imageUrl) {
            if (title != null) {
                headerHolder.tv_title.setText(title);
            }

            if (imageUrl != null) {
                headerHolder.sdv_image.setImageURI(Uri.parse(imageUrl));
            }
        }

        private HeaderHolder getNewHeaderHolder(ViewGroup parent) {
            return new HeaderHolder(getInflater().inflate(R.layout.fragment_theme_header, parent, false));
        }

        private EditorContainerHolder getNewEditorContainerHolder(ViewGroup parent) {
            return new EditorContainerHolder(getInflater().inflate(R.layout.fragment_theme_editors_container, parent, false));
        }

        private EditorHolder getNewEditorHolder(ViewGroup parent) {
            return new EditorHolder(getInflater().inflate(R.layout.fragment_theme_editor, parent, false));
        }

        private StoryHolder getNewStoryHolder(ViewGroup parent) {
            return new StoryHolder(getInflater().inflate(R.layout.fragment_theme_story, parent, false));
        }

        private LayoutInflater getInflater() {
            return LayoutInflater.from(context);
        }

        private class HeaderHolder extends RecyclerView.ViewHolder {
            TextView tv_title;
            SimpleDraweeView sdv_image;

            public HeaderHolder(View itemView) {
                super(itemView);

                tv_title = (TextView) itemView.findViewById(R.id.tv_title);
                sdv_image = (SimpleDraweeView) itemView.findViewById(R.id.sdv_image);
            }
        }

        private class EditorContainerHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_editors_container;

            public EditorContainerHolder(View itemView) {
                super(itemView);

                this.ll_editors_container = (LinearLayout) itemView.findViewById(R.id.ll_editors_container);
            }
        }

        private class EditorHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView sdv_image;

            public EditorHolder(View itemView) {
                super(itemView);

                sdv_image = (SimpleDraweeView) itemView.findViewById(R.id.sdv_image);
            }
        }

        private class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_title;
            SimpleDraweeView sdv_image;

            public StoryHolder(View itemView) {
                super(itemView);

                tv_title = (TextView) itemView.findViewById(R.id.tv_title);
                sdv_image = (SimpleDraweeView) itemView.findViewById(R.id.sdv_image);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                loadStoryActivity(getStory(getAdapterPosition()).getId());
            }
        }
    }
}
