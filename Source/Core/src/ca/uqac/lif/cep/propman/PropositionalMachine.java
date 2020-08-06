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

import ca.uqac.lif.cep.SynchronousProcessor;
import java.util.List;

/**
 * A finite-state machine that receives and produces multi-events.
 * @author Sylvain Hallé, Rania Taleb
 */
public abstract class PropositionalMachine extends SynchronousProcessor
{
  /**
   * Creates a new propositional machine
   */
  public PropositionalMachine()
  {
    super(1, 1);
  }
  
  /**
   * Gets the number associated to the initial state of the machine
   * @return The number of the initial state
   */
  public abstract Object getInitialState();
  
  /**
   * Gets all the outgoing transitions from a given state
   * @param state The state
   * @return A list of transitions; may be empty, but never null
   */
  /*@ non_null @*/ public abstract List<Transition> getTransitionsFor(Object state);
  
  /**
   * Gets the number of states in the machine
   * @return The number of states
   */
  public abstract long getStateCount();
  
  /**
   * Gets the number of transitions in the machine
   * @return The number of transitions
   */
  public abstract long getTransitionCount();
  
  /**
   * A transition in the propositional machine
   */
  public static class Transition
  {
    /**
     * The function that transforms the input event into the output event
     */
    protected MultiEventFunction m_function;

    /**
     * The multi-event condition on the transition
     */
    protected MultiEvent m_condition;

    /**
     * The destination state of the transition
     */
    protected Object m_destination;

    /**
     * Creates a new transition
     * @param destination The destination state of the transition
     * @param phi The multi-event condition on the transition
     * @param f The function that transforms the input event into the output event
     */
    public Transition(Object destination, MultiEvent condition, MultiEventFunction f)
    {
      super();
      m_destination = destination;
      m_condition = condition;
      m_function = f;
    }

    /**
     * Gets the multi-event condition on the transition
     * @return The condition
     */
    public MultiEvent getCondition()
    {
      return m_condition;
    }

    /**
     * Gets the function that transforms the input event into the output event
     * @return The function
     */
    public MultiEventFunction getFunction()
    {
      return m_function;
    }

    /**
     * Gets the destination state of the transition
     * @return The destination
     */
    public Object getDestination()
    {
      return m_destination;
    }
  }

  /**
   * Special transition taken only when no other transition fires from a given
   * source state
   */
  public static class TransitionOtherwise extends Transition
  {
    public TransitionOtherwise(Object destination, MultiEventFunction f)
    {
      super(destination, null, f);
    }

    @Override
    public String toString()
    {
      return "*";
    }
  }
}
