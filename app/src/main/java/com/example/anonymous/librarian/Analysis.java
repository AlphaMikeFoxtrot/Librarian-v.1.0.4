package com.example.anonymous.librarian;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;

public class Analysis extends AppCompatActivity {

    XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        plot = findViewById(R.id.plot);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(15);
        list.add(17);
        list.add(5);

        XYSeries series = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "books", 0, 21, 45, 78, 8, 65, 15, 21, 458, 45, 15);
        XYSeries series1 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "toys", 0, 4);

        plot.setPlotMargins(0, 0, 0, 0);
        plot.setPlotPadding(0, 0, 0, 0);
        plot.setPadding(10, 10, 10, 10);

        plot.setRangeBoundaries(0, 80, BoundaryMode.AUTO);
        plot.setRangeStepValue(10);

        plot.addSeries(series, new LineAndPointFormatter(Color.RED, Color.RED, null, new PointLabelFormatter(Color.RED)));
        plot.addSeries(series1, new LineAndPointFormatter(Color.GREEN, Color.GREEN, null, new PointLabelFormatter(Color.GREEN)));
    }
}
