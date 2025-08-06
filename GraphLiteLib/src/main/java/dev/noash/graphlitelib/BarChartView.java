package dev.noash.graphlitelib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {

    private List<Float> values = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private List<Integer> barColors = new ArrayList<>();

    private int[] gradientColors = new int[]{
            Color.parseColor("#3B82F6"),
            Color.parseColor("#60A5FA")
    };

    private Float customBarWidth = null;
    private Float customBarSpacing = null;

    public BarChartView(Context context) {
        super(context);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setValues(List<Float> values) {
        this.values = values;
        invalidate();
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
        invalidate();
    }

    public void setBarWidth(float barWidthPx) {
        this.customBarWidth = barWidthPx;
        invalidate();
    }

    public void setBarWidthDp(float barWidthDp) {
        float scale = getResources().getDisplayMetrics().density;
        this.customBarWidth = barWidthDp * scale;
        invalidate();
    }

    public void setBarSpacing(float barSpacingPx) {
        this.customBarSpacing = barSpacingPx;
        invalidate();
    }

    public void setBarColors(List<Integer> colors) {
        this.barColors = colors != null ? colors : new ArrayList<>();
        invalidate();
    }

    public void adjustWidthToContent() {
        if (values == null || values.isEmpty()) return;

        float defaultSpacing = 40f;
        float defaultBarWidth = 80f;

        float barWidth = (customBarWidth != null) ? customBarWidth : defaultBarWidth;
        float barSpacing = (customBarSpacing != null) ? customBarSpacing : defaultSpacing;

        int count = values.size();
        int totalWidth = Math.round((barWidth + barSpacing) * count + 100);

        getLayoutParams().width = totalWidth;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.isEmpty()) return;

        float barSpacingFromYAxis = 20f;
        float width = getWidth();
        float height = getHeight();
        float paddingLeft = 80f;
        float paddingRight = 40f;
        float paddingBottom = 80f;
        float paddingTop = 40f;

        float usableWidth = width - paddingLeft - paddingRight;
        float usableHeight = height - paddingTop - paddingBottom;
        int barCount = values.size();

        float max = 1f;
        for (float v : values) {
            if (v > max) max = v;
        }

        float barWidth = (customBarWidth != null) ? customBarWidth : usableWidth / (barCount * 1.5f);
        float barSpacing = (customBarSpacing != null) ? customBarSpacing : barWidth * 0.5f;

        Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint yValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(28f);
        labelPaint.setColor(Color.DKGRAY);

        yValuePaint.setTextAlign(Paint.Align.RIGHT);
        yValuePaint.setTextSize(26f);
        yValuePaint.setColor(Color.GRAY);

        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(3f);

        LinearGradient gradient = new LinearGradient(0, paddingTop, 0, height - paddingBottom,
                gradientColors, null, Shader.TileMode.CLAMP);
        barPaint.setShader(gradient);

        for (int i = 0; i < barCount; i++) {
            float val = values.get(i);
            float barHeight = (val / max) * usableHeight;

            float left = paddingLeft + barSpacingFromYAxis + i * (barWidth + barSpacing);
            float top = height - paddingBottom - barHeight;
            float right = left + barWidth;
            float bottom = height - paddingBottom;

            Paint customPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if (i < barColors.size()) {
                customPaint.setColor(barColors.get(i));
            } else {
                customPaint.setShader(new LinearGradient(0, paddingTop, 0, height - paddingBottom,
                        gradientColors, null, Shader.TileMode.CLAMP));
            }

            canvas.drawRoundRect(new RectF(left, top, right, bottom), 20f, 20f, customPaint);

            if (i < labels.size()) {
                canvas.drawText(labels.get(i), left + barWidth / 2, height - 20f, labelPaint);
            }
        }

        canvas.drawLine(
                paddingLeft + barSpacingFromYAxis,
                height - paddingBottom,
                width - paddingRight,
                height - paddingBottom,
                axisPaint
        );

        canvas.drawLine(
                paddingLeft,
                paddingTop,
                paddingLeft,
                height - paddingBottom,
                axisPaint
        );

        int ySteps = 5;
        for (int i = 0; i <= ySteps; i++) {
            float fraction = (float) i / ySteps;
            float value = max * (1f - fraction);
            float y = paddingTop + usableHeight * fraction;
            canvas.drawText(String.format("%.0f", value), paddingLeft - 10f, y + 10f, yValuePaint);
        }
    }
}
