package ca.uqac.lif.cep.propman;

import ca.uqac.lif.cep.ltl.Troolean;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ValuationIterator implements Iterator<Valuation>
{
  protected String[] m_variables;
  
  protected Troolean[] m_vector;
  
  protected boolean m_done;
  
  protected boolean m_update;
  
  public ValuationIterator(Set<String> domain)
  {
    super();
    m_variables = new String[domain.size()];
    m_vector = new Troolean[m_variables.length];
    int i = 0;
    for (String s : domain)
    {
      m_variables[i++] = s;
    }
    for (i = 0; i < m_variables.length; i++)
    {
      m_vector[i] = Troolean.FALSE;
    }
    m_done = false;
    m_update = false;
  }
  
  @Override
  public boolean hasNext()
  {
    if (m_update)
    {
      for (int i = 0; i < m_vector.length; i++)
      {
        if (m_vector[i] == Troolean.FALSE)
        {
          m_vector[i] = Troolean.TRUE;
          break;
        }
        else
        {
          m_vector[i] = Troolean.FALSE;
          if (i == m_vector.length - 1)
          {
            m_done = true;
          }
        }
      }
      m_update = false;
    }
    return !m_done;
  }

  @Override
  public Valuation next() throws NoSuchElementException
  {
    if (m_done)
    {
      throw new NoSuchElementException("No new valuation to enumerate");
    }
    Valuation v = new Valuation();
    for (int i = 0; i < m_vector.length; i++)
    {
      v.put(m_variables[i], m_vector[i]);
    }
    m_update = true;
    return v;
  }
}
