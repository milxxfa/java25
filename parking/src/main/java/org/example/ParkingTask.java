package org.example;
public class ParkingTask implements Runnable {

    private final IParkingLot parkingLot;

    public ParkingTask(IParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public void run() {
        try {
            // цикл поки потік не перервуть
            while (!Thread.currentThread().isInterrupted()) {
                parkingLot.parkCar(Thread.currentThread().getName());

                Thread.sleep((long) (Math.random() * 500));
            }
        } catch (InterruptedException e) {
            // переривання потоку
            System.out.println(Thread.currentThread().getName() + " був перерваний (паркування).");
            Thread.currentThread().interrupt();
        }
    }
}