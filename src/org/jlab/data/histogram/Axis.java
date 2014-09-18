package org.jlab.data.histogram;

import java.io.Serializable;

/**
 * Defines the attributes for creating a basic axis template with
 * a number of bins, a minimum value, and a maximum value
 * 
 * @author Erin Kirby
 * @version 062514
 */
public class Axis implements Serializable {
	
	public double[] axisMargins;
	private int numBins;
	private double minVal;
	private double maxVal;
	
	/**
	 * Creates a default axis with 1 bin, a minimum value of 0, and maximum
	 * value of 1.0.
	 */
	public Axis() {
		set(1, 0.0, 1.0);
	}
	
	/**
	 * Creates an axis using the desired limits for the bins
	 * 
	 * @param limits 	an array of the upper and lower limits of each bin
	 */
	public Axis(double[] limits) {
    	this.set(limits);
	}
	
	/**
	 * Creates an axis using the provided number of bins, minimum value, and maximum value
	 * 
	 * @param bins	the number of bins to have on the axis
	 * @param min	the minimum value for the axis
	 * @param max	the maximum value for the axis
	 */
	public Axis(int bins, double min, double max) {
		this.set(bins, min, max);
	}
    
	/**
	 * Sets the specified limits to the axis
	 * 
	 * @param limits	the desired limits for the bins on the axis
	 */
	public final void set(double[] limits) {
		axisMargins = new double[limits.length];
        for (int i=0; i<limits.length; i++) {
        	axisMargins[i] = limits[i];
        }
        
        numBins = axisMargins.length - 1;
        minVal = axisMargins[0];
        maxVal = axisMargins[numBins];
	}
	
	/**
	 * Sets the number of bins, the min, and the max to the axis
	 * as well as calculates the bin widths.
	 * 
	 * @param bins		the desired number of bins
	 * @param min		the minimum value
	 * @param max		the maximum value
	 */
	public final void set(int bins, double min, double max) {
		numBins = bins;

		if (min <= max) {
			minVal = min;
			maxVal = max;
		}

		else {
			minVal = max;
			maxVal = min;
		}
		double binWidth = (maxVal - minVal)/bins;
		axisMargins = new double[bins + 1];
		for (int i=0; i<=bins; i++) {
                    axisMargins[i] = minVal + i*binWidth;
		}
	}
	/**
	 * A getter for the array of bin limits
	 * 
	 * @return		a double array of the bin limits
	 */
    public double[] getLimits() {
    	return axisMargins;
    }
    
    /**
     * A getter for the number of bins, returned in array indexing format 
     * (i.e. 1 bin would return 0, 2 would return 1, etc.)
     * 
     * @return		the number of bins in array index format
     */
    public int getNBins() {
    	return numBins;
    }
    
    /**
     * A getter for the maximum value of the axis
     * 
     * @return		the maximum value of the axis
     */
    public double max() {
    	return maxVal;
    }
    
    public double[] getBinCenters(){
        double[] centers = new double[this.getNBins()];
        for(int loop = 0; loop < this.getNBins(); loop++)
            centers[loop] = this.getBinCenter(loop);
        return centers;
    }
    /**
     * A getter for the minimum value of the axis
     * 
     * @return	the minimum value of the axis
     */
    public double min() {
    	return minVal;
    }
    
    /**
     * Calculates and returns the median value of the bin number entered in the parameter
     * in array indexing format (i.e. for the 1st bin enter 0, the 2nd bin enter 1, etc.)
     * 
     * @param bin		which bin to get the median value of, entered in array indexing format
     * @return			the median value of the specified bin
     */
    public double getBinCenter(int bin) {
    	if (bin >= 0 && bin<axisMargins.length - 1) {
    		return axisMargins[bin] + (axisMargins[bin+1]-axisMargins[bin])*0.5;
    	}
    	return 0.0;
    }
    
    /**
     * Calculates and returns the width of the bin specified in the parameter
     * in array indexing format (i.e. for the 1st bin enter 0, the 2nd bin enter 1, etc.)
     * 
     * @param bin 		the bin to get the width of
     * @return			the width of the specified bin
     */
    public double getBinWidth(int bin) {
    	if (bin >= 0 && bin < numBins) {
    		return Math.abs(axisMargins[bin+1] - axisMargins[bin]);
    	}
    	return 0.0;
    }
    
    /**
     * Finds the specified value and returns which bin it is in
     * in array indexing format (i.e. if it is in the 1st bin, it would return 0, the 2nd bin would
     * return 1, ... , the nth bin would return n-1).
     * 
     * @param xVal		the data value to find on the axis
     * @return			the bin where the specified value is in array indexing format
     */
    public int getBin(double xVal) {
    	for (int i = 0; i < numBins; i++) {
    		if ((xVal >= axisMargins[i] && xVal < axisMargins[i+1])) {
    			return i;
    		}
    	}
    	/*
    	if (xVal == axisMargins[axisMargins.length - 1]) {
    		return numBins;
    	}*/
    	return -1;
    }
    
    /**
     * Scales the range by a specified unit
     * 
     * @param xUnit		the unit to multiply the range of values by
     * @return			min value + ((max value - min value) * xUnit)
     */
    public double convertFromUnit(double xUnit) {
    	double value = (maxVal - minVal) * xUnit;
    	
    	return minVal + value;
    }

}
