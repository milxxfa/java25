package org.example;

public class UnsynchronizedParkingLot implements IParkingLot {

    private final int capacity;
    private int currentCarCount;

    public UnsynchronizedParkingLot(int capacity) {
        this.capacity = capacity;
        this.currentCarCount = 0;
        System.out.println("Створено НЕБЕЗПЕЧНУ парковку місткістю " + capacity);
    }

    @Override
    public void parkCar(String threadName) throws InterruptedException {
        System.out.println(threadName + " намагається запаркуватися...");


        if (currentCarCount < capacity) {
            Thread.sleep(10);
            currentCarCount++;
            System.out.println(threadName + " ЗАПАРКУВАВСЯ. Всього: " + currentCarCount);
        } else {
            System.out.println(threadName + " ПОМИЛКА (Повно). Всього: " + currentCarCount);
        }
    }

    @Override
    public void unparkCar(String threadName) throws InterruptedException {
        System.out.println(threadName + " намагається виїхати...");

        if (currentCarCount > 0) {
            Thread.sleep(10);

            // 3. Дія
            currentCarCount--;
            System.out.println(threadName + " ВИЇХАВ. Всього: " + currentCarCount);
        } else {
            System.out.println(threadName + " ПОМИЛКА (Пусто). Всього: " + currentCarCount);
        }
    }

    @Override
    public int getCurrentCarCount() {
        return currentCarCount;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}
