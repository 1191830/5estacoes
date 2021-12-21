/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author pcoelho
 */
public class RouteTest {

    private Route route;

    public RouteTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        route.setId(1);

    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Route.
     */
    @Test
    public void testGetId() {
        route = new Route();
        route.setId(1);
        int expResult = 1;
        int result = route.getId();
        assertEquals(expResult, result);

    }

    /**
     * Test of getName method, of class Route.
     */
    @Test
    public void testGetName() {

        route = new Route();
        route.setName("teste");
        String expResult = "teste";
        String result = route.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of getPosition method, of class Route.
     */
    @Test
    public void testGetPosition() {

        route = new Route();
        route.setPosition(0);
        int expResult = 0;
        int result = route.getPosition();
        assertEquals(expResult, result);

    }

    /**
     * Test of getStations method, of class Route.
     */
    @Test
    public void testGetStations() {
        route = new Route();

        ArrayList<Station> aSt = new ArrayList<>();
        Station st1 = new Station();
        st1.setName("teste1");
        Station st2 = new Station();
        st2.setName("teste2");
        aSt.add(st1);
        aSt.add(st2);
        route.setStations(aSt);
        String expResult = "teste1";
        String result = route.getStations().get(0).getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of getDuration method, of class Route.
     */
    @Test
    public void testGetDuration() {
        route = new Route();
        route.setDuration(10);
        String expResult = "00:10";
        String result = route.getDuration();
        assertEquals(expResult, result);

    }

    /**
     * Test of getPrice method, of class Route.
     */
    @Test
    public void testGetPrice() {
        route = new Route();
        route.setPrice(3.00);
        double expResult = 3.00;
        double result = route.getPrice();
        assertEquals(expResult, result);

    }

    /**
     * Test of getNumberOfStations method, of class Route.
     */
    @Test
    public void testGetNumberOfStations() {
        route = new Route();
        route.setNumberOfStations(10);
        int expResult = 10;
        int result = route.getNumberOfStations();
        assertEquals(expResult, result);

    }

    /**
     * Test of getChangesOfLine method, of class Route.
     */
    @Test
    public void testGetChangesOfLine() {
        
        route = new Route();
        route.setChangesOfLine(10);
        int expResult = 10;
        int result = route.getChangesOfLine();
        assertEquals(expResult, result);   
 
    }

    /**
     * Test of setId method, of class Route.
     */
    @Test
    public void testSetId() {   
        route = new Route();
        route.setId(1);
        int expResult = 1;
        int result = route.getId();
        assertEquals(expResult, result);
     
    }

    /**
     * Test of setName method, of class Route.
     */
    @Test
    public void testSetName() {

        route = new Route();
        route.setName("teste");
        String expResult = "teste";
        String result = route.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setPosition method, of class Route.
     */
    @Test
    public void testSetPosition() {
        
        route = new Route();
        route.setPosition(0);
        int expResult = 0;
        int result = route.getPosition();
        assertEquals(expResult, result);

    }

    /**
     * Test of setStations method, of class Route.
     */
    @Test
    public void testSetStations() {
        
         route = new Route();
        ArrayList<Station> aSt = new ArrayList<>();
        Station st1 = new Station();
        st1.setName("teste1");
        Station st2 = new Station();
        st2.setName("teste2");
        aSt.add(st1);
        aSt.add(st2);
        route.setStations(aSt);
        String expResult = "teste1";
        String result = route.getStations().get(0).getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setDuration method, of class Route.
     */
    @Test
    public void testSetDuration() {
        route = new Route();
        route.setDuration(10);
        String expResult = "00:10";
        String result = route.getDuration();
        assertEquals(expResult, result);

    }

    /**
     * Test of setPrice method, of class Route.
     */
    @Test
    public void testSetPrice() {
        
        route = new Route();
        route.setPrice(3.00);
        double expResult = 3.00;
        double result = route.getPrice();
        assertEquals(expResult, result);
    
    }

    /**
     * Test of setNumberOfStations method, of class Route.
     */
    @Test
    public void testSetNumberOfStations() {
        route = new Route();
        route.setNumberOfStations(10);
        int expResult = 10;
        int result = route.getNumberOfStations();
        assertEquals(expResult, result);

    }

    /**
     * Test of setChangesOfLine method, of class Route.
     */
    @Test
    public void testSetChangesOfLine() {
        route = new Route();
        route.setChangesOfLine(10);
        int expResult = 10;
        int result = route.getChangesOfLine();
        assertEquals(expResult, result);   

    }

    /**
     * Test of calculatePrice method, of class Route.
     */
    @Test
    public void testCalculatePrice() {
        route = new Route();
        ArrayList<Station> aSt = new ArrayList<>();
        Station st1 = new Station();
        st1.setPrice(1);
        Station st2 = new Station();
        st2.setPrice(2);
        aSt.add(st1);
        aSt.add(st2);
        route.setStations(aSt);
        double expResult = 3;
        double result = route.getStations().get(0).getPrice() + route.getStations().get(1).getPrice();
        assertEquals(expResult, result);
        

    }

    /**
     * Test of calculateChangesOfLine method, of class Route.
     */
    @Test
    public void testCalculateChangesOfLine() {
        
        ArrayList<Station> stations;
        
        stations = new ArrayList();
        
        Station st1 = new Station();
        Line l1 = new Line();
        l1.setId(1);
      
        st1.setLine(l1);
        
        
        Station st2 = new Station();
        Line l2 = new Line();
        l2.setId(1);
        st2.setLine(l2);
        
       Station st3 = new Station();
        Line l3 = new Line();
        l3.setId(2);
        st3.setLine(l3);
       
        
        stations.add(st1);
        stations.add(st2);
        stations.add(st3);
        
        int expResult = 1;
        
        route = new Route();
        route.setStations(stations);      
        

        route.calculateChangesOfLine();
        
        int result = route.getChangesOfLine();
        assertEquals(expResult, result);
        
     

    }

    /**
     * Test of calculateTime method, of class Route.
     */
    @Test
    public void testCalculateTime() throws Exception {
        
        // Test com o ID da base de dados se a base de dados 1for novo tem de ser corregido
        
        ArrayList<Station> stations;     
        stations = new ArrayList();
    
        // Aveleda id 48 Line Station id 7 Linha verde
        Station st1 = new Station();
        st1.setId(48);
        Line l1 = new Line();
        l1.setId(7);
        st1.setLine(l1);
        
        //Casal Garcia id 49 Line Station id 7 Linha verde
        Station st2 = new Station(); 
        st2.setId(49);
        Line l2 = new Line();
        l2.setId(7);
        st2.setLine(l2);
        
        //Casal Gatão id 50 Line Station id 7 Linha verde
        Station st3 = new Station();
        st3.setId(50);
        Line l3 = new Line();
        l3.setId(7);
        st3.setLine(l3);
      
        
        stations.add(st1);
        stations.add(st2);
        stations.add(st3);
        

        route = new Route();
        route.setStations(stations);  
        
        
        route.calculateTime();
        
        String result = route.getDuration();
        String expResult = "04:25";
        
         assertEquals(expResult, result);
        
       
    }




    /**
     * Test of populateMetroAndFindRoute method, of class Route.
     */
    @Test
    public void testPopulateMetroAndFindRoute() throws Exception {

    }

    /**
     * Test of upTheLineOnePosition method, of class Route.
     */
    @Test
    public void testUpTheLineOnePosition() {

    }

    /**
     * Test of downTheLineOnePosition method, of class Route.
     */
    @Test
    public void testDownTheLineOnePosition() {

    }

    /**
     * Test of startAlgoFindRoute method, of class Route.
     */
    @Test
    public void testStartAlgoFindRoute() {

    }

    /**
     * Test of checkIfLineContainsStation method, of class Route.
     */
    @Test
    public void testCheckIfLineContainsStation() {

    }

    /**
     * Test of positionOfStationInLine method, of class Route.
     */
    @Test
    public void testPositionOfStationInLine() {
      
    }

    /**
     * Test of checkIfStationIsLastStation method, of class Route.
     */
    @Test
    public void testCheckIfStationIsLastStation() {
;
    }

    /**
     * Test of deepCopyOfRota method, of class Route.
     */
    @Test
    public void testDeepCopyOfRota() {

    }

    /**
     * Test of showRoutesFixtStartStation method, of class Route.
     */
    @Test
    public void testShowRoutesFixtStartStation() throws Exception {
    }
    
}
