package scannel.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import scannel.reader.MyLogger;
import scannel.reader.ReaderConfig;

public class DigitalIOController implements GpioPinListenerDigital{

	private static DigitalIOController ioController;
	
	private static GpioController gpio;
//	final GpioPinDigitalInput DI_1 = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_1);
//	final GpioPinDigitalInput DI_2 = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_2);
//	final GpioPinDigitalInput DI_3 = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_3);
//	final GpioPinDigitalInput DI_4 = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_4);
//	final GpioPinDigitalOutput DO_1 = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_1);
//	final GpioPinDigitalOutput DO_2 = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_2);
//	final GpioPinDigitalOutput DO_3 = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_3);
//	final GpioPinDigitalOutput DO_4 = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_4);
	private GpioPinDigitalInput[] DI_list = new GpioPinDigitalInput[4];
	private GpioPinDigitalOutput[] DO_list = new GpioPinDigitalOutput[4]; 
	
	private boolean[] di_activate = new boolean[4];
	private boolean[] do_activate = new boolean[4];
	private boolean isDIActivated = false;
	private boolean isDOActivated = false;
	private DigitalInputListener diListener;
	
	
	// This variable is used to avoid invoking platform native method while testing the ReaderTool software on
	// the reader without RPi board.
	public static boolean DISABLE_CONTROLLER = false;
	
	
	private DigitalIOController() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		if (gpio == null) {
			gpio = GpioFactory.getInstance();
		}
		
		DI_list[0] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_1);
		DI_list[1] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_2);
		DI_list[2] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_3);
		DI_list[3] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_4);
		
		DO_list[0] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_1, PinState.LOW);
		DO_list[1] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_2, PinState.LOW);
		DO_list[2] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_3, PinState.LOW);
		DO_list[3] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_4, PinState.LOW);
		
		isDIActivated = ReaderConfig.getInstance().getDITrigger();
		isDOActivated = ReaderConfig.getInstance().getDOTrigger();
		
		di_activate[0] = ReaderConfig.getInstance().getDI1();
		di_activate[1] = ReaderConfig.getInstance().getDI2();
		di_activate[2] = ReaderConfig.getInstance().getDI3();
		di_activate[3] = ReaderConfig.getInstance().getDI4();
		
		for (int i=0; i<di_activate.length; i++) {
			DI_list[i].removeAllListeners();
			if (di_activate[i]) {
				DI_list[i].addListener(this);
			}
		}
		
		do_activate[0] = ReaderConfig.getInstance().getDO1();
		do_activate[1] = ReaderConfig.getInstance().getDO2();
		do_activate[2] = ReaderConfig.getInstance().getDO3();
		do_activate[3] = ReaderConfig.getInstance().getDO4();
		
		for (int i=0; i<do_activate.length; i++) {
			DO_list[i].removeAllListeners();
			if (do_activate[i]) {
				DO_list[i].addListener(this);
			}
		}
		
//		System.out.println("================");
//		System.out.println(DI_list[0].getPin().getAddress());
//		System.out.println(DI_list[1].getPin().getAddress());
//		System.out.println(DI_list[2].getPin().getAddress());
//		System.out.println(DI_list[3].getPin().getAddress());
//		System.out.println("================");
	}

	public static DigitalIOController getInstance() {
		if (ioController == null) {
			System.out.println("Create DigitalIOController...");
			ioController = new DigitalIOController();
		}
		
		return ioController;
	}
	
	public void destroy() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		gpio.shutdown();
		gpio = null;
		ioController = null;
	}
	
	public void start() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		System.out.println("[DigitalIOController] start()");
		
	}
	
	public void stop() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		System.out.println("[DigitalIOController] stop()");
		DI_list[0].removeAllListeners();
		DI_list[1].removeAllListeners();
		DI_list[2].removeAllListeners();
		DI_list[3].removeAllListeners();
	}
	
	public void removeDigitalInputListener() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		DI_list[0].removeAllListeners();
		DI_list[1].removeAllListeners();
		DI_list[2].removeAllListeners();
		DI_list[3].removeAllListeners();
	}
	
	public void removeDigitalOutputListener() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		DO_list[0].removeAllListeners();
		DO_list[1].removeAllListeners();
		DO_list[2].removeAllListeners();
		DO_list[3].removeAllListeners();
	}
	
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		System.out.println("[DigitalIOController] handleGpioPinDigitalStateChangeEvent()");
		System.out.println("[DigitalIOController] "+event.getPin()+"="+event.getState());
		
		if (event.getPin() == DI_list[0]) {
			MyLogger.printLog("DI_1:"+event.getState());
		} else if (event.getPin() == DI_list[1]) {
			MyLogger.printLog("DI_2:"+event.getState());
		} else if (event.getPin() == DI_list[2]) {
			MyLogger.printLog("DI_3:"+event.getState());
		} else if (event.getPin() == DI_list[3]) {
			MyLogger.printLog("DI_4:"+event.getState());
		}
		
		if (isDIActivated) {
			if (this.allDigitalInputOff()) {
				diListener.digitalInputOff();
			} else {
				diListener.digitalInputOn();
			}
		}
			
		
			
	}
	
	public void setDIActivation(boolean bool) {
		isDIActivated = bool;
	}
	
	public boolean isDIActivated() {
		return isDIActivated;
	}
	
	public void setDOActivation(boolean bool) {
		isDOActivated = bool;
	}
	
	public boolean isDOActivated() {
		return isDOActivated;
	}
	
	public void enableDI(boolean[] setting) {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		if (setting.length != di_activate.length) {
			MyLogger.printLog("DI port number not match...");
			return;
		}
		
		di_activate = setting;
		
		for (int i=0; i<setting.length; i++) {
			DI_list[i].removeAllListeners();
			if (setting[i]) {
				DI_list[i].addListener(this);
			}
		}
		
	}
	
	public void enableDO(boolean[] setting) {
		if (setting.length != do_activate.length) {
			MyLogger.printLog("DO port number not match...");
			return;
		}
		
		do_activate = setting;
		
	}
	
	public void turnOnOutput() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		if (isDOActivated) {
			for (int i=0; i<do_activate.length; i++) {
				if (do_activate[i]) {
					DO_list[i].low();
				}
			}
		}
	}
	
	public void turnOffOutput() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		if (isDOActivated) {
			for (int i=0; i<do_activate.length; i++) {
				if (do_activate[i]) {
					DO_list[i].high();
				}
			}
		}
	}
	
	public void setDigitalInputListener(DigitalInputListener listener) {
		diListener = listener;
	}
	
	private boolean allDigitalInputOff() {
		// TODO This function needs to be modified when digital input issue is fixed. 
		
		boolean temp = true;
		
		for (int i=0; i<di_activate.length; i++) {
			if (di_activate[i]) {
				temp = temp && DI_list[i].isHigh();
			}
		}
		
		return temp;
	}
}
