package Server;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferPacket 
{
	private ArrayList<Object> packets;
	private int index;
	
	BufferPacket()
	{
		packets = new ArrayList<Object>();
	}
	
	public synchronized void addPacket(Object newPacket) 
	{
        packets.add(index, newPacket);
        index++;
        notify();
        //System.out.println("notify() exécuté"); 
    }
 
    public synchronized ArrayList<Object> getPackets() 
    {
    	@SuppressWarnings("unchecked")
		ArrayList<Object> result = (ArrayList<Object>) packets.clone();
    	
        //tant que la liste est vide
        while(index == 0) 
        {
            try 
            {
                //attente passive
                wait();
            } 
            catch(InterruptedException ie) 
            {
                ie.printStackTrace();
            }
        }
        
        packets.clear();
        
        return result;
    }
}
