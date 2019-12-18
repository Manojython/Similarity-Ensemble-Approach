package Parallel;

import static edu.rice.hj.Module0.finish;
import static edu.rice.hj.Module0.launchHabaneroApp;
import static edu.rice.hj.Module1.forall;

public class Main {

    public static void main(final String[] args) {

        launchHabaneroApp(() -> {
            forall(0, 2, (x)->{
                forall(0, 2, (y)->{
                    busyWaitAndPrint(x, y);
                });
            });
        });
    }

    private static void busyWaitAndPrint(final int x, final int y) {
        System.out.println("Here " + x + " " + y);
    }
}