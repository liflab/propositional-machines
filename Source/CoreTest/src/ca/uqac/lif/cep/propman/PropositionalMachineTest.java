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

import org.junit.Test;

import ca.uqac.lif.cep.propman.PropositionalMachine.Transition;
import ca.uqac.lif.cep.propman.PropositionalMachine.TransitionOtherwise;
import ca.uqac.lif.cep.tmf.SinkLast;
import java.util.Set;

import static org.junit.Assert.*;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.propman.MultiEventFunction.Identity;

public class PropositionalMachineTest
{
  @Test
  public void test1()
  {
    String[] variables = new String[] {"a", "b", "c"};
    MultiEventFactory factory = new MultiEventFactory(variables);
    MultiEventFunction f = new BlurVariable("a");
    PropositionalMachine machine = new PropositionalMachine();
    machine.addTransition(1, new Transition(2, factory.readFromValuations("TFF"), f));
    machine.addTransition(1, new TransitionOtherwise(1, Identity.instance));
    machine.addTransition(1, new Transition(2, factory.readFromValuations("FTF"), f));
    machine.addTransition(2, new TransitionOtherwise(2, Identity.instance));
    SinkLast sink = new SinkLast();
    Connector.connect(machine, sink);
    Pushable p = machine.getPushableInput();
    ConcreteMultiEvent e = null;
    Set<Valuation> vals = null;
    // Push event FFT
    p.push(factory.readFromValuations("FFT"));
    e = (ConcreteMultiEvent) sink.getLast()[0];
    vals = e.getValuations();
    assertEquals(1, vals.size());
    assertTrue(vals.contains(Valuation.readFromString("FFT", variables)));
    // Push event TFF
    p.push(factory.readFromValuations("TFF"));
    e = (ConcreteMultiEvent) sink.getLast()[0];
    vals = e.getValuations();
    assertEquals(2, vals.size());
    assertTrue(vals.contains(Valuation.readFromString("TFF", variables)));
    assertTrue(vals.contains(Valuation.readFromString("FTF", variables)));
    System.out.println(e);
    
  }
}
