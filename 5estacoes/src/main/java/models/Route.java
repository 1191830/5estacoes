/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import DAOs.StationDAO;
import DAOs.TripDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import utils.TimeParse;

/**
 *
 * @author nelso
 */
public class Route {

    private int id;
    private String name;
    private int position;
    // All routes
    public static ArrayList<ArrayList<Station>> routes;
    private ArrayList<Station> stations;
    private static ArrayList<Route> allRoutes = new ArrayList<>();
    private String duration;
    private double price;
    private int numberOfStations;
    private int changesOfLine;

    StationDAO stationDAO = new StationDAO();
    TripDAO tripDAO = new TripDAO();

    /**
     * Constructor empty
     */
    public Route() {
    }

    /**
     * Constructor complete
     *
     * @param id
     * @param name
     * @param position
     */
    public Route(int id, String name, int position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Route(ArrayList<Station> stations) {
        this.stations = stations;
    }

    /**
     * Getter id
     *
     * @return Integer
     */
    public int getId() {
        return id;
    }

    /**
     * Getter name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Getter stations
     *
     * @return ArrayList
     */
    public ArrayList<Station> getStations() {
        return stations;
    }

    /**
     * getter duration
     *
     * @return String
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Getter Price
     *
     * @return double
     */
    public double getPrice() {
        return price;
    }

    /**
     * Getter numberOfStations
     *
     * @return int
     */
    public int getNumberOfStations() {
        return numberOfStations;
    }

    /**
     * Getter ChangesOfLine
     * @return int
     */
    public int getChangesOfLine() {
        return changesOfLine;
    }
    
    /**
     * Setter id
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Setter stations
     *
     * @param stations
     */
    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    /**
     * setter Duration
     *
     * @param time
     */
    public void setDuration(int time) {
        String duration = TimeParse.timeToString(time);
        this.duration = duration;
    }

    /**
     * Setter price
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setter numberOfStations
     *
     * @param numberOfStations
     */
    public void setNumberOfStations(int numberOfStations) {
        this.numberOfStations = numberOfStations;
    }

    /**
     * Setter changesOfLine
     * @param changesOfLine 
     */
    public void setChangesOfLine(int changesOfLine) {
        this.changesOfLine = changesOfLine;
    }
    
    

    /**
     * Calculates the price of the Route, adding all the stations price
     */
    public void calculatePrice() {
        this.price = 0;
        for (int i = 0; i < stations.size(); i++) {
            this.price += stations.get(i).getPrice();
        }
    }
    
    /**
     * Calculates how many changes of line in this route
     */
    public void calculateChangesOfLine(){
        this.numberOfStations = 0;
        //first line
        int lineA = stations.get(0).getLine().getId();
        for (int i = 0; i < stations.size(); i++) {
            //current line
            int lineB = stations.get(i).getLine().getId();
            //if current line is different from line A that means there was a change
            if(lineA != lineB){
            
                this.changesOfLine ++;
                //line A is now the current line
                lineA = stations.get(i).getLine().getId();
            }
  
        }
        this.numberOfStations = stations.size();
    }

    /**
     * Calculates the time of the Route
     */
    public void calculateTime() throws SQLException {
        int time = 0;
        for (int i = 1; i < stations.size(); i++) {
            time += tripDAO.getDurationTrip(stations.get(i-1),stations.get(i));
        }
        this.duration = TimeParse.timeToString(time);
    }

    /**
     * Presents all the station in this route separated by '->'
     *
     * @return
     */
    public String toStringRouteStations() {

        String course = "";
        for (int i = 0 ; i < stations.size() ; i++) {
            course += stations.get(i).getName() + " (" + stations.get(i).getLine().getKey() + ")" + " -> ";
            //if this route has 14 stations we might need to break the line to show in the table
            if(i % 14 == 0 && i > 0){
                course += "\n";
            }
        }

        //return the string appended, eliminating the last separator
        return course.substring(0, course.length() - 4);
    }

    /**
     * Default to string
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", name=" + name
                + ", position=" + position + '}';
    }

    /**
     * Get Arraylist of Arraylist of Station and Finds Route
     *
     * @param nameOfStartStation
     * @param nameOfEndSation
     *
     */
    public ArrayList<Route> populateMetroAndFindRoute(String nameOfStartStation, String nameOfEndSation) throws SQLException {
        // Initialize Metro
        ArrayList<ArrayList<Station>> metro = new ArrayList<>();

        //Populate metro
        metro = stationDAO.getLinesAndStation();

        //Initialize start and end station
        Station startingStation = new Station();
        startingStation.setName(nameOfStartStation);
        Station endingStation = new Station();
        endingStation.setName(nameOfEndSation);

        //Initialize Arraylist of routes
        routes = new ArrayList<>();

        //Inilializa ArrayList of rota
        ArrayList<Station> rota = new ArrayList<>();
        // get station by name
        rota.add(startingStation);
        //Start the algorithm
        startAlgoFindRoute(metro, startingStation, endingStation, rota);

        //System.out.println("FIX FIRST STATION");
        //Show each rota in routes
        showRoutesFixtStartStation();

        return allRoutes;
    }

    /**
     * Go one Position Up the Line and if not in route add to the route
     *
     * @param metro
     * @param line
     * @param position
     * @param endStation
     * @param rota
     *
     */
    public static void upTheLineOnePosition(ArrayList<ArrayList<Station>> metro, int line, int position, Station endStation, ArrayList<Station> rota) {

        // Check against edge case tha te line only as one station
        if (position + 1 <= metro.get(line).size()) {

            Station nextStation = metro.get(line).get(position + 1);
            ArrayList<Station> route = deepCopyOfRota(rota);

            //
            if ((!checkIfLineContainsStation(route, nextStation) && !checkIfStationIsLastStation(nextStation, endStation))) {
                route.add(nextStation);
                startAlgoFindRoute(metro, nextStation, endStation, route);
            }

            if (checkIfStationIsLastStation(nextStation, endStation)) {
                route.add(nextStation);
                routes.add(route);
            }
        }
    }

    /**
     * Go one Position Down the Line and Add to route
     *
     * @param metro
     * @param line
     * @param position
     * @param endStation
     * @param rota
     *
     */
    public static void downTheLineOnePosition(ArrayList<ArrayList<Station>> metro, int line, int position, Station endStation, ArrayList<Station> rota) {

        //    if (position - 1 >= 0) {
        Station nextStation = metro.get(line).get(position - 1);
        ArrayList<Station> route = deepCopyOfRota(rota);

        if ((!checkIfLineContainsStation(route, nextStation) && !checkIfStationIsLastStation(nextStation, endStation))) {
            route.add(nextStation);
            startAlgoFindRoute(metro, nextStation, endStation, route);

        }

        //check if the Station name is the same as end Station
        if (checkIfStationIsLastStation(nextStation, endStation)) {
            route.add(nextStation);
            routes.add(route);
        }

        //  }
    }

    /**
     * Find Route with submay map, start Station endStation, And current route
     *
     * @param metro
     * @param startStation
     * @param endStation
     * @param rota
     */
    public static void startAlgoFindRoute(ArrayList<ArrayList<Station>> metro, Station startStation, Station endStation, ArrayList<Station> rota) {

        // check every line of the metro
        for (int line = 0; line < metro.size(); line++) {

            // if the line contains the stating station
            if (checkIfLineContainsStation(metro.get(line), startStation)) {

                //what is is position of the station in the line
                int position = positionOfStationInLine(metro.get(line), startStation);

                // if the stating station position is at the start of line
                if (position == 0) {

                    // System.out.println("Seguir para cima");
                    // Move up the line
                    upTheLineOnePosition(metro, line, position, endStation, rota);

                    // if the stating station position is at the end of line
                } else if (position == metro.get(line).size() - 1) {

                    // System.out.println("Seguir para baixo");
                    //Move down the line
                    downTheLineOnePosition(metro, line, position, endStation, rota);

                } else {

                    // System.out.println("Seguir para cima  +");
                    // Move up the line
                    upTheLineOnePosition(metro, line, position, endStation, rota);

                    // System.out.println("Seguir para baixo -");
                    //Move down the line
                    downTheLineOnePosition(metro, line, position, endStation, rota);
                }
            }
        }
    }

    /**
     * Check if a line or Route contains the station name
     *
     * @param line
     * @param station
     * @return boolean
     */
    public static boolean checkIfLineContainsStation(ArrayList<Station> line, Station station) {

        for (Station st : line) {

            if (st.getName().equals(station.getName())) {

                return true;
            }
        }
        return false;
    }

    /**
     * Gets the position of the station name int a line
     *
     * @param line
     * @param station
     * @return boolean
     */
    public static int positionOfStationInLine(ArrayList<Station> line, Station station) {
        int pos = 0;
        for (Station st : line) {

            if (st.getName().equals(station.getName())) {

                return pos;
            }
            pos++;
        }

        return pos;

    }

    /**
     * Check if next Station is the end station
     *
     * @param nextStation
     * @param endStation
     * @return boolean
     */
    public static boolean checkIfStationIsLastStation(Station nextStation, Station endStation) {

        if (nextStation.getName().equals(endStation.getName())) {

            return true;
        }

        return false;
    }

    /**
     * Create a copy of the route to Find new routes
     *
     * @param rota
     * @return boolean
     */
    public static ArrayList<Station> deepCopyOfRota(ArrayList<Station> rota) {

        ArrayList<Station> newRota = new ArrayList<>();

        for (Station st : rota) {
            Station newSt = new Station();
            newSt = st;
            newRota.add(newSt);

        }

        return newRota;
    }

    /**
     * Create a copy of the route to Find new routes
     */
    public void showRoutesFixtStartStation() throws SQLException {
        if (routes != null) {
            for (int i = 0; i < routes.size(); i++) {
                stations = routes.get(i);

                for (int j = 0; j < routes.get(i).size(); j++) {
                    if (j == 0) {
                        //FIX lack of line
                        Line line = routes.get(i).get(j + 1).getLine();
                        routes.get(i).get(j).setLine(line);
                        Station firstStation = stationDAO.getStation(routes.get(i).get(j).getName(), line);
                        routes.get(i).get(j).setId(firstStation.getId());
                        routes.get(i).get(j).setPrice(firstStation.getPrice());
                    }
                }

                Route newRoute = new Route();
                newRoute.setStations(stations);
                newRoute.calculatePrice();
                newRoute.calculateChangesOfLine();
                newRoute.calculateTime();
                allRoutes.add(newRoute);
            }
        }
    }

}
