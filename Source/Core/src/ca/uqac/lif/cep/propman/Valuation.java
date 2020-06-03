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

import ca.uqac.lif.cep.ltl.Troolean;
import java.util.HashMap;
import java.util.Map; 

/**
 * A map between variable names and ternary Boolean values (i.e. Trooleans).
 */
public class Valuation extends HashMap<String,Troolean.Value>
{
  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;
  
  @Override
  public int hashCode()
  {
    int h = 0;
    for (Map.Entry<String,Troolean.Value> e : entrySet())
    {
      if (e.getValue() == Troolean.Value.TRUE)
      {
        h++;
      }
      if (e.getValue() == Troolean.Value.INCONCLUSIVE)
      {
        h += 2;
      }
    }
    return h;
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
    for (Map.Entry<String,Troolean.Value> e : entrySet())
    {
      String k = e.getKey();
      if (!v.containsKey(k) || e.getValue() != v.get(k))
      {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Prints a valuation as a list of Boolean values
   * @param variables The order in which the variables must be enumerated
   * @return The string corresponding to the valuation
   */
  public String toString(String ... variables)
  {
    StringBuilder out = new StringBuilder();
    for (String v : variables)
    {
      Troolean.Value b = get(v);
      if (b == Troolean.Value.TRUE)
      {
        out.append("T");
      }
      else if (b == Troolean.Value.FALSE)
      {
        out.append("F");
      }
      else
      {
        out.append("?");
      }
    }
    return out.toString();
  }
  
  /**
   * Reads a valuation from a string
   * @param v The string to read from 
   * @param variables The names of the variables, in the order their value
   * appears in <tt>v</tt>
   * @return A valuation, or <tt>null</tt> if the string is malformed
   */
  public static Valuation readFromString(String v, String ... variables)
  {
    if (v.length() != variables.length)
    {
      // Incorrect length
      return null;
    }
    Valuation val = new Valuation();
    for (int i = 0; i < variables.length; i++)
    {
      String c = v.substring(i, i+1);
      val.put(variables[i], getBooleanValue(c));
    }
    return val;
  }
  
  /**
   * Extracts a Boolean value from a character
   * @param c The character
   * @return <tt>true</tt> if the character is interpreted as true,
   * <tt>false</tt> otherwise
   */
  protected static Troolean.Value getBooleanValue(String c)
  {
    if (c.compareTo("1") == 0 || c.compareToIgnoreCase("T") == 0 || c.compareTo("\u22a4") == 0)
    {
      return Troolean.Value.TRUE;
    }
    if (c.compareTo("?") == 0)
    {
      return Troolean.Value.INCONCLUSIVE;
    }
    return Troolean.Value.FALSE;
  }
}
