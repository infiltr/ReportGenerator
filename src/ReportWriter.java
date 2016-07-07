import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * <b> public ReportWriter(String file, ConfigReader properties) throws
 * UnsupportedEncodingException, FileNotFoundException, IOException </b>
 * <p>
 * Class provides interface to prepare and write data to file.
 * 
 * @param file
 *            - full path to target file
 * @param properties
 *            - object of {@link ConfigReader}
 * @exception FileNotFoundException
 *                if target-file doesn't exist
 */
public class ReportWriter {
	private BufferedWriter writer;
	private ConfigReader properties;
	static private int totalHeight;

	public ReportWriter(String file, ConfigReader properties) throws FileNotFoundException, IOException {
		totalHeight = 0;
		this.properties = properties;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-16"));

	}

	/**
	 * 
	 * @param line1
	 *            string with date
	 * @param line2
	 *            string with name
	 * @return minimum height to contain information in concrete width
	 */
	private int getDataHeight(String line1, String line2) {
		int max = 1;
		int height = 1;
		if (line1.length() > properties.getSecondWidth()) {
			height = (int) Math.ceil((double) line1.length() / properties.getSecondWidth());
			if (max < height) {
				max = height;
			}
		}
		if (line2.length() > properties.getThirdWidth()) {
			height = (int) Math.ceil((double) line2.length() / properties.getThirdWidth());
			if (max < height) {
				max = height;
			}
		}
		return max;
	}

