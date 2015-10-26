package ic.doc.camera;

public class Camera implements WriteListener {

	private boolean isOn = false;
	private Sensor sensor;
	private MemoryCard memoryCard;
	private boolean dataCurrentlyBeingWritten = false;

	public Camera(Sensor sensor, MemoryCard memoryCard) {
		this.sensor = sensor;
		this.memoryCard = memoryCard;
	}

	public void pressShutter() {
		if (isOn) {
			memoryCard.write(sensor.readData());
			dataCurrentlyBeingWritten = true;
		}
	}

	public void powerOn() {
		isOn = true;
		sensor.powerUp();
	}

	@Override
	public void writeComplete() {
		sensor.powerDown();
		dataCurrentlyBeingWritten = false;
	}

	public void powerOff() {
		if (!dataCurrentlyBeingWritten) {
			isOn = false;
			sensor.powerDown();
		}
	}
}
