package dev.noash.graphlite;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.noash.graphlitelib.GraphLine;
import dev.noash.graphlitelib.GraphView;

public class MainActivity extends AppCompatActivity {
    GraphView graph1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }

    private void findViews() {
        graph1 = findViewById(R.id.GV_graph1);
    }

    private void initViews() {
        setStatusBar(getWindow(), this);
        initGraph1();
    }

    private void initGraph1() {
        List<Float> avgNoise = Arrays.asList(4000f, 3000f, 5000f, 4500f, 6000f, 5500f);
        List<Float> maxNoise = Arrays.asList(3200f, 4100f, 3800f, 4300f, 5200f, 5800f);
        List<Float> avg1Noise = Arrays.asList(3000f, 3000f, 4000f, 4200f, 5800f, 5000f);

        GraphLine avgLine = new GraphLine(avgNoise)
                .setColor(Color.parseColor("#3B82F6"))
                .setLineWidth(12f)
                .setTitle("Value")
                .setShowPoints(true)
                .setPointRadius(18f);

        GraphLine maxLine = new GraphLine(maxNoise)
                .setColor(Color.parseColor("#F97316"))
                .setLineWidth(12f)
                .setTitle("Growth")
                .setShowPoints(true)
                .setPointRadius(18f);

        GraphLine avg1Line = new GraphLine(avg1Noise)
                .setColor(Color.parseColor("#000000"))
                .setLineWidth(12f)
                .setShowPoints(true)
                .setTitle("avg1")
                .setPointRadius(18f);

        graph1.setGraphLines(Arrays.asList(avgLine, maxLine));

        graph1.setXLabels(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        graph1.setShowAxis(true);
        graph1.setYLabelCount(5);

        graph1.setGradientColors(new int[]{
                Color.parseColor("#2563EB"),
                Color.parseColor("#3B82F6"),
                Color.parseColor("#A5D8FF"),
                Color.TRANSPARENT
        });
     //   graph1.setShowGradient(true);
        graph1.setAnimationType(GraphView.AnimationType.NONE);
        graph1.setAnimationDuration(2000);
    }

    public static void setStatusBar(Window window, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.statusBar));
        }
    }
}
