package com.xmx.homenurse.Measure.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.R;

import java.util.List;

/**
 * Created by The_onE on 2016/2/25.
 */
public class MeasureScheduleCardAdapter extends BaseAdapter {
    Context mContext;

    public MeasureScheduleCardAdapter(Context context) {
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
        return MeasureScheduleManager.getInstance().getMeasureSchedules().get(position).getId();
    }

    static class ViewHolder {
        TextView title;
        TextView time;
        TextView text;

        TextView remind;
        TextView daily;
        TextView period;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_measure_schedule_card, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.card_title);
            holder.time = (TextView) convertView.findViewById(R.id.card_time);
            holder.text = (TextView) convertView.findViewById(R.id.card_text);
            holder.remind = (TextView) convertView.findViewById(R.id.card_remind_tag);
            holder.daily = (TextView) convertView.findViewById(R.id.card_daily_tag);
            holder.period = (TextView) convertView.findViewById(R.id.card_period_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<MeasureSchedule> plans = MeasureScheduleManager.getInstance().getMeasureSchedules();
        if (position < plans.size()) {
            MeasureSchedule plan = plans.get(position);
            holder.title.setText(plan.getTitle());
            holder.time.setText(plan.getTimeString());
            holder.text.setText(plan.getText());

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
                String periodString = "";
                if (period / Constants.DAY_TIME > 0) {
                    long day = period / Constants.DAY_TIME;
                    periodString += day + "天";
                    period %= Constants.DAY_TIME;
                }
                if (period / Constants.HOUR_TIME > 0) {
                    long hour = period / Constants.HOUR_TIME;
                    periodString += +hour + "小时";
                    period %= Constants.HOUR_TIME;
                }
                if (period / Constants.MINUTE_TIME > 0) {
                    long minute = period / Constants.MINUTE_TIME;
                    periodString += +minute + "分钟";
                    period %= Constants.MINUTE_TIME;
                }
                if (period / Constants.SECOND_TIME > 0) {
                    long second = period / Constants.SECOND_TIME;
                    periodString += +second + "秒";
                }
                holder.period.setText(periodString);
                holder.period.setVisibility(View.VISIBLE);
            }
        } else {
            holder.title.setText("加载失败");
        }

        return convertView;
    }
}
