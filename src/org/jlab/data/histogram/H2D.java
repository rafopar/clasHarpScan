package org.jlab.data.histogram;

import org.jlab.data.base.MultiIndex;

/**
 * Specifies the methods to create a 2D Histogram and operations to fill it and
 * set values to its bins
 * 
 * @author Erin Kirby
 * @version 061714
 */
public class H2D {

	private String hName = "basic2D";
	private Axis xAxis = new Axis();
	private Axis yAxis = new Axis();
	private double[] hBuffer;
	private MultiIndex offset;

	public H2D() {
		offset = new MultiIndex(xAxis.getNBins(), yAxis.getNBins());
		hBuffer = new double[offset.getArraySize()];
	}

	/**
	 * Creates an empty 2D Histogram with 1 bin x and y axes
	 * 
	 * @param name
	 *            the desired name of the 2D Histogram
	 */
	public H2D(String name) {
		hName = name;
		offset = new MultiIndex(xAxis.getNBins(), yAxis.getNBins());
		hBuffer = new double[offset.getArraySize()];
	}

	/**
	 * Creates a 2D Histogram with the specified parameters.
	 * 
	 * @param name
	 *            the name of the histogram
	 * @param bx
	 *            the number of x axis bins
	 * @param xmin
	 *            the minimum x axis value
	 * @param xmax
	 *            the maximum x axis value
	 * @param by
	 *            the number of y axis bins
	 * @param ymin
	 *            the minimum y axis value
	 * @param ymax
	 *            the maximum y axis value
	 */
	public H2D(String name, int bx, double xmin, double xmax, int by,
			double ymin, double ymax) {
		hName = name;
		this.set(bx, xmin, xmax, by, ymin, ymax);
		offset = new MultiIndex(bx, by);
		hBuffer = new double[offset.getArraySize()];
	}

	/**
	 * Sets the bins to the x and y axes and creates the buffer of the histogram
	 * 
	 * @param bx
	 *            number of bins on the x axis
	 * @param xmin
	 *            the minimum value on the x axis
	 * @param xmax
	 *            the maximum value on the x axis
	 * @param by
	 *            number of bins on the y axis
	 * @param ymin
	 *            the minimum value on the y axis
	 * @param ymax
	 *            the maximum value on the y axis
	 */
	public final void set(int bx, double xmin, double xmax, int by,
			double ymin, double ymax) {
		xAxis.set(bx, xmin, xmax);
		yAxis.set(by, ymin, ymax);
		offset = new MultiIndex(bx, by);
		int buff = offset.getArraySize();
		hBuffer = new double[buff];
	}

	/**
	 * 
	 * @return the name of the Histogram
	 */
	public String getName() {
		return hName;
	}

	/**
	 * 
	 * @return the x-axis of the 2D Histogram
	 */
	public Axis getXAxis() {
		return xAxis;
	}

	/**
	 * 
	 * @return the y-axis of the 2D Histogram
	 */
	public Axis getYAxis() {
		return yAxis;
	}

	/**
	 * Checks if that bin is valid (exists)
	 * 
	 * @param bx
	 *            The x coordinate of the bin
	 * @param by
	 *            The y coordinate of the bin
	 * @return The truth value of the validity of that bin
	 */
	private boolean isValidBins(int bx, int by) {
		if ((bx >= 0) && (bx <= xAxis.getNBins()) && (by >= 0)
				&& (by <= yAxis.getNBins())) {
			return true;
		}
		return false;
	}

	/**
	 * Finds the bin content at that bin
	 * 
	 * @param bx
	 *            The x coordinate of the bin
	 * @param by
	 *            The y coordinate of the bin
	 * @return The content at that bin
	 */
	public double getBinContent(int bx, int by) {
		if (this.isValidBins(bx, by)) {
			int buff = offset.getArrayIndex(bx, by);
			return hBuffer[buff];
		}
		return 0.0;
	}

	/**
	 * Sets the bin to that value
	 * 
	 * @param bx
	 *            The x coordinate of the bin
	 * @param by
	 *            The y coordinate of the bin
	 * @param w
	 *            The desired value to set the bin to
	 */
	public void setBinContent(int bx, int by, double w) {
		if (this.isValidBins(bx, by)) {
			int buff = offset.getArrayIndex(bx, by);
			hBuffer[buff] = w;
		}
	}

	/**
	 * Adds 1.0 to the bin with that value
	 * 
	 * @param x
	 *            the x coordinate value
	 * @param y
	 *            the y coordinate value
	 */
	public void fill(double x, double y) {
		int bin = this.findBin(x, y);
		if (bin >= 0)
			this.addBinContent(bin);
	}

	public void fill(double x, double y, double w) {
		int bin = this.findBin(x, y);
		if (bin >= 0) {
			this.addBinContent(bin, w);
		}
	}

	/**
	 * Increments the current bin by 1.0
	 * 
	 * @param bin
	 *            the bin in array indexing format to increment
	 */
	private void addBinContent(int bin) {
		hBuffer[bin] = hBuffer[bin] + 1.0;
	}

	/**
	 * Increments the bin with that value by that weight
	 * 
	 * @param bin
	 *            the bin to add the content to, in array indexing format
	 * @param w
	 *            the value to add to the bin content
	 */
	private void addBinContent(int bin, double w) {
		hBuffer[bin] = hBuffer[bin] + w;
	}

	/**
	 * Finds which bin has that value.
	 * 
	 * @param x
	 *            The x value to search for
	 * @param y
	 *            The y value to search for
	 * @return The bin, in array indexing format, which holds that x-y value
	 */
	public int findBin(double x, double y) {
		int bx = xAxis.getBin(x);
		int by = yAxis.getBin(y);
		if (this.isValidBins(bx, by)) {
			return (offset.getArrayIndex(bx, by));
		}
		return -1;
	}

