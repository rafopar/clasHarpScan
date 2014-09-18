/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.harp;

import java.util.ArrayList;
import java.util.TreeMap;
import org.jlab.data.fitter.HarpFunctionFitter;
import org.jlab.data.fitter.HarpScanFunction;
import org.jlab.data.graph.DataTable;

/**
 *
 * @author gavalian
 */
public class Harp2WireAnalyzer {
    private double[]  graphLimits = null;
    public ArrayList<HarpScanFunction>  functions = new ArrayList<HarpScanFunction>();
    public TreeMap<String,Double> resultsMap = new TreeMap<String,Double>();
    
    public Harp2WireAnalyzer(DataTable table, int column, double[] limits){
        graphLimits = limits;
        for(int loop = 0; loop < 2; loop++){
            double[] xdata = table.getColumnAsDouble(0, 0, limits[loop*2],limits[loop*2+1]);
            double[] ydata = table.getColumnAsDouble(column, 0, limits[loop*2],limits[loop*2+1]);
            HarpScanFunction func = new HarpScanFunction(xdata,ydata);
            functions.add(func);
        }
    }
    
    public void fit(){
        HarpFunctionFitter fitter = new HarpFunctionFitter();
        for(HarpScanFunction func : functions){
            fitter.fit(func);
        }
    }
    
    public void analyze(){
                
        double scX    = functions.get(0).uparFit.get(3);
        double scXErr = functions.get(0).uparErr.get(3);
        
        double scY     = functions.get(1).uparFit.get(3);
        double scYErr  = functions.get(1).uparErr.get(3);

        double scXsig     = functions.get(0).uparFit.get(4);
        double scXsigErr  = functions.get(0).uparErr.get(4);
        
        double scYsig     = functions.get(1).uparFit.get(4);
        double scYsigErr  = functions.get(1).uparErr.get(4);
        //Harp3ScanTranslator trans = new Harp3ScanTranslator(sc45,scX,scY);
        //resultsMap.clear();
        
        //resultsMap.put("xpos", scX);
        //resultsMap.put("ypos", scY);
    }
    
    public void show(){
        System.err.println("Harp Scan results");
        int loop = 0;
        for(HarpScanFunction func : functions){
            System.err.println("FUNC " + loop + "Mean =  " + func.uparFit.get(3) + " Sigma = "
            +   func.uparFit.get(4) );
            loop++;
        }        
    }
}