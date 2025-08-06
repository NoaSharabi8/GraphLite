package dev.noash.graphlitelib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    public enum AnimationType {
        NONE, EASE_IN_OUT, FADE_IN, WAVE
    }

    private List<GraphLine> graphLines = new ArrayList<>();
    private List<Float> referenceData = new ArrayList<>();
    private List<String> xLabels = new ArrayList<>();
    private int selectedIndex = -1;

    private boolean showAxis = true;
    private int yLabelCount = 5;

    private float animationProgress = 1f;
    private boolean animationStarted = false;
    private boolean shouldAnimateNextDraw = false;
    private long animationDuration = 6000;
    private AnimationType animationType = AnimationType.NONE;

    private static final float TOUCH_TOLERANCE_X = 40f;

    private Paint linePaint, pointPaint, fillPaint, axisPaint, labelPaint;

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClickable(true);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);

        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(2f);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.DKGRAY);
        labelPaint.setTextSize(28f);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setGraphLines(List<GraphLine> lines) {
        this.graphLines = lines != null ? lines : new ArrayList<>();
        this.referenceData = !graphLines.isEmpty() ? graphLines.get(0).values : new ArrayList<>();
        shouldAnimateNextDraw = true;
        animationProgress = 0f;
        animationStarted = false;
        invalidate();
    }

    public void setXLabels(List<String> labels) {
        this.xLabels = labels != null ? labels : new ArrayList<>();
        invalidate();
    }

    public void setShowAxis(boolean show) {
        this.showAxis = show;
        invalidate();
    }

    public void setYLabelCount(int count) {
        if (count >= 2) {
            this.yLabelCount = count;
            invalidate();
        }
    }

    public void setAnimationType(AnimationType type) {
        this.animationType = type;
        invalidate();
    }

    public void setAnimationDuration(long durationMillis) {
        this.animationDuration = durationMillis;
    }

    private void startAnimation() {
        animationStarted = true;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(animationDuration);

        if (animationType == AnimationType.WAVE) {
            animator.setInterpolator(new OvershootInterpolator());
        } else if (animationType == AnimationType.FADE_IN) {
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
        } else if (animationType == AnimationType.EASE_IN_OUT) {
            animator.setInterpolator(AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.fast_out_slow_in));
        }

        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (graphLines.isEmpty() || referenceData.size() < 2) return;

        if (shouldAnimateNextDraw && !animationStarted) {
            if (animationType != AnimationType.NONE) {
                startAnimation();
                shouldAnimateNextDraw = false;
                return;
            } else {
                animationProgress = 1f;
                animationStarted = true;
            }
        }

        float width = getWidth();
        float height = getHeight();

        float paddingLeft = 80f;
        float paddingRight = 40f;
        float paddingTop = 40f;
        float paddingBottom = 60f;

        float usableWidth = width - paddingLeft - paddingRight;
        float usableHeight = height - paddingTop - paddingBottom;

        float maxValue = Float.MIN_VALUE;
        for (GraphLine line : graphLines) {
            for (Float value : line.values) {
                if (value > maxValue) maxValue = value;
            }
        }
        float range = maxValue == 0 ? 1 : maxValue;
        float xStep = usableWidth / (referenceData.size() - 1);

        for (GraphLine line : graphLines) {
            List<Float> data = line.values;
            Path path = new Path();

            float prevX = 0, prevY = 0;

            for (int i = 0; i < data.size(); i++) {
                float x = paddingLeft + i * xStep;
                float yRatio = data.get(i) / range;
                float y = paddingTop + usableHeight * (1f - yRatio * animationProgress);

                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    if (line.smoothLine) {
                        float midX = (prevX + x) / 2f;
                        float midY = (prevY + y) / 2f;
                        path.quadTo(prevX, prevY, midX, midY);
                    } else {
                        path.lineTo(x, y);
                    }
                }

                prevX = x;
                prevY = y;
            }

            linePaint.setColor(line.color);
            linePaint.setStrokeWidth(line.lineWidth);
            canvas.drawPath(path, linePaint);

            if (line.showPoints) {
                pointPaint.setColor(line.color);
                for (int i = 0; i < data.size(); i++) {
                    float x = paddingLeft + i * xStep;
                    float yRatio = data.get(i) / range;
                    float y = paddingTop + usableHeight * (1f - yRatio * animationProgress);
                    canvas.drawCircle(x, y, line.pointRadius, pointPaint);
                }
            }
        }

        if (showAxis) {
            canvas.drawLine(paddingLeft, paddingTop + usableHeight, width - paddingRight, paddingTop + usableHeight, axisPaint);
            canvas.drawLine(paddingLeft, paddingTop, paddingLeft, paddingTop + usableHeight, axisPaint);

            for (int i = 0; i < referenceData.size(); i++) {
                float x = paddingLeft + i * xStep;
                if (i < xLabels.size()) {
                    canvas.drawText(xLabels.get(i), x, height - 20f, labelPaint);
                }
            }

            labelPaint.setTextAlign(Paint.Align.RIGHT);
            for (int i = 0; i < yLabelCount; i++) {
                float fraction = (float) i / (yLabelCount - 1);
                float value = maxValue * (1f - fraction);
                float y = paddingTop + usableHeight * fraction;
                canvas.drawText(String.format("%.0f", value), paddingLeft - 12f, y + labelPaint.getTextSize() / 2f, labelPaint);
            }
            labelPaint.setTextAlign(Paint.Align.CENTER);
        }

        if (selectedIndex >= 0 && selectedIndex < referenceData.size()) {
            float x = paddingLeft + selectedIndex * xStep;

            float boxWidth = 250f;
            float lineHeight = 60f;
            float padding = 20f;
            float boxHeight = padding + 36f + (graphLines.size() * lineHeight) + padding * 3;
            float y = paddingTop + 20f;
            float boxLeft = Math.max(10f, Math.min(x - boxWidth / 2f, width - boxWidth - 10f));
            float boxTop = y;
            float boxCenterX = boxLeft + boxWidth / 2f;

            Paint tooltipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            tooltipPaint.setColor(Color.WHITE);
            tooltipPaint.setShadowLayer(8f, 0, 4f, Color.argb(100, 0, 0, 0));

            Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            borderPaint.setColor(Color.LTGRAY);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(2f);

            RectF rect = new RectF(boxLeft, boxTop, boxLeft + boxWidth, boxTop + boxHeight);
            canvas.drawRoundRect(rect, 16f, 16f, tooltipPaint);
            canvas.drawRoundRect(rect, 16f, 16f, borderPaint);

            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(36f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setFakeBoldText(true);

            canvas.drawText(
                    selectedIndex < xLabels.size() ? xLabels.get(selectedIndex) : "",
                    boxCenterX,
                    boxTop + 50f + padding,
                    textPaint
            );

            for (int i = 0; i < graphLines.size(); i++) {
                GraphLine line = graphLines.get(i);
                if (selectedIndex < line.values.size()) {
                    float value = line.values.get(selectedIndex);
                    String valueText = (line.title != null ? line.title + " : " : "") + String.format("%.0f", value);

                    Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    valuePaint.setColor(line.color);
                    valuePaint.setTextSize(32f);
                    valuePaint.setTextAlign(Paint.Align.CENTER);
                    valuePaint.setFakeBoldText(true);

                    canvas.drawText(
                            valueText,
                            boxCenterX,
                            boxTop + 70f + (i * 60f) + lineHeight,
                            valuePaint
                    );
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !referenceData.isEmpty()) {
            float touchX = event.getX();

            float width = getWidth();
            float paddingLeft = 80f;
            float paddingRight = 40f;
            float usableWidth = width - paddingLeft - paddingRight;
            float xStep = usableWidth / (referenceData.size() - 1);

            for (int i = 0; i < referenceData.size(); i++) {
                float x = paddingLeft + i * xStep;

                if (Math.abs(touchX - x) < TOUCH_TOLERANCE_X) {
                    selectedIndex = i;
                    invalidate();

                    return true;
                }
            }

            selectedIndex = -1;
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
