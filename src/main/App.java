package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import main.CustomMath;
import org.apfloat.Apfloat;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class CustomMath implements Runnable{
	private int num;
	private int precision;
	private static Apfloat sum = new Apfloat(1);
	
	public CustomMath(int num, int precision)
	{
		this.num = num;
		this.precision = precision;
	}
	
	public static Apfloat getFinalSum()
	{
		return sum;
	}
	
	private static Apfloat calculatePartOfSum(int num, int precision)
	{
		if(num == 0)
		{
			return new Apfloat(1).precision(precision);
		}
		
		Apfloat ch = new Apfloat(2*num+1).precision(precision);
		Apfloat zn = new Apfloat(fact(2*num)).precision(precision);
		
		Apfloat rez = ch.divide(zn).precision(precision);
		
		synchronized (sum){
			sum = sum.add(rez);
		}
		
		return rez;
	}
	
	private static BigInteger fact(int num) {
		
		 BigInteger fact = BigInteger.valueOf(1);
		 
		 for (int i = 1; i <= num; i++)
		 {
		    fact = fact.multiply(BigInteger.valueOf(i));
		 }
		    
		 return fact;
	}

	@Override
	public void run() {
		try{
			String currentTreadNum = Thread.currentThread().getName().substring(14);
			long startTime = System.currentTimeMillis();
		
			System.out.println("Thread-" + currentTreadNum + " started.");
			
			CustomMath.calculatePartOfSum(this.num, this.precision);
			
			long timeNeeded = System.currentTimeMillis() - startTime;
			
			System.out.println("Thread-"+  currentTreadNum + " execution time was (millis):" + timeNeeded);
			System.out.println("Thread-" + currentTreadNum + " stopped.");
		}
		catch(Exception e){
			System.out.println("Error occured:"+  e.getMessage());
		}
	}
}

public class App {

	public static boolean quiet = false;

	public static void main(String[] args) throws FileNotFoundException,
			InterruptedException, ExecutionException, RemoteException {

		int numThreads = 10, numTerms = 1000;
		String outFile = "e";

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.equals("-p")) {
				numTerms = Integer.parseInt(args[i + 1]);
				i++;
			} else if (arg.equals("-t") || arg.equals("-tasks")) {
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
		
		long startTimeAllCalculations = System.currentTimeMillis();
		
		int numtasks = numTerms;
		
		if(numtasks/2 > 5)
		{
			numtasks = numtasks/2 + 1;
		}
		
		int threadsNeed = numtasks/4;
		
		
		
		if(threadsNeed < numThreads && threadsNeed > 0)
		{
			numThreads = threadsNeed;
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		
		
		
		for(int i = 0; i <= numtasks/2; i++)
		{
			CustomMath a = new CustomMath(i,numTerms+1);
			executor.execute(a);
		}
		
		executor.shutdownNow();
		
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		
		PrintWriter file = new PrintWriter(new FileOutputStream(outFile, false));
		file.println(CustomMath.getFinalSum().toString());
		file.flush();
		file.close();
		
		long endTimeAllCalculations = System.currentTimeMillis() - startTimeAllCalculations;
		
		if (!quiet) {
            System.out.println("Threads used in current run " + numThreads);
            System.out.println("Total execution time for current run (millis):" + endTimeAllCalculations);
        }
		
}}



	
