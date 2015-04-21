package StateMachine;

public class GenericMessage<E> {
	
	public E message;
	public FSM.msgENUM type;
	
	public GenericMessage(E msg, FSM.msgENUM typ)
		{
		message=msg;
		type=typ;
		}

}
