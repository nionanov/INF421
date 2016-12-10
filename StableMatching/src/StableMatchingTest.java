// This is a test harness for an implementation of the Gale-Shapley
// stable matching algorithm.
// Author: François Pottier.

import java.io.PrintStream;
import java.util.Random;
import java.util.Arrays;

public class StableMatchingTest {

    // This is the student's implementation of the stable matching algorithm.
    // We view it as a black box, to be tested.

    private StableMatchingInterface box;

    // Up to a certain size, known as SMALL, we try every value of n. This is
    // cheap and allows giving the box a hard time.

    public final static int SMALL = 128;

    // Between SMALL and LARGE, we double the value of n at each step. This
    // allows us to quickly try larger and larger instances.

    // At n = 8192, one preference matrix occupies 256 megabytes, so a problem
    // instance is 512 megabytes. The test harness then requires 1 gigabyte
    // (because it stores both the problem instance and its inverse) plus
    // whatever the box itself requires.

    public final static int LARGE = 8192;

    // At each size, we generate a certain number of random instances.

    public final static int R = 16;

    // The box must reply under a certain time limit (expressed in milliseconds).

    public final static long TIMEOUT = 10000;

    // Setting this flag causes us to abandon after the first failure.

    public final static boolean STOP = false;

    // These fields are used to hold the result produced by the matching
    // algorithm. One might think that they should be local variables of the
    // method "run(n,menPrefs,womenPrefs)" below, but can't be, because they
    // are mutated in an inner class.

    private int[] bride;
    private Throwable exception;

    // This is the output stream that is used for logging.

    private PrintStream out;

    // Running counts of failures and successes.

    private int failures, successes;

    // A pseudo-random number generator.

    private Random random;

    // Constructor.

    StableMatchingTest (StableMatchingInterface box, PrintStream out, Random random)
    {
	this.box = box;
	this.out = out;
	failures = successes = 0;
	this.random = random;
    }

    // This method assumes that its argument is a permutation of [0,n), and
    // returns the inverse permutation.

    int[] inversePermutation (int[] p)
    {
	int n = p.length;
	int[] inverse = new int [n];
	for (int i = 0; i < n; i++)
	    inverse[p[i]] = i;
	return inverse;
    }

    // This method prints a preference matrix.
    
    void printPreferences (int[][] prefs)
    {
	int n = prefs.length;
	for (int i = 0; i < n; i++)
	    out.printf("%d prefers: %s\n", i, Arrays.toString(prefs[i]));
    }

    // These methods print the parameters and result of a test run.

    void printParameters (int n, int[][] menPrefs, int[][] womenPrefs)
    {
	out.println("The parameters of this test run were:");
	out.printf("n = %d\n", n);
	out.println("menPrefs =");
	printPreferences(menPrefs);
	out.println("womenPrefs =");
	printPreferences(womenPrefs);
    }

    void printResult (int[] bride)
    {
	out.println("The result of this test run was:");
	out.println("bride =");
	out.println(Arrays.toString(bride));
    }

    // This method records a failure.

    void fail ()
    {
	failures++;
	// If the user wishes that we stop after the first failure, do so.
	if (STOP)
	    System.exit(1);
    }

    // This method is a wrapper around the box's main method. It invokes the box,
    // controls its side effects (exceptions), and measures its time consumption.

