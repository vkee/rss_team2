package Servoing;

import org.ros.node.topic.Publisher;
import org.ros.message.rss_msgs.*;

public class GateController extends ServoController {

    private static final int GATE_OPEN_PWM = 740;
    private static final int GATE_CLOSED_PWM = 1660;

    public GateController(Publisher<ArmMsg> armPWMPub) {
        super(GATE_OPEN_PWM, GATE_CLOSED_PWM, Math.PI/2, GATE_CLOSED_PWM, GATE_OPEN_PWM, armPWMPub);
    }

    /**
     * Determines whether the gate servo is at zero
     * @param currPWM the last PWM value written to the gate servo
     */
    public boolean isClosed(int currPWM) {
        return (currPWM == GATE_CLOSED_PWM);
    }

    /**
     * Determines whether the gate is open
     * @param currPWM the last PWM value written to the gate
     */
    public boolean isOpen(int currPWM) {
        return (currPWM == GATE_OPEN_PWM);
    }

    /**
     * Returns the PWM value to write to the gate servo while closing
     * @param currPWM the last PWM value written to the gate servo
     * @return safe PWM value to write to the gate servo while closing
     */
    public int closePWM(int currPWM) {
        return getSafePWM(currPWM, GATE_CLOSED_PWM);
    }

    /**
     * Returns the safe PWM value to write to the gate servo while opening
     * @param currPWM the last PWM value written to the gate servo
     * @return safe PWM value to write to the gate servo while opening
     */
    public int openPWM(int currPWM) {
        return getSafePWM(currPWM, GATE_OPEN_PWM);

    }

    /**
     * Opens gate by calling ServoController.sendPWM() with appropriate PWM values
     * @param currPWM int array of current PWM values
     */
    public void open(int[] currPWM) {
    	// send PWM commands to open gate only
    	// the neck servo commands are just their current PWM
    	// the gate servo command is the safe PWM value to write while opening gate
        sendPWM(currPWM[0],getSafePWM(currPWM[1], GATE_OPEN_PWM),currPWM[2]);
    }

    /**
     * Closes gate by calling ServoController.sendPWM() with appropriate PWM values
     * @param currPWM int array of current PWM values
     */
    public void close(int[] currPWM) {
        // send PWM commands to open gate only
        // the neck servo commands are just their current PWM
        // the gate servo command is the safe PWM value to write while opening gate
        sendPWM(currPWM[0], getSafePWM(currPWM[1], GATE_CLOSED_PWM), currPWM[2]);
    }

}
