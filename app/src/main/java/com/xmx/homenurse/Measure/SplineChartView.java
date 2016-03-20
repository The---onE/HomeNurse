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
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
/**
 * @ClassName SplineChart01View
 * @Description  曲线图 的例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class SplineChartView extends DemoView {

    private String TAG = "SplineChart01View";
    private SplineChart chart = new SplineChart();
    //分类轴标签集合
    private LinkedList<String> labels = new LinkedList<>();
    private LinkedList<SplineData> chartData = new LinkedList<>();


    public SplineChartView(Context context) {
        super(context);
        initView();
    }

    public SplineChartView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public SplineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        chartRender();

        //綁定手势滑动事件
        this.bindTouch(this,chart);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h);
    }


    private void chartRender()
    {
        try {

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
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
            chart.getDataAxis().setAxisMax(45);
            chart.getDataAxis().setAxisMin(35);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(0.5);

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
            chart.getDataAxis().setHorizontalTickAlign(Align.CENTER);
            chart.getDataAxis().getTickLabelPaint().setTextAlign(Align.CENTER);

            //居中显示轴
            //plot.hideHorizontalLines();
            //plot.hideVerticalLines();
            //chart.setDataAxisLocation(XEnum.AxisLocation.VERTICAL_CENTER);
            //chart.setCategoryAxisLocation(XEnum.AxisLocation.HORIZONTAL_CENTER);


            //定义交叉点标签显示格式,特别备注,因曲线图的特殊性，所以返回格式为:  x值,y值
            //请自行分析定制
            chart.setDotLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    String label = "("+value+")";
                    return (label);
                }

            });
            //标题
            chart.setTitle("体温");
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

    public void setDataSet(double today, double d1, double d2,
                              double d3, double d4, double d5, double d6)
    {
        //线1的数据集
        List<PointD> linePoint1 = new ArrayList<>();

        linePoint1.add(new PointD(1d, today));
        linePoint1.add(new PointD(2d, d1));
        linePoint1.add(new PointD(3d, d2));
        linePoint1.add(new PointD(4d, d3));
        linePoint1.add(new PointD(5d, d4));
        linePoint1.add(new PointD(6d, d5));
        linePoint1.add(new PointD(7d, d6));
        SplineData dataSeries1 = new SplineData("体温",linePoint1,
                Color.rgb(54, 141, 238) );

        dataSeries1.getLinePaint().setStrokeWidth(10);

        chartData.add(dataSeries1);

        //批注
        List<AnchorDataPoint> mAnchorSet = new ArrayList<>();
        for (int i=0; i<7; ++i) {
            AnchorDataPoint an = new AnchorDataPoint(0,i,XEnum.AnchorStyle.CAPRECT);
            an.setAnchor("" + linePoint1.get(i).y);
            an.setTextColor(Color.BLACK);
            mAnchorSet.add(an);
        }
        chart.setAnchorDataPoint(mAnchorSet);

        refreshChart();
    }

    public void setLabels(int month, int day) {
        labels.add("");
        labels.add(""+month+"-"+(day-6));
        labels.add(""+month+"-"+(day-5));
        labels.add(""+month+"-"+(day-4));
        labels.add(""+month+"-"+(day-3));
        labels.add(""+month+"-"+(day-2));
        labels.add(""+month+"-"+(day-1));
        labels.add(""+month+"-"+day);
        labels.add("");
        refreshChart();
    }

    @Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}