    void run (final int n, final int[][] menPrefs, final int[][] womenPrefs)
    {
	// Log the parameters of this test case.
	out.println();
	out.printf("n = %d: running...\n", n);

	// Perform garbage collection prior to running the algorithm, so as to
	// (hopefully) avoid the need for GC while the algorithm is running.
	// This should allow us to obtain a more accurate time measurement.
	// (Do this only at large sizes, as it is slow. We do not measure
	// performance at small sizes.)

	if (n >= SMALL)
	    System.gc();

	// Run the student's method in a separate thread, so we can terminate it
	// if it does not finish in a reasonable time.

	bride = null;
	exception = null;

	Thread thread = new Thread () {

	    public void run () {

		long startTime = System.currentTimeMillis();
		try {
		    bride = box.constructStableMatching(n, menPrefs, womenPrefs);
		} catch (Throwable e) {
		    // This is either an exception thrown by the student's code, or
		    // ThreadDeath, caused by a call to thread.stop() below.
		    // We record the exception and terminate.
		    exception = e;
		}
		if (exception == null) {
		    long endTime = System.currentTimeMillis();
		    long duration = endTime - startTime;
		    out.printf("Elapsed time: %d milliseconds\n", duration);
		}

	    }

	};

	// Start the student's thread, and wait for it to terminate.

	thread.start();
	try {
	    thread.join(TIMEOUT);
	} catch (InterruptedException e) {
	    // In principle, we cannot be interrupted, unless the student does
	    // very weird things.
	    assert (false);
	}
	// If the thread is still alive, kill it and declare a timeout.
	// In principle, the thread could terminate normally after isAlive() returns
	// true and before we kill it. This does not seem to be a problem -- the
	// outcome is still considered a timeout.
	if (thread.isAlive()) {
	    thread.stop();
	    out.println("FAILURE: TIMEOUT!");
	    out.printf("Your code did not terminate within %d milliseconds.\n", TIMEOUT);
	    if (n < SMALL)
		printParameters(n, menPrefs, womenPrefs);
	    fail();
	    return;
	}
	
	// The thread has terminated on its own.

	// If it has raised an exception, log it.
	if (exception != null) {
	    out.println("FAILURE: EXCEPTION!");
	    out.println("Your code unexpectedly throws an exception:");
	    out.println(exception);
	    printParameters(n, menPrefs, womenPrefs);
	    fail();
	    return;
	}

	// The thread did not throw an exception.

	// We now check that the array "bride" is a valid permutation of [0,n).

	if (bride == null) {
	    out.println("FAILURE: NULL RESULT!");
	    out.println("Your code returns a null array.");
	    printParameters(n, menPrefs, womenPrefs);
	    printResult(bride);
	    fail();
	    return;
	}
	if (bride.length != n) {
	    out.println("FAILURE: INVALID LENGTH!");
	    out.printf("Your code returns an array of length %d,\nwhereas an array of length %d was expected.\n", bride.length, n);
	    printParameters(n, menPrefs, womenPrefs);
	    printResult(bride);
	    fail();
	    return;
	}
	boolean[] mark = new boolean [n];
	int[] groom = new int [n];
	for (int m = 0; m < n; m++) {
	    // If bride[m] lies outside [0,n), then the array "bride" does not make sense.
	    if (!(0 <= bride[m] && bride[m] < n)) {
		out.println("FAILURE: INVALID ENTRY!");
		out.printf("bride[%d] is %d, which lies outside [0,%d).\n", m, bride[m], n);
		printParameters(n, menPrefs, womenPrefs);
		printResult(bride);
		fail();
		return;
	    }
	    // If bride[m] is already marked, then the array "bride" contains a duplicate entry.
	    if (mark[bride[m]]) {
		out.println("FAILURE: NOT A MATCHING!");
		out.printf("bride[%d] and bride[%d] are both equal to %d.\n", groom[bride[m]], m, bride[m]);
		printParameters(n, menPrefs, womenPrefs);
		printResult(bride);
		fail();
		return;
	    }
	    mark[bride[m]] = true;
	    groom[bride[m]] = m;
	}

	// We have a perfect matching.

	// Now, check this matching is stable.

	int[][] menRankings = new int[n][];
	for (int m = 0; m < n; m++)
	    menRankings[m] = inversePermutation(menPrefs[m]);
	int[][] womenRankings = new int[n][];
	for (int w = 0; w < n; w++)
	    womenRankings[w] = inversePermutation(womenPrefs[w]);

	// For any man m and woman w that are not matched...
	for (int m = 0; m < n; m++)
	    for (int w = 0; w < n; w++)
		if (bride[m] != w)
		    // If m prefers w to his current bride and w prefers m to her current groom,
		    // then the matching is not perfect.
		    if (menRankings[m][w] < menRankings[m][bride[m]] && womenRankings[w][m] < womenRankings[w][groom[w]]) {
			out.println("FAILURE: NOT A STABLE MATCHING!");
			out.printf("The pair formed by the man %d and the woman %d is unstable.\n", m, w);
			out.printf("Indeed, man %d prefers woman %d to his bride %d\n", m, w, bride[m]);
			out.printf("and woman %d prefers man %d to her groom %d.\n", w, m, groom[w]);
			printParameters(n, menPrefs, womenPrefs);
			printResult(bride);
			fail();
			return;
		    }

	out.println("SUCCESS!");
	successes++;
    }

