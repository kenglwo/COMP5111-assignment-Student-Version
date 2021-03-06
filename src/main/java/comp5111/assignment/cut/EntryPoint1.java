// package castle.comp5111.example;
package comp5111.assignment.cut;

import org.junit.runner.JUnitCore;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class EntryPoint1 {
    public static void main(String[] args) {

				instrumentWithSoot(args);

        // after instrument, we run Junit tests
        runJunitTests();
        // after junit test running, we have already get the counting in the Counter class
        System.out.println("Invocation to branch: " + Counter.getNumStaticInvocations());
        // System.out.println("Invocation to instance methods: " + Counter.getNumInstanceInvocations());

				try {
					FileWriter file = new FileWriter("./coverage-report.txt", true);
					PrintWriter pw = new PrintWriter(new BufferedWriter(file));
					
					pw.println("Invocation to branch: " + Counter.getNumStaticInvocations());
					
					pw.close();
				} catch (IOException e){
					e.printStackTrace();
				}
    }

    private static void instrumentWithSoot(String[] classNames) {
    // private static void instrumentWithSoot(String className) {
        // the path to the compiled Subject class file
        String classUnderTestPath = "./raw-classes";
        String targetPath = "./target/classes";

        String classPathSeparator = ":";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            classPathSeparator = ";";
        }

        /*Set the soot-classpath to include the helper class and class to analyze*/
        Options.v().set_soot_classpath(Scene.v().defaultClassPath() + classPathSeparator + targetPath + classPathSeparator + classUnderTestPath);

        // we set the soot output dir to target/classes so that the instrumented class can override the class file
        Options.v().set_output_dir(targetPath);

        // retain line numbers
        Options.v().set_keep_line_number(true);
        // retain the original variable names
        Options.v().setPhaseOption("jb", "use-original-names:true");

        /* add a phase to transformer pack by call Pack.add */
        Pack jtp = PackManager.v().getPack("jtp");

        Instrumenter1 instrumenter = new Instrumenter1();
        jtp.add(new Transform("jtp.instrumenter", instrumenter));


        // pass arguments to soot
        soot.Main.main(classNames);
				
    }

    private static void runJunitTests() {
        Class<?> testClass = null;
        try {
            // here we programmitically run junit tests
            // testClass = Class.forName("castle.comp5111.example.test.RegressionTest");
            testClass = Class.forName("comp5111.assignment.cut.RegressionTest");
            JUnitCore junit = new JUnitCore();
            System.out.println("Running junit test: " + testClass.getName());
            junit.run(testClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
