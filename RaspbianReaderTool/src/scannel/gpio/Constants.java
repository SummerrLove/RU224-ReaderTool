package scannel.gpio;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Constants {

	/*
	 * The GPIO number is different from the image from Marko.
	 * Use command "gpio readall" in Raspbian to check the actual number 
	 * used by pi4j.
	 */
	
	public final static Pin GPIO_INPUT_1 = RaspiPin.GPIO_25;
	public final static Pin GPIO_INPUT_2 = RaspiPin.GPIO_24;
	public final static Pin GPIO_INPUT_3 = RaspiPin.GPIO_23;
	public final static Pin GPIO_INPUT_4 = RaspiPin.GPIO_22;
	
	public final static Pin GPIO_OUTPUT_1 = RaspiPin.GPIO_29;
	public final static Pin GPIO_OUTPUT_2 = RaspiPin.GPIO_28;
	public final static Pin GPIO_OUTPUT_3 = RaspiPin.GPIO_27;
	public final static Pin GPIO_OUTPUT_4 = RaspiPin.GPIO_26;
	
	public final static PinState ON = PinState.LOW;
	public final static PinState OFF = PinState.HIGH;
	
	

}
