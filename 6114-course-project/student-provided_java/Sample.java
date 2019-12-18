import java.util.Random;

public class Sample {

    public static void main(String args[])
    {
        Test();
    }
    
    public static final int SAMPLE_SIZE = 200;
    public static final int SMIN = 10;
    public static final int SMAX = 200;

    public static int[] generate(int size, int min, int max)
    {
        Random random = new Random();
        int[] temp = new int[size];
        
        for (int i = 0; i < temp.length; i ++) {
            temp[i] = random.nextInt(max - min) + min;
        }

        return temp;
    }

    public static void Test() 
    {
        int tempMin = 1;
        int tempMax = 12;
        int[] tempSample = generate(10, tempMin, tempMax);
        assert tempSample.length == 10 : "Sample Creation too small";
        for (int i = 0; i < tempSample.length; i ++) {
            assert tempSample[i] >= tempMin && tempSample[i] <= tempMax : "Sample at " + i + " is not uniform between " + tempMin + " and " + tempMax;
        }

        System.out.println("Sample test cases pass");
    }
}