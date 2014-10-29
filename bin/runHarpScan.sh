#!/bin/sh -f

SCRIPT_DIR=`dirname $0` ; export SCRIPT_DIR
HARPFILE_DIR=/Users/gavalian/Work/Software/Release-7.0/JavaProjects/clasHarpScan/data; export HARPFILE_DIR
echo +-------------------------------------------------------------------------
echo "|                       Starting HARP SCAN PROGRAM                      |"
echo +-------------------------------------------------------------------------
echo "\n"
HARPARGS="1200 800 HARP_2_WIRE 13 40.0 55.0 20.0 30.0"
java -cp "$SCRIPT_DIR/../lib/*:$SCRIPT_DIR/../dist/*" org.jlab.harp.gui.HarpScanGUIFULL $HARPARGS
