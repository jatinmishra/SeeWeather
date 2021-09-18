package com.apps.jm.seeweather;

/**
 * Created by Jatin on 8/18/2017.
 */

public class HomeCity {
    private static String cityName;

    public static void setCityName(String cityName) {
        HomeCity.cityName = cityName;
    }

    public static String getCityName() {
        return cityName;
    }
}
