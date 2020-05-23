package ca.uqac.lif.cep.propman;

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * A function that turns a multi-event into another multi-event.
 * @author Sylvain Hallé, Rania Taleb
 */
public abstract class MultiEventFunction extends UnaryFunction<MultiEvent,MultiEvent>
{
  public MultiEventFunction()
  {
    super(MultiEvent.class, MultiEvent.class);
  }
}