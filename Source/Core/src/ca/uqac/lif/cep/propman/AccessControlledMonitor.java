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

import static ca.uqac.lif.cep.Connector.INPUT;
import static ca.uqac.lif.cep.Connector.OUTPUT;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;

/**
 * A monitor made of an access proxy and a uni-monitor.
 * @author Sylvain Hallé, Rania Taleb
 */
public class AccessControlledMonitor extends GroupProcessor
{
  /**
   * Creates a new acces-controlled monitor.
   * @param <T> The type given to state labels in the access proxy
   * @param <U> The type given to state labels in the uni-monitor
   * @param proxy The access proxy
   * @param monitor The uni-monitor
   */
  public AccessControlledMonitor(PropositionalMachine proxy, PropositionalMachine monitor)
  {
    super(1, 1);
    MultiMonitor mm = new MultiMonitor(monitor);
    Connector.connect(proxy, mm);
    addProcessors(proxy, mm);
    associateInput(INPUT, proxy, INPUT);
    associateOutput(OUTPUT, mm, OUTPUT);
  }
}
