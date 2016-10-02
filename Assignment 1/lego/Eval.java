// Evaluator for Lego formulas
package lego;

import java.util.Stack;

import lego.parser.*;

// data structure to store values of free variables
class Env {
	Stack<Var> e = new Stack<Var>();
}

public class Eval {

	public static boolean eval(Formula f) {

		// System.out.println("atomic");

		if (f instanceof Atomic) {
			// System.out.println("atomic");
			// Variables
			int x = 0;
			int y = 0;
			// handle Atomic
			Atomic a = (Atomic) f;
			if (a.e1 instanceof Int) {
				x = Integer.parseInt(a.e1.toString());
			}
			if (a.e2 instanceof Int) {
				y = Integer.parseInt(a.e2.toString());
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
			return binEval(f);
		}
		if (f instanceof Unary) {
			// System.out.println("Unary");
			return unEval(f);
		} else {
			System.out.println("Formula given not recognized as Unary, " + "Binary, or Atomic. Trying quantified");
			return eval(f, new Env());
		}
		// return eval(f, new Env());
	}

	// there is a path through this method in which no value is returned
	public static boolean eval(Formula f, Env e) {
		//System.out.println("entered an enviroment");
		if (f instanceof Quantified) {
			Quantified q = (Quantified) f;
			
			if (q.quant.toString() == "forall") {
				// limit this to 100000 entries (for the purposes of simplifying
				// this assignment)
				boolean result;
				boolean[] resultArray = new boolean[100000];
				int lower = Integer.parseInt(q.dom.from.toString());
				int upper = Integer.parseInt(q.dom.to.toString());
				int index = 0;
				 e.e.push(q.var);
				 for(int i = lower; i < upper; i++){
					if (q.f instanceof Atomic){
						result = eval(q.f);
					}
					else{
					result = eval(q.f,e);
					}
					resultArray[index] = result;
					index++;
				 }
				 e.e.pop();
				 while (index > 0){
					 if (resultArray[index-1]== false){
						 // They all need to be true forall to be true
						 return false;
					 }
					 //if it is true continue iterating
					 index--;
				 }
				 return true;
			}
			
			if (q.quant.toString() == "exists"){
				// Do something
				// limit this to 100000 entries (for the purposes of simplifying
				// this assignment)
				boolean result;
				boolean[] resultArray = new boolean[100000];
				int lower = Integer.parseInt(q.dom.from.toString());
				int upper = Integer.parseInt(q.dom.to.toString());
				int index = 0;
				 e.e.push(q.var);
				 for(int i = lower; i < upper; i++){
					if (q.f instanceof Atomic){
						result = eval(q.f);
					}
					else{
					result = eval(q.f,e);
					}
					resultArray[index] = result;
					index++;
				 }
				 e.e.pop();
				 while (index > 0){
					 if (resultArray[index-1]== true){
						 // They all need to be true forall to be true
						 return true;
					 }
					 //if it is true continue iterating
					 index--;
				 }
				 return false;
			}
			
			else{
				System.out.println("Error 06: We have reached a 3rd state in quantification");
				return false;//safer assumption
			}
		} else{
			//eval(f, e) has been passed a non quantified formula
			System.out.println("Error 05:  eval(q.f, e) has been passed a non quantified formula");
			return false;
		}
	}

	public static boolean unEval(Formula f) {
		boolean result;
		if (f instanceof Unary) {
			Unary u = (Unary) f;
			if (u.f instanceof Unary) {
				result = unEval(u.f);
			}
			if (u.f instanceof Binary) {
				result = binEval(u.f);
			}
			if (u.f instanceof Atomic) {
				result = eval(u.f);
			} else {// result isn't Initalized bad...
				System.out.println("Error 02:Result didn't have a value in unEval setting it to true so it fails");
				result = true;// will return false (safer assumption)
			}
			return !result;
		}

		else {
			// Pretty much unreachable in normal operation
			System.out.println("Error 03: unEval got an equation that was not Binary, Unary, or Atomic");
			return false;
		}
	}

	public static boolean binEval(Formula f) {
		if (f instanceof Binary) {
			boolean result = false;
			boolean result2 = false;
			Binary b = (Binary) f;
			if (b.f1 instanceof Binary) {
				// System.out.println(b.f1.toString());
				result = binEval(b.f1);
			} else {
				// System.out.println(b.f1.toString()+" "+"atomic");
				result = eval(b.f1);
			}

			if (b.f2 instanceof Binary) {
				// System.out.println(b.f2.toString());
				result2 = binEval(b.f2);
			} else { // it will be atomic
				// System.out.println(b.f2.toString()+" "+"atomic");
				result2 = eval(b.f2);
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
