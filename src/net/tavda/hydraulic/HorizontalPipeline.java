package net.tavda.hydraulic;

import static java.lang.Math.*;

/**
 * Гидравлический расчёт горизонтального участка трубопровода
 */
public class HorizontalPipeline {
    private double consumption;
    private double tempIn;
    private double tempOut;
    private double diameter;
    private double length;
    private double roughness;
    private double sumLocalResist;
    /* Материал труб
    * 0 - Новые стальные без внутреннего защитного покрытия или с битумным защитным покрытием
    * 1 - Новые чугунные без внутреннего защитного покрытия или с битумным защитным покрытием
    * 2 - Неновые стальные и неновые чугунные без внутреннего защитного покрытия или с битумным защитным покр., v < 1.2м/c
    * 3 - Неновые стальные и неновые чугунные без внутреннего защитного покрытия или с битумным защитным покр., v > 1.2м/c
    * 4 - Асбестоцементные
    * 5 - Железобетонные виброгидропрессованные
    * 6 - Железобетонные центрифугированные
    * 7 - Стальные и чугунные с внутр. пластмассовым или полимерцементным покр., нанесенным методом центрифугирования
    * 8 - Стальные и чугунные с внутр. цементно-песчаным покр., нанесенным методом набрызга с последующим заглаживанием
    * 9 - Стальные и чугунные с внутр. цементно-песчаным покр., нанесенным методом  центрифугирования
    * 10 - Пластмассовые
    * 11   Стеклянные
    */
    private int material = -1;
    /**
     * Свойства материала
     */
    private final double[][] material_props = {
            {0.226, 1.000, 15.900, 0.810, 0.684},
            {0.284, 1.000, 14.400, 0.734, 2.360},
            {0.300, 1.000, 17.900, 0.912, 0.867},
            {0.300, 1.000, 21.000, 1.070, 0.000},
            {0.190, 1.000, 11.000, 0.561, 3.510},
            {0.190, 1.000, 15.740, 0.802, 3.510},
            {0.190, 1.000, 13.850, 0.706, 3.510},
            {0.190, 1.000, 11.000, 0.561, 3.510},
            {0.190, 1.000, 15.740, 0.802, 3.510},
            {0.190, 1.000, 13.850, 0.706, 3.510},
            {0.226, 0.000, 13.440, 0.685, 1.000},
            {0.226, 0.000, 14.610, 0.754, 1.000},
    };

    private double averageTemp;
    private double viscosity;
    private double averageDensity;
    private double consumptionLitersPerMinute;
    private double speed;
    private double reynoldsNumber;
    private double frictionCoefficient;
    private double frictionLoss;
    private double pressureFrictionLossKgPerSm2;
    private double pressureFrictionLossPa;
    private double pressureLocalResistLossKgPerSm2;
    private double pressureLocalResistLossPa;
    private double pressureLossKgPerSm2;
    private double pressureLossPa;
    private double pipelineResistance;
    private double hydraulicResistance;

    public HorizontalPipeline(double consumption, double tempIn, double tempOut, double diameter, double length, double roughness, double sumLocalResist) {
        this.consumption = consumption;
        this.tempIn = tempIn;
        this.tempOut = tempOut;
        this.diameter = diameter;
        this.length = length;
        this.roughness = roughness;
        this.sumLocalResist = sumLocalResist;
        calcFormulas();
    }

    public HorizontalPipeline(double consumption, double tempIn, double tempOut, double diameter, double length, int material) {
        this.consumption = consumption;
        this.tempIn = tempIn;
        this.tempOut = tempOut;
        this.diameter = diameter;
        this.length = length;
        this.material = material;
        calcSNiP();
    }

    private void calcGeneric() {
        averageTemp=(tempIn+tempOut)/2;
        viscosity = 0.0178 / (1.0 + (0.0337 * averageTemp) + (0.000221 * averageTemp * averageTemp));
        averageDensity = (((-0.003 * averageTemp * averageTemp) - (0.1511 * averageTemp)) + 1003.1) / 1000;
        consumptionLitersPerMinute = (consumption / averageDensity / 60) * 1000;
        speed = (4 * consumption) / averageDensity / PI / pow(this.diameter / 1000, 2.0) / 3600;
        reynoldsNumber = ((speed * diameter) / viscosity) * 10;
    }

