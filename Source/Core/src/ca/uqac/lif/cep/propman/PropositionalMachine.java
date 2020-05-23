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

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;
import java.util.Queue;

public class PropositionalMachine extends SynchronousProcessor
{
  /**
   * An index to the current state
   */
  protected int m_state;
  
  
  
  /**
   * Creates a new empty propositional machine
   */
  public PropositionalMachine()
  {
    super(1, 1);
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Let's do this later
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
