package Servoing;

public class TopNeckController extends ServoController {

    private static final int TOP_PWM_0 = 726;
    private static final int TOP_PWM_180 = 2371;

    public TopNeckController() {
            super(TOP_PWM_0, TOP_PWM_180, Math.PI, TOP_PWM_0, TOP_PWM_180);
    }

    /**
     * Determines whether the neck servo is at zero
     * @param currPWM the last PWM value written to the neck servo
     */
    public boolean isZero(int currPWM) {
        return (currPWM == PWM_0);
    }

    /**
     * Determines whether the neck servo is at the bend with a desired angle state
     * @param currPWM the last PWM value written to the neck
     */
    public boolean isRotated(int currPWM) {
        return (currPWM == PWM_180);
    }

    /**
     * Returns the PWM value to write to the neck servo
     * @param currPWM the last PWM value written to the neck servo
     */
    public int zero(int currPWM) {
        return getSafePWM(currPWM, PWM_0);
    }

    public int rotate(int currPWM) {
        return getSafePWM(currPWM, PWM_180);
    }
}
