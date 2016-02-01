package Packets;

import java.io.Serializable;

public class Packets implements Serializable
{
	private static final long serialVersionUID = 254263979698874592L;
	
	protected String ip;
	
	public Packets()
	{
		this.ip = "0.0.0.0:0";
	}
	
	public Packets(String ip) 
	{
		this.ip = ip;
	}
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [ip]: " + ip;
	}
	
	public String getIp()
	{
		return ip;
	}
	
	public void setIp(String ip)
	{
		if (ip != null)
			this.ip = ip;
	}
}
