package Utils;

import java.io.File;
import java.util.Comparator;

public class FileDateComparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2)
	{
		Long modified1= new Long(o1.lastModified());
		Long modified2 = new Long(o2.lastModified());
		return modified1.compareTo(modified2);
	}	

}
