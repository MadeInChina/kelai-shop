package cn.com.kelaikewang.commons.geography;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class GeographyUtils {
    private GeographyUtils(){

    }

    /**
     * 根据两个经纬度坐标计算距离
     * @param gpsFrom
     * @param gpsTo
     * @param ellipsoid
     * @return 两点距离，单位：米
     */
    public static double computeDistance(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid)
    {
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }
}
