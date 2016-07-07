import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <b> SourceReader(String file) throws FileNotFoundException, IOException </b>
 * <p>
 * Class provides methods to get data line by line
 * 
 * @param file
 *            - full path to source file
 * @exception FileNotFoundException
 *                if source file doesn't exist
 */
public class SourceReader {
	private BufferedReader sourceReader;

	SourceReader(String file) throws FileNotFoundException, IOException {
		sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-16"));
	}

	/**
	 * @return array, containing values from line or null
	 * @throws IOException
	 *             if error while reading next line
	 */
	public String[] getData() throws IOException {
		String temp = sourceReader.readLine();
		if (temp != null)
			return temp.split("	");
		return null;
	}

	void close() throws IOException {
		sourceReader.close();
	}
}
