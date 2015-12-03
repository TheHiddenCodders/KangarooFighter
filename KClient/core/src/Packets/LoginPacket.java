package Packets;

import java.io.Serializable;

/**
 * This object will be sent by client to server.
 * It contains a pseudo and an id
 * @author Nerisma
 *
 */
public class LoginPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -861319294637171380L;
	
	public String pseudo;
	public boolean accepted;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [pseudo]: " + pseudo + "\n"
				+ "- [accepted]: " + accepted + "\n";
	}
}
