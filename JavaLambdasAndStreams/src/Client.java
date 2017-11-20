import java.util.ArrayList;
import java.util.List;

public class Client {
	public static void main(String[] args) {
		List<Integer> aList = new ArrayList<>();
		aList.add(1);
		aList.add(2);
		aList.add(3);
		aList.add(4);
		
		//aList.forEach((Integer value) -> System.out.println(value));
		//aList.forEach((value) -> System.out.println(value)); // java will infer the parameter type when using lambdas
		//aList.forEach(value -> System.out.println(value)); // can remove parentheses from single argument only
		aList.stream().map(String::valueOf).forEach(System.out::println); // pass in the method reference directly and no argument is even needed.
		
		System.out.println(aList.stream().filter(e -> e % 2 == 0).mapToInt(e -> e * 2).sum());
	}
}
