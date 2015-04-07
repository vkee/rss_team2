package MotionPlanning;

import static org.junit.Assert.*;

import org.junit.Test;

public class AngleTest {

    public AngleTest(){
        
    }
    
    @Test
    public void test() {
        assert(RRT.getAngle(1.0, 1.0, 2.0, 2.0) == Math.PI/4);
    }

}
