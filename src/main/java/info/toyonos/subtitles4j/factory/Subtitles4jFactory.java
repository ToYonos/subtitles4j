package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

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

	public File toSRT(File input) throws Subtitles4jException
	{
		return toSubtitlesType(input, SubtitlesType.SRT);
	}
	
	public File toSubtitlesType(File input, SubtitlesType type) throws Subtitles4jException
	{
		String ext = FilenameUtils.getExtension(input.getName());
		if (type.hasExtension(ext))
		{
			return input;
		}
		else
		{
			SubtitlesType inputType = SubtitlesType.fromExtension(ext);
			if (inputType != null)
			{
				return factories.get(inputType).toFile(
					fromFile(input),
					new File(
						input.getParent(),
						FilenameUtils.getBaseName(input.getName()) + "." + inputType.getExtensions().iterator().next()
					)
				);
			}
			else
			{
				throw new UnsupportedSubtitlesExtension(ext);
			}
		}
	}
	
	public SubtitlesContainer fromFile(File input) throws Subtitles4jException
	{
		String ext = FilenameUtils.getExtension(input.getName());
		SubtitlesType inputType = SubtitlesType.fromExtension(ext);
		if (inputType != null)
		{
			return factories.get(inputType).fromFile(input);
		}
		else
		{
			throw new UnsupportedSubtitlesExtension(ext);
		}
	}
	
	public File toSRT(SubtitlesContainer container, File ouput) throws FileGenerationException 
	{
		return factories.get(SubtitlesType.SRT).toFile(container, ouput);
	}
	
	public File toASS(SubtitlesContainer container, File ouput) throws FileGenerationException 
	{
		return factories.get(SubtitlesType.ASS).toFile(container, ouput);
	}
}