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
  public Valuation[] intersects(MultiEvent e)
  {
    if (e instanceof ConcreteMultiEvent)
    {
      return intersectsWith((ConcreteMultiEvent) e);
    }
    if (e instanceof SymbolicMultiEvent)
    {
      return intersectsWith((SymbolicMultiEvent) e);
    }
    return null;
  }
  
  /**
   * Determines if this multi-event intersects with another concrete
   * multi-event.
   * @param e The other multi-event
   * @return <tt>int</tt> if they intersect, <tt>0</tt> otherwise
   */
  protected Valuation[] intersectsWith(ConcreteMultiEvent e)
  {
	  //int count =0;
	Valuation[] common_valuations = new Valuation[this.getValuations().size()];
	int i =0;

    for (Valuation v : m_valuations)
    {
      if (e.m_valuations.contains(v))
      {
        common_valuations[i] =v;
        i++;
      }
    }
    return common_valuations;
  }
  
  /**
   * Determines if this multi-event intersects with another symbolic
   * multi-event.
   * @param e The other multi-event
   * @return <tt>int</tt> if they intersect, <tt>0</tt> otherwise
   */
  protected Valuation[] intersectsWith(SymbolicMultiEvent e)
  {
	//int count = 0;
	Valuation[] common_valuations = new Valuation[this.getValuations().size()];
	int i= 0;
    PropositionalFormula f = e.getFormula();
    for (Valuation v : m_valuations)
    {
      if (f.evaluate(v) == Troolean.Value.TRUE)
      {
        common_valuations[i] =v;
        i++;
      }
    }
    return common_valuations;
  }
}