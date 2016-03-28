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

        chartData.clear();
        //线1的数据集
        List<PointD> linePoint1 = new ArrayList<>();

        linePoint1.add(new PointD(1d, todayH));
        linePoint1.add(new PointD(2d, dH1));
        linePoint1.add(new PointD(3d, dH2));
        linePoint1.add(new PointD(4d, dH3));
        linePoint1.add(new PointD(5d, dH4));
        linePoint1.add(new PointD(6d, dH5));
        linePoint1.add(new PointD(7d, dH6));
        SplineData dataSeries1 = new SplineData("收缩压", linePoint1,
                Color.RED);

        dataSeries1.getLinePaint().setStrokeWidth(10);
        chartData.add(dataSeries1);

        //线2的数据集
        List<PointD> linePoint2 = new ArrayList<>();

        linePoint2.add(new PointD(1d, todayL));
        linePoint2.add(new PointD(2d, dL1));
        linePoint2.add(new PointD(3d, dL2));
        linePoint2.add(new PointD(4d, dL3));
        linePoint2.add(new PointD(5d, dL4));
        linePoint2.add(new PointD(6d, dL5));
        linePoint2.add(new PointD(7d, dL6));
        SplineData dataSeries2 = new SplineData("舒张压", linePoint2,
                Color.BLUE);
        chartData.add(dataSeries2);

        //批注
        List<AnchorDataPoint> mAnchorSet = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            AnchorDataPoint an = new AnchorDataPoint(0, i, XEnum.AnchorStyle.CAPRECT);
            an.setAnchor("" + linePoint1.get(i).y);
            an.setTextColor(Color.BLACK);
            mAnchorSet.add(an);

            AnchorDataPoint an2 = new AnchorDataPoint(1, i, XEnum.AnchorStyle.CAPRECT);
            an2.setAnchor("" + linePoint2.get(i).y);
            an2.setTextColor(Color.BLACK);
            mAnchorSet.add(an2);
        }
        chart.setAnchorDataPoint(mAnchorSet);

        refreshChart();
    }
}
