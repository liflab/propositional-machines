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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * A finite-state machine that receives and produces multi-events.
 * @author Sylvain Hallé, Rania Taleb
 */
public class PropositionalMachine extends SynchronousProcessor
{
  /**
   * An index to the current state
   */
  protected int m_state = -1;
  
  /**
   * The index of the initial state of the machine
   */
  protected int m_initialState = -1;
  
  /**
   * The transition relation of the machine
   */
  protected Map<Integer,List<Transition>> m_delta;
  
  /**
   * Creates a new empty propositional machine
   */
  public PropositionalMachine()
  {
    super(1, 1);
    m_delta = new HashMap<Integer,List<Transition>>();
  }
  
  /**
   * Adds a transition to the machine
   * @param source The source state of the transition
   * @param t The transition
   * @return This propositional machine
   */
  public PropositionalMachine addTransition(int source, Transition t)
  {
    if (m_initialState == -1)
    {
      m_initialState = source;
      m_state = source;
    }
    List<Transition> list = new ArrayList<Transition>();
    if (m_delta.containsKey(source))
    {
      list = m_delta.get(source);
    }
    list.add(t);
    return this;
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    MultiEvent input_event = (MultiEvent) inputs[0];
    if (!m_delta.containsKey(m_state))
    {
      // No transition from this state: output nothing
      return false;
    }
    List<Transition> transitions = m_delta.get(m_state);
    Transition candidate = null, otherwise = null, to_take = null;
    for (Transition t : transitions)
    {
      if (t instanceof TransitionOtherwise)
      {
        otherwise = t;
      }
      else
      {
        MultiEvent condition = t.getCondition();
        if (input_event.intersects(condition))
        {
          // This happens at most once if the machine is deterministic
          candidate = t;
        }
      }
    }
    if (candidate != null)
    {
      // Take this transition
      to_take = candidate;
    }
    else if (otherwise != null)
    {
      to_take = otherwise;
    }
    if (to_take == null)
    {
      // Nowhere to go
      return false;
    }
    // Produce output event and update state
    MultiEventFunction f = to_take.getFunction();
    MultiEvent output_event = f.getValue(input_event);
    outputs.add(new Object[] {output_event});
    m_state = to_take.getDestination();
    return true;
  }

  @Override
  public PropositionalMachine duplicate(boolean with_state)
  {
    // TODO Let's do this later
    throw new UnsupportedOperationException("Not implemented yet");
  }
  
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
    protected int m_destination;
    
    /**
     * Creates a new transition
     * @param destination The destination state of the transition
     * @param phi The multi-event condition on the transition
     * @param f The function that transforms the input event into the output event
     */
    public Transition(int destination, MultiEvent condition, MultiEventFunction f)
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
    public int getDestination()
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
    public TransitionOtherwise(int destination, MultiEventFunction f)
    {
      super(destination, null, f);
    }
  }
}
