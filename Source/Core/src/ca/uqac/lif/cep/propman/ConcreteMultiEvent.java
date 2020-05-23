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
}