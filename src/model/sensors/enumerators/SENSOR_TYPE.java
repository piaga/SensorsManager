package model.sensors.enumerators;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/*
 * In IOMapper, ST is for SENSOR_TYPE 
 */
public enum SENSOR_TYPE {
	ACCELEROMETER(90),
	BATTERY(91),
	CAMERA(92),
	GPS(93),
	LIGHT(94),
	MAGNETIC(95),
	ORIENTATION(96),
	PRESSURE(97),
	PROXIMITY(98),
	
	JOYSTICK(1),
	RELAY(2),
	ROTARY_ENCODER(3),
	ULTRASONIC(4),
	HC_SR501_PIR_MOTION_SENSOR(5),
	FLAME(6),
	LINEAR_HALL(7),
	METAL_TOUCH(8),
	BIG_SOUND(9),
	SMALL_SOUND(10),
	RGB_LED(11),
	SMD_RGB(12),
	TWO_COLOR(13),
	SEVEN_COLOR_FLASH(14),
	LASER_EMIT(15),
	SHOCK(16),
	IR_RECEIVER(17),
	IR_EMISSION(18),
	TILT_SWITCH(19),
	BUTTON(20),
	ACTIVE_BUZZER(21),
	PASSIVE_BUZZER(22),
	PHOTO_RESISTOR(23),
	TEMP_AND_HUMIDITY(24),
	GY_521_MODULE(25),
	PHOTO_INTERRUPTER(26),
	TAP_MODULE(27),
	MEMBRANE_SWITCH(28),
	AVOIDANCE(29),
	TRACKING(30),
	MAGNETIC_SPRING(31),
	WATER_LEVER_SENSOR(32),
	POWER_SUPPLY_MODULE(33),
	LCD_1602_MODULE(34),
	DIGITAL_TEMPERATURE(35);
	
	private final int value;
	
	SENSOR_TYPE(int id)
	{
		value=id;
	}
	
	public static Optional<SENSOR_TYPE> GetById(int id)
	{
		return Arrays.stream(values()).filter(where->where.value==id).findFirst();
	}
}


