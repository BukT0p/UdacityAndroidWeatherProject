package com.dataart.vyakunin.udacityandroidweatherproject.datamodel.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherForDay {
    private Date dt;
    private int min;
    private int max;
    private String main;

    public WeatherForDay(Date dt, double min, double max, String main) {
        this.dt = dt;
        setMin(min);
        setMax(max);
        this.main = main;
    }

    public WeatherForDay() {

    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public void setDt(long dtSeconds) {
        this.dt = new Date(dtSeconds*1000);
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = (int) Math.round(min);
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.min = (int) Math.round(max);
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    @Override
    public String toString() {
        if (dt==null){
            return super.toString();
        }
        String notFormatted = "%s, %s temp. %s/%s";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return String.format(notFormatted,
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
                main,
                min,
                max);
    }
}
