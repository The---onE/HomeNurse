package com.xmx.homenurse.Measure.ChartView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @ClassName SplineChart01View
 * @Description 曲线图 的例子
 */
public class BloodPressureChartView extends BaseChartView {

    public BloodPressureChartView(Context context) {
        super(context);
    }

    public BloodPressureChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BloodPressureChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setDataSet(double todayH, double dH1, double dH2,
                           double dH3, double dH4, double dH5, double dH6,
                           double todayL, double dL1, double dL2,
                           double dL3, double dL4, double dL5, double dL6) {
        //标题
        chart.setTitle("血压");
        chart.addSubtitle("七日曲线");

        //坐标系
        //数据轴最大值
        chart.getDataAxis().setAxisMax(200);
        chart.getDataAxis().setAxisMin(50);
        //数据轴刻度间隔
        chart.getDataAxis().setAxisSteps(5);


        double[] x = {1d, 2d, 3d, 4d, 5d, 6d, 7d};
        double[] y = {dH6, dH5, dH4, dH3, dH2, dH1, todayH};
        double[] y2 = {dL6, dL5, dL4, dL3, dL2, dL1, todayL};
        //线1的数据集
        List<PointD> linePoint1 = new ArrayList<>();
        //线2的数据集
        List<PointD> linePoint2 = new ArrayList<>();
        //批注
        List<AnchorDataPoint> mAnchorSet = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < 7; ++i) {
            if (y[i] > 0 && y2[i] > 0) {
                linePoint1.add(new PointD(x[i], y[i]));
                AnchorDataPoint an = new AnchorDataPoint(0, count, XEnum.AnchorStyle.CAPRECT);
                an.setAnchor("" + y[i]);
                an.setTextColor(Color.BLACK);
                mAnchorSet.add(an);

                linePoint2.add(new PointD(x[i], y2[i]));
                AnchorDataPoint an2 = new AnchorDataPoint(1, count, XEnum.AnchorStyle.CAPRECT);
                an2.setAnchor("" + y2[i]);
                an2.setTextColor(Color.BLACK);
                mAnchorSet.add(an2);

                count++;
            }
        }
        chartData.clear();

        SplineData dataSeries1 = new SplineData("收缩压", linePoint1,
                Color.RED);
        dataSeries1.getLinePaint().setStrokeWidth(10);
        chartData.add(dataSeries1);

        SplineData dataSeries2 = new SplineData("舒张压", linePoint2,
                Color.BLUE);
        dataSeries2.getLinePaint().setStrokeWidth(10);
        chartData.add(dataSeries2);

        chart.setAnchorDataPoint(mAnchorSet);

        refreshChart();
    }
}
