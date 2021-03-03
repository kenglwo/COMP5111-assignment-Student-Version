// package castle.comp5111.example;
package comp5111.assignment.cut;

import org.junit.runner.JUnitCore;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;

public class EntryPoint {
    public static void main(String[] args) {

				// System.out.println("######## in EntryPoint ##########");
				// for (int i = 0; i < args.length; i++) {
				// 	String className = "comp5111.assignment.cut." + args[i] ;
				// 	instrumentWithSoot(className);
				// }

				instrumentWithSoot(args);

        // after instrument, we run Junit tests
        runJunitTests();
        // after junit test running, we have already get the counting in the Counter class
        System.out.println("Invocation to static methods: " + Counter.getNumStaticInvocations());
        System.out.println("Invocation to instance methods: " + Counter.getNumInstanceInvocations());
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

        Instrumenter instrumenter = new Instrumenter();
        jtp.add(new Transform("jtp.instrumenter", instrumenter));

        // String classUnderTest = "comp5111.assignment.cut.ToolBox";
        // String classUnderTest = "comp5111.assignment.cut.ToolBox$ArrayTools";
        // String classUnderTest = "comp5111.assignment.cut.ToolBox$CharSequenceTools";
        // String classUnderTest = "comp5111.assignment.cut.ToolBox$CharTools";
        // String classUnderTest = "comp5111.assignment.cut.ToolBox$LocaleTools";
        // String classUnderTest = "comp5111.assignment.cut.ToolBox$RegExTools";
        // String classUnderTest = "comp5111.assignment.cut.ToolBox$StringTools";
        // String classUnderTest = className;

        // String classUnderTest = "comp5111.assignment.cut." + classNames[0];
        // String classUnderTest = "comp5111.assignment.cut." + className;
        // String classUnderTest = className;

				// for (int i = 0; i < classNames.length; i++) {
					// String className = "comp5111.assignment.cut." + classNames[i] ;
					// System.out.println("prev class name: " + classNames[i]);
					// String newClassName = "comp5111.assignment.cut." + classNames[i] ;
					// classNames[i] = newClassName;
					// System.out.println("new class name: " + newClassName);
					// soot.Main.main(new String[]{className});
				// }
        // soot.Main.main(new String[]{classUnderTest});
        soot.Main.main(classNames);
				
        // pass arguments to soot
        // soot.Main.main(new String[]{classUnderTest});
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
