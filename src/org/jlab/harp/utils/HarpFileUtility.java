/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.harp.utils;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author gavalian
 */
public class HarpFileUtility {
    
    public static File lastFileModified(String dir, String startsWith) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {			
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        
        long lastMod = Long.MIN_VALUE;
        File choise = null;
        for (File file : files) {
            if(file.getName().startsWith(startsWith)==true){
                if (file.lastModified() > lastMod) {
                    choise = file;
                    lastMod = file.lastModified();
                }
            }
        }
        return choise;
    }
}
