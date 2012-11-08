/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 15.01.2011
 * Author(s): Malte
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.referee.data;

import android.util.Log;

/**
 * This class sends given RefereeMessages via Multicast UDP. The official
 * SSL-Protocol is used.
 * 
 * @author Malte
 * 
 */
public class RefereeMsgTransmitter {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private MulticastUDPTransmitter multicastUDPTransmitter;
	private byte[] packet;

	private final String address;
	private int targetPort;
	
	private static int id = 0;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public RefereeMsgTransmitter() {
		address = Constants.DEFAULT_ADRESS;
		targetPort = Constants.DEFAULT_PORT;
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------

	public void sendSimpleRefereeMsg(ERefereeCommand cmd) {
		sendOwnRefereeMsg(id, cmd, 0, 0, (short) 0, true);
		id++;
	}

	public void sendOwnRefereeMsg(int id, ERefereeCommand cmd, int goalsBlue,
			int goalsYellow, short timeLeft, boolean tigersAreYellow) {
		if (!isStarted()) {
			Log.e(Constants.MAIN_TAG, "Transmitter not started");
			return;
		}
		packet = RefereeMsgTranslator.build(id, cmd, goalsBlue, goalsYellow,
				timeLeft, tigersAreYellow);
		multicastUDPTransmitter.send(packet);
	}

	public void stop() {
		if (multicastUDPTransmitter != null) {
			multicastUDPTransmitter.cleanup();
		}
	}

	public void start() {
		stop();
		multicastUDPTransmitter = new MulticastUDPTransmitter(Constants.DEFAULT_SERVER_PORT,
				address, targetPort);
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------

	public boolean isStarted() {
		return multicastUDPTransmitter != null;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
		start();
	}
}
