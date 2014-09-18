/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.func;

/**
 *
 * @author gavalian
 */
public class FunctionFactory {
    public static double[] getUniformAxis(int bins, double min, double max){
        double[] axis = new double[bins];
        double width = (max-min)/bins;
        for(int loop = 0; loop < bins; loop++){
            axis[loop] = 0.5*width + loop * width;
        }
        return axis;
    }
    
    public static double[] getGaussianValues(double[] axis, double amp,double mean, double sigma){
        double[] values = new double[axis.length];
        for(int loop = 0; loop < values.length; loop++){
            double diff = mean - axis[loop];
            values[loop] = 2.0 + amp*Math.exp(-diff*diff/(2.0*sigma*sigma));
        }
        return values;
    }
    
    public static double[] getGaussianValues(double[] axis, double mean, double sigma){
        return FunctionFactory.getGaussianValues(axis, 1.0, mean, sigma);
    }
    
}
