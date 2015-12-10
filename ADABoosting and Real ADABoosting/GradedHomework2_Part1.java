import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;


public class GradedHomework2_Part1 {
	private final Path filepath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	public static int T;
	public static int n;
	public static int count=0;
	public static double epsilon;
	public static double alpha;
	public static double qiright;
	public static double qiwrong;
	public static double G;
	public static float xlist[] = new float[100];
	public static int ylist[] = new int[100];
	public static float zlist[] = new float[100];
	public static double prenormalizedpi[];
	public static double newpi[];
	public static float probability[];
	public static double errorcalculationarray[];
	public static double Zt=0;
	private static holder[] hs;
	public static double ctplus=0;
	public static double ctminus=0;
	public static int htofx[];
	public static double gtofx[];
	public static double ftofx[];
	public static double et=0;

	
	public GradedHomework2_Part1(String filename) {
		filepath = Paths.get(filename);
	}

	public static void main(String args[]) throws IOException {
		GradedHomework2_Part1 parser = new GradedHomework2_Part1("C:\\MyData\\File1.txt");
		parser.linewise();
		Scanner s = new Scanner(System.in);
		binaryboost();
	}
	public final void linewise() throws IOException {
		
		try (Scanner scanner = new Scanner(filepath, ENCODING.name())) {
			processfirstline(scanner.nextLine());// process the first line of
													// the file
				String a = scanner.nextLine();
				StringTokenizer st = new StringTokenizer(a, " ");
				while(st.hasMoreElements())
				{
					xlist[count]=Float.parseFloat(st.nextToken());
					count++;
				}
				count=0;
				String b = scanner.nextLine();
				st = new StringTokenizer(b, " ");
				while(st.hasMoreElements())
				{
					ylist[count]=Integer.parseInt(st.nextToken());
					count++;
				}
				count=0;
				String c = scanner.nextLine();
				st = new StringTokenizer(c, " ");
				while(st.hasMoreElements())
				{
					zlist[count]=Float.parseFloat(st.nextToken());
					count++;
				}
				
}
		ftofx = new double[count];
			
			
}
	
