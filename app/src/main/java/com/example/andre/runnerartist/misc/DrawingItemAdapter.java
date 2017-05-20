package com.example.andre.runnerartist.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andre.runnerartist.R;
import com.example.andre.runnerartist.model.Drawing;

import java.text.DateFormat;
import java.text.DecimalFormat;
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
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_drawing, null);
            vh = new ViewHolder();
            vh.txvItemDrawingMain = (TextView) convertView.findViewById(R.id.txvItemDrawingMain);
            vh.txvDistanceDuration = (TextView) convertView.findViewById(R.id.txvDistanceDuration);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // Message of creation time
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
        vh.txvItemDrawingMain.setText(creationTimeMessage);

        // Message of distance and duration of travel
        Long duration = (drawing.getFinishCreationTime() - drawing.getStartCreationTime()) / 60000;
        vh.txvDistanceDuration.setText(new DecimalFormat("#.##").format(drawing.getPath().distance()) + "km - " + duration + "min");

        return convertView;
    }

    private static class ViewHolder {
        TextView txvItemDrawingMain;
        TextView txvDistanceDuration;
    }
}
