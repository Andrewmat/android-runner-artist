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
            vh.txvDrawingComplement = (TextView) convertView.findViewById(R.id.txvDrawingComplement);
            vh.canvasDrawing = (CanvasDrawingView) convertView.findViewById(R.id.canvasDrawing);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // Message of creation time
        String creationTimeMessage;
        Long delta = System.currentTimeMillis() - drawing.getFinishCreationTime();
        if (delta < MAX_DELTA_TIME) {
            if (delta < MIN) {
                creationTimeMessage = "< 1m";
            } else if (delta < HOUR) {
                creationTimeMessage = delta / MIN + "m";
            } else if (delta < DAY) {
                creationTimeMessage = delta / HOUR + "h";
            } else {
                creationTimeMessage = delta / DAY + "d";
            }
        } else {
            creationTimeMessage = DateFormat.getDateInstance().format(new Date(drawing.getFinishCreationTime()));
        }

        String durationMessage = (drawing.getFinishCreationTime() - drawing.getStartCreationTime()) / 60000 + "min";
        String distanceMessage = new DecimalFormat("#.##").format(drawing.getDrawingPath().distance()) + "km";

        if (drawing.getDescription() != null && !drawing.getDescription().isEmpty()) {
            vh.txvItemDrawingMain.setText(drawing.getDescription());
            vh.txvDrawingComplement.setText(creationTimeMessage + " - " + durationMessage + " - " + distanceMessage);
        } else {
            vh.txvItemDrawingMain.setText("À " + creationTimeMessage);
            vh.txvDrawingComplement.setText(durationMessage + " - " + distanceMessage);
        }

        vh.canvasDrawing.withDrawing(drawing).invalidate();

        return convertView;
    }

    private static class ViewHolder {
        TextView txvItemDrawingMain;
        TextView txvDrawingComplement;
        CanvasDrawingView canvasDrawing;
    }
}
