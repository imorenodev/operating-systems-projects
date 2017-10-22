import java.util.Random ; 

public class CPU_event	// this class returns a result from a range by percentage
{
	private int	event ;		
	private int	rangeResult ;
	
	public void CPU_event()
	{
	
	}
	
	public int get_CPU_event()
	{
		Random random__X	= new Random();	//#010 declare random object
		
		double event	= random__X.nextGaussian() ;
		if (event >= 1.645)  // 5% - termination
			rangeResult = 1;
		else if ((event >= 1.036) && (event < 1.645)) // 10% - interrupt
			rangeResult = 2;
		else if ((event >= 0.524) && (event < 1.036)) // 15% - page fault
			rangeResult = 3;
		else if ((event >= 0.0) && (event < 0.524)) // 20% - block I/O
			rangeResult = 4;
		else // 50% - other
			rangeResult = 5;

		return rangeResult ;
	}
}