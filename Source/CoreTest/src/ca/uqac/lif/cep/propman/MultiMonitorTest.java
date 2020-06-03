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

import static org.junit.Assert.*;
import org.junit.Test;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.ltl.Troolean.Value;
import ca.uqac.lif.cep.propman.MultiMonitor.VerdictCount;
import ca.uqac.lif.cep.propman.PropositionalMachine.Transition;
import ca.uqac.lif.cep.propman.PropositionalMachine.TransitionOtherwise;
import ca.uqac.lif.cep.tmf.SinkLast;

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;
import static ca.uqac.lif.cep.propman.MultiMonitor.NU;

public class MultiMonitorTest
{
  
  @Test
  public void test1()
  {
    MultiEventFactory factory = new MultiEventFactory("a", "b");
    PropositionalMachine uni_monitor = new PropositionalMachine();
    uni_monitor.addTransition(0, new Transition(1, factory.readFromValuations("TF"), EPSILON));
    uni_monitor.addTransition(0, new Transition(2, factory.readFromValuations("FT"), EPSILON));
    uni_monitor.addTransition(0, new TransitionOtherwise(0, EPSILON));
    uni_monitor.addTransition(1, new TransitionOtherwise(1, NU));
    uni_monitor.addTransition(2, new TransitionOtherwise(2, EMPTY));
    MultiMonitor mul_monitor = new MultiMonitor(uni_monitor);
    SinkLast sink = new SinkLast();
    Connector.connect(mul_monitor, sink);
    Pushable p = mul_monitor.getPushableInput();
    VerdictCount beta = null;
    p.push(factory.readFromValuations("TF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 0, 0, 1);
    p.push(factory.readFromValuations("TF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 1, 0, 0);
  }
  
  @Test
  public void test2()
  {
    MultiEventFactory factory = new MultiEventFactory("a", "b");
    PropositionalMachine uni_monitor = new PropositionalMachine();
    uni_monitor.addTransition(0, new Transition(1, factory.readFromValuations("TF"), EPSILON));
    uni_monitor.addTransition(0, new Transition(2, factory.readFromValuations("FT"), EPSILON));
    uni_monitor.addTransition(0, new TransitionOtherwise(0, EPSILON));
    uni_monitor.addTransition(1, new TransitionOtherwise(1, NU));
    uni_monitor.addTransition(2, new TransitionOtherwise(2, EMPTY));
    MultiMonitor mul_monitor = new MultiMonitor(uni_monitor);
    SinkLast sink = new SinkLast();
    Connector.connect(mul_monitor, sink);
    Pushable p = mul_monitor.getPushableInput();
    VerdictCount beta = null;
    p.push(factory.readFromValuations("TF,FT"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 0, 0, 2);
    p.push(factory.readFromValuations("TF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 1, 1, 0);
  }
  
  @Test
  public void test3()
  {
    MultiEventFactory factory = new MultiEventFactory("a", "b");
    PropositionalMachine uni_monitor = new PropositionalMachine();
    uni_monitor.addTransition(0, new Transition(1, factory.readFromValuations("TF"), EPSILON));
    uni_monitor.addTransition(0, new Transition(2, factory.readFromValuations("FT"), EPSILON));
    uni_monitor.addTransition(0, new TransitionOtherwise(0, EPSILON));
    uni_monitor.addTransition(1, new TransitionOtherwise(1, NU));
    uni_monitor.addTransition(2, new TransitionOtherwise(2, EMPTY));
    MultiMonitor mul_monitor = new MultiMonitor(uni_monitor);
    SinkLast sink = new SinkLast();
    Connector.connect(mul_monitor, sink);
    Pushable p = mul_monitor.getPushableInput();
    VerdictCount beta = null;
    p.push(factory.readFromValuations("TF,FF"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 0, 0, 2);
    p.push(factory.readFromValuations("TF,FT"));
    beta = (VerdictCount) sink.getLast()[0];
    assertBeta(beta, 2, 0, 2);
  }
  
  /**
   * Asserts the values expected in the &beta; mapping
   * @param beta The mapping
   * @param num_true The value expected to be associated to verdict true
   * @param num_false The value expected to be associated to verdict false
   * @param num_inc The value expected to be associated to verdict inconclusive
   */
  public static void assertBeta(VerdictCount beta, int num_true, int num_false, int num_inc)
  {
    assertEquals("For true", num_true, beta.get(Value.TRUE));
    assertEquals("For inconclusive", num_inc, beta.get(Value.INCONCLUSIVE));
    assertEquals("For false", num_false, beta.get(Value.FALSE));
  }
}
