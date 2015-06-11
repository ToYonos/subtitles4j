package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.helper.Subtitles4jUtils;
import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class Subtitles4jFactory
{
	protected static class Subtitles4jFactoryHolder
	{
		public static final Subtitles4jFactory instance = new Subtitles4jFactory();
	}
	
	public static Subtitles4jFactory getInstance()
	{
		return Subtitles4jFactoryHolder.instance;
	}
	
	private Map<SubtitlesType, SubtitlesFactory> factories;

	private Subtitles4jFactory()
	{
		factories = new HashMap<>();
		for (SubtitlesType type : SubtitlesType.values())
		{
			try
			{
				factories.put(type, type.getClazz().newInstance());
			}
			catch (Exception e)
			{
				throw new RuntimeException("The factory for the type " + type + " is missing !", e);
			}
		}
	}
	
	public File toSubtitlesType(File input, SubtitlesType type) throws Subtitles4jException
	{
		String ext = FilenameUtils.getExtension(input.getName());
		if (type.hasExtension(ext))
		{
			// Same input/output extension
			return input;
		}
		else
		{
			return factories.get(type).toFile(
				fromFile(input),
				new File(
					input.getParent(),
					FilenameUtils.getBaseName(input.getName()) + "." + type.getExtensions().iterator().next()
				)
			);
		}
	}
	
	// TODO outputsteam version

	public File toSubtitlesType(SubtitlesContainer container, File ouput, SubtitlesType type) throws Subtitles4jException
	{
		return factories.get(type).toFile(container, ouput);
	}
	
	public OutputStream toSubtitlesType(SubtitlesContainer container, OutputStream ouput, SubtitlesType type) throws Subtitles4jException
	{
		return factories.get(type).toStream(container, ouput);
	}
	
	public SubtitlesContainer fromFile(File input) throws Subtitles4jException
	{
		SubtitlesType inputType = Subtitles4jUtils.getType(input);
		if (inputType != null)
		{
			return factories.get(inputType).fromFile(input);
		}
		else
		{
			throw new UnsupportedSubtitlesExtension(FilenameUtils.getExtension(input.getName()));
		}
	}
	
	public SubtitlesContainer fromStream(InputStream input, SubtitlesType inputType) throws Subtitles4jException
	{
		if (inputType != null)
		{
			return factories.get(inputType).fromStream(input);
		}
		return null;
	}
	
	public File toSRT(SubtitlesContainer container, File ouput) throws SubtitlesGenerationException 
	{
		return factories.get(SubtitlesType.SRT).toFile(container, ouput);
	}
	
	public File toASS(SubtitlesContainer container, File ouput) throws SubtitlesGenerationException 
	{
		return factories.get(SubtitlesType.ASS).toFile(container, ouput);
	}
	
	public File toSRT(File input) throws Subtitles4jException
	{
		return toSubtitlesType(input, SubtitlesType.SRT);
	}
	
	public File toASS(File input) throws Subtitles4jException
	{
		return toSubtitlesType(input, SubtitlesType.ASS);
	}
}