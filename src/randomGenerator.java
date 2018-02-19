
import java.lang.Math;

public class randomGenerator{
	
	public static double funcionBase(double seed, double a, double b, double m){
		return (a*seed + b)%m;
	}
	
	public static double f26a(double seed){
		double a=5;
		double b=0;
		double m=Math.pow(2,5);
		
		return funcionBase(seed,a,b,m);
	}
	
	public static double f26b(double seed){
		double a=7;
		double b=0;
		double m=Math.pow(2,5);
		
		return funcionBase(seed,a,b,m);
	}
	
	public static double f26_2(double seed){
		double a=3;
		double b=0;
		double m=31;
		
		return funcionBase(seed,a,b,m);
	}
	
	public static double f26_3(double seed){
		double a=Math.pow(7,5);
		double b=0;
		double m=Math.pow(2,31)-1;
		
		return funcionBase(seed,a,b,m);
	}
	
	public static double[] fcombinada(double seed, int n){
		double[] res = new double[n];

		double a1 = 157;
		double b1 = 0;
		double m1 = 32363;
		double seed1 = seed;

		double a2 = 146;
		double b2 = 0;
		double m2 = 31727;
		double seed2 = seed;

		double a3 = 142;
		double b3 = 0;
		double m3 = 31657;
		double seed3 = seed;

		double seed4 = seed;
		double a4;
		double b4 = 0;
		double m4 = 32362;

		for(int i=0;i<n;i++){
			seed1 = funcionBase(seed1,a1,b1,m1);
			seed2 = funcionBase(seed2,a2,b2,m2);
			seed3 = funcionBase(seed3,a3,b3,m3);
			a4 = seed1-seed2+seed3;

			seed4 = funcionBase(seed4,a4,b4,m4);

			res[i] = seed4;
		}
		
		return res;
	}
	
	public static double fFishman(double seed){
		double a=48271;
		double b=0;
		double m=Math.pow(2,31)-1;
		
		return funcionBase(seed,a,b,m);
	}
	
	public static double fMoore(double seed){
		double a=69621;
		double b=0;
		double m=Math.pow(2,31)-1;
		
		return funcionBase(seed,a,b,m);
	}
	
	public static double fRandu(double seed){
		double a=Math.pow(2,16)+3;
		double b=0;
		double m=Math.pow(2,31);
		
		return funcionBase(seed,a,b,m);
	}
}