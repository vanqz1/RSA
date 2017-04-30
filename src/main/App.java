package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.CustomMath;


import org.apfloat.Apfloat;

public class App {

	public static boolean quiet = false;

	public static void main(String[] args) throws FileNotFoundException,
			InterruptedException, ExecutionException, RemoteException {

		int numThreads = 400, numTerms = 110;
		String outFile = "pi";

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.equals("-p")) {
				numTerms = Integer.parseInt(args[i + 1]);
				i++;
			} else if (arg.equals("-t")) {
				numThreads = Integer.parseInt(args[i + 1]);
				i++;
			} else if (arg.equals("-o")) {
				outFile = args[i + 1];
				i++;
			} else if (arg.equals("-q")) {
				quiet = true;
			} else {
				System.err.println("Unknown option " + arg);
				System.exit(1);
			}
		}

		if (numTerms == 0) {
			System.err.println("Number of terms should be specified with -p");
			System.exit(1);
		}

		if (numThreads == 0) {
			numThreads = Runtime.getRuntime().availableProcessors();
			System.out.println("Using " + numThreads + " threads");
		}
		
		int threadsNeed = numTerms/4;
		
		if(threadsNeed < numThreads)
		{
			numThreads = threadsNeed;
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		
		for(int i = 0; i<=numTerms+1; i++)
		{
			CustomMath a = new CustomMath(i,numTerms+1);
			executor.execute(a);
		}
		
		executor.shutdown();
		
		while(!executor.isTerminated()){}
		
		System.out.println(CustomMath.getFinalSum());
}}

	
