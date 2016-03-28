package com.xmx.homenurse.Measure.Schedule;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Tools.DisplayUtil;
import com.xmx.homenurse.R;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

public class MeasureScheduleDetailActivity extends BaseTempActivity
        implements BGARefreshLayout.BGARefreshLayoutDelegate {

    SlideAndDragListView measureScheduleList;
    MeasureScheduleCardAdapter adapter;
    BGARefreshLayout mRefreshLayout;

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (MeasureScheduleManager.getInstance().updateMeasureSchedules() >= 0) {
                adapter.changeList();
            }
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_measure_schedule_detail);

        setTitle(R.string.measure_schedule);

        measureScheduleList = getViewById(R.id.list_plan_card);

        Menu menu = new Menu(new ColorDrawable(Color.WHITE), true, 0);
        menu.addItem(new MenuItem.Builder().setWidth(DisplayUtil.dip2px(this, 50))
                .setBackground(new ColorDrawable(Color.RED))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_menu_delete))
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(DisplayUtil.dip2px(this, 75))
                .setBackground(new ColorDrawable(Color.GRAY))
                .setText("测完啦")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize(20)
                .build());
        measureScheduleList.setMenu(menu);

        adapter = new MeasureScheduleCardAdapter(this);
        measureScheduleList.setAdapter(adapter);

        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshLayout = getViewById(R.id.item_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, false);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText(getString(R.string.loading));

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置整个加载更多控件的背景颜色资源id
        //refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景drawable资源id
        //refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源id
        //refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景drawable资源id
        //refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }

    @Override
    protected void setListener() {

        measureScheduleList.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
            @Override
            public void onSlideOpen(View view, View parentView, int position, int direction) {

            }

            @Override
            public void onSlideClose(View view, View parentView, int position, int direction) {

            }
        });

        measureScheduleList.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                MeasureSchedule plan = (MeasureSchedule) adapter.getItem(itemPosition);
                long id = plan.getId();
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        return Menu.ITEM_SCROLL_BACK;

                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 1: //Complete
                                MeasureScheduleSQLManager.getInstance().completeSchedule(id);
                                MeasureScheduleManager.getInstance().updateMeasureSchedules();
                                adapter.changeList();
                                return Menu.ITEM_SCROLL_BACK;
                            case 0: //Cancel
                                MeasureScheduleSQLManager.getInstance().cancelSchedule(id);
                                MeasureScheduleManager.getInstance().updateMeasureSchedules();
                                adapter.changeList();
                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        return Menu.ITEM_SCROLL_BACK;

                    default:
                        return Menu.ITEM_NOTHING;
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        long id = getIntent().getLongExtra("id", -1);
        for (int i = 0; i < adapter.getCount(); ++i) {
            if (adapter.getItemId(i) == id) {
                measureScheduleList.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        RefreshHandler timerHandler = new RefreshHandler();
        timerHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }
}
