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
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.PropositionalMachine.TransitionOtherwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

/**
 * A multi-monitor lifted from a uni-monitor expressed as a propositional
 * machine.
 * 
 * @author Sylvain Hallé, Rania Taleb
 */
public class MultiMonitor extends SynchronousProcessor
{
  /**
   * The uni-monitor from which the multi-monitor is lifted
   */
  /* @ non_null @ */ protected PropositionalMachine m_monitor;

  /**
   * An association between uni-monitor states and a number of paths
   */
  protected PathCount m_sigma;

  /**
   * An association between uni-monitor verdicts and a number of paths
   */
  protected VerdictCount m_verdicts;

  /**
   * Creates a new multi-monitor lifted from a uni-monitor
   * 
   * @param monitor
   *          The uni-monitor
   */
  public MultiMonitor(/* @ non_null @ */ PropositionalMachine monitor)
  {
    super(1, 1);
    m_monitor = monitor;
    // Initializes sigma
    m_sigma = new PathCount();
    m_sigma.put(m_monitor.getInitialState(), 1);
    m_verdicts = new VerdictCount();
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)// input: 1 multiEvent ---
                                                                     // output: verdicts and sigma
  {
    VerdictCount beta = new VerdictCount();
    PathCount sigma_prime = new PathCount();
    MultiEvent input_event = (MultiEvent) inputs[0];
    Set<Valuation> to_evaluate = new HashSet<Valuation>(); // will store all the valuations of the multi-event input
    to_evaluate.addAll(input_event.getValuations());
    boolean transition_taken = false;
    for (Entry<Integer, Integer> sigma_state : m_sigma.entrySet())
    { // iterating on each state of m_sigma
      if (sigma_state.getValue() == 0)
      {
        continue;
      }
      List<PropositionalMachine.Transition> outgoing_edges = new ArrayList<PropositionalMachine.Transition>();
      outgoing_edges = m_monitor.getTransitionsFor(sigma_state.getKey());
      PropositionalMachine.Transition otherwise = null;

      // iterate through the outgoing transitions of a state in m_sigma
      for (PropositionalMachine.Transition t : outgoing_edges)
      {
        int paths = 0;
        if (t instanceof TransitionOtherwise)
        {
          otherwise = t;
        }
        else // if it is not an otherwise transition, we test if we can check it or not
        {
          MultiEvent condition = t.getCondition();
          Set<Valuation> common_valuations = input_event.getIntersection(condition);
          if (common_valuations.size() > 0)// valuations that take this transition will not take any
                                           // other transition because the monitor is deterministic
          {
            transition_taken = true;
            // add the new state to sigma_prime
            if (sigma_prime.containsKey(t.getDestination()))
            {
              paths = sigma_prime.get(t.getDestination())
                  + sigma_state.getValue() * common_valuations.size();
              sigma_prime.put(t.getDestination(), paths);
            }
            else
            {
              paths = sigma_state.getValue() * common_valuations.size();
              sigma_prime.put(t.getDestination(), paths);
            }

            // get the value of the output (verdict) on this transition + update beta
            MultiEventFunction f = t.getFunction();
            MultiEvent output_event = f.getValue(input_event);

            // beta increment ..
            if (output_event instanceof SymbolicMultiEvent.All)
            {
              beta.increment(Troolean.Value.TRUE, paths);
            }

            if (output_event instanceof SymbolicMultiEvent.Nothing)
            {
              beta.increment(Troolean.Value.FALSE, paths);
            }
            else
            {
              beta.increment(Troolean.Value.INCONCLUSIVE, paths);
            }

            // remove the common valuations that are evaluated, the remaining non-evaluated
            // valuations will either go through another outgoing transition or take the
            // otherwise transition
            for (Valuation v : common_valuations)
            {
              to_evaluate.remove(v);
            }
          }
        }
      } // end of outgoing transitions from one sigma state

      // the remaining valuations in the set to_evaluate that did not take any
      // transition will take the otherwise transition
      if (otherwise != null && !to_evaluate.isEmpty())
      {
        transition_taken = true;
        int paths = 0;
        if (sigma_prime.containsKey(otherwise.getDestination()))
        {
          paths = sigma_prime.get(otherwise.getDestination())
              + sigma_state.getValue() * to_evaluate.size();
          sigma_prime.put(otherwise.getDestination(), paths);
        }
        else
        {
          paths = sigma_state.getValue() * to_evaluate.size();
          sigma_prime.put(otherwise.getDestination(), paths);
        }

        // get the value of the output (verdict) + update beta
        MultiEventFunction f = otherwise.getFunction();
        MultiEvent output_event = f.getValue(input_event);

        // beta increment ..
        if (output_event instanceof SymbolicMultiEvent.All)
        {
          beta.increment(Troolean.Value.TRUE, paths);
        }
        else if (output_event instanceof SymbolicMultiEvent.Nothing)
        {
          beta.increment(Troolean.Value.FALSE, paths);
        }
        else
        {
          beta.increment(Troolean.Value.INCONCLUSIVE, paths);
        }
      }
    } // end of states in m_sigma

    // update sigma and beta of the monitor
    m_sigma = sigma_prime;
    m_verdicts = beta;

    // the function will return false if none of the input valuations takes any
    // transition from any state
    if (!transition_taken)
    { // i.e. if nothing is removed from to_evaluate set
      return false;
    }
    outputs.add(new Object[] {beta});
    return true;
  } // end of compute

