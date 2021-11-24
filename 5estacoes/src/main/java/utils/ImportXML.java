/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import models.Station;
import models.StationLine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author pcoelho
 */
public class ImportXML {

    // Hasmap of line name and Key
    public static HashMap<Character, String> lines = new HashMap<>();
    // ArrayList of StationLine (station name  and lines keys)
    public static ArrayList<StationLine> stationLine = new ArrayList<>();

    //ArrayList of station and position in key line
    public static ArrayList<Station> stationInOrderInLine = new ArrayList<>();

    // Alert 
    private static final Alert a = new Alert(Alert.AlertType.NONE);

    /**
     * Method receives a file and read the XML
     *
     * @param file
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     *
     */
    public static void importXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
        Boolean xmlProcessed = false;

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Parse File
            Document doc = db.parse(file);

            // optional, but recommended, normalize 
            doc.getDocumentElement().normalize();

            // get list of nodes child of Data
            NodeList listOfNodes = doc.getDocumentElement().getChildNodes();

            // go through nodes
            for (int i = 0; i < listOfNodes.getLength(); i++) {

                // node from list of nodes
                Node node = listOfNodes.item(i);

                // test if node is an element
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    //Cast to element
                    Element element = (Element) node;

                    System.out.println(element.getNodeName());

                    if (element.getNodeName().contains("Lines")) {

                        // read the line of Lines node
                        readLinesXml(node);
                    } else if (element.getNodeName().contains("Stations")) {

                        //read the Staions 
                        readStationsXml(node);
                    } else {
                        // read the order of the line
                        readLineOrderXml(node);
                    }
                }
            }

            xmlProcessed = true;

        } catch (ParserConfigurationException | SAXException | IOException e) {

            e.printStackTrace();
            //TODO alert user

        } finally {

            if (xmlProcessed) {

                System.out.println("FILE WAS READ SUCCESFULLY");
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("XML FILE WAS READ SUCCESFULLY");
                a.show();

                System.out.println(lines);

                for (StationLine st : stationLine) {

                    System.out.println(st.getNameOfStation() + st.getLines());
                }

                for (Station st : stationInOrderInLine) {

                    System.out.println(st.getName() + " " + st.getKeyOfLine() + " " + st.getPosition());
                }

            } else {

                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("XML file is not a subway file.");
                a.show();

                System.out.println("ERROR!!!!!!!!!!!");
            }

        }

    }

    /**
     * Method receives receive the node of lines and reads the name of lines and
     * ke
     *
     * @param node
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static void readLinesXml(Node node) throws ParserConfigurationException, SAXException, IOException {

        // Get the list of nodes of line
        NodeList listOfNodes = node.getChildNodes();
        for (int i = 0; i < listOfNodes.getLength(); i++) {

            // get node from each item of lines
            Node node1 = listOfNodes.item(i);

            if (node1.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node1;

                // Get key attribute
                char key = element.getAttribute("Key").charAt(0);

                // Get name of line
                String name = element.getElementsByTagName("Name").item(0).getTextContent();
                System.out.println("Line--> key: " + key + " name: " + name);

                // check values are unique and add to lines hashmap
                if (!lines.containsKey(key) && !lines.containsValue(name)) {

                    lines.put(key, name);

                } else {

                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("XML Line consistency do not exist.");
                    a.show();
                    throw new ParserConfigurationException("XML Line consistency do not exist.");

                }
            }

        }

    }

    /**
     * Method receives receive the node of starions and reads the name of the
     * station and the lines keys
     *
     * @param node
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     *
     */
    public static void readStationsXml(Node node) throws ParserConfigurationException, SAXException, IOException {

        // Get the list of nodes of Station
        NodeList listOfNodes = node.getChildNodes();

        for (int i = 0; i < listOfNodes.getLength(); i++) {

            Node node1 = listOfNodes.item(i);

            if (node1.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node1;

                //Station name
                String name = element.getElementsByTagName("Name").item(0).getTextContent();

                // number of lines
                int numberLines = element.getElementsByTagName("Line").getLength();

                // new Arrylist of caracter to add to Station name
                ArrayList<Character> listOfStation = new ArrayList();

                // each line
                for (int j = 0; j < numberLines; j++) {

                    char lineChar = element.getElementsByTagName("Line").item(j).getTextContent().charAt(0);
                    // check if line key exist in Line
                    if (lines.containsKey(lineChar)) {
                        listOfStation.add(lineChar);

                    } else {

                        a.setAlertType(Alert.AlertType.ERROR);
                        a.setContentText("XML Station consistency Line do not exist.");
                        a.show();
                        throw new ParserConfigurationException("XML Station consistency Line do not exist.");

                    }

                    System.out.println("Station name --> " + name + " line: " + element.getElementsByTagName("Line").item(j).getTextContent());

                }

                // if the name of the station do not exist add to stationLine 
                if (!stationExist(name)) {

                    stationLine.add(new StationLine(name, listOfStation));
                } else {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("XML Station consistency Line do not exist.");
                    a.show();
                    throw new ParserConfigurationException("XML Station consistency Line do not exist.");

                }

            }

        }

    }

    /**
     * Check if the Station is in stationline arrayList
     *
     * @param nameStation
     * @return boolean
     */
    public static boolean stationExist(String nameStation) {

        for (StationLine st : stationLine) {

            if (st.getNameOfStation().equals(nameStation)) {
                return true;
            }

        }

        return false;

    }

    /**
     * Check if the Station is in stationline arrayList and line char is in that
     * line
     *
     * @param nameStation
     * @return boolean
     */
    public static boolean statioLineExist(String nameStation, char line) {

        for (StationLine st : stationLine) {

            if (st.getNameOfStation().equals(nameStation) && st.getLines().contains(line)) {
                return true;
            }

        }

        return false;

    }

    /**
     * Returns the number of station in a line
     *
     * @param line
     * @return int
     */
    public static int numberOfStationsInLine(char line) {
        int numberOfStationInLine = 0;

        for (StationLine st : stationLine) {

            if (st.getLines().contains(line)) {
                numberOfStationInLine++;
            }

        }

        return numberOfStationInLine;

    }
        
    
    

    /**
     * Method receives receive the node of Trips and reads the name of staion,
     * line and create the order
     *
     * @param node
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     *
     */
    public static void readLineOrderXml(Node node) throws ParserConfigurationException, SAXException, IOException {
        // TODo check if is in new stationIn OrderLine
        NodeList listOfNodes = node.getChildNodes();

        // position in the line
        int position = 0;
        for (int i = 0; i < listOfNodes.getLength(); i++) {

            Node node1 = listOfNodes.item(i);

            Station station = new Station();

            if (node1.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node1;
                String departure = element.getElementsByTagName("Departure").item(0).getTextContent();

                String arrival = element.getElementsByTagName("Arrival").item(0).getTextContent();
                char line = element.getElementsByTagName("Line").item(0).getTextContent().charAt(0);

                System.out.println("Departure: " + departure + " Arrival: " + arrival + " line: " + line);

                // check if departure and arrival stion exist in station line 
                if (statioLineExist(departure, line) && statioLineExist(arrival, line)) {

                    station.setName(departure);
                    station.setKeyOfLine(line);
                    station.setPosition(position);
                    stationInOrderInLine.add(station);

                    // In case that is the last station of the line add the arrival
                    if (position == numberOfStationsInLine(line) - 2) {
                        Station station2 = new Station();

                        position++;
                        station2.setName(arrival);
                        station2.setKeyOfLine(line);
                        station2.setPosition(position);
                        stationInOrderInLine.add(station2);
                        position = -1;
                    }

                }
                // increment position
                position++;

            }

        }
    }

    /**
     * Choose an XML File in project root directory
     *
     *
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    public static void fileChooseXML() throws ParserConfigurationException, SAXException, IOException {

        // Initialize FileChoose Object  
        FileChooser fc = new FileChooser();

        // Get The Path of the appilcation
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

        // Set the title of the popup window
        fc.setTitle("Select a xml File to import a subway");

        // Set the initial directory
        fc.setInitialDirectory(new File(currentPath));

        // filter XML file
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        // Select file
        File selectedFile = fc.showOpenDialog(null);

        // if exist send file to testXML method
        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
            importXMLFile(new File(selectedFile.getAbsolutePath()));

        } else {

            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("File was not selected.");
            a.show();

        }

    }

}
