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

/**
 * Propositional machine whose all transitions loop onto its single state.
 * This machine is suitable for modeling stateless modifications to an
 * input event trace.
 */
public class StatelessPropositionalMachine extends PropositionalMachine
{
  /**
   * Creates a new instance of the machine
   */
  public StatelessPropositionalMachine()
  {
    super();
  }
  
  /**
   * Adds a condition
   * @param condition The condition to be applied to the event. Set to
   * <tt>null</tt> to represent the "otherwise" (star) transition.
   * @param f The function used to transform the input event into an
   * output event
   */
  public void addCondition(MultiEvent condition, MultiEventFunction f)
  {
    if (condition == null)
    {
      addTransition(0, new TransitionOtherwise(0, f));
    }
    addTransition(0, new Transition(0, condition, f));
  }
}
