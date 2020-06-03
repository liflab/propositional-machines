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

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.propman.MultiMonitor.VerdictCount;
import ca.uqac.lif.cep.tmf.SinkLast;

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;
import static ca.uqac.lif.cep.propman.MultiMonitor.NU;

import static org.junit.Assert.*;

import static ca.uqac.lif.cep.propman.MultiMonitorTest.assertBeta;
import static ca.uqac.lif.cep.propman.PropositionalMachine.Transition;
import static ca.uqac.lif.cep.propman.PropositionalMachine.TransitionOtherwise;

public class AccessControlledMonitorTest
{
  
  @Test
  public void testPaper1()
  {
    String[] variables = new String[] {"a", "b", "c"};
    MultiEventFactory factory = new MultiEventFactory(variables);
    PropositionalMachine machine = PropositionalMachineTest.getMachine1();
    PropositionalMachine uni_monitor = new PropositionalMachine();
    uni_monitor.addTransition(1, new Transition(2, factory.readFromValuations("TFF"), EPSILON));
    uni_monitor.addTransition(1, new Transition(3, factory.readFromValuations("FTF"), EPSILON));
    uni_monitor.addTransition(1, new TransitionOtherwise(1, EPSILON));
    uni_monitor.addTransition(2, new Transition(4, factory.readFromValuations("TFF"), EPSILON));
    uni_monitor.addTransition(2, new TransitionOtherwise(2, EPSILON));
    uni_monitor.addTransition(3, new Transition(6, factory.readFromValuations("FTF"), NU));
    uni_monitor.addTransition(3, new TransitionOtherwise(1, EPSILON));
    uni_monitor.addTransition(4, new TransitionOtherwise(5, EMPTY));
    uni_monitor.addTransition(5, new TransitionOtherwise(5, EMPTY));
    uni_monitor.addTransition(6, new TransitionOtherwise(6, NU));
    AccessControlledMonitor acm = new AccessControlledMonitor(machine, uni_monitor);
    SinkLast sink = new SinkLast();
    Connector.connect(acm, sink);
    Pushable p = acm.getPushableInput();
    p.push(factory.readFromValuations("FFT"));
    VerdictCount beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 0, 0, 1);
    p.push(factory.readFromValuations("TFF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 0, 0, 2);
    p.push(factory.readFromValuations("FTF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 1, 0, 3);
    p.push(factory.readFromValuations("FFT"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 1, 1, 2);
    p.push(factory.readFromValuations("TFF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 2, 2, 4);
  }
}
