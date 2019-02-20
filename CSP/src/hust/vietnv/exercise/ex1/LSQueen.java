package hust.vietnv.exercise.ex1;

import localsearch.model.*;
import localsearch.constraints.alldifferent.*;
import localsearch.functions.basic.*;
import localsearch.selectors.*;

public class LSQueen {
	LocalSearchManager mgr;
    VarIntLS[] x;
    ConstraintSystem S;

    int n;
    
    public void stateModel() {
        mgr = new LocalSearchManager();
        x = new VarIntLS[n];
        for (int i=0; i<n; ++i){
            x[i] = new VarIntLS(mgr, 0, n-1);
        }

        S = new ConstraintSystem(mgr);
        S.post(new AllDifferent(x));

        IFunction[] f1 = new IFunction[n];
        for (int i = 0; i< n; ++i) {
            f1[i] = new FuncPlus(x[i], i);
        }
        S.post(new AllDifferent(f1));

        IFunction[] f2 = new IFunction[n];
        for (int i = 0; i< n; ++i) {
            f2[i] = new FuncPlus(x[i], -i);
        }
        S.post(new AllDifferent(f2));

        mgr.close();
    }

    public void printSolution() {
        for(int i = 0; i<n; ++i) {
            System.out.print(x[i].getValue() + " ");
        }
    }

    public void search() {
        System.out.println("Init violations = " + S.violations());

        int it = 0;
        MinMaxSelector mms = new MinMaxSelector(S);

        while(it < 1000 && S.violations() > 0) {
            VarIntLS selX = mms.selectMostViolatingVariable();
            int selV = mms.selectMostPromissingValue(selX);
            selX.setValuePropagate(selV);
            System.out.println("Step" + it + ", violations" + S.violations());
            ++it;
        }
    }

    public LSQueen(int n) {
        this.n = n;
    }

    public void solve() {
        stateModel();
        search();
    }

    public static void main(String[] args) {
        LSQueen nqueen = new LSQueen(8);
        nqueen.solve();
    }
}
