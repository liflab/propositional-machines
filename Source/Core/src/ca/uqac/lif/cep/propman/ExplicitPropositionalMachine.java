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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * A {@link PropositionalMachine} whose transition relation is explicitly
 * stored in a map.
 * @author Sylvain Hallé, Rania Taleb
 */
public class ExplicitPropositionalMachine extends PropositionalMachine
{
  /**
   * An index to the current state
   */
  protected Object m_state = null;

  /**
   * The index of the initial state of the machine
   */
  protected Object m_initialState = null;

  /**
   * The transition relation of the machine
   */
  protected Map<Object,List<Transition>> m_delta;

  /**
   * Creates a new empty propositional machine
   */
  public ExplicitPropositionalMachine()
  {
    super();
    m_delta = new HashMap<Object,List<Transition>>();
  }

  @Override
  public Object getInitialState()
  {
    return m_initialState;
  }

  @Override
  public long getStateCount()
  {
    return m_delta.size();
  }

  @Override
  public long getTransitionCount()
  {
    int size = 0;
    for (Map.Entry<Object,List<Transition>> e : m_delta.entrySet())
    {
      size += e.getValue().size();
    }
    return size;
  }

  /**
   * Sets the initial state of the machine
   * @param state The ID of the initial state
   * @return This machine
   */
  public ExplicitPropositionalMachine setInitialState(int state)
  {
    m_initialState = state;
    return this;
  }

  /**
   * Adds a transition to the machine
   * @param source The source state of the transition
   * @param t The transition
   * @return This propositional machine
   */
  public ExplicitPropositionalMachine addTransition(int source, Transition t)
  {
    if (m_initialState == null)
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
    m_delta.put(source, list);
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
    List<Transition> transitions = new ArrayList<Transition>();
    transitions = m_delta.get(m_state);
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
        if (input_event.getIntersection(condition).size() > 0)
        {
          // This happens at most once if the machine is deterministic
          candidate = t;
        }
      }
    } // end of for: end up with one candidate
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
    if (output_event != null)
    {
      outputs.add(new Object[] {output_event});
    }
    m_state = to_take.getDestination();
    return true;
  }

  @Override
  /*@ non_null @*/ public List<Transition> getTransitionsFor(Object state)
  {
    if (!m_delta.containsKey(state))
    {
      return new ArrayList<Transition>(0);
    }
    return m_delta.get(state);
  }

  @Override
  public ExplicitPropositionalMachine duplicate(boolean with_state)
  {
    // TODO Let's do this later
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Cleans up the transition relation by removing any states that
   * are not reachable from the initial state
   */
  protected void removeUnreachableStates()
  {
    Set<Object> seen = new HashSet<Object>();
    Set<Object> to_visit = new HashSet<Object>();
    to_visit.add(m_initialState);
    while (!to_visit.isEmpty())
    {
      Object current = null;
      for (Object s : to_visit)
      {
        current = s;
        break;
      }
      to_visit.remove(current);
      if (seen.contains(current))
      {
        continue;
      }
      seen.add(current);
      List<Transition> trans = m_delta.get(current);
      if (trans == null)
      {
        continue;
      }
      for (Transition t : trans)
      {
        Object dest = t.getDestination();
        if (!seen.contains(dest))
        {
          to_visit.add(dest);
        }
      }
    }
    Set<Object> all = new HashSet<Object>(m_delta.size());
    all.addAll(m_delta.keySet());
    for (Object i : all)
    {
      if (!seen.contains(i))
      {
        m_delta.remove(i);
      }
    }
  }
}
