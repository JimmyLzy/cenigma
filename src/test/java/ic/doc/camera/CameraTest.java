package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	Sensor sensor = context.mock(Sensor.class);
	MemoryCard memoryCard = context.mock(MemoryCard.class);
	Camera camera = new Camera(sensor, memoryCard);

	@Test
	public void switchingTheCameraOnPowersUpTheSensor() {

		context.checking(new Expectations() {
			{
				exactly(1).of(sensor).powerUp();
			}
		});

		camera.powerOn();
	}

	@Test
	public void switchingTheCameraOffPowersDownTheSensor() {

		context.checking(new Expectations() {
			{
				exactly(1).of(sensor).powerDown();
			}
		});

		camera.powerOff();
	}

	@Test
	public void pressingTheShutterWhenThePowerIsOffDoesNothing() {

		context.checking(new Expectations() {
			{
				never(sensor).readData();
			}
		});

		camera.pressShutter();
	}

	@Test
	public void pressingTheShutterWithThePowerOnCopiesDataFromTheSensorToTheMemoryCard() {

		context.checking(new Expectations() {
			{
				allowing(sensor).powerUp();
				exactly(1).of(memoryCard).write(allowing(sensor).readData());
			}
		});
		camera.powerOn();
		camera.pressShutter();
	}

	@Test
	public void ifDataCurrentlyBeingWrittenSwitchingCameraOffNotPowerDownSensor() {

		context.checking(new Expectations() {
			{
				allowing(sensor).powerUp();

				allowing(memoryCard).write(allowing(sensor).readData());
				exactly(0).of(sensor).powerDown();
			}
		});
		camera.powerOn();
		camera.pressShutter();
		camera.powerOff();
		context.checking(new Expectations() {
			{
				exactly(1).of(sensor).powerDown();
			}
		});
		camera.writeComplete();
	}

	@Test
	public void onceWritingDataCompletedThenCameraPowerDownTheSensor() {

		context.checking(new Expectations() {
			{
				exactly(1).of(sensor).powerDown();
			}
		});
		camera.writeComplete();
	}

}
