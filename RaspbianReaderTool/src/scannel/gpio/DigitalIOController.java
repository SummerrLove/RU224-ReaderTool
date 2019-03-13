package scannel.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import scannel.reader.MyLogger;

public class DigitalIOController implements GpioPinListenerDigital{

	private static DigitalIOController ioController;
	
	final GpioController gpio = GpioFactory.getInstance();
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
	
	public static boolean STOP_RUNNING = false;
	
	
	private DigitalIOController() {
		DI_list[0] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_1);
		DI_list[1] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_2);
		DI_list[2] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_3);
		DI_list[3] = gpio.provisionDigitalInputPin(Constants.GPIO_INPUT_4);
		
		DO_list[0] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_1, PinState.HIGH);
		DO_list[1] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_2, PinState.HIGH);
		DO_list[2] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_3, PinState.HIGH);
		DO_list[3] = gpio.provisionDigitalOutputPin(Constants.GPIO_OUTPUT_4, PinState.HIGH);
		
		
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
		gpio.shutdown();
		
		ioController = null;
	}
	
	public void start() {
		System.out.println("[DigitalIOController] start()");
//		DI_list[0].addListener(this);
//		DI_list[1].addListener(this);
//		DI_list[2].addListener(this);
//		DI_list[3].addListener(this);
		
	}
	
	public void stop() {
		System.out.println("[DigitalIOController] stop()");
		DI_list[0].removeAllListeners();
		DI_list[1].removeAllListeners();
		DI_list[2].removeAllListeners();
		DI_list[3].removeAllListeners();
	}
	
	public void removeDigitalInputListener() {
		DI_list[0].removeAllListeners();
		DI_list[1].removeAllListeners();
		DI_list[2].removeAllListeners();
		DI_list[3].removeAllListeners();
	}
	
	public void removeDigitalOutputListener() {
		DO_list[0].removeAllListeners();
		DO_list[1].removeAllListeners();
		DO_list[2].removeAllListeners();
		DO_list[3].removeAllListeners();
	}
	
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
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
		if (isDOActivated) {
			for (int i=0; i<do_activate.length; i++) {
				if (do_activate[i]) {
					DO_list[i].low();
				}
			}
		}
	}
	
	public void turnOffOutput() {
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
		boolean temp = true;
		
		for (int i=0; i<di_activate.length; i++) {
			if (di_activate[i]) {
				temp = temp && DI_list[i].isHigh();
			}
		}
		
		return temp;
		
//		if ((di_activate[0] && DI_1.isHigh())
//				&& (di_activate[1] && DI_2.isHigh())
//				&& (di_activate[2] && DI_3.isHigh())
//				&& (di_activate[3] && DI_4.isHigh())){
//			return true;
//		} else {
//			return false;
//		}
	}
}
