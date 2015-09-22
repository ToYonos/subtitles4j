package info.toyonos.subtitles4j.helper;

import info.toyonos.subtitles4j.factory.Subtitles4jException;
import info.toyonos.subtitles4j.factory.Subtitles4jFactory;
import info.toyonos.subtitles4j.factory.SubtitlesType;
import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;

public class Subtitles4jUtils
{
	/**
	 * Extract the <code>SubtitlesType</code> from a <code>File</code>, based on its extension
	 * @param subtitlesFile the input <code>File</code>
	 * @return the matching <code>SubtitlesType</code> or null if not supported
	 */
	public static SubtitlesType getType(String subtitlesFileName)
	{
		String ext = FilenameUtils.getExtension(subtitlesFileName);
		return SubtitlesType.fromExtension(ext);
	}
	
	/**
	 * <p>Shift the timestamps of an input subtitles stream</p>
	 * <p>It can be done in both ways, adding or removing milliseconds. If a timestamp become lower than zero, it is set to zero.</p>
	 * @param input the <code>InputStream</code> on the subtitles source
	 * @param inputType the <code>SubtitlesType</code> of the input
	 * @param output the <code>OutputStream</code> containing the modified subtitles 
	 * @param millis the time in millisecond to be added to each caption, can be negative
	 * @return the <code>OutputStream</code> containing the modified subtitles
	 * @throws Subtitles4jException if an error occurs during the operation
	 * @throws IOException if any IO error occurs
	 */
	public static OutputStream shift(InputStream input, SubtitlesType inputType, OutputStream output, int millis) throws Subtitles4jException, IOException
	{
		Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
		SubtitlesContainer container = factory.fromStream(input, inputType);
		container.shiftTime(millis);
		return factory.toSubtitlesType(container, output, inputType);
	}
	
	/**
	 * <p>Shift the timestamps of an input subtitles file</p>
	 * <p>It can be done in both ways, adding or removing milliseconds. If a timestamp become lower than zero, it is set to zero.</p>
	 * @param input the input subtitles <code>File</code> 
	 * @param millis the time in millisecond to be added to each caption, can be negative
	 * @return the modified <code>File</code>, which is the same as the input
	 * @throws Subtitles4jException if an error occurs during the operation
	 * @throws IOException if any IO error occurs
	 */
	public static File shift(File input, int millis) throws Subtitles4jException, IOException
	{
		Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
		SubtitlesContainer container = factory.fromFile(input);
		container.shiftTime(millis);
		return factory.toSubtitlesType(container, input, getType(input.getName()));
	}
}
