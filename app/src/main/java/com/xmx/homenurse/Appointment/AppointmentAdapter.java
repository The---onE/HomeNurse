package com.xmx.homenurse.Appointment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Data.AppointmentManager;
import com.xmx.homenurse.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class AppointmentAdapter extends BaseAdapter {
    Context mContext;

    public AppointmentAdapter(Context context) {
        mContext = context;
    }

    public void changeList() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return AppointmentManager.getInstance().getAppointments().size();
    }

    @Override
    public Object getItem(int i) {
        if (i < AppointmentManager.getInstance().getAppointments().size()) {
            return AppointmentManager.getInstance().getAppointments().get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if (i < AppointmentManager.getInstance().getAppointments().size()) {
            return AppointmentManager.getInstance().getAppointments().get(i).getId();
        } else {
            return i;
        }
    }

    static class ViewHolder {
        TextView time;
        TextView type;
        TextView symptom;
        TextView addTime;

        CardView card;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_appointment, null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            holder.type = (TextView) convertView.findViewById(R.id.item_type);
            holder.symptom = (TextView) convertView.findViewById(R.id.item_symptom);
            holder.addTime = (TextView) convertView.findViewById(R.id.item_add_time);
            holder.card = (CardView) convertView.findViewById(R.id.card_appointment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<Appointment> appointments = AppointmentManager.getInstance().getAppointments();
        if (position < appointments.size()) {
            Appointment appointment = appointments.get(position);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(appointment.getTime());
            holder.time.setText(timeString);
            holder.type.setText(Constants.APPOINTMENT_TYPE[appointment.getType()]);
            holder.symptom.setText(appointment.getSymptom());
            String addTimeString = df.format(appointment.getAddTime());
            holder.addTime.setText(addTimeString);

            switch (appointment.getStatus()) {
                case -1:
                    holder.card.setCardBackgroundColor(Color.GRAY);
                    break;
                default:
                    holder.card.setCardBackgroundColor(Color.WHITE);
                    break;
            }
        } else {
            holder.time.setText("加载失败");
        }

        return convertView;
    }
}