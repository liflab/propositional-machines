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

import ca.uqac.lif.cep.functions.Function;
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.util.Booleans.Or;

/**
 * A "dummy" main class; it is only there to make the generated
 * JAR runnable from the command line. In such a case, it simply
 * displays a message indicating that the JAR is a library, not
 * intended to be used as a stand-alone program.
 */
public class Main
{
  /**
   * Build string to identify versions
   */
  protected static final String VERSION_STRING = Main.class.getPackage().getImplementationVersion();
  
  /**
   * Main method.
   * @param args Command line arguments
   */
  public static void main(String[] args)
  {
    //System.out.println("Propositional machine palettes v" + VERSION_STRING);
    //System.out.println("(C) 2020 Laboratoire d'informatique formelle");
    //System.out.println("This JAR file is a library that is not meant to be run from the");
    //System.out.println("command line.");
    
    
    // prove that formula (a^c)\b  supports a\c
    PropositionalVariable p1 = new PropositionalVariable("a");
    PropositionalVariable p2 = new PropositionalVariable("b");    
    PropositionalVariable p3 = new PropositionalVariable("c");
    
    PropositionalVariable [] vars = new PropositionalVariable []{p1 , p2, p3};
    
    //Define the First Propositional Formula (a^c)\b
    PropositionalFormula phi = new PropositionalFormula(Troolean.OR_FUNCTION,new PropositionalFormula(Troolean.AND_FUNCTION,p1, p3), p2);

    //Define the Second Propositional Formula a\c
    PropositionalFormula phi_prime = new PropositionalFormula(Troolean.OR_FUNCTION,p1, p3);  
    
    //call the supports function to see if phi supports phi_prime
    phi.supports(phi_prime, vars);

    System.exit(0);

  }

  /**
   * Constructor. Should not be accessed.
   */
  private Main()
  {
    throw new IllegalAccessError("Main class");
  }
}