    public void calcFormulas() {
        calcGeneric();
        if (reynoldsNumber <= 2320) {
            frictionCoefficient = 64 / reynoldsNumber;
        } else if (reynoldsNumber <= 4000) {
            frictionCoefficient = 0.0000147 * reynoldsNumber;
        } else {
            frictionCoefficient = 0.11 * pow(68 / reynoldsNumber + roughness / diameter, 0.25);
        }
        frictionLoss = frictionCoefficient * pow(speed, 2) * averageDensity / 2 / 9.81 / diameter * 100;
        pressureFrictionLossKgPerSm2 = length * frictionLoss;
        pressureFrictionLossPa = pressureFrictionLossKgPerSm2 * 9.81 * 10000;
        pressureLocalResistLossKgPerSm2 = sumLocalResist * pow(speed, 2) * averageDensity * 1000 / 2 / 9.81 / 10000;
        pressureLocalResistLossPa = pressureLocalResistLossKgPerSm2 * 9.81 * 10000;
        pressureLossKgPerSm2 = pressureFrictionLossKgPerSm2 + pressureLocalResistLossKgPerSm2;
        pressureLossPa = pressureLossKgPerSm2 * 9.81 * 10000;
        pipelineResistance = pressureLossPa / pow(consumption, 2);
    }

    public void calcSNiP() {
        calcGeneric();
        double[] material_prop = material_props[material];
        hydraulicResistance = material_prop[3] / 1000 * pow(material_prop[1] + material_prop[4] / speed, material_prop[0]) / pow(this.diameter / 1000, material_prop[0] + 1) * pow(speed, 2);
        pressureLossKgPerSm2 = hydraulicResistance * length / 10;
        pressureLossPa = pressureLossKgPerSm2 * 9.81 * 10000;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public void setTempIn(double tempIn) {
        this.tempIn = tempIn;
    }

    public void setTempOut(double tempOut) {
        this.tempOut = tempOut;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setRoughness(double roughness) {
        this.roughness = roughness;
    }

    public void setSumLocalResist(double sumLocalResist) {
        this.sumLocalResist = sumLocalResist;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    /**
     * @return Средняя температура воды, градусов цельсия
     */
    public double getAverageTemp() {
        return averageTemp;
    }

    /**
     * @return Кинематический к-т вязкости воды (при t_ср), см^2/с
     */
    public double getViscosity() {
        return viscosity;
    }

    /**
     * @return Средняя плотность воды (при t_ср), т/м^3
     */
    public double getAverageDensity() {
        return averageDensity;
    }

    /**
     * @return Расход воды через трубопровод, л/мин
     */
    public double getConsumptionLitersPerMinute() {
        return consumptionLitersPerMinute;
    }

    /**
     * @return Скорость воды, м/с
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return Число Рейнольдса
     */
    public double getReynoldsNumber() {
        return reynoldsNumber;
    }

    /**
     * @return К-т гидравлического трения
     */
    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    /**
     * @return Удельные потери давления на трение, кг/(см^2*м)
     */
    public double getFrictionLoss() {
       return frictionLoss;
    }

    /**
     * @return Потери давления на трение, кг/см^2
     */
    public double getPressureFrictionLossKgPerSm2() {
        return pressureFrictionLossKgPerSm2;
    }

    /**
     * @return Потери давления на трение, Па
     */
    public double getPressureFrictionLossPa() {
        return pressureFrictionLossPa;
    }

    /**
     * @return Потери давления в местных сопротивлениях, кг/см^2
     */
    public double getPressureLocalResistLossKgPerSm2() {
        return pressureLocalResistLossKgPerSm2;
    }

    /**
     * @return Потери давления в местных сопротивлениях, Па
     */
    public double getPressureLocalResistLossPa() {
        return pressureLocalResistLossPa;
    }

    /**
     * @return Потери давления в трубопроводе, кг/см^2
     */
    public double getPressureLossKgPerSm2() {
       return pressureLossKgPerSm2;
    }

    /**
     * @return Потери давления в трубопроводе, Па
     */
    public double getPressureLossPa() {
        return pressureLossPa;
    }

    /**
     * @return Характеристика гидравлического сопротивления трубопровода, Па/(т/ч)
     */
    public double getPipelineResistance() {
        return pipelineResistance;
    }

    /**
     * @return Коэффициент гидравлического сопротивления по СНИП
     */
    public double getHydraulicResistance() {
        return hydraulicResistance;
    }
}
