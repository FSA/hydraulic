package net.tavda.hydraulic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HorizontalPipelineTest {
    private HorizontalPipeline formula;
    private HorizontalPipeline snip;

    @Before
    public void setup(){
        formula = new HorizontalPipeline(20,95,70,100,100,1,1.89);
        snip = new HorizontalPipeline(20,95,70,100,100,0);
    }

    @Test
    public void getAverageTemp() {
        assertEquals(formula.getAverageTemp(), 82.5, 0.0);
        assertEquals(snip.getAverageTemp(), 82.5, 0.0);
    }

    @Test
    public void getViscosity() {
        assertEquals(formula.getViscosity(), 0.0033683851975555555, 0.0);
        assertEquals(snip.getViscosity(), 0.0033683851975555555, 0.0);
    }

    @Test
    public void getAverageDensity() {
        assertEquals(formula.getAverageDensity(), 0.9702155, 0.0);
        assertEquals(snip.getAverageDensity(), 0.9702155, 0.0);
    }

    @Test
    public void getConsumptionLitersPerMinute() {
        assertEquals(formula.getConsumptionLitersPerMinute(), 343.5662832982294, 0.0);
        assertEquals(snip.getConsumptionLitersPerMinute(), 343.5662832982294, 0.0);
    }

    @Test
    public void getSpeed() {
        assertEquals(formula.getSpeed(), 0.7290702968883157, 0.0);
        assertEquals(snip.getSpeed(), 0.7290702968883157, 0.0);
    }

    @Test
    public void getReynoldsNumber() {
        assertEquals(formula.getReynoldsNumber(), 216445.0483327749, 0.0);
        assertEquals(snip.getReynoldsNumber(), 216445.0483327749, 0.0);
    }

    @Test
    public void getFrictionCoefficient() {
        assertEquals(formula.getFrictionCoefficient(), 0.03505510160036259, 0.0);
    }

    @Test
    public void getFrictionLoss() {
        assertEquals(formula.getFrictionLoss(), 9.214234179170615E-4, 0.0);
    }

    @Test
    public void getPressureFrictionLossKgPerSm2() {
        assertEquals(formula.getPressureFrictionLossKgPerSm2(), 0.09214234179170615, 0.0);
    }

    @Test
    public void getPressureFrictionLossPa() {
        assertEquals(formula.getPressureFrictionLossPa(), 9039.1637, 0.0001);
    }

    @Test
    public void getPressureLocalResistLossKgPerSm2() {
        assertEquals(formula.getPressureLocalResistLossKgPerSm2(), 0.004967865390084144, 0.0);
    }

    @Test
    public void getPressureLocalResistLossPa() {
        assertEquals(formula.getPressureLocalResistLossPa(), 487.3475947672545, 0.0);
    }

    @Test
    public void getPressureLossKgPerSm2() {
        assertEquals(formula.getPressureLossKgPerSm2(), 0.0971102071817903, 0.0);
        assertEquals(snip.getPressureLossKgPerSm2(), 0.084135, 0.000001);
    }

    @Test
    public void getPressureLossPa() {
        assertEquals(formula.getPressureLossPa(), 9526.511324533629, 0.0);
        assertEquals(snip.getPressureLossPa(), 8253.6, 0.1);
    }

    @Test
    public void getPipelineResistance() {
        assertEquals(formula.getPipelineResistance(), 23.816278311334074, 0.0);
    }

    public void getHydraulicResistance() {
        assertEquals(snip.getHydraulicResistance(),0.08,0.01);
    }
}