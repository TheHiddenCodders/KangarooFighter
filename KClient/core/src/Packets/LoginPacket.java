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
	public String pseudo;
	public String pwd;
	public boolean pwdMatch;
	public boolean pseudoExists;
	public boolean accepted;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [pseudo]: " + pseudo + "\n"
				+ "- [pwd]: " + pwd + "\n"
				+ "- [pwdMatch]: " + pwdMatch + "\n"
				+ "- [pseudoExists]: " + pseudoExists + "\n"
				+ "- [accepted]: " + accepted + "\n";
	}
}
