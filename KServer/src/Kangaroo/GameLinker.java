package Kangaroo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import Server.ClientProcessor;

public class GameLinker implements Map<ClientProcessor, Game>
{
	ArrayList<ClientProcessor> clients;

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<ClientProcessor, Game>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game get(Object key) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<ClientProcessor> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game put(ClientProcessor key, Game value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends ClientProcessor, ? extends Game> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Game remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Game> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
