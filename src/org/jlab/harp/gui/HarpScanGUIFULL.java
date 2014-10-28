/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.harp.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jlab.data.graph.DataSetXY;
import org.jlab.data.graph.DataTable;
import org.jlab.data.harp.HarpGenericAnalyzer;
import org.jlab.harp.utils.HarpFileUtility;
import org.jlab.scichart.canvas.ScChartCanvas;

/**
 *
 * @author gavalian
 */
public class HarpScanGUIFULL extends JFrame implements ActionListener {
     private ParameterPanel wirePositions = null;
    private ParameterPanel peakPositions = null;
    private JSplitPane  splitPane = null;
    private Integer     numberOfWires  = 3;
    public  Integer     harpWireToFit  = 13;
    private DataTable   dataTable = new DataTable();
    private ScChartCanvas canvas  = null;
    private String   currentFileName         = "";
    private String   currentFilePath         = "";
    private String   currentHarpFilesDir     = ".";
    private double   wireScanPositions[]     = null;
    private String   autoLoadFileStartsWith  = "harp_generic";
    private Properties analyzerProperties    = new Properties();
    private String     harpScanAnalyzerType  = "tagger";
    private String     propertiesFileName    = "";
    private HarpGenericAnalyzer  harpAnalyzer = new HarpGenericAnalyzer();

    public HarpScanGUIFULL(String type, double[] limits){        
        super(type);
        
        
        
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(400);
        
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        this.initButtons(buttonPanel);
        leftPanel.add(buttonPanel);
        
        splitPane.setLeftComponent(leftPanel);
        
        canvas = new ScChartCanvas(500,500,1,1);
        splitPane.setRightComponent(canvas);
        splitPane.addComponentListener(canvas);
        this.addComponentListener(canvas);                

        this.add(splitPane);        
        this.pack();
        this.setVisible(true);
        this.initializeEnvironment(type);
        //this.initializeDirectory();
    }
    
