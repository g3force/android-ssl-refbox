package edu.dhbw.mannheim.tigers.referee.data;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;



/**
 * This class is an {@link ITransmitter} implementation capable of sending some {@code byte[]}-data via UDP to a
 * multicast-group.
 * 
 * @author Gero
 */
public class MulticastUDPTransmitter {
	

	// Communication
	private MulticastSocket		socket		= null;
	
	private InetAddress			targetAddr	= null;
	private final int				targetPort;
	
	private DatagramPacket		tempPacket	= null;
	
	
	/**
	 * @see MulticastUDPTransmitter
	 * @param localPort
	 * @param targetAddr
	 * @param targetPort
	 */
	public MulticastUDPTransmitter(int localPort, String targetAddr, int targetPort) {
		this(localPort, targetAddr, targetPort, null);
	}
	
	
	/**
	 * @see MulticastUDPTransmitter
	 * @param localPort
	 * @param targetAddr
	 * @param targetPort
	 * @param nif
	 */
	public MulticastUDPTransmitter(int localPort, String targetAddr, int targetPort, NetworkInterface nif) {
		this.targetPort = targetPort;
		

		while (socket == null) {
			try {
				socket = new MulticastSocket(localPort);
				
				// Set nif
				if (nif != null) {
					socket.setNetworkInterface(nif);
				}
				
			} catch (SocketException err) {
				Log.i(Constants.MAIN_TAG, "Port " + localPort + " used, will try " + ++localPort + " instead!");
				continue;
			} catch (IOException err) {
				Log.e(Constants.MAIN_TAG, "Error while creating MulticastSocket!", err);
			}
		}
		
		try {
			this.targetAddr = InetAddress.getByName(targetAddr);
		} catch (UnknownHostException err) {
			Log.e(Constants.MAIN_TAG, "The Host could not be found!", err);
		}
	}
	

	public boolean send(byte[] data) {
			tempPacket = new DatagramPacket(data, data.length, targetAddr, targetPort);
			
			try {
				socket.send(tempPacket); // DatagramPacket is sent...
			} catch (NoRouteToHostException nrh) {
				Log.w(Constants.MAIN_TAG, "No route to host: '" + targetAddr + "'. Dropping packet...");
				return false;
			} catch (IOException err) {
				Log.e(Constants.MAIN_TAG, "Error while sending data to: '" + targetAddr + ":" + targetPort + "'!", err);
				return false;
			}
			
			return true;
	}
	

	public void cleanup() {
		targetAddr = null;
		tempPacket = null;
		
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public int getLocalPort() {
		return socket.getLocalPort();
	}
	

	public InetAddress getLocalAddress() {
		return targetAddr;
	}
	

	public int getTargetPort() {
		return targetPort;
	}
	

	MulticastSocket getSocket() {
		return socket;
	}
}
