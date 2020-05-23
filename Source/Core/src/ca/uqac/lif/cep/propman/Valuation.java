/*
    A BeepBeep palette for propositional machines
    Copyright (C) 2020 Rania Taleb, Sylvain Hallé and friends

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

import ca.uqac.lif.cep.ltl.Troolean;
import java.util.HashMap;
import java.util.Map; 

/**
 * A map between variable names and ternary Boolean values (i.e. Trooleans).
 */
public class Valuation extends HashMap<String,Troolean>
{
  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;
  
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof Valuation))
    {
      return false;
    }
    Valuation v = (Valuation) o;
    if (v.size() != size())
    {
      return false;
    }
    for (Map.Entry<String,Troolean> e : entrySet())
    {
      String k = e.getKey();
      if (!v.containsKey(k) || e.getValue() != v.get(k))
      {
        return false;
      }
    }
    return true;
  }
}