	/**
	 * Generates a 2D array with the content in the histogram
	 * 
	 * @return a 2D array with each bin in its array index
	 */
	public double[][] getContentBuffer() {
		double[][] buff = new double[xAxis.getNBins()][yAxis.getNBins()];
		for (int xloop = 0; xloop < xAxis.getNBins(); xloop++) {
			for (int yloop = 0; yloop < yAxis.getNBins(); yloop++) {
				buff[xloop][yloop] = this.getBinContent(xloop, yloop);
			}
		}
		return buff;
	}

	/**
	 * Creates an error buffer with each element being 0.0
	 * 
	 * @return a double 2D array with a size of xAxis * yAxis with each element
	 *         being 0.0
	 */
	public double[][] getErrorBuffer() {
		double[][] buff = new double[xAxis.getNBins()][yAxis.getNBins()];
		for (int xloop = 0; xloop < xAxis.getNBins(); xloop++) {
			for (int yloop = 0; yloop < yAxis.getNBins(); yloop++) {
				buff[xloop][yloop] = 0.0;
			}
		}
		return buff;
	}

	/**
	 * Specifies the region in the 2D histogram with those attributes
	 * 
	 * @param name
	 *            The name of the histogram
	 * @param bx_start
	 *            The x coordinate beginning
	 * @param bx_end
	 *            The x coordinate end
	 * @param by_start
	 *            The y coordinate beginning
	 * @param by_end
	 *            The y coordinate end
	 * @return A 2D histogram with the entered specifications
	 */
	public H2D getRegion(String name, int bx_start, int bx_end,
			int by_start, int by_end) {
		double xBinWidth = xAxis.getBinWidth(bx_start);
		double newXMin = xAxis.min() + (xBinWidth * bx_start);
		double newXMax = xAxis.min() + (xBinWidth * bx_end);

		double yBinWidth = yAxis.getBinWidth(by_start);
		double newYMin = yAxis.min() + (yBinWidth * by_start);
		double newYMax = yAxis.min() + (yBinWidth * by_end);
		H2D regHist = new H2D(name, bx_end - bx_start, newXMin,
				newXMax, by_end - by_start, newYMin, newYMax);

		double content = 0.0;
		for (int y = by_start; y < by_end; y++) {
			for (int x = bx_start; x < bx_end; x++) {
				content = this.getBinContent(x, y);
				regHist.setBinContent(x, y, content);
			}
		}
		return regHist;
	}

	/**
	 * Creates a projection of the 2D histogram onto the X Axis, adding up all
	 * the y bins for each x bin
	 * 
	 * @return a H1D object that is a projection of the Histogram2D
	 *         object onto the x-axis
	 */
	public H1D projectionX() {
		String name = "X Projection";
		double xMin = xAxis.min();
		double xMax = xAxis.max();
		int xNum = xAxis.getNBins() + 1;
		H1D projX = new H1D(name, xNum, xMin, xMax);

		double height = 0.0;
		for (int x = 0; x <= xAxis.getNBins(); x++) {
			height = 0.0;
			for (int y = 0; y <= yAxis.getNBins(); y++) {
				height += this.getBinContent(x, y);
			}
			projX.setBinContent(x, height);
		}

		return projX;
	}

	/**
	 * Creates a projection of the 2D histogram onto the Y Axis, adding up all
	 * the x bins for each y bin
	 * 
	 * @return a H1D object that is a projection of the Histogram2D
	 *         object onto the y-axis
	 */
	public H1D projectionY() {
		String name = "Y Projection";
		double yMin = yAxis.min();
		double yMax = yAxis.max();
		int yNum = yAxis.getNBins() + 1;
		H1D projY = new H1D(name, yNum, yMin, yMax);

		double height = 0.0;
		for (int y = 0; y <= yAxis.getNBins(); y++) {
			height = 0.0;
			for (int x = 0; x <= xAxis.getNBins(); x++) {
				height += this.getBinContent(x, y);
			}
			projY.setBinContent(y, height);
		}

		return projY;
	}

	/**
	 * Creates a 1-D Histogram slice of the specified y Bin
	 * 
	 * @param xBin		the bin on the y axis to create a slice of
	 * @return 			a slice of the x bins on the specified y bin as a 1-D Histogram
	 */
	public H1D sliceX(int xBin) {
		String name = "Slice of " + xBin + " X Bin";
		double xMin = xAxis.min();
		double xMax = xAxis.max();
		int xNum = xAxis.getNBins() + 1;
		H1D sliceX = new H1D(name, xNum, xMin, xMax);

		for (int x = 0; x <= xNum; x++) {
			sliceX.setBinContent(x, this.getBinContent(x, xBin));
		}

		return sliceX;
	}

	/**
	 * Creates a 1-D Histogram slice of the specified x Bin
	 * 
	 * @param yBin			the bin on the x axis to create a slice of
	 * @return 				a slice of the y bins on the specified x bin as a 1-D Histogram
	 */
	public H1D sliceY(int yBin) {
		String name = "Slice of " + yBin + " Y Bin";
		double yMin = yAxis.min();
		double yMax = yAxis.max();
		int yNum = yAxis.getNBins() + 1;
		H1D sliceY = new H1D(name, yNum, yMin, yMax);

		for (int y = 0; y <= yNum; y++) {
			sliceY.setBinContent(y, this.getBinContent(yBin, y));
		}

		return sliceY;
	}

	public double[] offset() {
		return hBuffer;
	}
}
