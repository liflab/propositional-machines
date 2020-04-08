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

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.ltl.Troolean;

/**
 * A function that extracts the value of a variable name from a valuation.
 */
public class PropositionalVariable extends UnaryFunction<Valuation,Troolean>
{
  /**
   * The name of the variable
   */
  /*@ non_null @*/ protected String m_varName;
  
  /**
   * Gets an instance of the propositional variable for a given name
   * @param var_name The name of the variable
   * @return The instance
   */
  public static PropositionalVariable get(/*@ non_null @*/ String var_name)
  {
    return new PropositionalVariable(var_name);
  }
  
  /**
   * Creates a new propositional variable
   * @param var_name The name of the variable
   */
  protected PropositionalVariable(String var_name)
  {
    super(Valuation.class, Troolean.class);
    m_varName = var_name;
  }

  @Override
  public Troolean getValue(Valuation v)
  {
    if (v.containsKey(m_varName))
    {
      return v.get(m_varName);
    }
    return Troolean.INCONCLUSIVE;
  }
}
