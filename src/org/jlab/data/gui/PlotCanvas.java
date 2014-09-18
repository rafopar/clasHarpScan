package org.jlab.data.gui;


import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.XChartPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jlab.data.func.FunctionFactory;
import org.jlab.data.graph.DataPoints;
import org.jlab.data.histogram.H1D;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gavalian
 */
public class PlotCanvas extends JFrame {
    private Font axisTitleFont = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private Font axisLabelFont = new Font(Font.DIALOG, Font.BOLD, 18);
    private ArrayList<Chart>  canvasCharts = new ArrayList<Chart>();
    private Integer  nGraphColumns = 1;
    private Integer  nGraphRows         = 1;
    private Integer  cellXsize    = 100;
    private Integer  cellYsize    = 100;
    
    public PlotCanvas(int xsize, int ysize, int columns, int rows) {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(xsize,ysize));
        
        nGraphColumns = columns;
        nGraphRows    = rows;
        
        cellXsize = (int) xsize/columns;
        cellYsize = (int) ysize/rows;
        
        GridLayout experimentLayout = new GridLayout(0,columns);
        
        this.setLayout(experimentLayout);
        //chart.getStyleManager().
        this.initCharts(columns*rows);
        this.pack();
    }
    
    
    public PlotCanvas() {
        super();
        this.setSize(900, 700);
        this.setPreferredSize(new Dimension(600,600));
        GridLayout experimentLayout = new GridLayout(0,2);
        this.setLayout(experimentLayout);
        this.pack();
    }
    
    private void initCharts(int count){
        for(int loop = 0; loop < count; loop++){
            Chart chart = new Chart(cellXsize, cellYsize, ChartTheme.Matlab);
            chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
            chart.getStyleManager().setAxisTickLabelsFont(this.axisLabelFont);
            chart.getStyleManager().setLegendFont(this.axisLabelFont);
            chart.getStyleManager().setAxisTitleFont(axisTitleFont); 
            double[] x = new double[]{0.0,1.0};
            double[] y = new double[]{0.0,1.0};
            Series series = chart.addSeries("empty", x, y);
            series.setLineStyle(SeriesLineStyle.NONE);
            series.setMarker(SeriesMarker.NONE);
            //chart.getStyleManager().setYAxisLogarithmic(true);
            canvasCharts.add(chart);
            JPanel pnlChart = new XChartPanel(chart);
            this.add(pnlChart);
        }
    }
    
    public void exportPDF(String filename){
        try {
            
        }
        catch(Exception e)  {
            //
        }
    }
    
    public void saveAll(String filename){
        for(int loop = 0; loop < canvasCharts.size(); loop++){
            String padname = filename + "_pad_" + loop;
            this.save(padname, loop);
        }
    }
    
    public void save(String filename, int pad){
        try {
            VectorGraphicsEncoder.saveVectorGraphic(canvasCharts.get(pad), filename, VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
        } catch (IOException ex) {
            Logger.getLogger(PlotCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void save(String filename){
        try {
            VectorGraphicsEncoder.saveVectorGraphic(canvasCharts.get(0), filename, VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
        } catch (IOException ex) {
            Logger.getLogger(PlotCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw(H1D h, int pad, String options , Color col){
        if(options.contains("same")==true){
            
            this.addHistogram(pad, h.name(),"","",
                    h.getAxis().getBinCenters(), h.getData(),col);
        } else {
            this.addHistogram(pad, h.name(),h.getXTitle(),h.getYTitle(),
                h.getAxis().getBinCenters(), h.getData(),col);
        }
    }
    
    public void draw(H1D h, int pad, String options ){
        if(options.contains("same")==true){
            this.addHistogram(pad, h.name(),"","",
                    h.getAxis().getBinCenters(), h.getData(),Color.blue);
        } else {
            this.addHistogram(pad, h.name(),h.getXTitle(),h.getYTitle(),
                h.getAxis().getBinCenters(), h.getData(),Color.blue);
        }
    }
    
    public void draw(DataPoints data,int pad, String options){
        if(options.contains("P")){
            this.addPointsErrors(pad,data.getName(), data.getEXVector(), 
                    data.getYVector(), data.getEYVector(), options);
        }
    }
    
    public void addLine(int gid, String name, double[] xdata, double[] ydata, String option){
        if(option.contains("same")==false){
            canvasCharts.get(gid).getSeriesMap().clear();
        }
        Series series = canvasCharts.get(gid).addSeries(name, xdata, ydata);
        series.setMarker(SeriesMarker.NONE);
    }
    
    public void addHistogram(int gid, String name, String xtitle, String ytitle,double[] xdata, double[] ydata,
            Color col){
        canvasCharts.get(gid).getSeriesMap().clear();
        Chart chart = canvasCharts.get(gid);
        //chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).build();
        //Series series = chart.addSeries("Histogram", xdata, ydata);        
        chart.getStyleManager().setChartType(StyleManager.ChartType.Bar);
        Series series = chart.addSeries(name, xdata, ydata);
        
        chart.getStyleManager().setBarWidthPercentage(1.0);
        chart.getStyleManager().setBarsOverlapped(true);
        if(xtitle.length()>1){
            chart.setXAxisTitle(xtitle);
        }
        if(ytitle.length()>1){
            chart.setXAxisTitle(ytitle);
        }
        series.setLineColor(col);
        //series.setLineStyle(SeriesLineStyle.DASH_DASH);
        //chart.getStyleManager();
    }
    
    public void addPoints(int gid, double[] xdata, double[] ydata, String option){
        if(option.contains("same")==false){
            canvasCharts.get(gid).getSeriesMap().clear();
        }
        Series series = canvasCharts.get(gid).addSeries("Gaussian P", xdata, ydata);        
        series.setMarker(SeriesMarker.CIRCLE);
        series.setLineStyle(SeriesLineStyle.NONE);
    }
    
    public void addPointsErrors(int gid, String name, double[] xdata, double[] ydata, 
            double[] yerr,String option){
        if(option.contains("same")==false){
            canvasCharts.get(gid).getSeriesMap().clear();
        }
        Series series = canvasCharts.get(gid).addSeries(name, xdata, ydata,yerr);
        series.setMarker(SeriesMarker.CIRCLE);
        series.setLineStyle(SeriesLineStyle.NONE);
    }
    
    public void setOptLegend(boolean flag){
        for(Chart item : canvasCharts){
            item.getStyleManager().setLegendVisible(flag);
        }
    }
    
    public void setLogScaleY(int index,boolean flag){
        canvasCharts.get(index).getStyleManager().setYAxisLogarithmic(flag);
    }
    
    public void setLegendShow(boolean flag, int pad){
        canvasCharts.get(pad).getStyleManager().setLegendVisible(false);
    }
    
    public void addChart(double[] xdata, double[] ydata){
        Chart chart = new Chart(300, 300, ChartTheme.Matlab);
        chart.setXAxisTitle("Momentum [GeV]");
        chart.setYAxisTitle("Resolution [GeV]");
        
        Series series = chart.addSeries("Gaussian", xdata, ydata);        
        series.setMarker(SeriesMarker.NONE);
        
        chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
        chart.getStyleManager().setAxisTickLabelsFont(this.axisLabelFont);
        chart.getStyleManager().setLegendFont(this.axisLabelFont);
        chart.getStyleManager().setAxisTitleFont(axisTitleFont); 
        chart.getStyleManager().setYAxisLogarithmic(true);
        JPanel pnlChart = new XChartPanel(chart);
        this.add(pnlChart);
    }
    
    public void addGaussFunction(){
        double[] xdata = FunctionFactory.getUniformAxis(100, 0.0,5.0);
        double[] ydata = FunctionFactory.getGaussianValues(xdata, 2.5, 0.5);
        this.addChart(xdata, ydata);
    }
    
    
    
    public static void main(String[] args){
        PlotCanvas frame = new PlotCanvas(800,800,2,3);
        //for(int loop = 0 ; loop < 3 ; loop++)
        //    frame.addGaussFunction();
        double[] xdata = FunctionFactory.getUniformAxis(80, 0.0,5.0);
        double[] ydata = FunctionFactory.getGaussianValues(xdata, 2.5, 0.5);
        frame.addLine(0, "x1",xdata, ydata,"");
        //frame.addPointsErrors(0, xdata, ydata,ydata,"same");
        
        double[] xdataL = FunctionFactory.getUniformAxis(10000, 0.0,5.0);
        double[] ydataL = FunctionFactory.getGaussianValues(xdataL, 2.5, 0.5);
        frame.addLine(1, "x2",xdataL, ydataL,"");
        //frame.addHistogram(2, xdata, ydata);
        frame.save("myfirstFile.pdf");
        frame.setVisible(true);
    }   
    
}
