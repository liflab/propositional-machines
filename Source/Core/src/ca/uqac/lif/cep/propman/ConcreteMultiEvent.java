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
 * A multi-event that contains a concrete set of valuations
 * @author Sylvain Hallé, Rania Taleb
 */
public class ConcreteMultiEvent implements MultiEvent
{
  /**
   * The set of valuations contained in this multi-event
   */
  protected Set<Valuation> m_valuations;
  
  /**
   * Creates a new empty concrete multi-event
   */
  public ConcreteMultiEvent()
  {
    super();
    m_valuations = new HashSet<Valuation>();
  }
  
  /**
   * Creates a new empty concrete multi-event with a given set of valuations
   * @param valuations The set of valuations contained in this multi-event
   */
  public ConcreteMultiEvent(Set<Valuation> valuations)
  {
    super();
    m_valuations = new HashSet<Valuation>(valuations.size());
    m_valuations.addAll(valuations);
  }

  @Override
  public Set<Valuation> getValuations()
  {
    return m_valuations;
  }
  
  @Override
  public Set<String> getDomain()
  {
    for (Valuation v : m_valuations)
    {
      return v.keySet();
    }
    return new HashSet<String>(0);
  }

  @Override
  public boolean intersects(MultiEvent e)
  {
    if (e instanceof ConcreteMultiEvent)
    {
      return intersectsWith((ConcreteMultiEvent) e);
    }
    if (e instanceof SymbolicMultiEvent)
    {
      return intersectsWith((SymbolicMultiEvent) e);
    }
    return false;
  }
  
  /**
   * Determines if this multi-event intersects with another concrete
   * multi-event.
   * @param e The other multi-event
   * @return <tt>true</tt> if they intersect, <tt>false</tt> otherwise
   */
  protected boolean intersectsWith(ConcreteMultiEvent e)
  {
    for (Valuation v : m_valuations)
    {
      if (e.m_valuations.contains(v))
      {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Determines if this multi-event intersects with another symbolic
   * multi-event.
   * @param e The other multi-event
   * @return <tt>true</tt> if they intersect, <tt>false</tt> otherwise
   */
  protected boolean intersectsWith(SymbolicMultiEvent e)
  {
    PropositionalFormula f = e.getFormula();
    for (Valuation v : m_valuations)
    {
      if (f.evaluate(v) == Troolean.TRUE)
      {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public String toString()
  {
    return m_valuations.toString();
  }
  
  /**
   * Prints a multi-event as a list of valuations
   * @param variables The order in which the variables must be enumerated
   * @return The string corresponding to the multi-event
   */
  @Override
  public String toString(String ... variables)
  {
    StringBuilder out = new StringBuilder();
    boolean first = true;
    for (Valuation v : m_valuations)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        out.append(",");
      }
      out.append(v.toString(variables));
    }
    return out.toString();
  }
}