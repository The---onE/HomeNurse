package com.xmx.homenurse.Measure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.renderer.plot.PlotGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @ClassName SplineChart01View
 * @Description 曲线图 的例子
 */
public class BloodPressureChartView extends DemoView {

    private String TAG = "SplineChart01View";
    private SplineChart chart = new SplineChart();
    //分类轴标签集合
    private LinkedList<String> labels = new LinkedList<>();
    private LinkedList<SplineData> chartData = new LinkedList<>();


    public BloodPressureChartView(Context context) {
        super(context);
        initView();
    }

    public BloodPressureChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BloodPressureChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chartRender();

        //綁定手势滑动事件
        this.bindTouch(this, chart);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
    }


    private void chartRender() {
        try {

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //平移时收缩下
            //float margin = DensityUtil.dip2px(getContext(), 20);
            //chart.setXTickMarksOffsetMargin(margin);

            //显示边框
            chart.hideBorder();

            //数据源
            chart.setCategories(labels);
            chart.setDataSource(chartData);

            //坐标系
            //数据轴最大值
            chart.getDataAxis().setAxisMax(200);
            chart.getDataAxis().setAxisMin(50);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(5);

            //标签轴最大值
            chart.setCategoryAxisMax(8);
            //标签轴最小值
            chart.setCategoryAxisMin(0);
            //数据轴刻度间隔

            //设置图的背景色
            //chart.setBackgroupColor(true,Color.BLACK);
            //设置绘图区的背景色
            //chart.getPlotArea().setBackgroupColor(true, Color.WHITE);

            //背景网格
            PlotGrid plot = chart.getPlotGrid();
            plot.showHorizontalLines();
            plot.showVerticalLines();
            plot.getHorizontalLinePaint().setStrokeWidth(3);
            plot.getHorizontalLinePaint().setColor(Color.rgb(127, 204, 204));
            plot.setHorizontalLineStyle(XEnum.LineStyle.DOT);


            //把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
            //chart.getDataAxis().getAxisPaint().setStrokeWidth(
            //		plot.getHorizontalLinePaint().getStrokeWidth());
            //chart.getCategoryAxis().getAxisPaint().setStrokeWidth(
            //		plot.getHorizontalLinePaint().getStrokeWidth());

            chart.getDataAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));
            chart.getCategoryAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));

            chart.getDataAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));
            chart.getCategoryAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));

            //居中
            chart.getDataAxis().setHorizontalTickAlign(Paint.Align.CENTER);
            chart.getDataAxis().getTickLabelPaint().setTextAlign(Paint.Align.CENTER);

            //居中显示轴
            //plot.hideHorizontalLines();
            //plot.hideVerticalLines();
            //chart.setDataAxisLocation(XEnum.AxisLocation.VERTICAL_CENTER);
            //chart.setCategoryAxisLocation(XEnum.AxisLocation.HORIZONTAL_CENTER);


            //定义交叉点标签显示格式,特别备注,因曲线图的特殊性，所以返回格式为:  x值,y值
            //请自行分析定制
            chart.setDotLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    String label = "(" + value + ")";
                    return (label);
                }

            });
            //标题
            chart.setTitle("血压");
            chart.addSubtitle("七日曲线");

            //显示十字交叉线
            chart.showDyLine();
            chart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);
            //扩大实际绘制宽度
            //chart.getPlotArea().extWidth(500.f);

            //封闭轴
            chart.setAxesClosed(true);

            //将线显示为直线，而不是平滑的
            chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);

            //不使用精确计算，忽略Java计算误差,提高性能
            chart.disableHighPrecision();

            //仅能横向移动
            //chart.setPlotPanMode(XEnum.PanMode.FREE);
            chart.disablePanMode();

            chart.disableScale();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    public void setDataSet(double todayH, double dH1, double dH2,
                           double dH3, double dH4, double dH5, double dH6,
                           double todayL, double dL1, double dL2,
                           double dL3, double dL4, double dL5, double dL6) {
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

    public void setLabels(int month, int day) {
        labels.clear();
        labels.add("");
        labels.add("" + month + "-" + (day - 6));
        labels.add("" + month + "-" + (day - 5));
        labels.add("" + month + "-" + (day - 4));
        labels.add("" + month + "-" + (day - 3));
        labels.add("" + month + "-" + (day - 2));
        labels.add("" + month + "-" + (day - 1));
        labels.add("" + month + "-" + day);
        labels.add("");
        refreshChart();
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
