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
  /**
   * Creates a new instance of the function
   */
  public MultiEventFunction()
  {
    super(MultiEvent.class, MultiEvent.class);
  }
  
  /**
   * Function that returns the same multi-event for all its inputs.
   */
  public static class EmitConstant extends MultiEventFunction
  {
    /**
     * The multi-event to return
     */
    protected MultiEvent m_constant;
    
    /**
     * Creates a new constant multi-event function
     * @param e The multi-event to return
     */
    public EmitConstant(MultiEvent e)
    {
      super();
      m_constant = e;
    }

    @Override
    public MultiEvent getValue(MultiEvent x)
    {
      return m_constant;
    }
    
    @Override
    public String toString()
    {
      if (m_constant == null)
      {
        return "\u03b5";
      }
      return m_constant.toString();
    }
  }
  
  /**
   * Function that makes no change to the input multi-event.
   */
  public static class Identity extends MultiEventFunction
  {
    /**
     * Reference to a single publicly visible instance of the function
     */
    public static final transient Identity instance = new Identity();
    
    /**
     * Creates a new instance of the function
     */
    protected Identity()
    {
      super();
    }

    @Override
    public MultiEvent getValue(MultiEvent x)
    {
      return x;
    }
  }
}