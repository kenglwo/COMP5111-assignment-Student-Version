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

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;



public class EntryPoint {
		public static float linenumber = 0;
    public static void main(String[] args) {

				instrumentWithSoot(args);


        // after instrument, we run Junit tests
        runJunitTests();
        // after junit test running, we have already get the counting in the Counter class
				
				// get total number of statement
				float linenumber = 0;
				try{

					// File file =new File("./target/classes/randoop0/comp5111/assignment/cut/RegressionTest0.class");
					File file = new File("./src/main/java/comp5111/assignment/cut/ToolBox.java");

					if(file.exists()){

							FileReader fr = new FileReader(file);
							LineNumberReader lnr = new LineNumberReader(fr);

							linenumber = 0;

							while (lnr.readLine() != null){
							linenumber++;
							}

							lnr.close();

					}else{
							 System.out.println("File does not exists!");
					}

				}catch(IOException e){
						e.printStackTrace();
				}

				float num = Counter.getNumStaticInvocations();
				float coverage = Counter.getNumStaticInvocations() / linenumber * 100;

        System.out.println("Invocation to statement: " + num);
				System.out.println("Total number of lines : " + linenumber);
				System.out.println("Coverage: " + coverage + "%");

				// if (args.length > 1){
					System.out.println("====== Statement coverage =====");

					try {
						FileWriter file = new FileWriter("./coverage-report.txt", true);
						PrintWriter pw = new PrintWriter(new BufferedWriter(file));
						
						pw.println("===== Statement Coverage =====");
						pw.println("Invocation to statement: " + num);
						pw.println("Total number of statement : " + linenumber);
						pw.println("Coverage : " + coverage + "%");
						pw.println("");
						
						pw.close();
					} catch (IOException e){
						e.printStackTrace();
					}
				// } else { // coverage for each class
				// 	try {
				// 	FileWriter file = new FileWriter("./coverage-report.txt", true);
				// 	PrintWriter pw = new PrintWriter(new BufferedWriter(file));
        //
				// 	pw.println("\t Class Name: " + args[1]);
				// 	pw.println("Invocation to statement: " + num);
				// 	pw.println("\t Coverage : " + coverage + "%");
				// 	} catch (IOException e){
				// 		e.printStackTrace();
				// 	}
				// }

				

    }

    private static void instrumentWithSoot(String[] classNames) {
    // private static void instrumentWithSoot(String className) {
        // the path to the compiled Subject class file
        String classUnderTestPath = "./raw-classes";
        String targetPath = "./target/classes";

        // String targetPathTest = "./target/classes/randoop0";
        String targetPathTest = "./target/classes/randoop0:./target/classes/randoop1:./target/classes/randoop2:./target/classes/randoop3:./target/classes/randoop4";

        String classPathSeparator = ":";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            classPathSeparator = ";";
        }

        /*Set the soot-classpath to include the helper class and class to analyze*/
        // Options.v().set_soot_classpath(Scene.v().defaultClassPath() + classPathSeparator + targetPath + classPathSeparator + classUnderTestPath);
        Options.v().set_soot_classpath(Scene.v().defaultClassPath() + classPathSeparator + targetPath + classPathSeparator + classUnderTestPath + classPathSeparator + targetPathTest);

        // we set the soot output dir to target/classes so that the instrumented class can override the class file
        Options.v().set_output_dir(targetPath);

        // retain line numbers
        Options.v().set_keep_line_number(true);
        // retain the original variable names
        Options.v().setPhaseOption("jb", "use-original-names:true");

        /* add a phase to transformer pack by call Pack.add */
        Pack jtp = PackManager.v().getPack("jtp");

        Instrumenter instrumenter = new Instrumenter();
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
