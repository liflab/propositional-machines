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

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.ltl.Troolean.Value;
import ca.uqac.lif.cep.propman.MultiMonitor.VerdictCount;
import java.math.BigInteger;

/**
 * Computes the entropy of a multi-monitor verdict.
 * @author Sylvain Hallé, Rania Taleb
 */
public class GetEntropy extends UnaryFunction<VerdictCount,Number>
{
  /**
   * A single visible instance of the function
   */
  public static final transient GetEntropy instance = new GetEntropy();
  
  /**
   * The value of ln 2
   */
  protected static final transient double LN2 = Math.log(2);
  
  /**
   * The precision used in integer divisions
   */
  protected static final float m_intPrecision = 1000;
  
  /**
   * The precision used in integer divisions
   */
  protected static final BigInteger m_precision = BigInteger.valueOf((long) m_intPrecision);

  /**
   * Creates a new instance of the function
   */
  private GetEntropy()
  {
    super(VerdictCount.class, Number.class);
  }
  
  @Override
  public Number getValue(VerdictCount x)
  {
    BigInteger total = x.get(Value.FALSE).add(x.get(Value.TRUE)).add(x.get(Value.INCONCLUSIVE));
    if (total.equals(BigInteger.ZERO))
    {
      return 0;
    }
    float p_false = (float) x.get(Value.FALSE).multiply(m_precision).divide(total).floatValue() / m_intPrecision;
    float p_true = (float) x.get(Value.TRUE).multiply(m_precision).divide(total).floatValue() / m_intPrecision;
    float p_inc = (float) x.get(Value.INCONCLUSIVE).multiply(m_precision).divide(total).floatValue() / m_intPrecision;
    double h = -(p_false * log2(p_false)) - (p_true * log2(p_true)) -( p_inc * log2(p_inc));
    return h;
  }
  
  /**
   * Computes the base-2 logarithm of a value.
   * @param x The value
   * @return The base-2 logarithm
   */
  protected static double log2(double x)
  {
    if (x == 0)
    {
      return 0;
    }
    return Math.log(x) / LN2;
  }
}
