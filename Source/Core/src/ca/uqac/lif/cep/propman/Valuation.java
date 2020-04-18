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
import ca.uqac.lif.cep.ltl.Troolean.Value;

import java.util.HashMap; 
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A map between variable names and ternary Boolean values (i.e. Trooleans).
 */
public class Valuation extends HashMap<String,Troolean.Value>
{
  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * Determines if "this" is a partial order of v
   * @param v The valuation
   * Iterate through the two valuations in parallel and at each time check if the troolean values are the same or not
   * If the troolean values are different then the value of "this" should be "INCONCLUSIVE" to be a partial order of v
   * @return <tt>true</tt> if it is a partial order; <tt>false</tt> otherwise
   */
  
  public boolean isPartialOrder(Valuation v){ 
	   
	    Set<Entry<String, Value>> entrySet_v = v.entrySet();
	    Set<Entry<String, Value>> entrySet_this = this.entrySet();
		Iterator<Entry<String, Value>> it_v = entrySet_v.iterator();
		Iterator<Entry<String, Value>> it_this = entrySet_this.iterator();

		while(it_v.hasNext() && it_this.hasNext()){
	       Map.Entry m_v = (Map.Entry)it_v.next();
	       Map.Entry m_this = (Map.Entry)it_this.next();
	       if (!m_this.getValue().toString().equals(m_v.getValue().toString())){
				//System.out.println(m_this.getValue().toString() + "  " + m_v.getValue().toString());
				if(!m_this.getValue().toString().equals("INCONCLUSIVE")){
					//System.out.println("false");
					return false;
				}
	       }
	       
	   }
		return true;
 }
  
  
  /**
   * Determines if a valuation is an optimal within a set of valuations
   * @param v The valuation set
   * to be optimal, a valuation should not have partialOrder valuation
   * @return <tt>true</tt> if it is a optimal; <tt>false</tt> otherwise
   */
 public boolean isOptimal(Set<Valuation> allValuations){
	 Iterator<Valuation> iterate = allValuations.iterator();
     Valuation next = new Valuation();

     while(iterate.hasNext()){
 		next = iterate.next();
 		if(!next.equals(this)){
 			 if (next.isPartialOrder(this)){
 	   			//System.out.println("it is a partial order ");
 	   		    return false;
 	   	    }
 		}
     }
     return true;
 }



}
