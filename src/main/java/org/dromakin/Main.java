package org.dromakin;

import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int THREAD_COUNT = 1000;

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                String way = generateRoute("RLRFR", 100);
                int count = (int) IntStream.range(0, way.length()).filter(j -> way.charAt(j) == 'R').count();
                // System.out.println("Кол-во R: " + count);
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            });

            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        System.out.printf("Самое частое количество повторений %s (встретилось %s раз)\n", maxEntry.getKey(), maxEntry.getValue());
        System.out.println("Другие размеры:");
        sizeToFreq.entrySet().stream().filter(entry -> !entry.equals(maxEntry)).forEach(m -> System.out.printf("- %s (%s раз)\n", m.getKey(), m.getValue()));
    }
}