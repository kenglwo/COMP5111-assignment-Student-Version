// package castle.comp5111.example;
package comp5111.assignment.cut;

import soot.*;
import soot.jimple.*;
import soot.util.Chain;

import java.util.Iterator;
import java.util.Map;

public class Instrumenter1 extends BodyTransformer {

    /* some internal fields */
    static SootClass counterClass;
    static SootMethod addStaticInvocationMethod, addInstanceInvocationMethod;

    static {
        counterClass = Scene.v().loadClassAndSupport("comp5111.assignment.cut.Counter");
        addStaticInvocationMethod = counterClass.getMethod("void addStaticInvocation(int)");
        addInstanceInvocationMethod = counterClass.getMethod("void addInstanceInvocation(int)");
    }



    /*
     * internalTransform goes through a method body and inserts counter
     * instructions before method returns
     */
    @Override
    protected void internalTransform(Body body, String phase, Map options) {
        // body's method
        SootMethod method = body.getMethod();
        // we dont instrument constructor (<init>) and static initializer (<clinit>)
        if (method.isConstructor() || method.isStaticInitializer()) {
            return;
        }

        // debugging
        System.out.println("instrumenting method: " + method.getSignature());

        // get body's unit as a chain
        Chain<Unit> units = body.getUnits();

        // get a snapshot iterator of the unit since we are going to
        // mutate the chain when iterating over it.
        //
        Iterator<?> stmtIt = units.snapshotIterator();

        // typical while loop for iterating over each statement
        while (stmtIt.hasNext()) {

            // cast back to a statement.
            Stmt stmt = (Stmt) stmtIt.next();

						
						// TODO: Branch coverage
            if (stmt instanceof IfStmt) {
                // 1. first, make a new invoke expression
                InvokeExpr incExpr = null;
								incExpr = Jimple.v().newStaticInvokeExpr(
												addStaticInvocationMethod.makeRef(), IntConstant.v(1));

                // 2. then, make a invoke statement
                Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);

                // 3. insert new statement into the chain, before return statement
                // (we are mutating the unit chain).
                units.insertBefore(incStmt, stmt);
            }
        }
    }
}