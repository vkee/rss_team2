package Servoing;

public class GateController extends ServoController {

    private static final int GATE_OPEN_PWM = 740;
    private static final int GATE_CLOSED_PWM = 1660;

    public GateController() {
        super(GATE_OPEN_PWM, GATE_CLOSED_PWM, Math.PI/2, GATE_CLOSED_PWM, GATE_OPEN_PWM);
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
     * Returns the PWM value to write to the gate servo
     * @param currPWM the last PWM value written to the gate servo
     */
    public int close(int currPWM) {
        return getSafePWM(currPWM, GATE_CLOSED_PWM);
    }

    //todo publish message instead of returning
    public int openPWM(int currPWM) {

        return getSafePWM(currPWM, GATE_OPEN_PWM);

    }

    /**
     *
     * @param currPWM array of current PWM values
     * @return
     */
    public ArmMsg open(int[] currPWM) {
        ArmMsg msg = new ArmMsg();
        msg.pwms[0] = currPWM[0];
        msg.pwms[1] = currPWM[1];
        msg.pwms[2] = getSafePWM(currPWM, GATE_OPEN_PWM);
        armPWMPub.publish(msg);
    }


}
