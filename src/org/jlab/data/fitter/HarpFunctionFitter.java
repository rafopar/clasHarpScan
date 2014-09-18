/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.fitter;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.freehep.math.minuit.FCNBase;
import org.freehep.math.minuit.FunctionMinimum;
import org.freehep.math.minuit.MnMigrad;
import org.freehep.math.minuit.MnUserParameters;

/**
 *
 * @author gavalian
 */
public class HarpFunctionFitter {
    
    public HarpFunctionFitter(){
        
    }
    
    public void fit(FCNBase func){
        
        HarpScanFunction harp = (HarpScanFunction) func;
        MnUserParameters upar = new MnUserParameters();
         upar.add("p0",     1., 0.1);
         upar.add("p1",     1., 0.1);
         upar.add("amp",  100., 0.1);
         upar.add("mean", harp.getMean() , 0.1);
         upar.add("sigma",  harp.getRMS(), 0.1);
         
         MnMigrad migrad = new MnMigrad(func, upar);
         FunctionMinimum min = migrad.minimize();
        
         //System.out.println("minimum: "+min);
         MnUserParameters userpar = min.userParameters();
         System.err.println("MEAN = " + userpar.toString());
         
         
         harp.uparFit.clear();
         harp.uparErr.clear();
         for(int loop = 0; loop < 5; loop++){
             harp.uparFit.add(userpar.value(loop));
             harp.uparErr.add(userpar.error(loop));
         }
    }
}
