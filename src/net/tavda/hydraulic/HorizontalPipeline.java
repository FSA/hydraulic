package net.tavda.hydraulic;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

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

    /**
     * Проведение расчёта по формулам теоретической гидравлики
     * @param consumption Расход воды через трубопровод, т/ч
     * @param tempIn Температура воды на входе, градусов цельсия
     * @param tempOut Температура воды на выходе, градусов цельсия
     * @param diameter Внутренний диаметр трубопровода, мм
     * @param length Длина трубопровода, м
     * @param roughness Экв. шероховатость внутр. поверхностей труб, мм
     * @param sumLocalResist Сумма к-тов местных сопротивлений
     */
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

    /**
     * Проведение расчёта по СНиП
     * @param consumption Расход воды через трубопровод, т/ч
     * @param tempIn Температура воды на входе, градусов цельсия
     * @param tempOut Температура воды на выходе, градусов цельсия
     * @param diameter Внутренний диаметр трубопровода, мм
     * @param length Длина трубопровода, м
     * @param material Материал труб:<br>
     *       0 - Новые стальные без внутреннего защитного покрытия или с битумным защитным покрытием<br>
     *       1 - Новые чугунные без внутреннего защитного покрытия или с битумным защитным покрытием<br>
     *       2 - Неновые стальные и неновые чугунные без внутреннего защитного покрытия или с битумным защитным покр., v &lt; 1.2м/c<br>
     *       3 - Неновые стальные и неновые чугунные без внутреннего защитного покрытия или с битумным защитным покр., v &gt; 1.2м/c<br>
     *       4 - Асбестоцементные<br>
     *       5 - Железобетонные виброгидропрессованные<br>
     *       6 - Железобетонные центрифугированные<br>
     *       7 - Стальные и чугунные с внутр. пластмассовым или полимерцементным покр., нанесенным методом центрифугирования<br>
     *       8 - Стальные и чугунные с внутр. цементно-песчаным покр., нанесенным методом набрызга с последующим заглаживанием<br>
     *       9 - Стальные и чугунные с внутр. цементно-песчаным покр., нанесенным методом  центрифугирования<br>
     *      10 - Пластмассовые<br>
     *      11 - Стеклянные
     */
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

    /**
     * Провести расчёт по формулам теоретической гидравлики
     */
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

    /**
     * Провести расчёт по СНиП
     */
    public void calcSNiP() {
        calcGeneric();
        double[] material_prop = material_props[material];
        hydraulicResistance = material_prop[3] / 1000 * pow(material_prop[1] + material_prop[4] / speed, material_prop[0]) / pow(this.diameter / 1000, material_prop[0] + 1) * pow(speed, 2);
        pressureLossKgPerSm2 = hydraulicResistance * length / 10;
        pressureLossPa = pressureLossKgPerSm2 * 9.81 * 10000;
    }

    /**
     * Установить расход воды через трубопровод
     * @param consumption Расход воды через трубопровод, т/ч
     */
    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    /**
     * Установить температуру воды на входе
     * @param tempIn Температура воды на входе, градусов цельсия
     */
    public void setTempIn(double tempIn) {
        this.tempIn = tempIn;
    }

    /**
     * Установить температуру воды на выходе
     * @param tempOut Температура воды на выходе, градусов цельсия
     */
    public void setTempOut(double tempOut) {
        this.tempOut = tempOut;
    }

    /**
     * Установить внутренний диаметр трубопровода
     * @param diameter Внутренний диаметр трубопровода, мм
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * Установить длину трубопровода
     * @param length Длина трубопровода, м
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Установить экв. шероховатость внутр. поверхностей труб
     * @param roughness Экв. шероховатость внутр. поверхностей труб, мм
     */
    public void setRoughness(double roughness) {
        this.roughness = roughness;
    }

    /**
     * Установить сумму к-тов местных сопротивлений
     * @param sumLocalResist Сумма к-тов местных сопротивлений
     */
    public void setSumLocalResist(double sumLocalResist) {
        this.sumLocalResist = sumLocalResist;
    }

    /**
     * Установить матриал труб
     * @param material Материал труб:
     */
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
