package com.xmx.homenurse.Record;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by The_onE on 2016/4/2.
 */
public class RecordAdapter extends BaseAdapter {
    Context mContext;
    List<Record> mRecords;

    RecordAdapter(Context context, List<Record> record) {
        mContext = context;
        mRecords = record;
    }

    @Override
    public int getCount() {
        return mRecords.size();
    }

    @Override
    public Object getItem(int i) {
        return mRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mRecords.get(i).mId;
    }

    static class ViewHolder {
        TextView title;
        TextView text;
        TextView suggestion;
        TextView time;
        CardView card;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_amount_a_day, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.text = (TextView) convertView.findViewById(R.id.tv_text);
            holder.suggestion = (TextView) convertView.findViewById(R.id.tv_suggestion);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.card = (CardView) convertView.findViewById(R.id.record_card);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < mRecords.size()) {
            Record record = mRecords.get(position);
            String title = record.mTitle;
            String text = record.mText;
            String suggestion = record.mSuggestion;
            Date time = record.mTime;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            holder.title.setText(title);
            holder.text.setText(text);
            holder.suggestion.setText(suggestion);
            holder.time.setText(df.format(time));
            int type = record.mType;
            int bg = Color.GRAY;
            switch (type) {
                case Constants.GOOD_TYPE:
                    bg = Color.GREEN;
                    break;
                case Constants.HIGH_TYPE:
                    bg = Color.BLUE;
                    break;
                case Constants.HIGHEST_TYPE:
                    bg = Color.RED;
                    break;
            }
            holder.card.setCardBackgroundColor(bg);
        } else {
            holder.title.setText("加载失败");
        }

        return convertView;
    }
}
