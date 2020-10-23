package com.softeng.quickcash;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyLocationTest {

    @Test
    public void CalcDistanceToLocation_Test1() {
        MyLocation myLocation = new MyLocation();
        LongLatLocation location = new LongLatLocation(44.642173, -63.587985);

        myLocation.lastLocation = location;
        float dist = myLocation.calcDistanceToLocation(location);

        assertEquals(0,dist,2);

    }

}
