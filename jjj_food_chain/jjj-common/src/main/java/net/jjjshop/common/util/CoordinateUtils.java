package net.jjjshop.common.util;

public class CoordinateUtils {
    // WGS84标准参考椭球中的地球长半径(单位:米)
    private static final double EARTH_RADIUS_WGS84 = 6378137.0;

    /**
     * 计算两个坐标的距离(粗略计算，单位:米)
     * 计算公式参照 google map 的距离计算
     *
     * @param lat1 坐标1纬度
     * @param lng1 坐标1经度
     * @param lat2 坐标2纬度
     * @param lng2 坐标2经度
     * @return
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {

        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

        return Math.round(s * EARTH_RADIUS_WGS84);
    }

    public static void main(String args[]){
        //double distance = CoordinateUtils.distance(30.332946, 114.116931, 39.737725, 116.302257);
        //System.out.println(distance);
        int a = (int)(100.60 * 10 / 100);
        System.out.println(a);
    }
}
