package com.fruits.jdkapi.regex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetJavaVerboseClassPkgs {
	public static final String VERBOSE_CLASS_LOG_DIR = "verboseclasslogdir";
	public static final String VERBOSE_CLASS_FILE_NAME = "ClassVerbose.log";
	public static final String PCSERVER_CLASSES = "PCServerClasses.log";
	public static final String PCSERVER_PKGS = "PCServerPkgs.log";
	public static final String RELEASE_CLASSES = "ReleaseClasses.log";
	public static final String RELEASE_PKGS = "ReleasePkgs.log";
	//public static final String PCSERVER_CLASSES_PATTERN  = "(\\[Loaded\\s)(.+?)(\\sfrom\\sfile:/C:/view_storage/SJ934/PowerClient/release/build/lib/pc/pcserver.jar\\])";
	public static final String PCSERVER_CLASSES_PATTERN  = "(\\[Loaded\\s)(.+?)(\\sfrom\\s(.+?)/pcserver.jar\\])";

	public static List<String> getFilePaths(String dirname) throws IOException {
		List<String> files = new ArrayList<String>();
		File dir = new File(dirname);
		if(!dir.exists()) return files;
		
		if(dir.isDirectory()){
			File[] fls = dir.listFiles(new FilenameFilter(){
				public boolean accept(File dir, String name) {
					return true;
				}
			});
			for(File fl : fls){
				if(fl.exists() && fl.isDirectory()) {
				  files.addAll(getFilePaths(fl.getCanonicalPath()));
				}else{
					String fn = fl.getCanonicalPath();
					if(fn.endsWith(".log"))
					  files.add(fn);
				}
			}
		}else{
			String fn = dir.getCanonicalPath();
			if(fn.endsWith(".log"))
			  files.add(fn);
		}
    return files;		
	}
	
	public static List<String> readFile(String fileName) throws IOException {
		List<String> lines = new ArrayList<String>();
		if(null == fileName) return lines;
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		while(null != (line = reader.readLine())){
			lines.add(line);
		}
    reader.close();
		return lines;
	}
	
	public static void writeFile(String fileName, List<String> lines) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		for(String line : lines) {
			if(null != line)
				writer.write(line);
			  writer.write(System.getProperty("line.separator"));
		}
		writer.close();
	}
	
	//Extract classes from Java Verbose log.
	public static List<String> extractPCServerClass(List<String> logs) {
		List<String> pcServerClasses = new ArrayList<String>();
		Pattern pattern = Pattern.compile(GetJavaVerboseClassPkgs.PCSERVER_CLASSES_PATTERN);
		for(String log : logs) {
		  Matcher matcher = pattern.matcher(log);
		  if(matcher.matches()) {
		  	//System.out.println(matcher.group(2));
		  	pcServerClasses.add(matcher.group(2));
		  }
		}
		return pcServerClasses;
	}
	
	//Get the pkgs with classes.
	public static List<String> extractPCServerPkgs(List<String> pcServerClasses) {
		List<String> pcServerPkgs = new ArrayList<String>();
		for(String cls : pcServerClasses) {
			int lastIndex = cls.lastIndexOf(".");
			pcServerPkgs.add(cls.substring(0,lastIndex));
		}
		
		return pcServerPkgs;
	}
	
	//Remove the duplidated pkgs and sort it.
	public static List<String> sortPCServerPkgs(List<String> pcServerPkgs) {
		Set<String> pkgs  = new HashSet<String>();
		pkgs.addAll(pcServerPkgs);
		List<String> newPCServerPkgs = new ArrayList<String>();
		newPCServerPkgs.addAll(pkgs);
		Collections.sort(newPCServerPkgs);
		return newPCServerPkgs;
		
	}
	
	public static List<String> getPCServerClasses(List<String> lines) {
	  List<String> pcServerClasses = new ArrayList<String>();
	  
	  return pcServerClasses;
	}
	
	public static void main(String[] args) throws Exception {
		String verboseClassLogDir = System.getProperty(GetJavaVerboseClassPkgs.VERBOSE_CLASS_LOG_DIR);
		List<String> logPaths = GetJavaVerboseClassPkgs.getFilePaths(verboseClassLogDir);
		
		List<String> logs = new ArrayList<String>();
		System.out.println("Got log files: " + logPaths.size());
		for(String logPath : logPaths){
			System.out.println(logPath);
		}
		for(String logPath : logPaths){
			logs.addAll(GetJavaVerboseClassPkgs.readFile(logPath));
		}
		System.out.println("Lines of verbose class log: " + logs.size());

		List<String> pcServerClasses = extractPCServerClass(logs);
		List<String> pcServerPkgs = extractPCServerPkgs(pcServerClasses);
		writeFile(GetJavaVerboseClassPkgs.PCSERVER_CLASSES, sortPCServerPkgs(pcServerClasses));
		writeFile(GetJavaVerboseClassPkgs.PCSERVER_PKGS, sortPCServerPkgs(pcServerPkgs));
		writeFile(GetJavaVerboseClassPkgs.RELEASE_PKGS,sortPCServerPkgs(extractPCServerPkgs(GetJavaVerboseClassPkgs.readFile(GetJavaVerboseClassPkgs.RELEASE_CLASSES))));

		/*
		List<String> files = GetPCServerPkgs.getFilePaths("classlog");
		for(String file : files){
		  System.out.println(file);
		}
		*/
	}
}
