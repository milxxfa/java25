package org.example;

public class SynchronizedParkingLot implements IParkingLot {

    private final int capacity;
    private int currentCarCount;

    public SynchronizedParkingLot(int capacity) {
        this.capacity = capacity;
        this.currentCarCount = 0;
        System.out.println("Створено БЕЗПЕЧНУ парковку місткістю " + capacity);
    }

    @Override
    public synchronized void parkCar(String threadName) throws InterruptedException {
        while (currentCarCount == capacity) {
            System.out.println(threadName + " ОЧІКУЄ (Повно). Всього: " + currentCarCount);
            wait();
        }

        currentCarCount++;
        System.out.println(threadName + " ЗАПАРКУВАВСЯ. Всього: " + currentCarCount);
        notifyAll();
    }

    @Override
    public synchronized void unparkCar(String threadName) throws InterruptedException {
        while (currentCarCount == 0) {
            System.out.println(threadName + " ОЧІКУЄ (Пусто). Всього: " + currentCarCount);
            wait();
        }

        currentCarCount--;
        System.out.println(threadName + " ВИЇХАВ. Всього: " + currentCarCount);
        notifyAll();
    }

    @Override
    public synchronized int getCurrentCarCount() {
        return currentCarCount;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}