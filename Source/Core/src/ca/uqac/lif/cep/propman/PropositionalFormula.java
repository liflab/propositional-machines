/*
    Monitors for multi-events
    Copyright (C) 2020 Sylvain Hallé, Rania Taleb, Raphaël Khoury

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
import java.util.HashSet;
import java.util.Set;

/**
 * A function tree whose nodes are Troolean functions, and whose leaves are
 * {@link PropositionalVariable}s.
 */
public class PropositionalFormula extends FunctionTree
{
  /**
   * Creates a new propositional formula
   * @param f The function
   */
  public PropositionalFormula(Function f)
  {
    super(f);
  }
  
  /**
   * Creates a new propositional formula
   * @param functions The functions
   */
  public PropositionalFormula(Function ... functions)
  {
    super(functions);
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
    if (!(outputs[0] instanceof Troolean))
    {
      throw new FunctionException("Output of expression is not a Troolean");
    }
    return (Troolean.Value) outputs[0];
  }
  
  /**
   * Gets the set of propositional variables contained in this formula
   * @return The set of variable names
   */
  public Set<String> getDomain()
  {
    Set<String> domain = new HashSet<String>();
    fetchDomain(this, domain);
    return domain;
  }
  
  /**
   * Recursively fetches the name of all propositional variables in a
   * propositional formula
   * @param f The formula
   * @param domain A pre-initialized set that where variable names are
   * added
   */
  protected static void fetchDomain(Function f, Set<String> domain)
  {
    if (f instanceof PropositionalVariable)
    {
      domain.add(((PropositionalVariable) f).m_varName);
    }
    else if (f instanceof PropositionalFormula)
    {
      PropositionalFormula pf = (PropositionalFormula) f;
      for (Function fc : pf.m_children)
      {
        fetchDomain(fc, domain);
      }  
    }
  }
}
