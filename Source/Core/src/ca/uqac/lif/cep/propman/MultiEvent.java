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
   * Determines the number of common valuations between two multi-events
   * @param e The other multi-event
   * @return <tt>int != 0</tt> if they have common valuations,
   * <tt>0</tt> otherwise
   */
  public Valuation[] intersects(MultiEvent e);
  
  /**
   * Gets the domain of this multi-event, i.e. the set of all propositional
   * variables in the valuations it contains
   * @return The set of variable names
   */
  public Set<String> getDomain();
  
  /**
   * Prints the contents of the event by fixing the ordering of variables
   * @param variables The order in which variables are enumerated
   * @return The string rendition of the multi-event
   */
  public String toString(String ... variables);
}
