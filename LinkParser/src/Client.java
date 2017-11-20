import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Client {
	public static void main(String[] args) throws IOException, URISyntaxException {
		BufferedWriter writerFound = new BufferedWriter(new FileWriter("faculty_OH_working.txt"));
		BufferedWriter writerBroken = new BufferedWriter(new FileWriter("faculty_OH_broken.txt"));

		try {
			BufferedReader reader = new BufferedReader(new FileReader("DepartmentInfo.txt"));
			try {
				int count = 0;
				JFrame f = new JFrame();
				String line = null;
				while ((line = reader.readLine()) != null) {
					String pattern1 = "<contact>";
					String pattern2 = "</contact>";

					Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
					Matcher m = p.matcher(line);
					while (m.find()) {
						URL url = new URL (m.group(1));
						
						// We want to check the current URL
					    HttpURLConnection.setFollowRedirects(false);

					    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

					    // We don't need to get data
					    httpURLConnection.setRequestMethod("HEAD");

					    // Some websites don't like programmatic access so pretend to be a browser
					    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
					    int responseCode = httpURLConnection.getResponseCode();

					    // We only accept response code 200
					    if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
								writerBroken.write(m.group(1));
					    } else {
								writerFound.write(m.group(1));
								writerFound.newLine();
						}
					    System.out.println(m.group(1));
					}
				}
			} catch (IOException e) {
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
		System.out.println("Finished.");
		writerBroken.close();
		writerFound.close();
	}
}
