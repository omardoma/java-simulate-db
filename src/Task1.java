import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Task1 {

	private static String[][] loadCSV(String filePath) throws IOException {
		ArrayList<String[]> lines = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(
				new FileReader(Task1.class.getClassLoader().getResource(filePath).getPath()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line.split(","));
		}
		reader.close();
		return lines.toArray(new String[][] {});
	}

	private static String[][] loadXML(String filePath) throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File(Task1.class.getClassLoader().getResource(filePath).getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("row");
		String[][] result = new String[nList.getLength()][3];
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				result[i][0] = eElement.getElementsByTagName("FIELD1").item(0).getTextContent();
				result[i][1] = eElement.getElementsByTagName("FIELD2").item(0).getTextContent();
				result[i][2] = eElement.getElementsByTagName("FIELD3").item(0).getTextContent();
			}
		}
		return result;
	}

	public static void findBestDeal() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Type the csv or xml file path to load the Database: ");
				// Read file based on its type csv or xml
				String filePath = sc.nextLine().trim();
				String type = Task1.class.getClassLoader().getResource(filePath).getFile()
						.substring(Task1.class.getClassLoader().getResource(filePath).getFile().length() - 3);
				String[][] database = null;
				if (type.equalsIgnoreCase("csv")) {
					database = loadCSV(filePath);
				} else if (type.equalsIgnoreCase("xml")) {
					database = loadXML(filePath);
				} else {
					throw new Exception();
				}
				System.out.print("Please enter a product name (Ex: iPhone 7): ");
				// Get the product name
				String productName = sc.nextLine().trim();
				// Find the best deal in loaded file
				String[] bestRow = null;
				for (String[] row : database) {
					if (row[1].trim().equalsIgnoreCase(productName)
							&& (bestRow != null ? Integer.parseInt(row[2]) < Integer.parseInt(bestRow[2]) : true)) {
						bestRow = row;
					}
				}
				System.out.println();
				// If a deal was found
				if (bestRow != null) {
					System.out.println("Store: " + bestRow[0]);
					System.out.println("Product: " + bestRow[1]);
					System.out.println("Price: " + bestRow[2]);
					System.out.println();
				} else { // If no deal was found
					System.out.println("No Records Found\n");
				}
				// Prompt the user for restart of the program or END
				System.out.print("again or end ? ");
				String end = sc.nextLine();
				// Quit the program if he types END and re run the whole process otherwise
				if (end.trim().equalsIgnoreCase("END")) {
					break;
				}
				System.out.println();

			} catch (Exception e) {
				System.err.println("File format is invalid or file does not exist.");
				System.out.println();
				continue;
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		findBestDeal();
	}
}
