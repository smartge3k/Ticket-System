package de.uniba.rz.backend.AmpqImplementation;

public abstract class QueueReceiver extends Thread{
	/**
	 * Starts the server (consumer).
	 */
	public abstract void startServer();

	/**
	 * Stops the server (consumer) gracefully.
	 */
	public abstract void stopServer();
}
