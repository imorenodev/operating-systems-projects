/**
 * Purpose: RecursionClient class demonstrates the solutions to the Recursive Function Exercises
 * @author Ian Moreno
 * @date 09/25/2017
 */
public class RecursionClient {
	public static void main(String[] args) {
		System.out.println("Problem #1)  " + problemOne(8, 12));
		System.out.println("Problem #2)  " + problemTwo(problemTwo(problemTwo(problemTwo(problemTwo(10))))));
		System.out.println("Problem #3)  " + problemThree(30, 12));
		System.out.println("Problem #4)  " + problemFour(32));
		System.out.println("Problem #5)  " + problemFive(5));
		System.out.println("Problem #6)  " + problemSix(35, 8));
		System.out.println("Problem #7)  " + problemSeven(10));
		System.out.println("Problem #8)  " + problemEight(problemEight(problemEight(problemEight(18)))));
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #1
	 * @param x int variable representing x
	 * @param y int variable representing y
	 * @return int value representing the solution to the problem
	 */
	private static int problemOne(int x, int y) {
		if (x < y) return problemOne( x+1, y-2 ) + 3;
		if (x > y) return problemOne( y-1, x ) - 1;
		return ( x*x + y*y );
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #2
	 * @param x int variable representing x
	 * @return int value representing the solution to the problem
	 */
	private static int problemTwo(int x) {
		if (x > 7) return problemTwo( x-5 ) - 3;
		if (x > 3 && x <= 7) return problemTwo( x+2 ) + 2;
		return x + 5;
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #3
	 * @param x int variable representing x
	 * @param y int variable representing y
	 * @return int value representing the solution to the problem
	 */
	private static int problemThree(int x, int y) {
		if (x > y) return Math.max(problemThree( x-4, y+3 ), problemThree( y, x ));
		return x * y;
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #4
	 * @param x int variable representing x
	 * @return int value representing the solution to the problem
	 */
	private static int problemFour(int x) {
		if ((x % 2) == 0) return problemFour( (x/2) - 1 ) + 3;
		if (((x % 2) != 0) && (x >= 0)) return 2 * problemFour( x-3 ) - 5;
		return (x*x) - 3;
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #5
	 * @param n int variable representing n
	 * @return int value representing the solution to the problem
	 */
	private static int problemFive(int n) {
		if (n == 1) return 8;
		return 2 * problemFive( n-1 ) - 4;
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #6
	 * @param x int variable representing x
	 * @param y int variable representing y
	 * @return int value representing the solution to the problem
	 */
	private static int problemSix(int x, int y) {
		if (x >= y) 	return problemSix( x-y, y+2 ) + y;
		return (x*x) - y;
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #7
	 * @param n int variable representing n
	 * @return int value representing the solution to the problem
	 */
	private static int problemSeven(int n) {
		if (n == 1) return 1;
		return problemSeven( n-1 ) + 5;
	}
	
	/**
	 * Purpose: method returns the recursive solution to problem #8
	 * @param x int variable representing x
	 * @return int value representing the solution to the problem
	 */	
	private static int problemEight(int x) {
		if (x >= 10) return problemEight( x-5 ) - 2;
		if ((x >= 7) && (x < 10)) return problemEight( x+4 ) + 6;
		return x - 4;
	}
}


/** Program Output:
Problem #1)  136
Problem #2)  7
Problem #3)  396
Problem #4)  10
Problem #5)  68
Problem #6)  41
Problem #7)  46
Problem #8)  -6
 */