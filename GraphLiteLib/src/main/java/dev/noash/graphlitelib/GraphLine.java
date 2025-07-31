package dev.noash.graphlitelib;

import android.graphics.Color;

import java.util.List;

public class GraphLine {
    public List<Float> values;
    public int color = Color.BLACK;
    public boolean showPoints = false;
    public boolean smoothLine = false;
    public float lineWidth = 4f;
    public float pointRadius = 6f;
    public String title = null;

    public GraphLine(List<Float> values) {
        this.values = values;
    }

    public GraphLine setColor(int color) {
        this.color = color;
        return this;
    }
    public GraphLine setTitle(String title) {
        this.title = title;
        return this;
    }

    public GraphLine setShowPoints(boolean showPoints) {
        this.showPoints = showPoints;
        return this;
    }

    public GraphLine setSmoothLine(boolean smoothLine) {
        this.smoothLine = smoothLine;
        return this;
    }

    public GraphLine setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public GraphLine setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
        return this;
    }
}
