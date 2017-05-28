package main;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apfloat.Apfloat;

public class CustomMath implements Runnable{
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
