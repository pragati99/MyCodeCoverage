package edu.utdallas;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * @author: Miti Desai Create Time: 03/15/17
 */

public class MyJUnitExecutionListener extends RunListener {

	// Called before all tests
	public void testRunStarted(Description description) throws Exception {
		if (null == CodeCoverageCollect.Coverages_testCase) {
			CodeCoverageCollect.Coverages_testCase = new Object2ObjectOpenHashMap<String, Object2ObjectOpenHashMap<String, IntSet>>();
		}

		System.out.println("\n\nTesting Started!!!");
	}

	// Called before each @Test
	public void testStarted(Description description) {
		// Note: Java is pass by value, so this works
		CodeCoverageCollect.Name_testCase = "[TEST] " + description.getClassName() + ":" + description.getMethodName()
				+ ":::";
		CodeCoverageCollect.Coverage_testCase = new Object2ObjectOpenHashMap<String, IntSet>();
	}

	// Called after each @Test Finishes
	public void testFinished(Description description) {
		CodeCoverageCollect.Coverages_testCase.put(CodeCoverageCollect.Name_testCase,
				CodeCoverageCollect.Coverage_testCase);
	}

	// Called after all tests finish
	@SuppressWarnings("unchecked")
	public void testRunFinished(Result result) throws IOException {
		System.out.println("Testing Finished!!!\n\n");

		// Write to stmt-cov.txt
		File fout = new File("stmt-cov.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		StringBuilder builder = new StringBuilder();
		for (String testCaseName : CodeCoverageCollect.Coverages_testCase.keySet()) {
			builder.append(testCaseName + " ");

			Object2ObjectOpenHashMap<String, IntSet> caseCoverage = CodeCoverageCollect.Coverages_testCase
					.get(testCaseName);

			for (String className : caseCoverage.keySet()) {
				int[] lines = caseCoverage.get(className).toIntArray();

				Arrays.sort(lines);
				for (int i = 0; i < lines.length; i++) {
					builder.append(className + ":" + lines[i] + ",");
				}
			}
		}
	bw.write(builder.toString());
		
		bw.close();

		// Total prioritization file write
				HashMap<String, HashSet<String>> caseCoverage1 = new HashMap<String, HashSet<String>>();
		LinkedHashMap<String, Integer> linkhashmap = new LinkedHashMap<String,Integer>();

		for (String testCaseName : CodeCoverageCollect.Coverages_testCase.keySet()) {
			HashSet<String> hashSet = new HashSet<String>();
			Object2ObjectOpenHashMap<String, IntSet> caseCoverage = CodeCoverageCollect.Coverages_testCase
					.get(testCaseName);

			for (String className : caseCoverage.keySet()) {
				int[] lines = caseCoverage.get(className).toIntArray();

				for (int i = 0; i < lines.length; i++) {
					hashSet.add(className + ":" + lines[i]);

				}
			}
			caseCoverage1.put(testCaseName, hashSet);
			linkhashmap.put(testCaseName, hashSet.size());
		}
		File fout1 = new File("stmt-cov-prioritized-total.txt");
		FileOutputStream fos1 = new FileOutputStream(fout1);
		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(fos1));

		StringBuilder builder1 = new StringBuilder();
		Object[] a = linkhashmap.entrySet().toArray();
		Arrays.sort(a, new Comparator<Object>() {
		    public int compare(Object o1, Object o2) {
		        return ((Map.Entry<String, Integer>) o2).getValue()
		                   .compareTo(((Map.Entry<String, Integer>) o1).getValue());
		    }
		});
		for (Object e : a) {
		    builder1.append(((Map.Entry<String, Integer>) e).getKey() + " : "
		            + ((Map.Entry<String, Integer>) e).getValue());
		}
		
		
		
		

		 try {
			 bw1.write(builder1.toString());
			    }
			    catch (IOException e) {
			    e.printStackTrace();
			    }
		
			    bw1.close();
	}

}
