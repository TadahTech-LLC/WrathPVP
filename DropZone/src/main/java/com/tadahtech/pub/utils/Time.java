package com.tadahtech.pub.utils;

/**
 * @author Timothy Andis
 */
public class Time {

    public int hours, minutes, seconds, total;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.total = (hours * 60 * 60) + (minutes * 60) + seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTotal() {
        return total;
    }
}
