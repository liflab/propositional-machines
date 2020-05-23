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
import java.util.HashMap;
import java.util.Queue;

/**
 * A multi-monitor lifted from a uni-monitor expressed as a propositional
 * machine.
 * @author Sylvain Hallé, Rania Taleb
 */
public class MultiMonitor extends SynchronousProcessor
{
  /**
   * The uni-monitor from which the multi-monitor is lifted
   */
  /*@ non_null @*/ protected PropositionalMachine m_monitor;
  
  /**
   * An association between uni-monitor states and a number of paths 
   */
  protected PathCount m_sigma;

  /**
   * Creates a new multi-monitor lifted from a uni-monitor 
   * @param monitor The uni-monitor
   */
  public MultiMonitor(/*@ non_null @*/ PropositionalMachine monitor)
  {
    super(1, 1);
    m_monitor = monitor;
    // Initializes sigma
    m_sigma = new PathCount();
    m_sigma.put(m_monitor.getInitialState(), 1);
  }
  
  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    VerdictCount beta = new VerdictCount();
    PathCount sigma_prime = new PathCount();
    // TODO Rania: lines 7-15 of Algorithm 1 go here
    return true;
  }
  
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
   * A data structure associating machine states to a number of uni-traces.
   * This corresponds to the mapping &sigma; in Algorithm 1.
   */
  protected static class PathCount extends HashMap<Integer,Integer>
  {
    /**
     * Dummy UID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Increments the number of paths associated to a state
     * @param state The state
     * @param paths The number of paths to add
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
   * A data structure associating monitor verdicts to a number of uni-traces.
   * This corresponds to the mapping &beta; in Algorithm 1.
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
     * Increments the number of paths assocaited to a given monitor
     * verdict
     * @param v The verdict
     * @param paths The number of paths to add
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
     * @param v The verdict
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
  }
}
