package scannel.gpio;

import java.util.Timer;
import java.util.TimerTask;

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
	private GpioPinDigitalInput[] DI_list = new GpioPinDigitalInput[IO_SIZE];
	private GpioPinDigitalOutput[] DO_list = new GpioPinDigitalOutput[IO_SIZE]; 
	
//	private boolean[] di_activate = new boolean[4];
	private boolean[] do_activate = new boolean[IO_SIZE];
	private boolean isDIActivated = false;
	private boolean isDOActivated = false;
	private DigitalInputListener diListener;
	
	private PinState[] di_start = new PinState[IO_SIZE];
	private PinState[] di_stop = new PinState[IO_SIZE];
	
	private int[] delay_start, delay_stop;
	private Timer startTimer, stopTimer;
	private TimerTask startTask, stopTask;
	
	// This variable is used to avoid invoking platform native method while testing the ReaderTool software on
	// the reader without RPi board.
	public static boolean DISABLE_CONTROLLER = false;
	
	private final static int IO_SIZE = 4;
	
	
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
		
		for (int i=0; i<di_start.length; i++) {
			DI_list[i].removeAllListeners();
			di_start[i] = ReaderConfig.getInstance().getDIStart(i+1);
			di_stop[i] = ReaderConfig.getInstance().getDIStop(i+1);
			if ((di_start[i] != null) || (di_stop[i] != null)) {
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
		
		startTask = new TimerTask() {

			@Override
			public void run() {
				if (diListener != null) {
					diListener.digitalInputOn();
				}
			}
			
		};
		
		stopTask = new TimerTask() {

			@Override
			public void run() {
				if (diListener != null) {
					diListener.digitalInputOff();
				}
			}
			
		};
		
		startTimer = new Timer();
		stopTimer = new Timer();
		
		delay_start = new int[IO_SIZE];
		delay_stop = new int[IO_SIZE];
		
		
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

		startTimer.cancel();
		startTimer = null;
		stopTimer.cancel();
		stopTimer = null;
		
		gpio.shutdown();
		gpio = null;
		ioController = null;
	}
	
	public void start() {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		System.out.println("[DigitalIOController] start()");
//		DI_list[0].addListener(this);
//		DI_list[1].addListener(this);
//		DI_list[2].addListener(this);
//		DI_list[3].addListener(this);
		
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
//			if (this.startReading(event)){
//				diListener.digitalInputOn();
//			} else if (this.stopReading(event)) {
//				diListener.digitalInputOff();
//			} else {
//				// do nothing
//			}
			this.checkDIStart(event);
			this.checkDIStop(event);
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
	
//	public void enableDI(boolean[] setting) {
//		if (DISABLE_CONTROLLER) {
//			return;
//		}
//		
//		if (setting.length != di_activate.length) {
//			MyLogger.printLog("DI port number not match...");
//			return;
//		}
//		
//		di_activate = setting;
//		
//		for (int i=0; i<setting.length; i++) {
//			DI_list[i].removeAllListeners();
//			if (setting[i]) {
//				DI_list[i].addListener(this);
//			}
//		}
//		
//	}
	
	public void enableDI(PinState[] start, PinState[] stop) {
		if (DISABLE_CONTROLLER) {
			return;
		}
		
		if ((start.length != di_start.length) || (stop.length != di_stop.length)){
			MyLogger.printLog("DI port number not match...");
			return;
		}
		
		di_start = start;
		di_stop = stop;
		
		for (int i=0; i<di_start.length; i++) {
			DI_list[i].removeAllListeners();
			if ((di_start[i] != null) || (di_stop[i] != null)) {
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
	
//	private boolean allDigitalInputOff() {
//		boolean temp = true;
//		
//		for (int i=0; i<di_activate.length; i++) {
//			if (di_activate[i]) {
//				temp = temp && DI_list[i].isHigh();
//			}
//		}
//		
//		return temp;
//	}
	
	private boolean startReading(GpioPinDigitalStateChangeEvent event) {
		for (int i=0; i<di_start.length; i++) {
			if (event.getPin() == DI_list[i]
					&& event.getState() == di_start[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean stopReading(GpioPinDigitalStateChangeEvent event) {
		for (int i=0; i<di_stop.length; i++) {
			if (event.getPin() == DI_list[i]
					&& event.getState() == di_stop[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	private void checkDIStart(GpioPinDigitalStateChangeEvent event) {
		for (int i=0; i<di_stop.length; i++) {
			if (event.getPin() == DI_list[i] && event.getState() == di_start[i]) {
				
				int delayTime = delay_start[i];
				if (delayTime > 0) {
					startTimer.cancel();
					startTimer.schedule(startTask, delayTime);
					MyLogger.printLog("Start inventory in "+delayTime+" milliseconds...");
				} else {
					diListener.digitalInputOn();
				}
				
				break;
			}
		}
	}
	
	private void checkDIStop(GpioPinDigitalStateChangeEvent event) {
		for (int i=0; i<di_stop.length; i++) {
			if (event.getPin() == DI_list[i] && event.getState() == di_stop[i]) {
				
				int delayTime = delay_stop[i];
				if (delayTime > 0) {
					stopTimer.cancel();
					stopTimer.schedule(stopTask, delayTime);
					MyLogger.printLog("Stop inventory in "+delayTime+" milliseconds...");
				} else {
					diListener.digitalInputOff();
				}
				
				break;
			}
		}
	}
	
	public void setStartDelay(int[] time) {
		delay_start = time;
	}
	
	public void setStopDelay(int[] time) {
		delay_stop = time;
	}
}
