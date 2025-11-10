package org.example;


public class UnparkingTask implements Runnable {

    private final IParkingLot parkingLot;

    public UnparkingTask(IParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 1000));

            while (!Thread.currentThread().isInterrupted()) {
                parkingLot.unparkCar(Thread.currentThread().getName());

                Thread.sleep((long) (Math.random() * 700));
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " був перерваний (виїзд).");
            Thread.currentThread().interrupt();
        }
    }
}