package Server;

import java.util.ArrayList;

import Packets.Packets;

public class BufferPacket 
{
	/** packets : packets ready to be send */
	private ArrayList<Packets> packets;
	
	/** Default Constructor
	 * 
	 */
	BufferPacket()
	{
		packets = new ArrayList<Packets>();
	}
	
	/** Allow a thread to send packets to on other
	 * 
	 * @param newPacket : the packet to send
	 */
	public synchronized void sendPacket(Packets newPacket) 
	{
		// Add packet in the buffer
        packets.add(newPacket);
        
        // Notify the other thread, the receiving of new packets
        notify();
    }
	
	/** Allow a thread to read packets from an other
	 * 
	 * @return packets send by other thread
	 */
    public synchronized ArrayList<Packets> readPackets() 
    {    	
        //While there is no packet to read
        while(packets.size() == 0) 
        {
            try 
            { 
            	// Wait for the other thread receiving new packets
                wait();
            } 
            catch(InterruptedException ie) 
            {
                ie.printStackTrace();
            }
        }
        
        // Read the packets
        @SuppressWarnings("unchecked")
		ArrayList<Packets> result =(ArrayList<Packets>) packets.clone();
        
        // Delete packets for next reading
        packets.clear();
        
        // Send the result
        return result;
    }
}
