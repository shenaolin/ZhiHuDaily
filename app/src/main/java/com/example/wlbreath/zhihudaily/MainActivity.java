package com.example.wlbreath.zhihudaily;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wlbreath.zhihudaily.bean.ThemeCategoryBean;
import com.example.wlbreath.zhihudaily.bean.ThemeMenuBean;
import com.example.wlbreath.zhihudaily.fragment.NewsPageFragment;
import com.example.wlbreath.zhihudaily.fragment.ThemaPageFragment;
import com.example.wlbreath.zhihudaily.presenter.MainPagePresenter;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.example.wlbreath.zhihudaily.view.IMainPageView;

public class MainActivity extends AppCompatActivity implements NewsPageFragment.UpdateActionBarTitle, IMainPageView {
    private final String TAG = "main activity";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mLvThemeMenu;
    public FrameLayout mFlContentArea;
    private FrameLayout mFlErrorPage;
    private TextView mTvErrorPageReloadBtn;
    private ActionBarDrawerToggle mDrawerToggle;

    private MainPagePresenter mPresenter;

    private ThemeMenuAdapter mThemeMenuAdapter;

    // 主要用来判断现在点击的item是否已经显示了
    // 如果显示不更新fragment
    private int prevFragmentIndex = 0;
    private int curtFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "main activity created");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mLvThemeMenu = (ListView) findViewById(R.id.lv_theme_menu);
        mFlContentArea = (FrameLayout) findViewById(R.id.fl_content_area);
        mFlErrorPage = (FrameLayout) findViewById(R.id.fl_error_page);
        mTvErrorPageReloadBtn = (TextView) mFlErrorPage.findViewById(R.id.tv_reload_btn);

        mPresenter = new MainPagePresenter(getApplicationContext(), this);
        mThemeMenuAdapter = new ThemeMenuAdapter(getApplicationContext());

        initView();

        updateThemeMenu();
        displayHomeFragment();
    }

    private void initView() {
        initErrorPage();
        initActionBar();
        initThemeMenu();
    }

    private void initActionBar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setTitle(getResources().getString(R.string.home));
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.main_menu_open, R.string.main_menu_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                displayFragment();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    private void initThemeMenu() {
        Log.d(TAG, "initThemeMenu function is called");

        mLvThemeMenu.addHeaderView(getThemeMenuHeader());
        mLvThemeMenu.setAdapter(mThemeMenuAdapter);
        mLvThemeMenu.setOnItemClickListener(new ThemeMenuOnItemClickListener());
    }

    @Override
    public void updateThemeMenu(ThemeMenuBean themeMenu) {
        mThemeMenuAdapter.setMenuBean(themeMenu);
    }

    private void updateThemeMenu() {
        mPresenter.updateThemeMenu();
    }

    private void initErrorPage() {

        mTvErrorPageReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isOnline(getApplicationContext())) {
                    showOfflineMessage(mDrawerLayout);
                    return;
                }
                Log.d(TAG, "MainActivity: reload");
                updateThemeMenu();
                displayHomeFragment();
            }
        });
    }

    private void showOfflineMessage(View view) {
        Snackbar snackbar = Snackbar.make(
                view, getResources().getText(R.string.offline), Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));

        snackbar.show();
    }

    private void displayFragment() {
//      already display not to update
        if (curtFragmentIndex == prevFragmentIndex) {
            return;
        }

        prevFragmentIndex = curtFragmentIndex;

        // display home page
        if (curtFragmentIndex == 0) {
            displayHomeFragment();
            return;
        }

        // display theme
        displayThemeFrament(mThemeMenuAdapter.getMenuBean().getThemeBean(curtFragmentIndex - 1));
    }

    public void displayHomeFragment() {
        updateActionBarTitle(getResources().getString(R.string.home));

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_content_area, getHomeFragment()).commit();
    }

    public void displayThemeFrament(ThemeCategoryBean theme) {
        updateActionBarTitle(theme.getName());

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_content_area, getThemeFragment(theme)).commit();
    }

    public Fragment getHomeFragment() {
        return new NewsPageFragment();
    }

    public Fragment getThemeFragment(ThemeCategoryBean themeCategoryBean) {
        Fragment fragment = new ThemaPageFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("themeCategory", themeCategoryBean);
        fragment.setArguments(bundle);

        return fragment;
    }

    public void displayContentArea(boolean show) {
        if (show) {
            mFlContentArea.setVisibility(View.VISIBLE);
        } else {
            mFlContentArea.setVisibility(View.GONE);
        }
    }

    public void displayErrorPage(boolean show) {
        if (show) {
            mFlErrorPage.setVisibility(View.VISIBLE);
        } else {
            mFlErrorPage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //这里我们用来更新menuitem的内容
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.actionbar_menu_setting:
                break;

            case R.id.actionbar_menu_show_mode:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private View getThemeMenuHeader() {
        return View.inflate(this, R.layout.activity_main_theme_menu_header, null);
    }

    @Override
    public void updateActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null && title != null) {
            actionBar.setTitle(title);
        }
    }

    private class ThemeMenuAdapter extends BaseAdapter {
        public static final int HOME_ITEM = 0;
        public static final int THEME_ITEM = 1;

        private Context context;
        private ThemeMenuBean menuBean;

        private int selectedItemPosition = 0;

        public ThemeMenuAdapter(Context context) {
            this.context = context;
        }

        public ThemeMenuAdapter(Context context, ThemeMenuBean menuBean) {
            this.context = context;
            this.menuBean = menuBean;
        }

        public void updateItemSelected(int position) {
            selectedItemPosition = position;
            notifyDataSetChanged();
        }

        public ThemeMenuBean getMenuBean() {
            return menuBean;
        }

        public void setMenuBean(ThemeMenuBean menuBean) {
            this.menuBean = menuBean;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HOME_ITEM;
            }
            return THEME_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            int count = 1;
            if (menuBean != null) {
                count += getSubscribedCount() + getOthersCount();
            }
            return count;
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
            if (getItemViewType(position) == HOME_ITEM) {
                if (convertView == null) {
                    convertView = getNewHomeItem(parent);
                }
                convertView.setBackgroundResource(position == selectedItemPosition ? R.color.grey : R.color.white);
                return convertView;
            }

            if (convertView == null) {
                convertView = getNewThemeItem(parent);
            }
            ThemeCategoryBean themeCategoryBean = getThemeBean(position);
            convertView = initItemIcon(convertView, getItemIcon(position));
            convertView = initItemTitle(convertView, themeCategoryBean.getName());
            convertView.setBackgroundResource(position == selectedItemPosition ? R.color.grey : R.color.white);
            return convertView;
        }

        private View getNewHomeItem(ViewGroup parent) {
            return getInflater().inflate(R.layout.activity_main_theme_menu_home_item, parent, false);
        }


        private View getNewThemeItem(ViewGroup parent) {
            return getInflater().inflate(R.layout.activity_main_theme_menu_theme_item, parent, false);
        }

        private View initItemTitle(View view, String title) {
            TextView tv = (TextView) view.findViewById(R.id.tv_title);
            tv.setText(title);

            return view;
        }

        private View initItemIcon(View view, Bitmap icon) {
            ImageView iv = (ImageView) view.findViewById(R.id.iv_icon);
            iv.setImageBitmap(icon);

            return view;
        }

        private Bitmap getItemIcon(int position) {
            return BitmapFactory.decodeResource(context.getResources(),
                    isThemeSubscribed(position) ? R.drawable.ic_menu_arrow : R.drawable.ic_menu_follow);
        }

        private int convertToThemeItemPosition(int position) {
            return position - 1;
        }

        private ThemeCategoryBean getThemeBean(int position) {
            ThemeCategoryBean theme = null;

            if (getMenuBean() != null) {
                theme = isThemeSubscribed(position)
                        ? getSubscribedThemeBean(position) : getOtherThemeBean(position);
            }

            return theme;
        }

        private boolean isThemeSubscribed(int position) {
            return convertToThemeItemPosition(position) < getSubscribedCount();
        }

        private ThemeCategoryBean getSubscribedThemeBean(int position) {
            return menuBean.getSubscribed().get(convertToThemeItemPosition(position));
        }

        private ThemeCategoryBean getOtherThemeBean(int position) {
            return menuBean.getOthers().get(convertToThemeItemPosition(position) - getSubscribedCount());
        }


        private int getSubscribedCount() {
            return menuBean.getSubscribed().size();
        }

        private int getOthersCount() {
            return menuBean.getOthers().size();
        }

        private LayoutInflater getInflater() {
            return LayoutInflater.from(context);
        }
    }

    private class ThemeMenuOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int headerViewsCount = mLvThemeMenu.getHeaderViewsCount();

            if (position < headerViewsCount) {
                return;
            }

            position = position - headerViewsCount;

            // 更新menu中那个item被选中
            if (mThemeMenuAdapter != null) {
                mThemeMenuAdapter.updateItemSelected(position);
            }

            curtFragmentIndex = position;

            // close menu
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
