/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hp
 */
public class TimeParseTest {
    
    private TimeParse timeParse;
    
    public TimeParseTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        timeParse = new TimeParse();
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
   
    @org.junit.jupiter.api.Test
    public void testTimeToInt() {
        
        assertEquals(83, timeParse.timeToInt("01:23"));
    
    }

    /**
     * Test of timeToString method, of class TimeParse.
     */
    @org.junit.jupiter.api.Test
    public void testTimeToString() {
        
        assertEquals("01:23",timeParse.timeToString(83));
    }
    
}
