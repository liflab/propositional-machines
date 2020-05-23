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
    Set<Valuation> valuations = new HashSet<Valuation>();
    ValuationIterator it = new ValuationIterator(domain);
    while (it.hasNext())
    {
      Valuation v = it.next();
      if (m_formula.evaluate(v) == Troolean.TRUE)
      {
        valuations.add(v);
      }
    }
    return valuations;
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
   * Determines if this multi-event intersects with another symbolic
   * multi-event.
   * @param e The other multi-event
   * @return <tt>true</tt> if they intersect, <tt>false</tt> otherwise
   */
  protected boolean intersectsWith(SymbolicMultiEvent e)
  {
    Set<String> domain = getDomain();
    domain.addAll(e.getDomain());
    ValuationIterator it = new ValuationIterator(domain);
    while (it.hasNext())
    {
      Valuation v = it.next();
      if (e.m_formula.evaluate(v) == Troolean.TRUE && m_formula.evaluate(v) == Troolean.TRUE)
      {
        return true;
      }
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
    Set<Valuation> vals = e.getValuations();
    for (Valuation v : vals)
    {
      if (m_formula.evaluate(v) == Troolean.TRUE)
      {
        return true;
      }
    }
    return false;
  }
}
