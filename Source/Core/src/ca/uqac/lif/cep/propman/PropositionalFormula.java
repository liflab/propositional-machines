/*
    A BeepBeep palette for propositional machines
    Copyright (C) 2020 Rania Taleb, Sylvain Hallé and friends

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.propman;

import ca.uqac.lif.cep.functions.Function;
import ca.uqac.lif.cep.functions.FunctionException;
import ca.uqac.lif.cep.functions.FunctionTree;
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.ltl.Troolean.Value;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A function tree whose nodes are Troolean functions, and whose leaves are
 * {@link PropositionalVariable}s.
 */
public class PropositionalFormula extends FunctionTree
{
	
	public PropositionalFormula(Function... functions) {
		super(functions);
		// TODO Auto-generated constructor stub
	}


	public PropositionalFormula(Function f) {
		super(f);
		// TODO Auto-generated constructor stub
	}
  /**
   * Evaluates a propositional formula on a given valuation.
   * @param v The valuation
   * @return The value of the formula for this valuation
   */
  public Troolean.Value evaluate(Valuation v)
  {
    Object[] inputs = new Object[] {v};
    Object[] outputs = new Object[1];
    evaluate(inputs, outputs);
    if (!(outputs[0] instanceof Troolean.Value))
    {
      throw new FunctionException("Output of expression is not a Troolean Value");
    }
    return (Troolean.Value) outputs[0];
  }
  

/**
   * Determines if the current formula supports another propositional formula.
   * Get the sets of OPTIMAL relaxations in "this" and "phi" and iterate through them to find 2 valuations that evaluates to TRUE
   * Check if the valuation of "phi" is a partial order of that of "this"
   * @return <tt>true</tt> if "this" supports "phi"; <tt>false</tt> otherwise
   */
  public boolean supports(/*@ non_null @*/ PropositionalFormula phi, PropositionalVariable[] vars) 
  {
    System.out.println("Optimal Valuations of the first formula:");
	Set<Valuation> this_rel = getOptimalRelaxations(vars);
	
    System.out.println("Optimal Valuations of the second formula:");
    Set<Valuation> phi_rel = phi.getOptimalRelaxations(vars);
    
    Iterator<Valuation> this_iterate = this_rel.iterator();
    Iterator<Valuation> phi_iterate = phi_rel.iterator();
    
    Valuation this_next = new Valuation();
    Valuation phi_next = new Valuation();
    
    while(this_iterate.hasNext()){
    	this_next = this_iterate.next();
    	if (this.evaluate(this_next) == Troolean.Value.TRUE){
    		while(phi_iterate.hasNext()){
    			phi_next = phi_iterate.next();
    	    	if (phi.evaluate(phi_next) == Troolean.Value.TRUE){
    	    		if(phi_next.isPartialOrder(this_next)){
    	    		    System.out.println("First formula supports the second one because:");
    	    		    System.out.print("Valuation v1 of the first formula is an optimal relaxation:");
    	    		    printValuation(this_next,Troolean.Value.TRUE);
    	    		    System.out.print("Valuation v2 of the second formula is an optimal relaxation:");
    	    		    printValuation(phi_next,Troolean.Value.TRUE);
    	    		    System.out.print("and v2 is a partial order of v1");
    	    			return true;
    	    		}
    	    	}
    		}
    	}
    }  
    return false;
  }
  
 
  /*@ non_null @*/ protected Set<Valuation> getOptimalRelaxations(PropositionalVariable[] vars)
  {
      Set<Valuation> allTrueValuations = new HashSet<Valuation>();
      Set<Valuation> vals = new HashSet<Valuation>();
      Troolean.Value[] values = new Troolean.Value[vars.length];
      allTrueValuations = this.generateAllValuations(vars, values, 0, vals);
      
      Iterator<Valuation> iterate = allTrueValuations.iterator();
      Set<Valuation> optimalValuations = new HashSet<Valuation>();
      Valuation next = new Valuation();
      
      while(iterate.hasNext()){
    	  next = iterate.next();
    	  if (next.isOptimal(allTrueValuations)){
    		  this.printValuation(next, evaluate(next));    		 
    		  optimalValuations.add(next);
    	  }
      }
      return optimalValuations;
  }
    
	public void printValuation(Valuation v, Troolean.Value value) 
	{ 
		
		Set<Entry<String, Value>> entrySet = v.entrySet();
		Iterator<Entry<String, Value>> it = entrySet.iterator();
	    
		System.out.print("{ ");
		while(it.hasNext()){
	       Map.Entry m = (Map.Entry)it.next();
	       if (m.getValue().toString().equals("TRUE")){
				System.out.print(m.getKey() + "=T "); 
	       }
	       if (m.getValue().toString().equals("INCONCLUSIVE")){
				System.out.print(m.getKey() + "=? "); 
	       }
	   }
		System.out.print(" }");  
		System.out.println(" => " + value);	
	}
    
    
	/**
	   * Generate all possible permutations that give a true valuation 
	   * @param array of propositional variables to know the number of digits of permutation, an index "i" to iterate and know when we reach the end of values[] to create and store valuation 
	   * empty array to store values and empty set to store valuations and return it 
	   * @return the set of valuations that evaluate to true on this propositional formula
	   */
	public Set<Valuation> generateAllValuations(PropositionalVariable[] vars, Troolean.Value[] values, int i, Set<Valuation> trueValuations) { 
    	
    	if (i == values.length) { 
    		Valuation v = new Valuation();
    		int j,k;
    		for (j =0, k =0; j< vars.length && k<values.length; k++, j++){
    			v.put(vars[j].getM_varName(), values[k]);
    		}
    		 
    		if (evaluate(v) == Troolean.Value.TRUE){
    			trueValuations.add(v);
    			//printValuation(v , evaluate(v));
    		}
			return null;
    	} 

        // Assign "INCONCLUSIVE" at ith position and try for all other permutations for remaining positions 
    	values[i] = Value.INCONCLUSIVE; 
    	generateAllValuations(vars, values, i + 1, trueValuations); 

       // Assign "TRUE" at ith position and try for all other permutations for remaining positions 
    	values[i] = Value.TRUE; 
    	generateAllValuations(vars, values, i + 1, trueValuations); 
    	return trueValuations; 
    	}



    
}