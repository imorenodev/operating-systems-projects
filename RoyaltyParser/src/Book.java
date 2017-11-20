import java.util.Map;

public class Book {
	private String Title = "",
			   Author ="",
			   ASIN = "",
			   Marketplace = "",
			   UnitsSold = "",
			   UnitsRefunded ="",
			   NetUnitsSold ="",
			   RoyaltyType ="",
			   TransactionType ="",
			   Currency= "",
			   AvgListPriceWithoutTax ="",
			   AvgOfferPriceWithoutTax ="",
			   AvgFileSize ="",
			   AvgDeliveryCost ="",
			   Royalty ="";    		
		
		public Book(Map<String, String> bookData, int columnNum) {
			Title = bookData.get("Title");
			Author = bookData.get("Author");
			ASIN = bookData.get("ASIN"); 
			Marketplace = bookData.get("MarketPlace");
			UnitsSold = bookData.get("Units Sold");
			UnitsRefunded = bookData.get("Units Refunded"); 
			NetUnitsSold = bookData.get("Net Units Sold");
			RoyaltyType = bookData.get("Royalty Type");
			TransactionType = bookData.get("Transaction Type");
			Currency = bookData.get("Currency");
			AvgListPriceWithoutTax = bookData.get("Avg. List Price Without Tax");
			AvgOfferPriceWithoutTax = bookData.get("Avg. Offer Price Without Tax");
			AvgFileSize = bookData.get("Avg. File Size");
			AvgDeliveryCost = bookData.get("Avg. Delivery Cost");
			Royalty = bookData.get("Royalty");
		}
		
		public String getASIN() {
			return ASIN;
		}
		
		public String toString() {
			return ("Title: " + Title + " " +
				    "Author: " + Author + " " + 
					"Royalty: " + Royalty);
		}
}
