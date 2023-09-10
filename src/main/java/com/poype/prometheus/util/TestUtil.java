package com.poype.prometheus.util;

import java.util.Random;

import static java.lang.Math.abs;

public class TestUtil {

    public static synchronized int randomNumber() {
        Random rand = new Random();
        return abs(rand.nextInt());
    }
}
