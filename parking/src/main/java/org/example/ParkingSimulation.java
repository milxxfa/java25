package org.example;

import java.util.ArrayList;
import java.util.List;


public class ParkingSimulation {

    public static void main(String[] args) {
        int parkingCapacity = 5;
        int numParkingThreads = 10;
        int numUnparkingThreads = 10;
        long simulationTimeMs = 5000; 

        System.out.println("=================================================");
        System.out.println("  ЗАПУСК НЕКОРЕКТНОЇ СИМУЛЯЦІЇ (БЕЗ СИНХРОНІЗАЦІЇ) ");
        System.out.println("=================================================");
        System.out.println("Очікуйте помилок (більше " + parkingCapacity + " або менше 0 авто)\n");

        IParkingLot unsafeParking = new UnsynchronizedParkingLot(parkingCapacity);
        runSimulation(unsafeParking, numParkingThreads, numUnparkingThreads, simulationTimeMs);

        System.out.println("\n\n=================================================");
        System.out.println("   ЗАПУСК КОРЕКТНОЇ СИМУЛЯЦІЇ (З СИНХРОНІЗАЦІЄЮ)  ");
        System.out.println("=================================================");
        System.out.println("Тут помилок бути не повинно.\n");

        IParkingLot safeParking = new SynchronizedParkingLot(parkingCapacity);
        runSimulation(safeParking, numParkingThreads, numUnparkingThreads, simulationTimeMs);
    }

    private static void runSimulation(IParkingLot parkingLot, int numParkers, int numUnparkers, long duration) {

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numParkers; i++) {
            Thread t = new Thread(new ParkingTask(parkingLot), "Паркувальник-" + i);
            threads.add(t);
        }


        for (int i = 0; i < numUnparkers; i++) {
            Thread t = new Thread(new UnparkingTask(parkingLot), "Виїзд-" + i);
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("\n--- ЗУПИНКА СИМУЛЯЦІЇ ---");
        for (Thread t : threads) {
            t.interrupt();
        }

        for (Thread t : threads) {
            try {
                t.join(100); // Чекаємо до 100мс
            } catch (InterruptedException e) {
            }
        }

        System.out.println("--- СИМУЛЯЦІЮ ЗАВЕРШЕНО ---");
        System.out.println("Фінальна кількість авто: " + parkingLot.getCurrentCarCount());
        System.out.println("Місткість: " + parkingLot.getCapacity());
        System.out.println("Результат: " +
                (parkingLot.getCurrentCarCount() > parkingLot.getCapacity() || parkingLot.getCurrentCarCount() < 0
                        ? "ПОМИЛКА!" : "КОРЕКТНО"));
    }
}