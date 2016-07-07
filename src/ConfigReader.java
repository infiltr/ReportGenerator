import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class ParametersException extends RuntimeException {
	public ParametersException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}

/**
 * <b> public ConfigReader(String XMLfile) throws SAXException, IOException,
 * ParserConfigurationException </b>
 * <p>
 * Class provides interface to get settings from XML file. Few "getters" to get
 * parameters
 * 
 * @param file
 *            - full path to config file
 * @exception ParserConfigurationException
 *                if a DocumentBuilder cannot be created which satisfies the
 *                configuration requested.
 * @exception SAXException
 *                if any parse errors occur
 * @exception ParametersException
 *                if wrong size of fields
 */
public class ConfigReader {
	private int genSize[] = new int[2];
	private String fieldValue[] = new String[3];
	private int fieldSize[] = new int[3];

	public ConfigReader(String XMLfile) throws SAXException, IOException, ParserConfigurationException {
		File XMLFile = new File(XMLfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(XMLFile);
		NodeList nList = doc.getElementsByTagName("page");
		Node nNode = nList.item(0);
		Element eElement = (Element) nNode;
		genSize[0] = Integer.valueOf(eElement.getElementsByTagName("width").item(0).getTextContent());
		genSize[1] = Integer.valueOf(eElement.getElementsByTagName("height").item(0).getTextContent());
		nList = doc.getElementsByTagName("column");
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			eElement = (Element) nNode;
			fieldSize[i] = Integer.valueOf(eElement.getElementsByTagName("width").item(0).getTextContent());
			fieldValue[i] = eElement.getElementsByTagName("title").item(0).getTextContent();
		}

		if (getGenHeight() < 4) {
			throw new ParametersException("general height less than 4");
		}
		if (getGenWidth() < (getFirstWidth() + getSecondWidth() + getThirdWidth()) + 10) {
			throw new ParametersException("general width less than summ of widths");
		}

		if (getSecondWidth() < 4) {
			throw new ParametersException("second width less than 4");
		}
		if (getThirdWidth() < 5) {
			throw new ParametersException("third width less than 5");
		}
	}

	public int getGenWidth() {
		return genSize[0];
	}

	public int getGenHeight() {
		return genSize[1];
	}

	public int getFirstWidth() {
		return fieldSize[0];
	}

	public int getSecondWidth() {
		return fieldSize[1];
	}

	public int getThirdWidth() {
		return fieldSize[2];
	}

	public String getFirstTitle() {
		return fieldValue[0];
	}

	public String getSecondTitle() {
		return fieldValue[1];
	}

	public String getThirdTitle() {
		return fieldValue[2];
	}

}