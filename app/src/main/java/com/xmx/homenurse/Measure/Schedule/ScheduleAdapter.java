package com.xmx.homenurse.Measure.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.R;

import java.util.List;

/**
 * Created by The_onE on 2016/1/30.
 */
public class ScheduleAdapter extends BaseAdapter {
    Context mContext;

    public ScheduleAdapter(Context context) {
        mContext = context;
    }

    public void changeList() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return MeasureScheduleManager.getInstance().getMeasureSchedules().size();
    }

    @Override
    public Object getItem(int position) {
        if (position < MeasureScheduleManager.getInstance().getMeasureSchedules().size()) {
            return MeasureScheduleManager.getInstance().getMeasureSchedules().get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView title;
        TextView time;
        TextView before;

        TextView remind;
        TextView daily;
        TextView period;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_measure_schedule, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            holder.before = (TextView) convertView.findViewById(R.id.item_before);
            holder.remind = (TextView) convertView.findViewById(R.id.remind_tag);
            holder.daily = (TextView) convertView.findViewById(R.id.daily_tag);
            holder.period = (TextView) convertView.findViewById(R.id.period_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<MeasureSchedule> measureSchedule = MeasureScheduleManager.getInstance().getMeasureSchedules();
        if (position < measureSchedule.size()) {
            MeasureSchedule plan = measureSchedule.get(position);
            holder.title.setText(plan.getTitle());
            holder.title.setTextColor(Color.BLACK);

            holder.time.setText(plan.getTimeString());

            holder.before.setText(plan.getBeforeString());

            if (plan.isRemindFlag()) {
                holder.remind.setVisibility(View.VISIBLE);
            } else {
                holder.remind.setVisibility(View.GONE);
            }

            if (plan.isDailyFlag()) {
                holder.daily.setVisibility(View.VISIBLE);
            } else {
                holder.daily.setVisibility(View.GONE);
            }

            int period = plan.getPeriod();
            if (period <= 0) {
                holder.period.setVisibility(View.GONE);
            } else {
                String periodString = "周期";
                if (period / Constants.DAY_TIME > 0) {
                    long day = period / Constants.DAY_TIME;
                    periodString = "" + day + "天";
                } else if (period / Constants.HOUR_TIME > 0) {
                    long hour = period / Constants.HOUR_TIME;
                    periodString = "" + hour + "小时";
                } else if (period / Constants.MINUTE_TIME > 0) {
                    long minute = period / Constants.MINUTE_TIME;
                    periodString = "" + minute + "分钟";
                } else if (period / Constants.SECOND_TIME > 0) {
                    long second = period / Constants.SECOND_TIME;
                    periodString = "" + second + "秒";
                }
                holder.period.setText(periodString);
                holder.period.setVisibility(View.VISIBLE);
            }
        } else {
            holder.title.setText("加载失败");
            holder.time.setText("");
        }

        return convertView;
    }
}