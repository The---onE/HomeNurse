package com.xmx.homenurse.Measure.ChartView;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @ClassName SplineChart01View
 * @Description 曲线图 的例子
 */
public class TemperatureChartView extends BaseChartView {

    public TemperatureChartView(Context context) {
        super(context);
    }

    public TemperatureChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemperatureChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDataSet(double today, double d1, double d2,
                           double d3, double d4, double d5, double d6) {
        //标题
        chart.setTitle("体温");
        chart.addSubtitle("七日曲线");

        //坐标系
        //数据轴最大值
        chart.getDataAxis().setAxisMax(45);
        chart.getDataAxis().setAxisMin(35);
        //数据轴刻度间隔
        chart.getDataAxis().setAxisSteps(0.5);

        //线1的数据集
        List<PointD> linePoint1 = new ArrayList<>();

        double[] x = {1d, 2d, 3d, 4d, 5d, 6d, 7d};
        double[] y = {d6, d5, d4, d3, d2, d1, today};
        //批注
        List<AnchorDataPoint> mAnchorSet = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < 7; ++i) {
            if (y[i] > 0) {
                linePoint1.add(new PointD(x[i], y[i]));
                AnchorDataPoint an = new AnchorDataPoint(0, count, XEnum.AnchorStyle.CAPRECT);
                an.setAnchor("" + y[i]);
                an.setTextColor(Color.BLACK);
                mAnchorSet.add(an);
                count++;
            }
        }
        SplineData dataSeries1 = new SplineData("体温", linePoint1,
                Color.rgb(54, 141, 238));
        dataSeries1.getLinePaint().setStrokeWidth(10);
        chartData.clear();
        chartData.add(dataSeries1);
        chart.setAnchorDataPoint(mAnchorSet);

        refreshChart();
    }
}