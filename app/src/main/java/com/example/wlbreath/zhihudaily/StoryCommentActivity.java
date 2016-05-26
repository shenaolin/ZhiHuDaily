package com.example.wlbreath.zhihudaily;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wlbreath.zhihudaily.bean.CommentBean;
import com.example.wlbreath.zhihudaily.presenter.StoryCommentPagePresenter;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.example.wlbreath.zhihudaily.view.IStoryCommentView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StoryCommentActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, IStoryCommentView {

    private final String TAG = "StoryCommentActivity";

    private long mStoryId;

    private ListView mListView;
    private SwipeRefreshLayout mSrl;
    private FrameLayout mFlErrorPage;
    private TextView mTvErrorPageReloadBtn;

    private StoryCommentPagePresenter mPresenter;

    private CommentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_comment);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");

        final Intent intent = getIntent();

        mStoryId = intent.getLongExtra("storyId", -1);

        int longCommentCount = intent.getIntExtra("longCommentCount", 0);
        int shortCommentCount = intent.getIntExtra("shortCommentCount", 0);

        mAdapter = new CommentAdapter(getApplicationContext(), longCommentCount, shortCommentCount);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        mSrl.setColorSchemeColors(getResources().getColor(R.color.blue));
        mSrl.setOnRefreshListener(this);

        mFlErrorPage = (FrameLayout) findViewById(R.id.fl_error_page);
        mTvErrorPageReloadBtn = (TextView) mFlErrorPage.findViewById(R.id.tv_reload_btn);


        mPresenter = new StoryCommentPagePresenter(getApplicationContext(), this);

        init();
    }

    private void init() {
        initActionBar();
        initErrorPage();

        initComments();
    }

    private void initErrorPage() {
        mTvErrorPageReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isOnline(getApplicationContext())) {
                    showErrorMessage(getErrorPage(), getResources().getString(R.string.offline));
                    return;
                }
                initComments();
            }
        });
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        updateTitle(mAdapter.getLongCommentCount(), mAdapter.getShortCommentCount());
    }

    @Override
    public void updateLongComments(List<CommentBean> longComments) {
        getCommentAdapter().updateLongComments(longComments);
    }

    @Override
    public void updateShortComments(List<CommentBean> shortComments) {
        getCommentAdapter().updateShortComments(shortComments);
    }

    @Override
    public void appendShortComments(List<CommentBean> shortComments) {
        getCommentAdapter().appendShortComments(shortComments);
    }

    @Override
    public void appendLongComments(List<CommentBean> longComments) {
        getCommentAdapter().appendLongComments(longComments);
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
    public ViewGroup getContentArea() {
        if (mSrl == null) {
            mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        }
        return mSrl;
    }

    @Override
    public void updateTitle(int longCommentCount, int shortCommentCount) {
        int count = longCommentCount + shortCommentCount;

        String title = "评论";

        if (count != 0) {
            title = count + "条" + title;
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void uploadCommentCount(int longCommentCount, int shortCommentCount) {
        mAdapter.updateLongCommentCount(longCommentCount);
        mAdapter.updateShortCommentCount(shortCommentCount);
    }


    private void initComments() {
        mPresenter.loadCommentMeta(mStoryId);
        mPresenter.loadStoryLongComments(mStoryId);
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

    private CommentAdapter getCommentAdapter() {
        return mAdapter;
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

    private SwipeRefreshLayout getSrl() {
        if (mSrl == null) {
            mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        }

        return mSrl;
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
        if (!NetworkUtils.isOnline(getApplicationContext())) {
            showErrorMessage(getErrorPage(), getResources().getString(R.string.offline));
            getSrl().setRefreshing(false);
            return;
        }

        initComments();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        CommentAdapter adapter = getCommentAdapter();

        // 下面之所以要检测adpater是否为空是为了防止在listview设置adpater之前就触发了该事件
        if (adapter == null || firstVisibleItem + visibleItemCount < totalItemCount) {
            return;
        }

        int longCommentCount = adapter.getLongCommentCount();
        int shortCommentCount = adapter.getShortCommentCount();

        List<CommentBean> longComments = adapter.getLongComments();
        List<CommentBean> shortComments = adapter.getShortComments();

        if (longCommentCount != 0) {
            if (longComments != null && longComments.size() < longCommentCount) {
                loadBeforeLongComments();

            } else {
                if (adapter.showShortComments()
                        && shortComments != null && shortComments.size() < shortCommentCount) {
                    loadBeforeShortComments();
                }
            }
        } else {
            if (adapter.showShortComments()
                    && shortComments != null && shortComments.size() < shortCommentCount) {
                loadBeforeShortComments();
            }
        }
    }

    private void loadBeforeLongComments() {
        List<CommentBean> comments = mAdapter.getLongComments();

        mPresenter.loadStoryLongCommentsBeforeId(mStoryId, comments.get(comments.size() - 1).getId());
    }

    private void loadBeforeShortComments() {
        List<CommentBean> comments = mAdapter.getShortComments();

        mPresenter.loadStoryShortCommentsBeforeId(mStoryId, comments.get(comments.size() - 1).getId());
    }

    private class CommentAdapter extends BaseAdapter {
        private final int REPLY_SHRINK_LINE = 2;

        private Context context;

        private final int LONG_COMMENT_NUMBER_ITEM = 0;
        private final int SHORT_COMMENT_NUMBER_ITEM = 1;
        private final int LONG_COMMENT_EMPTY_HINT_ITEM = 2;
        private final int COMMENT_ITEM = 3;

        private int shortCommentCount;
        private int longCommentCount;

        private List<CommentBean> longComments;
        private List<CommentBean> shortComments;

        private boolean isShowShortComment = false;

        public CommentAdapter(Context context) {
            this.context = context;
            this.shortCommentCount = 0;
            this.longCommentCount = 0;
        }

        public CommentAdapter(Context context, int longCommentCount, int shortCommentCount) {
            this.context = context;

            this.shortCommentCount = shortCommentCount;
            this.longCommentCount = longCommentCount;
        }

        public int getShortCommentCount() {
            return shortCommentCount;
        }

        public int getLongCommentCount() {
            return longCommentCount;
        }

        public List<CommentBean> getLongComments() {
            return longComments;
        }

        public List<CommentBean> getShortComments() {
            return shortComments;
        }

        public boolean showShortComments() {
            return isShowShortComment;
        }

        public void updateLongCommentCount(int longCommentCount) {
            if (this.longCommentCount != longCommentCount) {
                this.longCommentCount = longCommentCount;
                notifyDataSetChanged();
            }
        }

        public void updateShortCommentCount(int shortCommentCount) {
            if (this.shortCommentCount != shortCommentCount) {
                this.shortCommentCount = shortCommentCount;
                notifyDataSetChanged();
            }
        }

        public void updateLongComments(List<CommentBean> longComments) {
            this.longComments = longComments;
            notifyDataSetChanged();
        }

        public void appendLongComments(List<CommentBean> longComments) {
            if (longComments == null || longComments.size() == 0) {
                return;
            }

            if (this.longComments == null) {
                this.longComments = new ArrayList<>();
            }

            this.longComments.addAll(longComments);
            notifyDataSetChanged();
        }

        public void updateShortComments(List<CommentBean> shortComments) {
            this.shortComments = shortComments;
            notifyDataSetChanged();
        }

        public void appendShortComments(List<CommentBean> shortComments) {
            if (shortComments == null || shortComments.size() == 0) {
                return;
            }

            if (this.shortComments == null) {
                this.shortComments = new ArrayList<>();
            }

            this.shortComments.addAll(shortComments);
            notifyDataSetChanged();
        }

        private void displayShortComment(boolean show) {
            isShowShortComment = show;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = 1;

            if (longCommentCount == 0) {
                count++;

            } else {
                count += longComments == null ? 0 : longComments.size();
            }

            if (longCommentCount == 0) {
                count++;

                if (isShowShortComment && shortComments != null) {
                    count += shortComments.size();
                }

            } else {
                if (longComments != null && longComments.size() >= longCommentCount) {
                    count++;
                    if (isShowShortComment && shortComments != null) {
                        count += shortComments.size();
                    }
                }
            }

            return count;
        }

        public CommentBean getComment(int position) {
            CommentBean comment = null;

            if (longComments == null || longComments.size() == 0) {
                position -= 3;
                if (shortComments != null && position >= 0 && position < shortComments.size()) {
                    comment = shortComments.get(position);
                }

            } else {
                if (position >= 1 && position <= longComments.size()) {
                    comment = longComments.get(position - 1);

                } else if (shortComments != null && shortComments.size() != 0) {
                    position -= 2 + longComments.size();

                    if (position >= 0 && position < shortComments.size()) {
                        comment = shortComments.get(position);
                    }
                }
            }

            return comment;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return LONG_COMMENT_NUMBER_ITEM;
            }

            int type;

            if (longCommentCount == 0) {
                if (position == 1) {
                    type = LONG_COMMENT_EMPTY_HINT_ITEM;

                } else if (position == 2) {
                    type = SHORT_COMMENT_NUMBER_ITEM;

                } else {
                    type = COMMENT_ITEM;
                }

            } else {
                if (position >= 1 && position <= longCommentCount) {
                    type = COMMENT_ITEM;

                } else if (position == longCommentCount + 1) {
                    type = SHORT_COMMENT_NUMBER_ITEM;

                } else {
                    type = COMMENT_ITEM;
                }
            }

            return type;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case LONG_COMMENT_NUMBER_ITEM:
                    if (convertView == null) {
                        convertView = getNewLongCommentNumberItemView(parent);
                        convertView.setTag(new LongCommentNumberItemHolder(convertView));
                    }

                    convertView = bindLongCommentNumberItem(convertView, longCommentCount);
                    break;

                case SHORT_COMMENT_NUMBER_ITEM:
                    if (convertView == null) {
                        convertView = getNewShortCommentNumberItemView(parent);
                        convertView.setTag(new ShortCommentNumberItemHolder(convertView));
                    }

                    convertView = bindShortCommentNumberItem(convertView, shortCommentCount);
                    break;

                case LONG_COMMENT_EMPTY_HINT_ITEM:
                    if (convertView == null) {
                        convertView = getNewLongCommentEmptyHintItemView(parent);
                        convertView.setTag(new LongCommentEmptyHintItemHolder(convertView));
                    }
                    int tmpCount = 0;
                    if (longComments != null) {
                        tmpCount = longComments.size();
                    }
                    bindLongCommentEmptyHintItem(convertView, tmpCount);
                    break;

                case COMMENT_ITEM:
                    if (convertView == null) {
                        convertView = getNewCommentItemView(parent);
                        convertView.setTag(new CommentItemHolder(convertView));
                    }

                    bindCommentItem(convertView, position);
                    break;
            }

            return convertView;
        }

        private View bindLongCommentNumberItem(View view, int number) {
            LongCommentNumberItemHolder holder = (LongCommentNumberItemHolder) view.getTag();
            holder.tv_number.setText(number + "条长评");

            return view;
        }

        private View bindShortCommentNumberItem(View view, int number) {
            final ShortCommentNumberItemHolder holder = (ShortCommentNumberItemHolder) view.getTag();
            holder.tv_number.setText(number + "条短评");

            return view;
        }

        private View bindLongCommentEmptyHintItem(View view, int number) {
            return view;
        }

        private void bindCommentItem(View view, int position) {
            CommentBean comment = getComment(position);

            if (comment == null) {
                Log.e(TAG, "bindCommentItem: comment is null");
                return;
            }
            final CommentItemHolder holder = (CommentItemHolder) view.getTag();

            // reset some configure
            holder.tv_reply_to.setMaxLines(Integer.MAX_VALUE);

            holder.sdv_image.setImageURI(Uri.parse(comment.getAvatar()));
            holder.tv_number_like.setText(String.valueOf(comment.getLikes()));
            holder.tv_content.setText(comment.getContent());
            holder.tv_author.setText(comment.getAuthor());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
            holder.tv_time.setText(dateFormat.format(new Date(comment.getTime())));
            holder.tv_expend_shrink_btn.setTag(comment);

            if (comment.getReply_to() == null) {
                holder.tv_reply_to.setVisibility(View.GONE);
                holder.tv_expend_shrink_btn.setVisibility(View.GONE);

            } else {
                holder.tv_reply_to.setVisibility(View.VISIBLE);

                String replyMsgAuthor = "//" + comment.getReply_to().getAuthor() + ":";
                String replyMsg = replyMsgAuthor + " " + comment.getReply_to().getContent();
                Spannable spannableReplyMsg = new SpannableString(replyMsg);
                int start = 0;
                int end = replyMsgAuthor.length();
                spannableReplyMsg.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                spannableReplyMsg.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.tv_reply_to.setText(spannableReplyMsg);

                if (comment.isExpend()) {
                    holder.tv_expend_shrink_btn.setVisibility(View.VISIBLE);
                    holder.tv_expend_shrink_btn.setText(getResources().getText(R.string.shrink));

                } else {
                    holder.tv_reply_to.post(new Runnable() {
                        @Override
                        public void run() {

                            if (holder.tv_reply_to.getLineCount() <= REPLY_SHRINK_LINE) {
                                holder.tv_expend_shrink_btn.setVisibility(View.GONE);

                            } else {
                                holder.tv_expend_shrink_btn.setVisibility(View.VISIBLE);
                                holder.tv_reply_to.setMaxLines(REPLY_SHRINK_LINE);
                                holder.tv_expend_shrink_btn.setText(getResources().getText(R.string.expend));
                            }
                        }
                    });
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 16/3/26  
                }
            });
        }

        private View getNewLongCommentNumberItemView(ViewGroup parent) {
            return getInflater().inflate(
                    R.layout.activity_story_comment_long_comment_number_item, parent, false);
        }

        private View getNewShortCommentNumberItemView(ViewGroup parent) {
            return getInflater().inflate(
                    R.layout.activity_story_comment_short_comment_number_item, parent, false);
        }

        private View getNewLongCommentEmptyHintItemView(ViewGroup parent) {
            return getInflater().inflate(
                    R.layout.activity_story_comment_long_comment_empty_hint_item, parent, false);
        }

        private View getNewCommentItemView(ViewGroup parent) {
            return getInflater().inflate(R.layout.activity_story_comment_comment_item, parent, false);
        }

        private LayoutInflater getInflater() {
            return LayoutInflater.from(context);
        }

        private class LongCommentNumberItemHolder {
            View itemView;
            TextView tv_number;

            public LongCommentNumberItemHolder(View itemView) {
                this.itemView = itemView;
                this.tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            }
        }

        private class ShortCommentNumberItemHolder implements View.OnClickListener {
            View itemView;
            TextView tv_number;
            ImageView iv_image;

            public ShortCommentNumberItemHolder(final View itemView) {
                this.itemView = itemView;
                this.tv_number = (TextView) itemView.findViewById(R.id.tv_number);
                this.iv_image = (ImageView) itemView.findViewById(R.id.iv_image);

                this.itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                displayShortComment(!isShowShortComment);

                List<CommentBean> shortComments = getShortComments();
                if (shortComments == null || shortComments.size() == 0) {
                    mPresenter.loadStoryShortComments(mStoryId);
                }
            }
        }

        private class LongCommentEmptyHintItemHolder {
            View itemView;
            LinearLayout ll_content;

            public LongCommentEmptyHintItemHolder(View itemView) {
                this.itemView = itemView;
                this.ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            }
        }

        private class CommentItemHolder {
            View itemView;
            TextView tv_time;
            TextView tv_author;
            TextView tv_content;
            TextView tv_reply_to;
            TextView tv_number_like;
            SimpleDraweeView sdv_image;
            TextView tv_expend_shrink_btn;

            public CommentItemHolder(View itemView) {
                this.itemView = itemView;

                this.tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                this.tv_author = (TextView) itemView.findViewById(R.id.tv_author);
                this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
                this.tv_reply_to = (TextView) itemView.findViewById(R.id.tv_reply_to);
                this.tv_number_like = (TextView) itemView.findViewById(R.id.tv_number_like);
                this.sdv_image = (SimpleDraweeView) itemView.findViewById(R.id.sdv_image);
                this.tv_expend_shrink_btn = (TextView) itemView.findViewById(R.id.tv_expend_shrink_btn);

                this.tv_expend_shrink_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentBean comment = (CommentBean) v.getTag();
                        if (comment == null) {
                            Log.e(TAG, "tv_expend_shrink_btn tag: null");
                            return;
                        }

                        comment.setIsExpend(!comment.isExpend());

                        if (comment.isExpend()) {
                            tv_expend_shrink_btn.setText(getResources().getText(R.string.shrink));
                            tv_reply_to.setMaxLines(Integer.MAX_VALUE);

                        } else {
                            tv_expend_shrink_btn.setText(getResources().getText(R.string.expend));
                            tv_reply_to.setMaxLines(REPLY_SHRINK_LINE);
                        }
                    }
                });
            }
        }
    }
}