    public void initializeEnvironment(String harStyle){
        currentHarpFilesDir = System.getenv("HARPFILE_DIR");
        
        if(currentHarpFilesDir==null){
            System.err.println("** ERROR ** : --> HARPFILE_DIR environment is not defined...");                      
        } else {
            File harpFile = HarpFileUtility.lastFileModified(currentHarpFilesDir,autoLoadFileStartsWith);
            currentFileName = harpFile.getName();
            currentFilePath = harpFile.getAbsolutePath();
        }
        
        System.err.println("[CURRENT PATH] file : -----> " + currentFilePath);
        String etcDirectory = System.getenv("SCRIPT_DIR");

        if(etcDirectory==null){
            System.err.println("** ERROR ** : --> SCRIPT_DIR environment is not defined...");
        } else {
            String propertyFile = etcDirectory + "/../etc/harpscan.properties";
            propertiesFileName  = propertyFile;
            
            //this.initProperties();
            File   configFile   = new File(propertyFile);
            System.err.append("Oppening File: ---> " + propertyFile + "  status = " + configFile.exists());
            if(configFile.exists()==true){
                System.err.println("Loading Properties : ----> " + propertyFile);
                try {
                    analyzerProperties.load(new FileInputStream(configFile));
                } catch (IOException ex) {
                    Logger.getLogger(HarpScanGUIFULL.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else { 
                System.err.println("  ***************** \n");
                this.initProperties();
            }
            
            //this.initProperties();           
        } 
        
        analyzerProperties.list(System.err);
    }
    
    public void initProperties(){
        
        analyzerProperties.clear();
        analyzerProperties.setProperty("scan.limits.tagger.x.min", "52.0");
        analyzerProperties.setProperty("scan.limits.tagger.x.max", "56.0");
        analyzerProperties.setProperty("scan.limits.tagger.y.min", "20.0");
        analyzerProperties.setProperty("scan.limits.tagger.y.max", "30.0");
        analyzerProperties.setProperty("scan.limits.tagger.sc45.min", "20.0");
        analyzerProperties.setProperty("scan.limits.tagger.sc45.max", "30.0");
        
        analyzerProperties.setProperty("scan.limits.2h03.x.min", "52.0");
        analyzerProperties.setProperty("scan.limits.2h03.x.max", "56.0");
        analyzerProperties.setProperty("scan.limits.2h03.y.min", "20.0");
        analyzerProperties.setProperty("scan.limits.2h03.y.max", "30.0");
        analyzerProperties.setProperty("scan.limits.2h03.sc45.min", "20.0");
        analyzerProperties.setProperty("scan.limits.2h03.sc45.max", "30.0");
        
        analyzerProperties.setProperty("scan.limits.2c21.x.min", "52.0");
        analyzerProperties.setProperty("scan.limits.2c21.x.max", "56.0");
        analyzerProperties.setProperty("scan.limits.2c21.y.min", "20.0");
        analyzerProperties.setProperty("scan.limits.2c21.y.max", "30.0");
        
        System.err.println("Saving Propertis : ----> " + propertiesFileName);
        
        
        File outfile = new File(propertiesFileName);
        try {
            
            analyzerProperties.store(new FileOutputStream(outfile), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HarpScanGUIFULL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HarpScanGUIFULL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void initializeDirectory(){
        currentHarpFilesDir = System.getenv("HARPFILE_DIR");
        if(currentHarpFilesDir==null){
            currentHarpFilesDir = ".";
            System.err.println("--> ERROR: environment variable HARPFILE_DIR is not set");
        } else {
            File harpFile = HarpFileUtility.lastFileModified(currentHarpFilesDir,autoLoadFileStartsWith );
            currentFileName = harpFile.getName();
            currentFilePath = harpFile.getAbsolutePath();
            System.err.println("file : -----> " + currentFilePath);
            //this.loadData(currentFilePath);
        }
    }
    
    public String getTempImagePath(){
        StringBuilder str = new StringBuilder();
        if(System.getenv("SCRIPT_DIR")!=null){
            str.append(System.getenv("SCRIPT_DIR"));
            str.append("/../etc/temp/temptorary_image_scan.png");
            return str.toString();
        }
        return null;
    }
    
    public void makeLogEntry(){
        /*
        String imagePath = this.getTempImagePath();
        if(imagePath!=null){        
            canvas.exportPNG(imagePath);
            MakeLogEntry log = new MakeLogEntry();
            StringBuilder str = new StringBuilder();
            for(int loop = 0; loop < harpAnalyzer.functions.size();loop++){
                String[] labels = harpAnalyzer.getLegend(loop);
                for(String text : labels) {
                    str.append(text);
                    str.append("\n");
                }
            }
            log.setFitParameters(str.toString());
            //log.setFitParameters(" ");
            //log.setImgPath(picpath);
            log.setImgPath(imagePath);
//log.getRunNumber();                                                                        
            log.addComments();
        }*/
    }
    
    
    public void initButtons(JPanel panel){
        Box vertical = Box.createVerticalBox();
        
        Box boxOF = Box.createHorizontalBox();
        boxOF.add(new JLabel("Choose File : "));
        JButton buttonOpenFile = new JButton("Open File");
        buttonOpenFile.addActionListener(this);
        boxOF.add(buttonOpenFile);
        
        vertical.add(boxOF);
        Box boxFT = Box.createHorizontalBox();
        boxFT.add(new JLabel("Refit Data : "));
        JButton buttonFitData = new JButton("Fit Data");
        buttonFitData.addActionListener(this);
        boxFT.add(buttonFitData);
        vertical.add(boxFT);
        
        Box boxLG = Box.createHorizontalBox();
        boxLG.add(new JLabel("Submit Log : "));        
        JButton buttonLog = new JButton("Log Entry");
        buttonLog.addActionListener(this);
        boxLG.add(buttonLog);
        vertical.add(boxLG);
        
        Box boxWR = Box.createHorizontalBox();
        boxWR.add(new JLabel("Refit Data : "));
        String[] wireNames = new String[]{"Upstream Left","UpstreamRight",
            "Tagger Left", " Tagger Right",
            "Downstream Left","Downstream Right",
            "Downstream Top", "Downstream Bottom",
            "BLM-1","BLM-2","HPS Left","HPS Right",
            "ECAL Cosm 1","ECAL Cosm 2","ECAL Cosm 3","ECAL Cosm 4",
        };
        JComboBox comboWire = new JComboBox(wireNames);
        comboWire.setSelectedIndex(11);
        boxWR.add(comboWire);
        vertical.add(boxWR);
        
        
        panel.add(vertical);
    }
    
    
    public void loadData(String filename){
        DataTable  table = new DataTable();
        table.readFile(filename);
        
        table.show();
        harpAnalyzer.init(table, 13);
        harpAnalyzer.fitData();
        ArrayList<DataSetXY>  harpData = harpAnalyzer.getHarpData();
        int ngraphs = harpData.size();
        System.err.println(" HARP SCAN DATA SIZE = " + ngraphs);
        canvas.divide(1, ngraphs);
        canvas.repaint();
        for(int loop =0; loop < ngraphs; loop++){
            DataSetXY data = harpData.get(loop);
            canvas.addPoints(loop, 
                    data.getDataX().getArray(), 
                    data.getDataY().getArray(),4);
        }
        
        for(int loop =0; loop < ngraphs; loop++){
            DataSetXY data = harpAnalyzer.getHarpFuncs().get(loop).getDataSet();
            canvas.addLine(loop, 
                    data.getDataX().getArray(), 
                    data.getDataY().getArray(),4);
        }
    }
    
    public void loadData(){
        JFileChooser chooser = new JFileChooser(currentHarpFilesDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Harp Scan Files", "txt","text");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            currentFileName = chooser.getSelectedFile().getName();
            currentFilePath = chooser.getSelectedFile().getAbsolutePath();
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName() + 
                    "  " + chooser.getSelectedFile().getAbsolutePath());            
            this.loadData(chooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    public static void main(String[] args){
        
        String wireScanType = "HARP_2_WIRE";
        int    nwires       = 3;
        int  windowSizeX = 1200;
        int  windowSizeY =  800;
        int wiretofit    =   13;
        /*
        if(args.length>0){
            windowSizeX = Integer.parseInt(args[0]);
            windowSizeY = Integer.parseInt(args[1]);
            wireScanType = args[2];
            wiretofit    = Integer.parseInt(args[3]);
            
            if(wireScanType.compareTo("HARP_3_WIRE")==0){
                nwires = 3;
            }
            if(wireScanType.compareTo("HARP_2_WIRE")==0){
                nwires = 2;
            }
            if(wireScanType.compareTo("HARP_SVT")==0){
                nwires = 2;
            }
        }*/
        
        double[] limits = new double[nwires*2];
        System.err.println("setting limits for harp scan nargs = " + args.length);
        /*
        int argcount = 4;
        for(int loop = 0; loop < nwires*2; loop++){
            limits[loop] = Double.parseDouble(args[argcount]);
            argcount++;
            System.err.println(loop + "  " + limits[loop]);
        }*/
        
        HarpScanGUIFULL harp = new HarpScanGUIFULL(wireScanType,limits);
        harp.harpWireToFit = wiretofit;
        harp.setSize(windowSizeX,windowSizeY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Open File")==0){
            this.loadData();
        }
        if(e.getActionCommand().compareTo("Fit Data")==0){
            this.loadData(currentFilePath);
        }
 
        if(e.getActionCommand().compareTo("Log Entry")==0){
            this.makeLogEntry();
        }
 

        //System.err.println("action command = " + e.getActionCommand());
        //this.loadData();
    }
}