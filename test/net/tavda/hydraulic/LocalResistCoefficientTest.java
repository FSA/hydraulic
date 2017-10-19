package net.tavda.hydraulic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalResistCoefficientTest {
    private LocalResistCoefficient instance;

    @Before
    public void setup() {
        instance=new LocalResistCoefficient();
    }

    @Test
    public void calcExpand() {
        assertEquals(instance.calcExpand(50,100),0.56,0.01);
        assertEquals(instance.calcExpand(100,50),0.37,0.01);
    }

    @Test
    public void calcDiaphragm() {
        assertEquals(instance.calcDiaphragm(100,94),0.22,0.01);
    }

    @Test
    public void calcDiaphragmDiff() {
        assertEquals(instance.calcDiaphragmDiff(50,40,30),8.18,0.01);
    }

    @Test
    public void calcWelded() {
        assertEquals(instance.calcWelded(100,6),0.21,0.01);
    }

    @Test
    public void calcTurn() {
        assertEquals(instance.calcTurn(90),1,0.01);
    }

    @Test
    public void calcTurnSmooth() {
        assertEquals(instance.calcTurnSmooth(90,20,40,0.036),20.05,0.01);
    }

    @Test
    public void calcGradual() {
        assertEquals(instance.calcGradual(90,100,50),0.13,0.01);
    }

    @Test
    public void calcValve() {
        assertEquals(instance.calcValve(100,0.01),23797.53,0.01);
    }

}