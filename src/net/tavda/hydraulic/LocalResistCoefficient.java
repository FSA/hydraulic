package net.tavda.hydraulic;

import static java.lang.Math.E;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;

/**
 * Расчёт коэффициента местного сопротивления для участка трубопровода
 * @author FSA
 */
public class LocalResistCoefficient {

    public LocalResistCoefficient() {
    }

    /**
     * Внезапное расширение или сужение
     * @param d1 диаметр до расширения/сужения
     * @param d2 диаметр после расширеничя/сужения
     * @return Коэффициент местного сопротивления
     */
    public double calcExpand(double d1,double d2) {
        if(d1<d2) {
            return pow(1-pow(d1/d2,2),2);
        } else {
            return pow(1/(0.57+0.043/(1.1-pow(d2/d1,2)))-1,2);
        }
    }

    /**
     * Диафрагма на трубе постоянного сечения
     * @param dt диаметр друбы
     * @param dd диаметр диафрагмы
     * @return Коэффициент местного сопротивления
     */
    public double calcDiaphragm(double dt,double dd) {
        return pow(1/(pow(dd/dt,2)*(0.57+0.043/(1.1-pow(dd/dt,2))))-1,2);
    }

    /**
     * Диафрагма на входе в трубу другого диаметра
     * @param d1 диаметр трубы на входе
     * @param d2 диаметр трубы на выходе
     * @param dd диаметр диафрагмы
     * @return Коэффициент местного сопротивления
     */
    public double calcDiaphragmDiff(double d1,double d2,double dd) {
        return pow(1/(pow(dd/d1,2)*(0.57+0.043/(1.1-pow(dd/d1,2))))-1/pow(d2/d1,2),2);
    }

    /**
     * Сварной стык
     * @param dt диаметр трубы
     * @param delta высота сварного стыка
     * @return Коэффициент местного сопротивления
     */
    public double calcWelded(double dt, double delta) {
        return 14*pow(delta/dt,1.5);
    }

    /**
     * Поворот трубы (резкий)
     * @param alpha угол между осями труб
     * @return Коэффициент местного сопротивления
     */
    public double calcTurn(double alpha) {
        return 1-cos(alpha/180*PI);
    }

    /**
     * Поворот трубы (плавный)
     * @param alpha угол между осями труб
     * @param dt - диаметр трубы
     * @param r - радиус поворота трубы
     * @param lambda ?????
     * @return Коэффициент местного сопротивления
     */
    public double calcTurnSmooth(double alpha, double dt, double r, double lambda) {
        return (-0.0000038*pow(alpha,2)+0.317*log(alpha)+0.00217*alpha-0.593)*(0.2+0.001*pow(100*lambda,8))*pow(dt/r,0.5);
    }

    /**
     * Постепенное расширение или сужение трубопровода
     * @param alpha угол конуса сужения
     * @param d1 диаметр на входе
     * @param d2 диаметр на выходе
     * @return Коэффициент местного сопротивления
     */
    public double calcGradual(double alpha,double d1,double d2) {
        if(d1<d2) {
            return pow(E, 82.816/pow(E,alpha)-17.226/alpha+0.26578)*pow(1-pow(d1/d2,2),2);
        } else {
            return pow(0.000027332* pow(alpha,2)+1.6779/alpha-0.0013371*alpha,0.5)*pow(1/(0.57+0.043/(1.1-pow(d2/d1,2)))-1,2);
        }
    }

    /**
     * Задвижка
     * @param dt диаметр трубы
     * @param a высота щели задвижки
     * @return Коэффициент местного сопротивления
     */
    public double calcValve(double dt,double a) {
        return exp(-3.2322*exp(a/dt)-0.82243*log(a/dt)+5.735);
    }
}
