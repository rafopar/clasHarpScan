/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.fitter;

import java.util.ArrayList;
import java.util.TreeMap;
import org.freehep.math.minuit.FCNBase;

/**
 *
 * @author gavalian
 */
public class HarpScanFunction implements FCNBase {
    double[] xdata = new double[0];
    double[] ydata = null;
    //TreeMap<String,Double> uparFit = new TreeMap<String,Double>();
    //TreeMap<String,Double> uparErr = new TreeMap<String,Double>();
    public ArrayList<Double> uparFit = new ArrayList<Double>();
    public ArrayList<Double> uparErr = new ArrayList<Double>();
    
    public HarpScanFunction(double[] x, double[] y){
        xdata = x;
        ydata = y;
    }
    
    @Override
    public double valueOf(double[] par) {
        double chi2 = 0.0;
        for(int loop = 0; loop < xdata.length; loop++){
            double x     = xdata[loop];
            double mean  = par[3];
            double diff  = x - mean;
            double sigma = par[4]; 
            double func = par[0] *(1 + par[1]*x + par[2]*Math.exp(-diff*diff/(2.0*sigma*sigma)));
            if(func>0){
                chi2 += (func-ydata[loop])*(func-ydata[loop])/func;
            }
        }
        //System.err.println("chi2 = " + chi2);
        return chi2;
    }
    
    
    public double getMean(){
        double meanSumm = 0;
        int    meanCount = 0;
        for(int loop = 0; loop < xdata.length; loop++){
            if(ydata[loop]>0){
                meanSumm += xdata[loop]*ydata[loop];
                meanCount += ydata[loop];
            }
        }
        if(meanCount==0) return 0;
        return meanSumm/meanCount;
    }
    
    public double getRMS(){
        double mean = this.getMean();
        double quadrSumm = 0.0;
        int    quadrCount = 0;
        for(int loop = 0; loop < xdata.length; loop++){
            if(ydata[loop]!=0){
                quadrSumm += ydata[loop]*(xdata[loop]-mean)*(xdata[loop]-mean);
                quadrCount+=ydata[loop];
            }
        }
        if(quadrCount==0) return 0.0;
        return Math.sqrt(quadrSumm/quadrCount);
    }
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        /*
        for(int loop = 0; loop < xdata.length; loop++){
            str.append(String.format("%12.5f  %12.5f\n",xdata[loop],ydata[loop]));
        }*/
        str.append("FUNC = ");
        for(int loop = 0; loop < uparFit.size(); loop++){
            str.append(String.format("%12.5f +/- %12.5f",uparFit.get(loop)
            ,uparErr.get(loop)));
        }
        return str.toString();
    }
}
