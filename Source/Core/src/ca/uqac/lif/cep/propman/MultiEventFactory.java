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

import java.util.HashSet;
import java.util.Set;

public class MultiEventFactory
{
  /**
   * The variables used in the multi-events to generate
   */
  /*@ non_null @*/ protected String[] m_variables;
  
  /**
   * Creates a new multi-event factory
   * @param variables The variables used in the multi-events to generate
   */
  public MultiEventFactory(/*@ non_null @*/ String ... variables)
  {
    super();
    m_variables = variables;
  }
  
  /**
   * Reads a multi-event from a string, by attempting to guess if the
   * event is written as a list of valuations or as a Boolean formula.
   * @param s The string to read from
   * @return The multi-event, or <tt>null</tt> if no event could be
   * read from the string
   */
  /*@ null @*/ public MultiEvent readFromString(/*@ non_null @*/ String s)
  {
    // Guess
    if (s.matches("[TF\\?,]*"))
    {
      return readFromValuations(s);
    }
    return readFromFormula(s);
  }
  
  /**
   * Reads a multi-event from a list of valuations
   * @param s The string containing the list of valuations
   * @return The multi-event, or <tt>null</tt> if the input string
   * was malformed
   */
  /*@ null @*/ public MultiEvent readFromValuations(/*@ non_null @*/ String s)
  {
    String[] valuations = s.split(",");
    Set<Valuation> s_val = new HashSet<Valuation>();
    for (String v : valuations)
    {
      Valuation val = Valuation.readFromString(v, m_variables);
      if (val != null)
      {
        s_val.add(val);
      }
    }
    ConcreteMultiEvent e = new ConcreteMultiEvent(s_val);
    return e;
  }
  
  /*@ null @*/ public MultiEvent readFromFormula(/*@ non_null @*/ String s)
  {
    return null;
  }
}
