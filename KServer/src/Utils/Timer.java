package Utils;

public class Timer 
{
	private long lastTime;
	private long elapsedTime;
	
	/**
	 * 
	 */
	public Timer()
	{
		restart();
	}
	
	public void update()
	{
		// Get delta
		long difTime = System.currentTimeMillis() - lastTime;

		// Add to elapsedTime
		elapsedTime += difTime;
		
		// Update lastTime
		lastTime = System.currentTimeMillis();
	}
	
	public void restart()
	{
		elapsedTime = 0;
		lastTime = System.currentTimeMillis();
	}
	
	/**
	 * @return elapsed time in seconds
	 */
	public float getElapsedTime()
	{
		update();
		return elapsedTime / 1000f;
	}	
}
