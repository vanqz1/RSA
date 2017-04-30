package main;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apfloat.Apfloat;

public class CustomMath implements Runnable{
	private int num;
	private int precision;
	private static ArrayList<Apfloat> partsOfSum;
	
	public CustomMath(int num, int precision)
	{
		this.num = num;
		this.precision = precision;
		this.partsOfSum = new ArrayList<Apfloat>();
	}
	
	public static Apfloat getFinalSum()
	{
		Apfloat sum = new Apfloat(1);
		
		for(int i = 0; i < partsOfSum.size(); i++)
		{
			sum = sum.add(partsOfSum.get(i));
		}
		
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
		partsOfSum.add(rez);
		
		System.out.println(Thread.currentThread().getName());
		
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
		CustomMath.calculatePartOfSum(this.num, this.precision);
	}
}
