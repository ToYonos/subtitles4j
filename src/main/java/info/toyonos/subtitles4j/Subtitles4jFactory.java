package info.toyonos.subtitles4j;

import java.io.File;
import java.util.Map;

public class Subtitles4jFactory
{
	private Map<SubtitlesType, SubtitlesFactory> factories;

	public Subtitles4jFactory()
	{
		for (SubtitlesType type : SubtitlesType.values())
		{
			try
			{
				factories.put(type, type.getClazz().newInstance());
			}
			catch (Exception e)
			{
				// TODO log
			}
		}
	}

	public File toSRT(File input)
	{
		// 1) Determine the type of the file
		// 2) If not SRT, parse it
		// 3) Convert it to SRT
		return null;
	}

	public File toSRT(SubtitlesContainer container)
	{
		return factories.get(SubtitlesType.SRT).toFile(container);
	}
	
	public SubtitlesContainer fromFile(File input)
	{
		// 1) Determine the type of the file
		// 2) Parse it
		return null;
	}
}