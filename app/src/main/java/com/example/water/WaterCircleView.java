package com.example.water;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class WaterCircleView extends View {

    private Paint bgPaint, waterPaint, textPaint, subTextPaint;
    private float progress = 0f;
    private String mainText = "0%";
    private String subText = "0 ml / 2000 ml";
    private RectF rectF;

    public WaterCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#E3F2FD"));
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(20f);

        waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        waterPaint.setColor(Color.parseColor("#2196F3"));
        waterPaint.setStyle(Paint.Style.STROKE);
        waterPaint.setStrokeWidth(20f);
        waterPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#0D47A1"));
        textPaint.setTextSize(80f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        subTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        subTextPaint.setColor(Color.parseColor("#1E88E5"));
        subTextPaint.setTextSize(35f);
        subTextPaint.setTextAlign(Paint.Align.CENTER);

        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 30;
        int cx = width / 2;
        int cy = height / 2;

        rectF.set(cx - radius, cy - radius, cx + radius, cy + radius);

        // Background circle
        canvas.drawCircle(cx, cy, radius, bgPaint);

        // Water arc
        float angle = 360f * (progress / 100f);
        canvas.drawArc(rectF, -90, angle, false, waterPaint);

        // Percentage text
        canvas.drawText(mainText, cx, cy + 25, textPaint);

        // Sub text
        canvas.drawText(subText, cx, cy + 75, subTextPaint);
    }

    public void setProgress(float progress, int current, int goal) {
        this.progress = progress;
        this.mainText = (int) progress + "%";
        this.subText = current + " ml / " + goal + " ml";
        invalidate();
    }
}