  @Override
  public void reset()
  {
    super.reset();
    m_sigma = new PathCount();
    m_sigma.put(m_monitor.getInitialState(), 1);
  }

  @Override
  public MultiMonitor duplicate(boolean with_state)
  {
    // TODO Let's do this later
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * A data structure associating machine states to a number of uni-traces. This
   * corresponds to the mapping &sigma; in Algorithm 1.
   */
  protected static class PathCount extends HashMap<Integer, Integer>
  {
    /**
     * Dummy UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Increments the number of paths associated to a state
     * 
     * @param state
     *          The state
     * @param paths
     *          The number of paths to add
     */
    public void increment(int state, int paths)
    {
      if (!containsKey(state))
      {
        put(state, paths);
      }
      else
      {
        int c = get(state);
        put(state, c + paths);
      }
    }
  }

  /**
   * A data structure associating monitor verdicts to a number of uni-traces. This
   * corresponds to the mapping &beta; in Algorithm 1.
   */
  protected static class VerdictCount
  {
    /**
     * The number of uni-traces leading to the true verdict
     */
    protected int m_numTrue = 0;

    /**
     * The number of uni-traces leading to the false verdict
     */
    protected int m_numFalse = 0;

    /**
     * The number of uni-traces leading to the inconclusive verdict
     */
    protected int m_numInconclusive = 0;

    /**
     * Increments the number of paths associated to a given monitor verdict
     * 
     * @param v
     *          The verdict
     * @param paths
     *          The number of paths to add
     */
    public void increment(Troolean.Value v, int paths)
    {
      if (v == Troolean.Value.TRUE)
      {
        m_numTrue += paths;
      }
      if (v == Troolean.Value.FALSE)
      {
        m_numFalse += paths;
      }
      if (v == Troolean.Value.INCONCLUSIVE)
      {
        m_numInconclusive += paths;
      }
    }

    /**
     * Gets the number of uni-traces associated to a monitor verdict
     * 
     * @param v
     *          The verdict
     * @return The number of traces
     */
    public int get(Troolean.Value v)
    {
      if (v == Troolean.Value.TRUE)
      {
        return m_numTrue;
      }
      if (v == Troolean.Value.FALSE)
      {
        return m_numFalse;
      }
      if (v == Troolean.Value.INCONCLUSIVE)
      {
        return m_numInconclusive;
      }
      return 0; // Not supposed to happen
    }
    
    @Override
    public String toString()
    {
      StringBuilder out = new StringBuilder();
      out.append("{T=").append(m_numTrue).append(",F=").append(m_numFalse).append(",?=").append(m_numInconclusive).append("}");
      return out.toString();
    }
  }
}
