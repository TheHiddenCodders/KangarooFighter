package Packets;


public class GameClientPacket extends Packets
{
	private static final long serialVersionUID = 4448845967749323548L;
	
	public boolean leftArrow;
	public boolean rightArrow;
	public boolean topArrow;
	public boolean bottomArrow;
	public boolean leftPunch;
	public boolean rightPunch;
	public boolean guard;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [leftArrow]: " + leftArrow + "\n"
				+ "- [rightArrow]: " + rightArrow + "\n"
				+ "- [topArrow]: " + topArrow + "\n"
				+ "- [bottomArrow]: " + bottomArrow + "\n"
				+ "- [leftPunch]: " + leftPunch + "\n"
				+ "- [rightPunch]: " + rightPunch + "\n"
				+ "- [guard]: " + guard + "\n";
	}
	
	public GameClientPacket()
	{
		super();
	}
	public GameClientPacket(GameClientPacket copy)
	{
		super(copy.getIp());
		
		System.err.println("Called");
		
		this.leftArrow = copy.leftArrow;
		this.rightArrow  = copy.rightArrow;
		this.topArrow = copy.topArrow;
		this.bottomArrow = copy.bottomArrow;
		this.leftPunch = copy.leftPunch;
		this.rightPunch = copy.rightPunch;
		this.guard = copy.guard;
	}
}
