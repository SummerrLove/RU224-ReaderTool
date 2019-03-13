package scannel.gpio;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;

public interface DigitalInputListener {

	public void handleInput(GpioPinDigitalInput pin, PinState state);
	public void digitalInputOn();
	public void digitalInputOff();
}
