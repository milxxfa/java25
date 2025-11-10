package org.example;

public interface IParkingLot {

    void parkCar(String threadName) throws InterruptedException;

    void unparkCar(String threadName) throws InterruptedException;

    int getCurrentCarCount();

    int getCapacity();
}