	/**
	 * create block nx3 with information and spaces
	 * 
	 * @param line
	 *            - string array with number(1), date(2) and name(3)
	 * @throws IOException
	 *             if can't write to file
	 */
	public void writeData(String[] line) throws IOException {
		int height = getDataHeight(line[1], line[2]);
		// each records = Nx3
		StringBuffer[][] dataLines = new StringBuffer[height * 3][];
		for (int i = 0; i < height * 3; i++) {
			dataLines[i] = new StringBuffer[3];
			for (int j = 0; j < 3; j++)
				dataLines[i][j] = new StringBuffer();
		}
		// left border
		for (int i = 0; i < height; i++)
			dataLines[i][0].append("| ");
		// write number and spaces
		dataLines[0][0].append(line[0]);
		for (int i = 0; i < height; i++) {
			int tempLength = properties.getFirstWidth() - dataLines[i][0].length() + 2;
			for (int j = 0; j < tempLength; j++)
				dataLines[i][0].append(" ");
		}

		// second border
		for (int i = 0; i < height; i++)
			dataLines[i][0].append(" | ");

		// parse date
		Vector<String> tempDate = new Vector<String>();
		String splittedDate[] = line[1].split("/");
		for (int i = 0; i < splittedDate.length; i++) {
			tempDate.add(splittedDate[i]);
			tempDate.add("/");
		}
		tempDate.remove(tempDate.size() - 1);

		// prepare date
		int curLength = 0;
		int curLine = 0;
		for (int i = 0; i < tempDate.size(); i++) {
			curLength += tempDate.get(i).length();
			if (curLength <= properties.getSecondWidth()) {
				dataLines[curLine][1].append(tempDate.get(i));
			} else {
				curLength = 0;
				curLine++;
				i--;
			}
		}

		// write spaces to next border
		for (int i = 0; i < height; i++) {
			int tempLength = properties.getSecondWidth() - dataLines[i][1].length();
			for (int j = 0; j < tempLength; j++)
				dataLines[i][1].append(" ");
		}
		// third border
		for (int i = 0; i < height; i++)
			dataLines[i][1].append(" | ");

		// parse name
		Vector<StringBuffer> splittedName = new Vector<StringBuffer>();
		splittedName.add(new StringBuffer(""));
		String characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
		{
			int i = 0;
			int k = 0;
			while (i != line[2].length()) {
				if (characters.contains(String.valueOf(Character.toLowerCase(line[2].charAt(i))))) {
					splittedName.get(k).append(line[2].charAt(i));
				} else {
					k++;
					splittedName.add(new StringBuffer(""));
					splittedName.get(k).append(line[2].charAt(i));
					splittedName.add(new StringBuffer(""));
					k++;
				}
				i++;
			}
		}
		Vector<String> tempName = new Vector<>();
		for (int i = 0; i < splittedName.size(); i++) {
			if (characters.contains(String.valueOf(Character.toLowerCase(splittedName.get(i).charAt(0))))) {
				Hyphenator h = new Hyphenator();
				tempName.addAll(h.hyphenateWord(splittedName.get(i).toString()));
			} else {
				tempName.add(splittedName.get(i).toString());
			}
		}
		// prepare name
		curLength = 0;
		curLine = 0;
		for (int i = 0; i < tempName.size(); i++) {
			if ((curLength == 0) && (tempName.get(i).toString().equals(" "))) {
				continue;
			}
			curLength += tempName.get(i).length();
			if (curLength <= properties.getThirdWidth()) {
				if ((curLength != 1) || (!tempName.get(i).toString().equals(" "))) {
					dataLines[curLine][2].append(tempName.get(i));
					System.out.print(curLength + " ->");
					System.out.println(tempName.get(i));
				}
			} else {
				curLength = 0;
				curLine++;
				i--;
			}
		}
		// additional line for extra height
		for (int i = 0; i < curLine - height + 1; i++) {
			dataLines[height + i][0].append("| ");
			for (int j = 0; j < properties.getFirstWidth() + 1; j++)
				dataLines[height + i][0].append(" ");
			dataLines[height + i][1].append("| ");
			for (int j = 0; j < properties.getSecondWidth() + 1; j++)
				dataLines[height + i][1].append(" ");
			dataLines[height + i][1].append("| ");
		}
		if (height <= curLine)
			height = curLine + 1;

		// write spaces to next border
		for (int i = 0; i < height; i++) {
			int tempLength = properties.getThirdWidth() - dataLines[i][2].length();
			for (int j = 0; j < tempLength; j++)
				dataLines[i][2].append(" ");
		}
		// third border
		for (int i = 0; i < height; i++)
			dataLines[i][2].append(" |");

		// write one record to file
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < 3; j++) {
				writer.write(dataLines[i][j].toString());
			}
			writer.write(System.getProperty("line.separator"));
			totalHeight++;
			checkAndSeparate();
		}
		writeLineSeparator();
	}

	/**
	 * write page's separator if needed
	 * 
	 * @throws IOException
	 *             if can't write to file
	 */
	public void checkAndSeparate() throws IOException {
		if (totalHeight == properties.getGenHeight()) {
			writer.write("~" + System.getProperty("line.separator"));
			writeTitle();
		}
	}

	/**
	 * write record's separator, like ---------------
	 * 
	 * @throws IOException
	 *             if can't write to file
	 */
	public void writeLineSeparator() throws IOException {
		if (totalHeight != 2) {
			for (int i = 0; i < properties.getGenWidth(); i++) {
				writer.write("-");
			}
			writer.write(System.getProperty("line.separator"));
			totalHeight++;
			checkAndSeparate();
		}
	}

	/**
	 * write title
	 * 
	 * @throws IOException
	 *             if can't write to file
	 */
	public void writeTitle() throws IOException {
		totalHeight = 2;
		writer.write("| " + properties.getFirstTitle());
		for (int i = 0; i < properties.getFirstWidth() - properties.getFirstTitle().length(); i++)
			writer.write(" ");
		writer.write(" | " + properties.getSecondTitle());
		for (int i = 0; i < properties.getSecondWidth() - properties.getSecondTitle().length(); i++)
			writer.write(" ");
		writer.write(" | " + properties.getThirdTitle());
		for (int i = 0; i < properties.getThirdWidth() - properties.getThirdTitle().length(); i++)
			writer.write(" ");
		writer.write(" |");

		writer.write(System.getProperty("line.separator"));
		for (int i = 0; i < properties.getGenWidth(); i++)
			writer.write("-");
		writer.write(System.getProperty("line.separator"));
	}

	public void close() throws IOException {
		writer.flush();
		writer.close();
	}
}
