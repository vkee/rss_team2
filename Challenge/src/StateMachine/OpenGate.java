package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import org.ros.message.rss_msgs.*;

/**
 * This state rotates the back servo until the gate is open
 */
public class OpenGate implements FSMState {
    //TODO: make it actually open the gate

    private FSM fsm;	

    public OpenGate(FSM stateMachine)
    {
        fsm = stateMachine;

        //init any variables for this state

    }	


    public stateENUM getName()
    {return stateENUM.OPENGATE;}


    public boolean accepts(msgENUM msgType)
    {
        if (msgType == msgENUM.SERVO) return true;
        return false;
    }


    public void update(GenericMessage msg)
    {
        //do stuff

//        System.out.println("Current state: OpenGate.");

        // get gate PWM value from arm message
        ArmMsg message = (ArmMsg)msg.message;
        int gatePWM = (int) message.pwms[1]; // convert from long to int

        // if gate is not open, open gate
        //System.out.println("Open PWM: 740");
        if (!fsm.gateServo.isOpen(gatePWM))
        {
            int[] messagePWMs = new int[3];
            messagePWMs[0] = (int) message.pwms[0]; // convert from long to int
            messagePWMs[1] = (int) message.pwms[1]; // convert from long to int
            messagePWMs[2] = (int) message.pwms[2]; // convert from long to int
            fsm.gateServo.open(messagePWMs);
//            System.out.println("Trying to open gate.");
        }
        else //if condition to leave state
        {
            //			System.out.println("Done opening gate.");
            //			System.out.println("I'm going to try pooping out some blocks now.");

            fsm.updateState(new MoveToRelease(fsm));
        }

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }


}