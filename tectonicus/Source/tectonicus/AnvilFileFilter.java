package tectonicus;

import java.io.File;
import java.io.FilenameFilter;

public class AnvilFileFilter implements FilenameFilter
{
	@Override
	public boolean accept(File dir, String file)
	{
		return file.endsWith(".mca");
	}
}