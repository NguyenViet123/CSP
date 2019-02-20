package hust.vietnv.exercise.ex1;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

class AssignMove{
	int i;
	int v;
	public AssignMove(int i, int v) {
		this.i = i;
		this.v = v;
	}
}

public class Example {

	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[5];
		for(int i = 0; i < 5; i++) {
			x[i] = new VarIntLS(mgr, 1, 5);
		}
		S = new ConstraintSystem(mgr);
		
		S.post(new NotEqual(new FuncPlus(x[2], 3), x[1]));
		S.post(new LessOrEqual(x[3], x[4]));
		S.post(new IsEqual(new FuncPlus(x[2], x[3]),new FuncPlus(x[0], 1)));
		S.post(new LessOrEqual(x[4], 3));
		S.post(new IsEqual(new FuncPlus(x[4], x[1]), 7));
		S.post(new Implicate(new IsEqual(x[2], 1), new NotEqual(x[4], 2)));
		mgr.close();
	}
	
	public void hillClimbing(IConstraint c, int maxIter) {
		VarIntLS[] y = c.getVariables();//cho bien dinh nghia rang buoc c
		ArrayList<AssignMove> cand = new ArrayList<AssignMove>();
		Random R = new Random();
		int it = 0;
		while(it < maxIter && c.violations() > 0) {
			cand.clear();
			int minDelta = Integer.MAX_VALUE;
			for(int i = 0; i < y.length; i++) {
				for(int v = y[i].getMinValue(); v <= y[i].getMaxValue(); v++) {
					int d = c.getAssignDelta(y[i], v);
					if (d < minDelta) {
						minDelta = d;
						cand.clear();
						cand.add(new AssignMove(i, v));
					}else if(d == minDelta) {
						cand.add(new AssignMove(i, v));
					}
				}
			}
			int idx = R.nextInt(cand.size());
			AssignMove m = cand.get(idx);
			y[m.i].setValuePropagate(m.v);
			System.out.println("Step " + it + ", violations = " + c.violations());
			it++;
		}
	}
	
	public void search() {
		hillClimbing(S, 10000);
	}
	
	public void solve() {
		stateModel();
		search();
		for(int i = 0; i < x.length; i++) {
			System.out.print(x[i].getValue() + " ");
		}
	}
	
	public static void main(String[] args) {
		Example s = new Example();
		s.solve();
	}

}
