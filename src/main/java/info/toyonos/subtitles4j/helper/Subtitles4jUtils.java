package info.toyonos.subtitles4j.helper;

import info.toyonos.subtitles4j.factory.Subtitles4jException;
import info.toyonos.subtitles4j.factory.Subtitles4jFactory;
import info.toyonos.subtitles4j.factory.SubtitlesType;
import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;

public class Subtitles4jUtils
{
	public static SubtitlesType getType(File subtitlesFile)
	{
		String ext = FilenameUtils.getExtension(subtitlesFile.getName());
		return SubtitlesType.fromExtension(ext);
	}
	
	public static OutputStream shift(InputStream input, OutputStream output, SubtitlesType inputType, int millis) throws Subtitles4jException
	{
		Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
		SubtitlesContainer container = factory.fromStream(input, inputType);
		container.shiftTime(millis);
		return factory.toSubtitlesType(container, output, inputType);
	}
	
	public static File shift(File input, int millis) throws Subtitles4jException
	{
		Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
		SubtitlesContainer container = factory.fromFile(input);
		container.shiftTime(millis);
		return factory.toSubtitlesType(container, input, getType(input));
	}
}
