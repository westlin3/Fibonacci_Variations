import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.Thread;

public class Fibonacci_Variations {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    /* define constants */

    static long MAXVALUE =  200000000;

    static long MINVALUE = -200000000;

    static int numberOfTrials = 20;
    static int MAXINPUTSIZE  = 92;
    static int MININPUTSIZE  =  1;

    static String ResultsFolderPath = "/home/curtis/Bean/LAB5/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;

    static void runFullExperiment(String resultsFileName){

        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);
        } catch(Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...
        }

        ThreadCpuStopWatch BatchStopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial

        resultsWriter.println("#InputSize    AverageTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();

        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize+=1) {
            long Fib_value = 1;
            // progress message...
            System.out.println("Running test for input size "+inputSize+" ... ");

            /* repeat for desired number of trials (for a specific size of input)... */
            long batchElapsedTime = 0;

            /* force garbage collection before each batch of trials run so it is not included in the time */
            //System.gc();

            // instead of timing each individual trial, we will time the entire set of trials (for a given input size)
            // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the
            // stopwatch methods themselves

            BatchStopwatch.start(); // comment this line if timing trials individually

            // list for dynamic recursive function
            long[] fib = new long[93];
            // run the trials
            for (long trial = 0; trial < numberOfTrials; trial++) {
                Fib_value = FibonacciRecursiveDP(inputSize, fib);
            }

            batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually
            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch

            /* print data for this size of input */
            resultsWriter.printf("%12d  %15.2f %d\n",inputSize, averageTimePerTrialInBatch, Fib_value); // might as well make the columns look nice
            resultsWriter.flush();
            System.out.println(" ....done.");

        }

    }

    public static void main(String[] args)  {
        // full experiments names were edited for each trial
        runFullExperiment("FibRecursiveDP-Exp1-ThrowAway.txt");
        runFullExperiment("FibRecursiveDP-Exp2.txt");
        runFullExperiment("FibRecursiveDP-Exp3.txt");
    }

    // Iterative approach to the fibonacci formula
    public static long FibonacciLoop(int iterations) {
        long temp_1 = 1;
        long temp_2 = 0;
        long fibonacci = 1;
        for (int i = 0; i < iterations; i++) {
            // added a short sleep as program was too fast to show scaling
            try {
                Thread.sleep(1);
            } catch( InterruptedException e) {
                e.printStackTrace();
            }
            fibonacci = temp_1 + temp_2;
            temp_1 = temp_2;
            temp_2 = fibonacci;
        }

        return fibonacci;
    }

    // Recursive brute force of the Fibonacci sequence
    public static long FibonacciRecursive(long n) {
        if (n <= 1)
            return n;
        //short sleeps was commented out for tests because the recursive method is slow on its own
//        try {
//            Thread.sleep(1);
//        } catch( InterruptedException e) {
//            e.printStackTrace();
//        }
        return FibonacciRecursive(n-1) + FibonacciRecursive(n-2);
    }

    public static long FibonacciRecursiveDP(int n, long fib[]) {
        // sleep add to functions to better show time scaling
        try {
            Thread.sleep(1);
        } catch( InterruptedException e) {
            e.printStackTrace();
        }
        if (n==0)
            return 0;
        if (n==1)
            return 1;
        if (fib[n] != 0) {
            return fib[n];
        } else {
            fib[n] = FibonacciRecursiveDP(n-1, fib) + FibonacciRecursiveDP(n-2, fib);
            return fib[n];
        }
    }

    // Wrapper function for Fibonacci matrix
    public static long FibonacciMatrix(int n) {
        if (n == 0)
            return 1;
        if (n == 1)
            return 1;

        return PowerMatrix(n);

    }

    public static long[][] MultiplyMatrices(long matrix[][]) {
        // multiply the matrix  {0 1}
        //                      {1 1}
        long x1 = matrix[0][0] * 0 + matrix[0][1] * 1;
        long x2 = matrix[0][0] * 1 + matrix[0][1] * 1;
        long y1 = matrix[1][0] * 0 + matrix[1][1] * 1;
        long y2 = matrix[1][0] * 1 + matrix[1][1] * 1;

        //   returnMatrix  {x1 x2}
        //                 {y1 y2}
        long returnMatrix[][] = {{x1, x2},{y1, y2}};

        return returnMatrix;
    }

    public static long PowerMatrix(int n) {
        long matrix[][] = new long[][]{{0,1},{1,1}};

        for (int i = 2; i <= n; i++) {
            // short sleep added to better show time scaling
            try {
                Thread.sleep(1);
            } catch( InterruptedException e) {
                e.printStackTrace();
            }
            matrix = MultiplyMatrices(matrix);
        }
        return matrix[1][0];
    }
}
