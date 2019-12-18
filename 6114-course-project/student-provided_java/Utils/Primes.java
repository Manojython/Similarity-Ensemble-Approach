package Utils;

import java.util.ArrayList;

public class Primes {

    public static ArrayList<Integer> generate(int count) {
        ArrayList<Integer> primes = new ArrayList<Integer>();

        for(int z = 1; z <= count; z++) {
            boolean isPrime = true;
            //check to see if the number is primes
            for(int j = 2; j < z ; j++) {
                if(z % j == 0){
                    isPrime = false;
                    break;
                }
            }

            // print the number 
            if(isPrime) {
                primes.add(z);
            }
        }

        return primes;
    }
} 