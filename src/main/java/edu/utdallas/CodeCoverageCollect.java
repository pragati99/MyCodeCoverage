package edu.utdallas;

import it.unimi.dsi.fastutil.ints.IntSet;

import java.lang.reflect.Array;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class CodeCoverageCollect {
	// Need to Map: Test Case -> Class -> Statement Coverage
	public static Object2ObjectOpenHashMap<String, Object2ObjectOpenHashMap<String, IntSet>> Coverages_testCase;
	public static Object2ObjectOpenHashMap<String, IntSet> Coverage_testCase;
	public static String Name_testCase;

    // Called whenever executing a line
    public static void addMethodLine(String className, Integer line){
    	if (Coverage_testCase == null) {
    		return;
    	}
    	
    	IntSet lines = Coverage_testCase.get(className);
        if (lines != null) {
        	lines.add(line);
        String x = lines.toArray().toString();
        //System.out.println(x.length());
        }
        else {
        	lines = new IntOpenHashSet(new int[]{line});
            Coverage_testCase.put(className, lines);
            
        }
    }
}
