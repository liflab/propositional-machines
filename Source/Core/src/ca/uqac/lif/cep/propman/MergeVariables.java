package ca.uqac.lif.cep.propman;

import java.util.HashSet;
import java.util.Set;

public class MergeVariables extends MultiEventFunction
{
  /**
   * The name of the first variable to merge
   */
  protected String m_variable1;
  
  /**
   * The name of the second variable to merge
   */
  protected String m_variable2;
  
  /**
   * Creates a new instance of the function
   * @param variable1 The name of the first variable to merge
   * @param variable2 The name of the second variable to merge
   */
  public MergeVariables(String variable1, String variable2)
  {
    super();
    m_variable1 = variable1;
    m_variable2 = variable2;
  }

  @Override
  public MultiEvent getValue(MultiEvent x)
  {
    Set<Valuation> vals = x.getValuations();
    Set<Valuation> new_vals = new HashSet<Valuation>();
    new_vals.addAll(vals);
    for (Valuation v : vals)
    {
     Valuation v2 = new Valuation();
     v2.putAll(v);
     // Swap the roles of var1 and var2 in new valuation
     v2.put(m_variable1, v.get(m_variable2));
     v2.put(m_variable2, v.get(m_variable1));
     new_vals.add(v2);
    }
    return new ConcreteMultiEvent(new_vals);
  }
}
