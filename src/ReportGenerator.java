/**
 * Read config using {@link ConfigReader}, read data using {@link SourceReader}
 * and write report using {@link ReportWriter}
 * 
 * @author Andrew Pozdnyakov
 * @author infiltr1@gmail.com
 *
 */
public class ReportGenerator {
	public static void main(String args[]) {
		if (args.length != 3) {
			System.err.println("Not Enough or too many Input Arguments");
			return;
		}
		try {
			ConfigReader properties = new ConfigReader(args[0]);
			SourceReader sourceReader = new SourceReader(args[1]);
			ReportWriter rw = new ReportWriter(args[2], properties);

			rw.writeTitle();
			String tempData[];
			while ((tempData = sourceReader.getData()) != null) {
				rw.writeData(tempData);
			}
			sourceReader.close();
			rw.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}