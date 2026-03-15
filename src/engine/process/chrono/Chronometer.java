package engine.process.chrono;

import config.GameConfiguration;

public class Chronometer {
	private CyclicCounter hour = new CyclicCounter(0, GameConfiguration.END_OF_DAY_HOUR, 10);
	private CyclicCounter minute = new CyclicCounter(0, 59, 0);

	public void increment() {
		minute.increment();
		if (minute.getValue() == 0) {
			hour.increment();
		}
	}

	public void decrement() {
		minute.decrement();
		if (minute.getValue() == 59) {
			hour.decrement();
		}
	}

	public CyclicCounter getHour() {
		return hour;
	}

	public CyclicCounter getMinute() {
		return minute;
	}

	public String toString() {
		return hour.toString() + " : " + minute.toString();
	}

	public static String transform(int value) {
		String result = "";
		if (value < 10) {
			result = "0" + value;
		} else {
			result = String.valueOf(value);
		}
		return result;
	}

	public void init() {
		hour.setValue(10);
		minute.setValue(0);
	}

}
