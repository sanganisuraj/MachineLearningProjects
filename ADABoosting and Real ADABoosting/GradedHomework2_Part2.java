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


public class GradedHomework2_Part2 {
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

	
	public GradedHomework2_Part2(String filename) {
		filepath = Paths.get(filename);
	}

	public static void main(String args[]) throws IOException {
		GradedHomework2_Part2 parser = new GradedHomework2_Part2("C:\\MyData\\File1.txt");
		parser.linewise();
		Scanner s = new Scanner(System.in);
		realboost();
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
	
	public static void realboost()
	{
		double Z=0;
		hs = new holder[n];
		int counter=count;
		double garray[] = new double[n];
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
			System.out.println("Iteration Number " + iterations);
//		System.out.println(probability.length);
		counter=0;
		int v=1;
		int lerrorneg=0;
		int rerrorpos=0;
		int error;
		HashMap<Float, Double> a = new HashMap<Float, Double>();
		HashMap<Float, Double> b = new HashMap<Float, Double>();
		HashMap<Float, Double> c = new HashMap<Float, Double>();
		HashMap<Float, Double> d = new HashMap<Float, Double>();
		for(int i=1;i<count;i++)
		{
			double tempg=0;
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
//			System.out.println(k);
			while(k<errorarray.length)
			{
			if(ylist[k]==1) 
			{
//				System.out.println("right" + v);
				rerrorpos++;
//				System.out.println("Putting" + xlist[k] + " " + errorcalculationarray[k]);
				c.put(xlist[k], errorcalculationarray[k]);
				
//				System.out.println(rerror + "rerror");
//				wronglyclassified.put(k, ylist[k]);
			}
			if(ylist[k]==-1)
			{
				d.put(xlist[k], errorcalculationarray[k]);
			}
			
			k++;
			if(k==errorarray.length)
				break;
			
			}
//			System.out.println("A" + a);
//			System.out.println("B" + b);
//			System.out.println("C" + c);
//			System.out.println("D" + d);
//			
			double pwminus=0, prplus=0, pwplus=0, prminus=0;
			for(float key: a.keySet()){
		    	pwminus +=a.get(key);
	    	}
//	    	System.out.println(sa);
			for(float key: b.keySet()){
		    	prplus +=b.get(key);
	    	}
//			System.out.println(sb);
			for(float key: c.keySet()){
		    	pwplus +=c.get(key);
	    	}
//			System.out.println(sc);
			for(float key: d.keySet()){
		    	prminus +=d.get(key);
	    	}
//	    	System.out.println(sd);
	    	tempg = Math.sqrt(pwminus * prplus) + Math.sqrt(pwplus * prminus);
	    	hs[v] = new holder(prplus, prminus, pwplus, pwminus);
	    	garray[v] = tempg;
//	    	System.out.println(G);
		

			a = new HashMap<>();
			b = new HashMap<>();
			c = new HashMap<>();
			d = new HashMap<>();
			v++;
		}
		double min = Double.MAX_VALUE;
		int minindex=0;
		for(int i=1;i<garray.length;i++)
		{
			if(min>garray[i])
				{
					min=garray[i];
					minindex=i;
				}
		}
//		System.out.println("Minindex" + minindex);
//		System.out.println(hs[minindex].prminus);
//		System.out.println(hs[minindex].pwminus);
//		System.out.println(hs[minindex].pwplus);
//		System.out.println(hs[minindex].prplus);
		G = min;
		System.out.println("Classifier h = I(x<" + (minindex-0.5) + ")");
		System.out.println("G Error =" + G);
		ctminus = (Math.log((hs[minindex].pwplus + epsilon)/(hs[minindex].prminus + epsilon)))/2; 
		ctplus = (Math.log((hs[minindex].prplus + epsilon)/(hs[minindex].pwminus + epsilon)))/2;
		System.out.println("C_Plus ="  + ctplus);
		System.out.println("C_Minus = " + ctminus);
		htofx = new int[n];
		gtofx = new double[n];
//		ftofx = new double[n];
		for(int i=0;i<htofx.length;i++)
		{
			if(i<minindex)
				htofx[i]=1;
			else
				htofx[i]=-1;
		}
		for(int i=0;i<gtofx.length;i++)
		{
			if(htofx[i]==1)
				gtofx[i]=ctplus;
			else
				gtofx[i]=ctminus;
		}
//		for(int i=0;i<gtofx.length;i++)
//			System.out.println(gtofx[i]);
		prenormalizedpi = new double[n];
		newpi = new double[n];
		for(int i=0;i<prenormalizedpi.length;i++)
		{
			prenormalizedpi[i] = errorcalculationarray[i] * Math.exp(-ylist[i]*gtofx[i]);
		}
		Z = 2*G;
		System.out.println("Normalization Factor Z = " + Z);
		if(Zt==0)
			Zt=Z;
		else
			Zt = Zt*Z;
//		for(int i=0;i<prenormalizedpi.length;i++)
//			System.out.println(prenormalizedpi[i]);
		for(int i=0;i<newpi.length;i++)
		{
			newpi[i] = prenormalizedpi[i]/Zt;
		}
		System.out.println("Pi after Normalization = ");
		for(int i=0;i<newpi.length;i++)
			System.out.println(newpi[i]);
		for(int i=0;i<gtofx.length;i++)
		{
//			ftofx[i] = gtofx[i];
//			System.out.println("PRinting" + gtofx[i]);
//			System.out.println(ftofx[i]);
			System.out.println("f(x) = ");
			if(htofx[i]==1)
				ftofx[i]=ftofx[i]+ctplus;
			else
				ftofx[i]=ftofx[i]+ctminus;
			System.out.println(ftofx[i]);
		}
		int ct=0;
		for(int i=0;i<htofx.length;i++)
		{
			if(htofx[i]!=ylist[i])
			{
				ct++;
			}
		}
		et=(double) ct/n;
		System.out.println("Boosted Classifier Error" + et);
		System.out.println("Bound on Error" + Zt);
		for(int i=0;i<count;i++)
			errorcalculationarray[i]=newpi[i];
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
