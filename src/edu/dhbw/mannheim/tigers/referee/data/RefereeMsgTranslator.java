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



/**
 * This class provides methods to:<br>
 * - {@link #build} data packets out of {@link RefereeMsg}<br>
 * - {@link #translate} given data packets and return a new {@link RefereeMsg}<br>
 * Data packets are treated according to
 * <a href = http://small-size.informatik.uni-bremen.de/referee:protocol>that</a> protocol.
 * 
 * 
 * @author Malte, Dion, Gero
 * 
 */
public class RefereeMsgTranslator
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static byte[] build(int id, ERefereeCommand cmd, int goalsBlue, int goalsYellow, short timeLeft,
			boolean tigersAreYellow)
	{
		byte idBytes = sIntTouByte(id);
		byte goalBlueBytes = sIntTouByte(goalsBlue);
		byte goalYellowBytes = sIntTouByte(goalsYellow);
		
		byte[] packet = { RefereeMap.getByte(cmd, tigersAreYellow), idBytes, goalBlueBytes, goalYellowBytes, 0,
				(byte) timeLeft };
		return packet;
	}
	
	
	private static byte sIntTouByte(int sInt)
	{
		// Okay, seems as if the cast just cuts, without any extra-handling for the sign-bit, perfect! =)
		// int offset = sInt & 0x80; // Detect whether the critical (byte-sign) bit is set
		byte uByte = (byte) sInt; // Cut
		// uByte &= 0x7F; // Empty critical bit
		// uByte |= offset; // Fill it with the correct information
		return uByte;
	}
	
	
	public static RefereeMsg translate(byte[] packet, boolean weAreYellow)
	{
		// --- putting the remaining time and the command counter ---
		final short timeRemaining = (short) ((packet[4] << 8) + (packet[5] & 0xff));
		final byte idBytes = packet[1];
		final byte cmdByte = packet[0];
		
		
		// --- putting the goals (yellow/blue) ---
		final byte tigerGoalBytes;
		final byte enemyGoalBytes;
		
		if (weAreYellow)
		{
			tigerGoalBytes = packet[3];
			enemyGoalBytes = packet[2];
		} else
		{
			tigerGoalBytes = packet[2];
			enemyGoalBytes = packet[3];
		}
		
		// --- convert byte (unsigned) to int (Does NOT respect byte-order!) ---
		final int goalsTigers = uByteTosInt(tigerGoalBytes);
		final int goalsEnemies = uByteTosInt(enemyGoalBytes);
		final int id = uByteTosInt(idBytes);
		
		// --- convert cmdByte to command ---
		ERefereeCommand cmd = RefereeMap.getCommand(cmdByte, weAreYellow);
		return new RefereeMsg(id, cmd, goalsTigers, goalsEnemies, timeRemaining);
	}
	
	
	private static int uByteTosInt(byte uByte)
	{
		int offset = uByte & 0x80; // 0 or 128
		uByte &= 0x7f; // Remove first bit, which is our sign (Java) or extra-unsigned-bit (C)
		return uByte + offset;
	}
}
