package Utils;

import java.io.File;
import java.util.Comparator;

public class EloComparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2)
	{
		Integer elo1 = new Integer(ServerUtils.getData(o1.getName(), "elo"));
		Integer elo2 = new Integer(ServerUtils.getData(o2.getName(), "elo"));
		return elo2.compareTo(elo1);
	}	

}
