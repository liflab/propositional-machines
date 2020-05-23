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

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * A function that turns a multi-event into another multi-event. This function
 * may return <tt>null</tt> to represent that a machine emits no output.
 * @author Sylvain Hallé, Rania Taleb
 */
public abstract class MultiEventFunction extends UnaryFunction<MultiEvent,MultiEvent>
{
  public MultiEventFunction()
  {
    super(MultiEvent.class, MultiEvent.class);
  }
}