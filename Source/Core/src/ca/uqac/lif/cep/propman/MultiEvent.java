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
