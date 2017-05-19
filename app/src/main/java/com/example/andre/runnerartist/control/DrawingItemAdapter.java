package com.example.andre.runnerartist.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andre.runnerartist.R;
import com.example.andre.runnerartist.model.Drawing;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class DrawingItemAdapter extends BaseAdapter {
    private Context ctx;
    private List<Drawing> drawings;
    private static final long SEC = 1000;
    private static final long MIN = SEC * 60;
    private static final long HOUR = MIN * 60;
    private static final long DAY = HOUR * 24;
    private static final long MAX_DELTA_TIME = DAY * 7; // one week

    public DrawingItemAdapter(Context ctx, List<Drawing> drawings) {
        this.ctx = ctx;
        this.drawings = drawings;
    }
    @Override
    public int getCount() {
        return drawings.size();
    }

    @Override
    public Drawing getItem(int position) {
        return drawings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Drawing drawing = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_drawing, null);
            holder = new ViewHolder();
            holder.txvItemDrawingMain = (TextView) convertView.findViewById(R.id.txvItemDrawingMain);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String creationTimeMessage;
        Long delta = System.currentTimeMillis() - drawing.getFinishCreationTime();
        if (delta < MAX_DELTA_TIME) {
            if (delta < MIN) {
                creationTimeMessage = "Menos de 1m atrás";
            } else if (delta < HOUR) {
                creationTimeMessage = delta / MIN + " minutos atrás";
            } else if (delta < DAY) {
                creationTimeMessage = delta / HOUR + " horas atrás";
            } else {
                creationTimeMessage = delta / DAY + " dias atrás";
            }
        } else {
            creationTimeMessage = DateFormat.getDateInstance().format(new Date(drawing.getFinishCreationTime()));
        }

        holder.txvItemDrawingMain.setText(creationTimeMessage);

        return convertView;
    }

    static class ViewHolder {
        TextView txvItemDrawingMain;
    }
}
