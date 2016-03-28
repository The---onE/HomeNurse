package com.xmx.homenurse.Measure.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;
import org.xclcharts.view.ChartView;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by The_onE on 2016/3/22.
 */
public class BaseChartView extends ChartView {
    SplineChart chart = new SplineChart();
    //分类轴标签集合
    private LinkedList<String> labels = new LinkedList<>();
    LinkedList<SplineData> chartData = new LinkedList<>();

    public BaseChartView(Context context) {
        super(context);
        initView();
    }

    public BaseChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }//Demo中bar chart所使用的默认偏移值。

    //偏移出来的空间用于显示tick,axistitle....
    protected int[] getBarLnDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 60); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom
        return ltrb;
    }

    protected int[] getPieDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 20); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 65); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 20); //bottom
        return ltrb;
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

            //显示十字交叉线
            chart.showDyLine();
            chart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);
            //扩大实际绘制宽度
            //chart.getPlotArea().extWidth(500.f);

            //封闭轴
            chart.setAxesClosed(true);

            //将线显示为直线，而不是平滑的
            //chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);
            chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

            //不使用精确计算，忽略Java计算误差,提高性能
            chart.disableHighPrecision();

            //仅能横向移动
            //chart.setPlotPanMode(XEnum.PanMode.FREE);
            chart.disablePanMode();

            chart.disableScale();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setLabels(int month, int day) {
        labels.clear();
        labels.add("");
        Date date = new Date();
        date.setMonth(month - 1);
        date.setDate(day - 6);
        for (int i = 0; i < 7; ++i) {
            labels.add("" + (date.getMonth() + 1) + "-" + date.getDate());
            date.setDate(date.getDate() + 1);
        }
        labels.add("");
        refreshChart();
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
