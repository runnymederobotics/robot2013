package robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;

public class EncoderAverager implements PIDSource {

    Encoder encOne, encTwo;
    int signOne = 1;
    int signTwo = 1;
    boolean countEncoder;

    public EncoderAverager(Encoder encOne, Encoder encTwo, boolean countEncoder) {
        this.encOne = encOne;
        this.encTwo = encTwo;
        this.countEncoder = countEncoder;
    }

    public EncoderAverager(Encoder encOne, boolean reverseOne, Encoder encTwo, boolean reverseTwo, boolean countEncoder) {
        this.encOne = encOne;
        this.signOne = reverseOne ? -1 : 1;
        this.encTwo = encTwo;
        this.signTwo = reverseTwo ? -1 : 1;
        this.countEncoder = countEncoder;
    }

    public double getRate() {
        return (encOne.getRate() * signOne + encTwo.getRate() * signTwo) / 2;
    }

    public int get() {
        return (encOne.get() * signOne + encTwo.get() * signTwo) / 2;
    }

    public double pidGet() {
        if (countEncoder) {
            return get();
        } else {
            return getRate();
        }
    }
}
