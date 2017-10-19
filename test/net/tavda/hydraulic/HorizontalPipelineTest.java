package net.tavda.hydraulic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HorizontalPipelineTest {
    private HorizontalPipeline instance;

    @Before
    public void setup(){
        instance = new HorizontalPipeline(20,95,70,100,100,1,1.89);
    }

    @Test
    public void getAverageTemp() {
        assertEquals(instance.getAverageTemp(), 82.5, 0.0);
    }

    @Test
    public void getViscosity() {
        assertEquals(instance.getViscosity(), 0.0033683851975555555, 0.0);
    }

    @Test
    public void getAverageDensity() {
        assertEquals(instance.getAverageDensity(), 0.9702155, 0.0);
    }

    @Test
    public void getConsumptionLitersPerMinute() {
        assertEquals(instance.getConsumptionLitersPerMinute(), 343.5662832982294, 0.0);
    }

    @Test
    public void getSpeed() {
        assertEquals(instance.getSpeed(), 0.7290702968883157, 0.0);
    }

    @Test
    public void getReynoldsNumber() {
        assertEquals(instance.getReynoldsNumber(), 216445.0483327749, 0.0);
    }

    @Test
    public void getFrictionCoefficient() {
        assertEquals(instance.getFrictionCoefficient(), 0.03505510160036259, 0.0);
    }

    @Test
    public void getFrictionLoss() {
        assertEquals(instance.getFrictionLoss(), 9.214234179170615E-4, 0.0);
    }

    @Test
    public void getPressureFrictionLossKgPerSm2() {
        assertEquals(instance.getPressureFrictionLossKgPerSm2(), 0.09214234179170615, 0.0);
    }

    @Test
    public void getPressureFrictionLossPa() {
        assertEquals(instance.getPressureFrictionLossPa(), 9039.1637, 0.0001);
    }

    @Test
    public void getPressureLocalResistLossKgPerSm2() {
        assertEquals(instance.getPressureLocalResistLossKgPerSm2(), 0.004967865390084144, 0.0);
    }

    @Test
    public void getPressureLocalResistLossPa() {
        assertEquals(instance.getPressureLocalResistLossPa(), 487.3475947672545, 0.0);
    }

    @Test
    public void getPressureLossKgPerSm2() {
        assertEquals(instance.getPressureLossKgPerSm2(), 0.0971102071817903, 0.0);
    }

    @Test
    public void getPressureLossPa() {
        assertEquals(instance.getPressureLossPa(), 9526.511324533629, 0.0);
    }

    @Test
    public void getPipelineResistance() {
        assertEquals(instance.getPipelineResistance(), 23.816278311334074, 0.0);
    }
}