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
 * A multi-event that represents its set of valuations as a propositional
 * logic formula.
 * @author Sylvain Hallé, Rania Taleb
 */
public class SymbolicMultiEvent implements MultiEvent
{
  /**
   * Static reference to the symbolic multi-event that contains all valuations
   */
  public static final transient All ALL = new All();
  
  /**
   * Static reference to the symbolic multi-event that contains no valuation
   */
  public static final transient Nothing NOTHING = new Nothing();
  
  /**
   * The propositional formula in this symbolic multi-event
   */
  protected PropositionalFormula m_formula;

  /**
   * Creates a new symbolic multi-event
   * @param formula The formula contained in this symbolic multi-event
   */
  public SymbolicMultiEvent(PropositionalFormula formula)
  {
    super();
    m_formula = formula;
  }

  /**
   * Gets the formula contained in this symbolic multi-event
   * @return The formula
   */
  public PropositionalFormula getFormula()
  {
    return m_formula;
  }

  @Override
  public Set<String> getDomain()
  {
    return m_formula.getDomain();
  }

  @Override
  public Set<Valuation> getValuations()
  {
    Set<String> domain = getDomain();
    HashSet<Valuation> valuations = new HashSet<Valuation>();
    ValuationIterator it = new ValuationIterator(domain);
    while (it.hasNext())
    {
      Valuation v = it.next();
      if (m_formula.evaluate(v) == Troolean.Value.TRUE)
      {
        valuations.add(v);
      }
    }
    return valuations;
  }

  @Override
  public Set<Valuation> getIntersection(MultiEvent e)
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
   * Determines if this multi-event intersects with another symbolic
   * multi-event.
   * @param e The other multi-event
   * @return <tt>true</tt> if they intersect, <tt>false</tt> otherwise
   */
  protected Set<Valuation> intersectsWith(SymbolicMultiEvent e)
  {
    Set<Valuation> common_valuations = new HashSet<Valuation>();
    Set<String> domain = getDomain();
    domain.addAll(e.getDomain());
    ValuationIterator it = new ValuationIterator(domain);
    while (it.hasNext())
    {
      Valuation v = it.next();
      if (e.m_formula.evaluate(v) == Troolean.Value.TRUE && m_formula.evaluate(v) == Troolean.Value.TRUE)
      {
        common_valuations.add(v);
      }
    }
    return common_valuations;
  }

  /**
   * Determines if this multi-event intersects with another concrete
   * multi-event.
   * @param e The other multi-event
   * @return <tt>true</tt> if they intersect, <tt>false</tt> otherwise
   */
  protected Set<Valuation> intersectsWith(ConcreteMultiEvent e)
  {
    Set<Valuation> vals = e.getValuations();
    Set<Valuation> common_valuations = new HashSet<Valuation>();
    for (Valuation v : vals)
    {
      if (m_formula.evaluate(v) == Troolean.Value.TRUE)
      {
        common_valuations.add(v);
      }
    }
    return common_valuations;
  }
  
  /**
   * Multi-event that contains all valuations
   */
  protected static class All extends SymbolicMultiEvent
  {
    public All()
    {
      super(new PropositionalFormula(Troolean.TRUE));
    }
    
    @Override
    protected Set<Valuation> intersectsWith(SymbolicMultiEvent e)
    {
      return e.getValuations();
    }
    
    @Override
    protected Set<Valuation> intersectsWith(ConcreteMultiEvent e)
    {
      return e.getValuations();
    }
  }

  /**
   * Multi-event that contains no valuation
   */
  protected static class Nothing extends SymbolicMultiEvent
  {
    public Nothing()
    {
      super(new PropositionalFormula(Troolean.FALSE));
    }
  }
  
  @Override
  public String toString()
  {
    return m_formula.toString();
  }

  @Override
  public String toString(String... variables)
  {
    return toString();
  }
}
