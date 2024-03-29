// Evaluator for Lego formulas
//Edited by 10107325 Cody Weeden
package lego;

import java.util.Stack;

import lego.parser.*;

// data structure to store values of free variables
class Env {
	Stack e;

	Env() {
		e = new Stack<Tuple>();
	}
}

class Tuple<One, Two> {
	public final One one;
	public final Two two;

	public Tuple(One one, Two two) {
		this.one = one;
		this.two = two;
	}
}

public class Eval {

	public static boolean eval(Formula f) throws Exception {
		return eval(f, new Env());
	}

	// there is a path through this method in which no value is returned
	public static boolean eval(Formula f, Env e) throws Exception {
		// System.out.println("entered an enviroment");

		if (f instanceof Atomic) {
			// System.out.println("atomic");
			// Variables
			int x = 0;
			int y = 0;

			if (e.e.isEmpty()) {
				System.out.println("The stack is empty");
			}
/*			else{
				System.out.println("The stack is full");
			}*/
			/*
			 * else if (e.e.size() == 2){ y = (int) e.e.peek(); e.e.pop(); x =
			 * (int) e.e.peek(); } else if (e.e.size () == 1){ x = (int)
			 * e.e.peek();
			 * 
			 * }
			 */
			/*
			 * else{
			 * System.out.println("the stack has more than 2 values on it."); }
			 */
			// System.out.println((e.e.peek().toString()));

			// handle Atomic
			Atomic a = (Atomic) f;
			// int result = 0;
			Tuple t1;
			Tuple t2;
			boolean result;
			// System.out.println(a.e1.toString());
			// System.out.println(a.e2.toString());

			if (a.e1 instanceof Int) {
				x = Integer.parseInt(a.e1.toString());
				// System.out.println(x);
			} else if (a.e1 instanceof Var) {
				t1 = (Tuple) e.e.peek();
				x = (int) t1.two;
				// System.out.println(x);
			} else if (a.e1 instanceof BinExp) {
				// System.out.println("a1 is Exp");
				x = (int) evalExp(a.e1, e);
			}

			if (a.e2 instanceof Int) {
				y = Integer.parseInt(a.e2.toString());
			} else if (a.e2 instanceof Var) {
				t2 = (Tuple) e.e.peek();
				y = (int) t2.two;
			} else if (a.e2 instanceof Exp) {
				//System.out.println("a2 is Exp");
				y = (int) evalExp(a.e2, e);
			}
			if (a.rel_op.toString() == ">") {
				if (x > y)
					return true;
				else
					return false;

			}
			if (a.rel_op.toString() == ">=") {
				if (x >= y)
					return true;
				else
					return false;

			}
			if (a.rel_op.toString() == "=") {
				if (x == y) {
					return true;
				} else {
					return false;
				}
			}
		}

		if (f instanceof Binary) {
			// System.out.println("Binary");
			return binEval(f, e);
		}
		if (f instanceof Unary) {
			// System.out.println("Unary");
			return unEval(f, e);
		}

		if (f instanceof Quantified) {
			Quantified q = (Quantified) f;

			if (q.quant.toString() == "forall") {
				// limit this to 100000 entries (for the purposes of simplifying
				// this assignment)
				boolean result= false;
				//boolean[] resultArray = new boolean[100000];
				int lower = Integer.parseInt(q.dom.from.toString());
				int upper = Integer.parseInt(q.dom.to.toString());
				int index = 0;
				boolean prevResult = true;
				for (int i = lower; i < upper; i++) {
					Tuple t = new Tuple(q.var, i);
					e.e.push(t);
					result = eval(q.f, e);
					e.e.pop();
					if (!(result&&prevResult)){
						return false;
					}
					//resultArray[index] = result;
					//index++;
					/*
					 * if (!e.e.isEmpty()) e.e.pop();
					 */ }
				return result;
				//e.e.pop();
				/*while (index > 0) {
					if (resultArray[index - 1] == false) {
						// They all need to be true forall to be true
						return false;
					}
					// if it is true continue iterating
					index--;
				}
				return true;*/
			}

			if (q.quant.toString() == "exists") {
				// Do something
				// limit this to 100000 entries (for the purposes of simplifying
				// this assignment)
				boolean result = false;
				//boolean[] resultArray = new boolean[100000];
				int lower = Integer.parseInt(q.dom.from.toString());
				int upper = Integer.parseInt(q.dom.to.toString());
				int index = 0;
				boolean prevResult = false;
				for (int i = lower; i < upper; i++) {
					Tuple t = new Tuple(q.var, i);
					e.e.push(t);
					result = eval(q.f, e);
					e.e.pop();
					if (result || prevResult){
						return true;
					}
					/*resultArray[index] = result;
					if (index > 99990) {
						System.out.println("Reaching result array limits");
					}
					index++;*/
				}
				return result;
				
	
			}

		else {
				System.out.println("Error 06: We have reached a 3rd state in quantification");
				return false;// safer assumption
			}
		} else {
			// eval(f, e) has been passed a non quantified formula
			System.out.println("Error 05:  eval(q.f, e) has been passed a non quantified formula");
			return false;
		}
	}

