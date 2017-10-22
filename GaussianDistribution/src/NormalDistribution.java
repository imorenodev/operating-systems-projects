import java.util.Random;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

public class NormalDistribution {
	
	private static int loopK	= 1000 ;

	public static void main(String[] args) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		Random r1 = new Random();
		
		// check next Gaussian value  

	   float pct5 = 0;
	   float pct10 = 0;	
	   float pct15 = 0; 
	   float pct20 = 0; 
	   float pct50 = 0;
	   
	   for (int ii=0; ii<loopK; ii++)
	   { 
		   double d1	= r1.nextGaussian() ;
		   if (d1 >= 1.645) 
			   pct5++;
		   else if ((d1 >= 1.036) && (d1 < 1.645))
			   pct10++;
		   else if ((d1 >= 0.524) && (d1 < 1.036))
			   pct15++;
		   else if ((d1 >= 0.0) && (d1 < 0.524))
			   pct20++;
		   else
			   pct50++;
		   
		   System.out.printf("Gaussian value: %f\n"
				   ,d1
				   );
	   }
	   
	   System.out.printf	("\n=====\n5pct: %.3f\t 10pct: %.3f\t 15pct: %.3f\t 20pct: %.3f\t 50pct: %.3f\n====="
			   ,pct5/loopK*100
			   ,pct10/loopK*100
			   ,pct15/loopK*100
			   ,pct20/loopK*100
			   ,pct50/loopK*100
			   );

		writer.write	(String.format("\n=====\n5pct: %.3f\t 10pct: %.3f\t 15pct: %.3f\t 20pct: %.3f\t 50pct: %.3f\n====="
			   ,pct5/loopK*100
			   ,pct10/loopK*100
			   ,pct15/loopK*100
			   ,pct20/loopK*100
			   ,pct50/loopK*100
			   ));
		writer.close();

	}

}
/**
 * OUTPUT
  
=====
5pct: 5.000	 10pct: 9.600	 15pct: 14.000	 20pct: 19.700	 50pct: 51.700
=====
*/