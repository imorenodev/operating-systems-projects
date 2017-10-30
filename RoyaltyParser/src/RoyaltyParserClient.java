import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class RoyaltyParserClient {
	private static List<Book> bookList = null;

    public static void echoAsCSV(Sheet sheet) {
        Row row = null;
        Row headerRow = null;
        Iterator<Row> rowIterator = sheet.iterator(); 
        Map<String, String> bookData = new HashMap<>();
        bookList = new ArrayList<>();
        
        // remove first two rows of spreadsheet
        rowIterator.next();
        rowIterator.remove();
        	headerRow = rowIterator.next();
		rowIterator.remove();
        
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            
            if (row.getCell(0) != null) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					//System.out.print("\"" + row.getCell(j) + "\";");
					if (row.getCell(j).toString() != "") {
						bookData.put(headerRow.getCell(j).toString(), row.getCell(j).toString());
					}
				}
				bookList.add(new Book(bookData, row.getLastCellNum()));
            }
            rowIterator.remove();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputStream inp = null;
        try {
            inp = new FileInputStream("KDPRoyalties.xlsx");
            Workbook wb = WorkbookFactory.create(inp);

			//System.out.println(wb.getSheetAt(0).getSheetName());
			echoAsCSV(wb.getSheetAt(0));
        } catch (InvalidFormatException ex) {
            Logger.getLogger(RoyaltyParserClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RoyaltyParserClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoyaltyParserClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inp.close();
            } catch (IOException ex) {
                Logger.getLogger(RoyaltyParserClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for (int i = 0; i < 3; i++) {
        		bookList.remove(bookList.size() - 1);
        }

        for (Book book : bookList) {
			System.out.println(book);
        }
        
        List<String> asins = new ArrayList<>();
        asins.add("1234");
        asins.add("1337");
        
        Author claire = new Author("Claire", asins);
        
        System.out.println(claire);
    }

    
}