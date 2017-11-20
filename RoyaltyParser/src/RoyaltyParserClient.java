import java.util.*;

public class RoyaltyParserClient {

    public static void main(String[] args) {
        List<String> asins = new ArrayList<>();
		asins.add("B00R6VNRMO");
		asins.add("B00X09L2OG");
		
		Author claire = new Author("Claire", asins);
		
		AuthorReport amazonReport = new AuthorReport("Claire's Amazon Report", RetailerRawData.getData("KDPRoyalties.xlsx"), claire.getASINList());

		System.out.println(claire);
		
		System.out.println(amazonReport);
    }

}