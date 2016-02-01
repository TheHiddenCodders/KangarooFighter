package Packets;


@SuppressWarnings("serial")
public class SignOutPacket extends Packets
{
	public String pseudo;
	public String pwd;
	public boolean accepted;
	public boolean pseudoExists;
		
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [pseudo]: " + pseudo + "\n"
				+ "- [pwd]: " + pwd + "\n"
				+ "- [accepted]: " + accepted + "\n"
				+ "- [pseudoExists]: " + pseudoExists + "\n";
	}
}