    // This method constructs an identity array.

    int[] identityArray (int n)
    {
	int[] p = new int [n];
	for (int i = 0; i < n; i++)
	    p[i] = i;
	return p;
    }

    // This method constructs a reverse identity array.

    int[] reverseIdentityArray (int n)
    {
	int[] p = new int [n];
	for (int i = 0; i < n; i++)
	    p[i] = n-1-i;
	return p;
    }

    // This method constructs a preferences matrix where all people agree.

    int[][] uniformPrefs (int[] individualPrefs)
    {
	int n = individualPrefs.length;
	int[][] prefs = new int[n][];
	for (int i = 0; i < n; i++)
	    prefs[i] = individualPrefs;
	return prefs;
    }

    // This method constructs a preferences matrix where (at most) two distinct
    // visions exist.

    int[][] mixedPrefs (int[] individualPrefsOne, int[] individualPrefsTwo)
    {
	int n = individualPrefsOne.length;
	int[][] prefs = new int[n][];
	for (int i = 0; i < n; i++)
	    prefs[i] = (i % 2 == 0) ? individualPrefsOne : individualPrefsTwo;
	return prefs;
    }

    // This method constructs a random permutation of [0,n).
    // Source: Wikipedia, "inside-out" variant of the Fisher-Yates shuffle.

    int[] randomPermutation (int n)
    {
	int[] p = new int [n];
	// p[0] = 0;
	for (int i = 1; i < n; i++) {
	    // Pick a value j so that 0 <= j <= i.
	    int j = random.nextInt(i+1);
	    // Initialize p[i] to i, and swap p[i] and p[j].
	    // These two operations can be done in only two instructions, as follows.
	    p[i] = p[j];
	    p[j] = i;
	}
	return p;
    }

    // This method constructs a random preference matrix.

    int[][] randomPrefs (int n)
    {
	int[][] prefs = new int[n][];
	for (int i = 0; i < n; i++)
	    prefs[i] = randomPermutation(n);
	return prefs;
    }

    // This method submits the box to a series of tests at size n.

    public void test (int n)
    {
	// Construct non-random instances of size n.

	int[] identity = identityArray(n);
	int[] reverse = reverseIdentityArray(n);

	// An instance where all men agree and all women agree.
	run(n, uniformPrefs(identity), uniformPrefs(identity));
	// Another instance where all men agree and all women agree.
	run(n, uniformPrefs(identity), uniformPrefs(reverse));
	// An instance where all men agree and women have diverging visions.
	run(n, uniformPrefs(identity), mixedPrefs(identity, reverse));
	// An instance where all women agree and men have diverging visions.
	run(n, mixedPrefs(identity, reverse), uniformPrefs(identity));

	// Construct random instances of size n.

	for (int r = 0; r < R; r++)
	    run(n, randomPrefs(n), randomPrefs(n));
    }

    // This method submits the box to a series of tests at multiple sizes.

    public void test ()
    {
	int n;

	// First, test at all sizes sequentially up to a certain size.
	for (n = 0; n <= SMALL; n++)
	    test(n);
	// Then, test at larger and larger sizes, doubling the size at each step.
	// (Thus, all sizes will be powers of two; this might not be a good thing
	// in general, as we do not test the algorithm at other sizes, but it
	// should be OK here; we are interested mainly in the performance data).
	for (n = 2 * SMALL; n <= LARGE; n *= 2)
	    test(n);

	out.println();
	out.printf("Done testing. In total, %d success(es) and %d failure(s).\n", successes, failures);
    }

}

