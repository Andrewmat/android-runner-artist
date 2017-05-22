package com.example.andre.runnerartist.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.andre.runnerartist.model.Drawing;

public class CanvasDrawingView extends View {
    private ShapeDrawable drawable;
    private Drawing drawing;

    public CanvasDrawingView(Context context) {
        super(context);
        init();
    }

    public CanvasDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasDrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CanvasDrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.BLACK);
        drawable.getPaint().setStrokeWidth(5);
        drawable.setBounds(0, 0, 100, 100);
        this.setWillNotDraw(false);
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
        this.setWillNotDraw(false);
    }
    public CanvasDrawingView withDrawing(Drawing drawing) {
        setDrawing(drawing);
        return this;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        drawable = new ShapeDrawable(new PathShape(drawing.getDrawingPath().asDrawablePath((double) Math.min(getWidth(), getHeight())), getWidth(), getHeight()));
        drawable.getPaint().setColor(Color.BLACK);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }
}
