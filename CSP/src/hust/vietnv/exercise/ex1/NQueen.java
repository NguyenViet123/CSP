package hust.vietnv.exercise.ex1;

import java.io.PrintWriter;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class NQueen {

	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;
	int n;
		
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[n];
		for(int i = 0; i < n; i++) {
			x[i] = new VarIntLS(mgr, 0, n-1);
		}
		S = new ConstraintSystem(mgr);//khoi tao he thong rang buoc
		S.post(new AllDifferent(x));//hai bien bat ki la khac nhau
		
		IFunction[] f1 = new IFunction[n];
		for(int i = 0; i < n; i++) {
			f1[i] = new FuncPlus(x[i], i);
		}
		S.post(new AllDifferent(f1));
		IFunction[] f2 = new IFunction[n];
		for(int i = 0; i < n; i++) {
			f2[i] = new FuncPlus(x[i], -i);
		}
		S.post(new AllDifferent(f2));
		
		mgr.close();
	}
	
	public void search() {
		printSolution();
		System.out.println("Init violations = " + S.violations());
		int it = 0;
		
		MinMaxSelector mms = new MinMaxSelector(S);
		
		while(it < 100000 && S.violations() > 0) {
			VarIntLS sel_x = mms.selectMostViolatingVariable();
			int sel_v = mms.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(sel_v);
			System.out.println("Step " + it + ", violations: " + S.violations());
			it++;
		}
		printSolution();
	}
	
	
	public void printHTML(String fn) {
		try {
			PrintWriter out = new PrintWriter(fn);
			out.println("<table border = 1>");
			for(int i = 0; i < n; i++) {
				out.println("<tr>");
				for(int j = 0; j < n; j++) {
					if(x[j].getValue() == i) {
						out.println("<td width='20px' height='20px' bgcolor='red'></td>");
					}else {
						out.println("<td width='20px' height='20px' bgcolor='blue'></td>");
					}
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.close();
		}catch (Exception e) {
			
		}
	}
	
	public void printSolution() {
		for(int i = 0; i < n; i++) {
			System.out.print(x[i].getValue() + " ");
		}
		System.out.println();
	}
	
	public void hillClimbing() {
		int it = 0;
		while(it < 100000 && S.violations() > 0) {
			int min_delta = Integer.MAX_VALUE;
			int seli = -1;
			int selv = -1;
			for(int i = 0; i < n; i++) {
				for(int v = x[i].getMinValue(); v <= x[i].getMaxValue(); v++) {
					int delta = S.getAssignDelta(x[i], v);
					if (delta < min_delta) {
						min_delta = delta;
						seli = i;
						selv = v;
					}
				}
			}
			x[seli].setValuePropagate(selv);
			System.out.println("Step " + it + ", violations  = " + S.violations());
			it++;
		}
	}
	
	public NQueen(int n) {
		this.n = n;
	}
	
	public void solve() {
		stateModel();
		search();
	}
	
	public static void main(String[] args) {
		System.out.println("START");
		NQueen s = new NQueen(20);
		s.solve();
		s.printHTML("test.html");
	}
}