	public static void binaryboost()
	{   
		double Z=0;
		HashMap<Integer, HashMap> wronglyclassifiedone = new HashMap<Integer, HashMap>();
		HashMap<Integer, HashMap> wronglyclassifiedtwo = new HashMap<Integer, HashMap>();
//		System.out.println(count);
		int counter=count;
		
		for(int i=0;i<ylist.length;i++)
		{
			if(ylist[i]!=0)
				counter++;
		}
//		probability=new float[count];
//
		int errorarray[] = new int[counter];
		errorcalculationarray = new double[counter];
		int iterations = 1;
		
		for(int i=0;i<count;i++)
		{
//			probability[i]=(float) ylist[i]/count;
			errorcalculationarray[i]=zlist[i];
		}
		while(iterations<=T)
		{
			System.out.println("Iteration " + iterations);
//		System.out.println(probability.length);
		counter=0;
		int v=1;
		int lerrorneg=0;
		int rerrorpos=0;
		int error;
		HashMap<Float, Double> a = new HashMap<Float, Double>();
		HashMap<Float, Double> b = new HashMap<Float, Double>();
		for(int i=1;i<count;i++)
		{
			int j=i-1;
			int wc=0;
			while(j>=0)
			{
			if(ylist[j]==-1)
			{
//				System.out.println("left" + v);
				lerrorneg++;
//				System.out.println("Putting" + xlist[j] + " " + errorcalculationarray[j]);
				a.put(xlist[j], errorcalculationarray[j]);
				
//				wronglyclassified.put(j, ylist[j]);
//				System.out.println("l error" + lerror);
			}
			if(ylist[j]==1)
			{
				b.put(xlist[j], errorcalculationarray[j]);
			}
			j--;
			if(j<0)
				break;
			}			
			int k=i;
			while(k<errorarray.length)
			{
			if(ylist[k]==1)
			{
//				System.out.println("right" + v);
				rerrorpos++;
//				System.out.println("Putting" + xlist[k] + " " + errorcalculationarray[k]);
				a.put(xlist[k], errorcalculationarray[k]);
				
//				System.out.println(rerror + "rerror");
//				wronglyclassified.put(k, ylist[k]);
			}
			if(ylist[k]==-1)
			{
				b.put(xlist[k], errorcalculationarray[k]);
			}
			
			k++;
			if(k==errorarray.length)
				break;
			
			}
			wronglyclassifiedone.put(v, a);
			wronglyclassifiedtwo.put(v, b);
//			System.out.println("LEFT" + wronglyclassifiedone);
//			System.out.println("RIGHT" + wronglyclassifiedtwo);
			a = new HashMap<>();
			b = new HashMap<>();
			error=lerrorneg+rerrorpos;
//			System.out.println(error);
			errorarray[i]=error;
			lerrorneg=0;
			rerrorpos=0;
			v++;
		}
		
		int epsilonarrayone = 0;
		double minone=Double.MAX_VALUE;
		int minindexone=0;
		double temparrayone[]=new double[v];
		for(int i=1;i<v;i++)
		{
	    HashMap<Float, Double> s = wronglyclassifiedone.get(i);
//	    System.out.println("s" + s);
	    
//	    System.out.println("keyset" + s.keySet());
	    double temp=0;
	    
	    	for(float key: s.keySet()){
//	    		System.out.println("Retreiving from s" + s.get(key));
		    	temp+=s.get(key);
	    	}
//	    	System.out.println("Temp" + temp);
	    	temparrayone[i]=temp;
	    	
		}
		
	    
//	    System.out.println(epsilonarray[i]);
		for(int i=1;i<temparrayone.length;i++)
		{
	    if(minone>temparrayone[i])
	    {
//	    	System.out.println(temparray[i]);
	    	minone=temparrayone[i];
	    	minindexone=i;
	    }
		}
	    double mintwo=Double.MAX_VALUE;
		int minindextwo=0;
		double temparraytwo[]=new double[v];
		for(int i=1;i<v;i++)
		{
	    HashMap<Float, Double> t = wronglyclassifiedtwo.get(i);
//	    System.out.println("s" + s);
	    
//	    System.out.println("keyset" + s.keySet());
	    double temp=0;
	    
	    	for(float key: t.keySet()){
//	    		System.out.println("Retreiving from s" + s.get(key));
		    	temp+=t.get(key);
	    	}
//	    	System.out.println("Temp" + temp);
	    	temparraytwo[i]=temp;
	    	
		}
		
	    
//	    System.out.println(epsilonarray[i]);
		for(int i=1;i<temparraytwo.length;i++)
		{
	    if(mintwo>temparraytwo[i])
	    {
//	    	System.out.println(temparray[i]);
	    	mintwo=temparrayone[i];
	    	minindextwo=i;
	    }
		}
//	    System.out.println(mintwo + " " + minindextwo);
	    double min=0;
	    int minindex=0;
	    boolean flag = true;
	    if(minone<=mintwo)
	    {
	    	epsilon=minone;
	    	min=minone;
	    	minindex=minindexone;
	    }
	    else
	    {
	    	epsilon=mintwo;
	    	flag=false;
	    	min=mintwo;
	    	minindex=minindextwo;
	    }
	    System.out.println("Classifier h = I(x<" + (minindex-0.5) + ")");
		System.out.println("Error=" + epsilon);
		double d = java.lang.Math.log((1-epsilon)/(epsilon));
		alpha = d/2;
		System.out.println("Alpha=" + alpha);
		qiright=Math.exp(-alpha);
//		System.out.println("qiright"+ qiright);
		qiwrong=Math.exp(alpha);
//		System.out.println("qiwrong" + qiwrong);
		
		HashMap<Integer, Boolean> flagset = new HashMap<Integer, Boolean>();
		for(int i=0; i <count;i++)
		{
			flagset.put(i,  true);
		}
		HashMap<Float, Float> temporary = new HashMap<Float, Float>();
		if(flag==true)
			temporary = wronglyclassifiedone.get(minindex);
		else
			temporary = wronglyclassifiedtwo.get(minindex);
//		System.out.println("Temporary" + temporary);
		for(Float key: temporary.keySet()){
//            System.out.println(key  +" :: "+ temporary.get(key));
            for(int i=0;i<count;i++)
            {
            	if(xlist[i]==key)
            	{
            		 flagset.put(i, false);
            	}
            }
         }
//		System.out.println(flagset);
		prenormalizedpi = new double[count];
		newpi = new double[count];
		for(int i=0;i<count;i++)
		{
			if(flagset.get(i)==true)
			{
				prenormalizedpi[i]=errorcalculationarray[i]*qiright;
			}
			else
			{
				prenormalizedpi[i]=errorcalculationarray[i]*qiwrong;
			}
//			System.out.println("Prenormalized" + prenormalizedpi[i]);
			Z+=prenormalizedpi[i];
			
		}
		System.out.println("Normalization Factor Z=" + Z);
		if(Zt==0)
			Zt=Z;
		else
			Zt = Zt*Z;
		System.out.println("Pi after normalization =");
		for(int i=0;i<count;i++)
		{
			if(flagset.get(i)==true)
			{
				
				newpi[i]=(prenormalizedpi[i])/Zt;
			}
			else
			{
				newpi[i]=(prenormalizedpi[i])/Zt;
			}
			System.out.println(newpi[i]);
			errorcalculationarray[i]=newpi[i];
			
		}
		System.out.println("Boosted Classifier f(x) =" + alpha + " I(x<" + (minindex-0.5) + ")");
		System.out.println("Boosted Classifier Error "+ minindex);
		System.out.println("Bound on Error" + Zt);
		epsilon = 0;
		alpha = 0;
		qiright= 0;
		qiwrong = 0;
		minone=0; minindexone=0;
		mintwo=0; minindextwo=0;
		v=1;
		for(int i=0;i<count;i++)
			{
			prenormalizedpi[i]=0;
			newpi[i]=0;
			errorarray[i]=0;
			}
		wronglyclassifiedone = new HashMap<>();
		wronglyclassifiedtwo = new HashMap<>();
		temporary = new HashMap<>();
		Z=0;
		flagset = new HashMap<>();
		iterations++;
		}
		
		
		
	}
	

	public final void processfirstline(String line)
	{
	Scanner scanner = new Scanner(line);

	T = Integer.parseInt(scanner.next());
	n = Integer.parseInt(scanner.next());
	String a = scanner.next();
	epsilon = Double.valueOf(a);
	System.out.println(T + " " + n +" " + epsilon);
	}
}

