package ca.uqac.lif.cep.propman;

import java.util.Set;

/**
 * An event that represents multiple valuations at the same time
 * @author Sylvain Hallé, Rania Taleb
 */
public interface MultiEvent
{
  /**
   * Gets the set of all valuations represented by this event
   * @return The set of valuations
   */
  public Set<Valuation> getValuations();
  
  /**
   * Determines if a multi-event has at least one common valuation with another
   * multi-event
   * @param e The other multi-event
   * @return <tt>true</tt> if they have a valuation in common,
   * <tt>false</tt> otherwise
   */
  public boolean intersects(MultiEvent e);
  
  /**
   * Gets the domain of this multi-event, i.e. the set of all propositional
   * variables in the valuations it contains
   * @return The set of variable names
   */
  public Set<String> getDomain();
}
