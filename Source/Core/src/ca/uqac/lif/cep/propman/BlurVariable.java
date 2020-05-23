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

import ca.uqac.lif.cep.ltl.Troolean;
import java.util.HashSet;
import java.util.Set;

/**
 * Transforms a multi-event to add uncertainty about the value of a variable
 * <i>x</i>. Formally, for any valuation where <i>x</i> is true (resp. false),
 * the function will add to the output multi-event the corresponding valuation
 * where <i>x</i> is false (resp. true). The end result is that no conclusion
 * can ever be drawn on the value of <i>x</i> in the output multi-event --in
 * other words, it "blurs" the value of <i>x</i>.
 *  
 * @author Sylvain Hallé, Rania Taleb
 */
public class BlurVariable extends MultiEventFunction
{
  /**
   * The name of the variable to blur
   */
  protected String m_variable;
  
  /**
   * Creates a new instance of the function
   * @param variable The name of the variable to blur
   */
  public BlurVariable(String variable)
  {
    super();
    m_variable = variable;
  }
  
  @Override
  public MultiEvent getValue(MultiEvent x)
  {
    Set<Valuation> vals = x.getValuations();
    Set<Valuation> new_vals = new HashSet<Valuation>();
    new_vals.addAll(vals);
    for (Valuation v : vals)
    {
     Valuation v2 = new Valuation();
     v2.putAll(v2);
     if (v.get(m_variable) == Troolean.Value.TRUE)
     {
       v2.put(m_variable, Troolean.Value.FALSE);
     }
     else
     {
       v2.put(m_variable, Troolean.Value.TRUE);
     }
     new_vals.add(v2);
    }
    return new ConcreteMultiEvent(vals);
  }
}
