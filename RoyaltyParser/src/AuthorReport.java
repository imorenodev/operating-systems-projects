import java.util.ArrayList;
import java.util.List;

public class AuthorReport {
	List<Book> authorsBookList = new ArrayList<>();
	String reportName = "";
	
	public AuthorReport(String aReportName, List<Book> rawDataBookList, List<String> asins) {
		reportName = aReportName;
		buildAuthorsBookList(rawDataBookList, asins);
	}
	
	public List<Book> getAuthorReport() {
		return authorsBookList;
	}

	private void buildAuthorsBookList(List<Book> rawDataBookList, List<String> asins) {
		for (String asin : asins) {
			for (Book book : rawDataBookList) {
				if (book.getASIN().equals(asin)) authorsBookList.add(book);
			}
		}
	}
	
	public String toString() {
		StringBuilder bookListStrings = new StringBuilder();
		
		for (Book book : authorsBookList) {
			bookListStrings.append(book + "\n");
		}

		return "Report Name: " + reportName  + "\n"
							  + bookListStrings;
	}
}
