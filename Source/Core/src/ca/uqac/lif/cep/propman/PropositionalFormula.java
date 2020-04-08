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

import ca.uqac.lif.cep.functions.FunctionException;
import ca.uqac.lif.cep.functions.FunctionTree;
import ca.uqac.lif.cep.ltl.Troolean;
import java.util.Set;

/**
 * A function tree whose nodes are Troolean functions, and whose leaves are
 * {@link PropositionalVariable}s.
 */
public class PropositionalFormula extends FunctionTree
{
  /**
   * Evaluates a propositional formula on a given valuation.
   * @param v The valuation
   * @return The value of the formula for this valuation
   */
  public Troolean evaluate(Valuation v)
  {
    Object[] inputs = new Object[] {v};
    Object[] outputs = new Object[1];
    evaluate(inputs, outputs);
    if (!(outputs[0] instanceof Troolean))
    {
      throw new FunctionException("Output of expression is not a Troolean");
    }
    return (Troolean) outputs[0];
  }
  
  /**
   * Determines if the current formula supports another propositional formula.
   * @param phi The other formula
   * @return <tt>true</tt> if it supports it; <tt>false</tt> otherwise
   */
  public boolean supports(/*@ non_null @*/ PropositionalFormula phi)
  {
    Set<Valuation> this_rel = getOptimalRelaxations();
    Set<Valuation> phi_rel = phi.getOptimalRelaxations();
    // TODO!
    return false;
  }
  
  /**
   * Computes the set of optimal relaxations of this propositional formula.
   * @return The set of relaxations
   */
  /*@ non_null @*/ protected Set<Valuation> getOptimalRelaxations()
  {
    // TODO!
    return null;
  }
}
