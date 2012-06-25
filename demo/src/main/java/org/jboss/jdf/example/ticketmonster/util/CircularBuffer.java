package org.jboss.jdf.example.ticketmonster.util;

import java.util.ArrayList;
import java.util.List;

public class CircularBuffer<T> {

    private T[] buffer;

    private volatile int cursor = 0;

    private final int capacity;

    public CircularBuffer(int capacity) {
        buffer = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public void add(T item) {
        synchronized (buffer) {
            buffer[(cursor++)%capacity] = item;

        }
    }

    public List<T> getContents() {
        List<T> returnedItems = new ArrayList<T>();
        synchronized (buffer) {
            if (cursor > capacity) {
                for (int i= cursor % capacity; i<capacity; i++) {
                    returnedItems.add(buffer[i]);
                }
                for (int i= 0; i<cursor % capacity; i++) {
                    returnedItems.add(buffer[i]);
                }

                return returnedItems;
            } else {
                for (int i= 0; i<cursor; i++) {
                    returnedItems.add(buffer[i]);
                }
            }
        }
        return returnedItems;
    }
}
