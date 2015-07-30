package com.example.jason.healthcaremobileappdemo.Logic;

/**
 * Created by jason on 7/19/15.
 *
 * Abstract data class for demo
 */
public class Exercise {
    String name;
    String repsSetsAndTimesPerDay;
    int photoId;

    Exercise(String name, String repsSetsAndTimesPerDay, int photoId) {
        this.name = name;
        this.repsSetsAndTimesPerDay = repsSetsAndTimesPerDay;
        this.photoId = photoId;
    }

}
