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
public class PulesChartView extends BaseChartView {

    public PulesChartView(Context context) {
        super(context);
    }

    public PulesChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PulesChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDataSet(double today, double d1, double d2,
                           double d3, double d4, double d5, double d6) {
        //标题
        chart.setTitle("脉搏");
        chart.addSubtitle("七日曲线");

        //坐标系
        //数据轴最大值
        chart.getDataAxis().setAxisMax(140);
        chart.getDataAxis().setAxisMin(50);
        //数据轴刻度间隔
        chart.getDataAxis().setAxisSteps(5);

        //线1的数据集
        List<PointD> linePoint1 = new ArrayList<>();

        linePoint1.add(new PointD(1d, today));
        linePoint1.add(new PointD(2d, d1));
        linePoint1.add(new PointD(3d, d2));
        linePoint1.add(new PointD(4d, d3));
        linePoint1.add(new PointD(5d, d4));
        linePoint1.add(new PointD(6d, d5));
        linePoint1.add(new PointD(7d, d6));
        SplineData dataSeries1 = new SplineData("脉搏", linePoint1,
                Color.rgb(54, 141, 238));

        dataSeries1.getLinePaint().setStrokeWidth(10);

        chartData.add(dataSeries1);

        //批注
        List<AnchorDataPoint> mAnchorSet = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            AnchorDataPoint an = new AnchorDataPoint(0, i, XEnum.AnchorStyle.CAPRECT);
            an.setAnchor("" + linePoint1.get(i).y);
            an.setTextColor(Color.BLACK);
            mAnchorSet.add(an);
        }
        chart.setAnchorDataPoint(mAnchorSet);

        refreshChart();
    }
}