import java.util.ArrayList;

public class DistributionDriver {
	public static void main(String[] args) {
		CPU_event rand = new CPU_event();
		int[] list = new int[6];
		
		for (int i = 0; i < 10000; i++) {
			list[rand.get_CPU_event()]++;
		}
		for (int n : list) {
			System.out.println(n);
		}
	}
}
