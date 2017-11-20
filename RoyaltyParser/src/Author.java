import java.util.*;

public class Author {
	private List<String> ASINList = new ArrayList<>();
	private String name = "Author";
	
	public Author(String aName, List<String> asinList) {
		if (aName != null) name = aName;
		ASINList = asinList;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getASINList() {
		return new ArrayList<>(ASINList);
	}
	
	public void setASINList(List<String> asinList) {
		ASINList = asinList;
	}
	
	public boolean removeASIN(String asin) {
		boolean removedSuccessfully = false;
		
		if (asin != null) {
			ASINList.remove(asin);
			removedSuccessfully = true;
		}
		
		return removedSuccessfully;
	}
	
	public boolean removeAllASIN(List<String> asinList) {
		boolean removedSuccessfully = false;
		
		if (asinList != null) {
			ASINList.removeAll(asinList);
		}

		return removedSuccessfully;
	}
	
	public String toString() {
		StringBuilder asinString = new StringBuilder();
		
		for (String s : ASINList) {
			asinString.append((" " + s.toString()));
		}

		return ("Name: " + name + " ASINs:" + asinString);
	}
}
