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
