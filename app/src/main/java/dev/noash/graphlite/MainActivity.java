package dev.noash.graphlite;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

import dev.noash.graphlitelib.BarChartView;
import dev.noash.graphlitelib.GraphLine;
import dev.noash.graphlitelib.LineChartView;

public class MainActivity extends AppCompatActivity {
    LineChartView graph1;
    BarChartView graph2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }

    private void findViews() {
        graph1 = findViewById(R.id.lineChartView);
        graph2 = findViewById(R.id.barChartView);
    }

    private void initViews() {
        setStatusBar(getWindow(), this);
        initGraph1();
        initGraph2();
    }

    private void initGraph1() {
        List<Float> avgNoise = Arrays.asList(4000f, 3000f, 5000f, 4500f, 6000f, 5500f);
        List<Float> maxNoise = Arrays.asList(3200f, 4100f, 3800f, 4300f, 5200f, 5800f);
        List<Float> list = Arrays.asList(2500f, 2200f, 3800f, 3500f, 4500f, 5000f);

        //init design - avgNoise
        GraphLine avgLine = new GraphLine(avgNoise)
                .setColor(Color.parseColor("#3B82F6"))
                .setLineWidth(12f)
                .setTitle("Value")
                .setShowPoints(true)
                .setPointRadius(18f);

        //init design - maxNoise
        GraphLine maxLine = new GraphLine(maxNoise)
                .setColor(Color.parseColor("#F97316"))
                .setLineWidth(12f)
                .setTitle("Growth")
                .setShowPoints(true)
                .setPointRadius(18f);

        //init design - listNoise
        GraphLine listLine = new GraphLine(list)
                .setColor(Color.parseColor("#2CA02C"))
                .setLineWidth(12f)
                .setTitle("Line")
                .setShowPoints(true)
                .setPointRadius(18f);

        graph1.setGraphLines(Arrays.asList(avgLine, maxLine));
        graph1.setXLabels(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        graph1.setShowAxis(true);
        graph1.setYLabelCount(5);
        graph1.setAnimationType(LineChartView.AnimationType.NONE);
        graph1.setAnimationDuration(3000);
    }

    private void initGraph2() {
        List<Float> values = Arrays.asList(8500f, 6200f, 3100f, 1200f, 600f);
        List<String> labels = Arrays.asList("Mobile", "Desktop", "Tablet", "Smart TV", "Wearable");

        graph2.setBarColors(Arrays.asList(
                Color.parseColor("#3B82F6"),  // Mobile
                Color.parseColor("#60A5FA"),  // Desktop
                Color.parseColor("#A78BFA"),  // Tablet
                Color.parseColor("#F472B6"),  // Smart TV
                Color.parseColor("#FBBF24")   // Wearable
        ));

        graph2.setValues(values);
        graph2.setLabels(labels);
        graph2.setBarWidthDp(50);
        graph2.setBarWidth(dpToPx(50));
        graph2.setBarSpacing(dpToPx(20));
        graph2.adjustWidthToContent();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }


    public static void setStatusBar(Window window, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.statusBar));
        }
    }
}
