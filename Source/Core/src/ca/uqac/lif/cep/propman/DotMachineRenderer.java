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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ca.uqac.lif.cep.propman.PropositionalMachine.Transition;

/**
 * Renders a propositional machine as a Graphviz file.
 * @author Sylvain Hallé, Rania Taleb
 */
public class DotMachineRenderer
{
  protected Map<Object,String> m_nicknames;
  
  public DotMachineRenderer()
  {
    super();
    m_nicknames = new HashMap<Object,String>();
  }
  
  public DotMachineRenderer setNickname(Object o, String name)
  {
    m_nicknames.put(o, name);
    return this;
  }
  
  public void render(PrintStream ps, PropositionalMachine machine)
  {
    render(ps, machine, m_nicknames);
  }
  
  public static void render(PrintStream ps, PropositionalMachine machine, Map<Object,String> nicknames)
  {
    ps.println("digraph G {");
    ps.println("node [shape=\"circle\",fillstyle=\"solid\",fillcolor=\"#ffffff\"];");
    for (Entry<Integer,List<Transition>> entry : machine.m_delta.entrySet())
    {
      int from = entry.getKey();
      for (Transition t : entry.getValue())
      {
        ps.print(from + " -> " + t.getDestination());
        ps.print(" [label=\"");
        MultiEvent cond = t.getCondition();
        if (cond == null)
        {
          ps.print("*");
        }
        else
        {
          if (nicknames != null && nicknames.containsKey(cond))
          {
            ps.print(nicknames.get(cond));
          }
          else
          {
            ps.print(cond);
          }
        }
        ps.print(" / ");
        ps.println(t.getFunction() + "\"];");
      }
    }
    ps.println("}");
  }
}
