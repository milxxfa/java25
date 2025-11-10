package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParkingLotTest {

    @Test(timeout = 2000)
    public void testParkingFullWaits() throws InterruptedException {
        int capacity = 2;
        final IParkingLot parking = new SynchronizedParkingLot(capacity);

        parking.parkCar("Test-1");
        parking.parkCar("Test-2");
        assertEquals(capacity, parking.getCurrentCarCount());

        Thread waitingThread = new Thread(() -> {
            try {
                parking.parkCar("WaitingThread");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        waitingThread.start();

        Thread.sleep(500);

        assertEquals(Thread.State.WAITING, waitingThread.getState());
        assertEquals(capacity, parking.getCurrentCarCount()); // Ніхто більше не запаркувався

        parking.unparkCar("Test-Unpark");


        waitingThread.join(500);

        assertEquals(capacity, parking.getCurrentCarCount());
        assertFalse("Потік повинен був завершитися", waitingThread.isAlive());
    }

    @Test(timeout = 2000)
    public void testUnparkingEmptyWaits() throws InterruptedException {
        int capacity = 2;
        final IParkingLot parking = new SynchronizedParkingLot(capacity);

        assertEquals(0, parking.getCurrentCarCount());

        Thread waitingThread = new Thread(() -> {
            try {
                parking.unparkCar("WaitingThread");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        waitingThread.start();


        Thread.sleep(500);


        assertEquals(Thread.State.WAITING, waitingThread.getState());
        assertEquals(0, parking.getCurrentCarCount());


        parking.parkCar("Test-Park");


        waitingThread.join(500);


        assertEquals(0, parking.getCurrentCarCount());
        assertFalse("Потік повинен був завершитися", waitingThread.isAlive());
    }

    @Test
    public void testUnsynchronizedBreaksCapacity() throws InterruptedException {
        int capacity = 5;
        final IParkingLot parking = new UnsynchronizedParkingLot(capacity);
        int numThreads = 15;

        Runnable parkTask = () -> {
            try {
                parking.parkCar(Thread.currentThread().getName());
            } catch (InterruptedException e) {

            }
        };

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(parkTask);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join(500);
        }


        System.out.println("[Test] Небезпечний фінальний рахунок: " + parking.getCurrentCarCount());


        assertTrue("Несинхронізований код порушив обмеження місткості",
                parking.getCurrentCarCount() > capacity);
    }
}