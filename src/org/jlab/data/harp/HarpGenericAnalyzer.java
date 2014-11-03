/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.harp;

import java.awt.List;
import java.util.ArrayList;
import java.util.TreeMap;
import org.jlab.data.fitter.DataFitter;
import org.jlab.data.func.F1D;
import org.jlab.data.graph.DataSetXY;
import org.jlab.data.graph.DataTable;
import org.jlab.data.graph.DataVector;
import org.jlab.data.utils.PeakFinder;

/**
 *
 * @author gavalian
 */
public class HarpGenericAnalyzer {
   
    private ArrayList<DataSetXY>  harpData = new ArrayList<DataSetXY>();
    private ArrayList<F1D>        harpFunc = new ArrayList<F1D>();
    
    private Double  graphCutSigmas  = 5.0;
    
    public HarpGenericAnalyzer(){        
    
    }
    
    public void init(DataTable table, int column){
        harpData.clear();
        DataSetXY dataFull = table.getDataSet(0, column);
        DataSetXY dataNorm = dataFull.getDataSetRieman(100);
        //System.err.println(dataNorm.toString());
        //dataNorm.show();
        PeakFinder peak = new PeakFinder();
        peak.doClustering(dataNorm);
        
        harpData.clear();
        java.util.List<DataVector> clusters = peak.getClusters();
        for(DataVector vec : clusters){
            if(vec.getSize()>5){
                double mean = vec.geatMean();
                double rms  = vec.getRMS();
                double xmin = mean - graphCutSigmas*rms;
                double xmax = mean + graphCutSigmas*rms;
                DataSetXY dataset = table.getDataSet(0, column, 0, xmin, xmax);
                harpData.add(dataset);
            }
        }
        
    }
    
    public void fitData(){
        harpFunc.clear();
        for(int loop = 0; loop < harpData.size(); loop++){
            F1D func = new F1D("gaus+p1",
                    harpData.get(loop).getDataX().getMin(),
                    harpData.get(loop).getDataX().getMax()
            );
            
            func.parameter(0).set(harpData.get(loop).getDataY().getMax(), 
                    0.0, harpData.get(loop).getDataY().getMax()*10.0);
            func.parameter(1).setValue(harpData.get(loop).getDataX().geatMean());
            func.parameter(2).set(harpData.get(loop).getDataX().getRMS()*0.1,
                    0.0,harpData.get(loop).getDataX().getRMS()*3.0);
            func.parameter(3).set(0.0, -100, 100);
            func.parameter(4).set(0.0, -100, 100);
            func.show();
            harpFunc.add(func);
        }
        
        
        for(int loop = 0; loop < harpData.size(); loop++){
            DataFitter.fit(harpData.get(loop), harpFunc.get(loop));            
        }
        
        for(int loop = 0; loop < harpData.size(); loop++){
            harpFunc.get(loop).show();
        }
        
    }
    
    public ArrayList<F1D>  getHarpFuncs(){
        return harpFunc;
    }
    
    public ArrayList<DataSetXY>  getHarpData(){
        return harpData;
    }
    
    
    public String[] getLegend(int index){
        String[] legend = new String[3];
 
        legend[0] = String.format("%-12s %8.5f", "mean", 
                harpFunc.get(index).parameter(1).value());
        legend[1] = String.format("%-12s %8.5f", "sigma", 
                harpFunc.get(index).parameter(2).value());
        legend[2] = String.format("%-12s %8.5f", "chi2",
                harpFunc.get(index).getChiSquare(harpData.get(index))/
                harpFunc.get(index).getNDF(harpData.get(index))
                );
        return legend;
    }
}