	public static boolean unEval(Formula f, Env e) throws Exception {
		boolean result;
		if (f instanceof Unary) {
			Unary u = (Unary) f;
			result = eval(u.f,e);
			} else {// result isn't Initalized bad...
				System.out.println("Error 02:Result didn't have a value in unEval setting it to true so it fails");
				result = true;// will return false (safer assumption)
			}
			if (result == true){
				return false;
			}
			else{
				return true;
			}
		}


public static Object evalExp (Exp e1, Env e) throws Exception{
	if (e1 instanceof Int){
		return e1.getClass().getDeclaredField("n").get(e1);
		
	}
	else if (e1 instanceof Var) {
		String varName = e1.toString();
		Stack localStack = (Stack) e.e.clone();
		while (!localStack.isEmpty()) {
			 Tuple var =(Tuple) localStack.pop();
			/* System.out.println(var.one.toString().getClass());
			 System.out.println(varName.getClass());*/
			if (varName.equals(var.one.toString())) {	return var.two;}
		}
		throw new Exception("Variable " + varName + " undefined");
	}
	else if (e1 instanceof BinExp) {
		BinExp b = (BinExp) e1;
		String op = b.bin_op.toString();
		Exp exp1 = b.e1;
		Exp exp2 = b.e2;
		Integer val1 = (Integer) evalExp(exp1, e);
		Integer val2 = (Integer) evalExp(exp2, e);
		
		if (op.equals("+")) { return (val1 + val2); }
		else if (op.equals("-")) { return (val1 - val2); }
		else if (op.equals("*")) { return (val1 * val2); }
		else if (op.equals("/")) { 
			if (val2 == 0) { 
				throw new Exception ("Division by zero in " + val1 + " / " + val2);
			}
			return (val1 / val2); }
		else if (op.equals("mod")) { 
			if (val2 == 0) { 
				throw new Exception ("Mod by zero in " + val1 + " % " + val2);
			}
			return (val1 % val2); }
	}
	else if (e1 instanceof Exp) {
		return evalExp(e1, e); //how do you get in here?
	}
	throw new Exception("Unknown expression format");
}
	
	

	
	public static boolean binEval(Formula f, Env e) throws Exception {
		if (f instanceof Binary) {
			boolean result = false;
			boolean result2 = false;
			Binary b = (Binary) f;
			if (b.f1 instanceof Binary) {
				// System.out.println(b.f1.toString());
				result = binEval(b.f1, e);
			} else {
				// System.out.println(b.f1.toString()+" "+"atomic");
				result = eval(b.f1, e);
			}

			if (b.f2 instanceof Binary) {
				// System.out.println(b.f2.toString());
				result2 = binEval(b.f2, e);
			} else { // it will be atomic
				// System.out.println(b.f2.toString()+" "+"atomic");
				result2 = eval(b.f2, e);
			}

			/*
			 * //For Result Type Error Checking System.out.println(result);
			 * System.out.println(result2);
			 */

			if (b.bin_conn.toString() == "&&") {
				return result && result2;
			}
			if (b.bin_conn.toString() == "||") {
				return result || result2;
			}
			if (b.bin_conn.toString() == "->") {
				return !(result && (!result2));
			}
			if (b.bin_conn.toString() == "<->") {
				return !(result && !result2) && !(result2 && !result);
			} else {
				return false;
			}
		} else {
			// pretty much an unreachable state. in normal operation
			System.out.println("Error 04: There was a Non Binary argument passed to binary eval");
			return false;
		}
	}
}
