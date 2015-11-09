package Packets;

import java.io.Serializable;

/**
 * This object will be sent by client to server.
 * It contains a pseudo and an id
 * @author Nerisma
 *
 */
@SuppressWarnings("serial")
public class LoginPacket implements Serializable
{
	public String ip;
	public String pseudo;
	public boolean accepted;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [IP]: " + ip + "\n"
				+ "- [pseudo]: " + pseudo + "\n"
				+ "- [accepted]: " + accepted + "\n";
	}
}
