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
	private static final SubtitlesContainerProcessing SHIFTING_PROCESSING = new SubtitlesContainerProcessing()
	{
		@Override
		public void process(SubtitlesContainer container, Object... params)
		{
			container.shiftTime((Integer) params[0]);	
		}
	};
	
	private static final SubtitlesContainerProcessing REMOVING_PROCESSING = new SubtitlesContainerProcessing()
	{
		@Override
		public void process(SubtitlesContainer container, Object... params)
		{
			container.remove((String) params[0], (Boolean) params[1]);	
		}
	};
	
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
		return transformSubtitles(input, inputType, output, SHIFTING_PROCESSING, millis);
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
		return transformSubtitles(input, SHIFTING_PROCESSING, millis);
	}
	
	/**
	 * <p>Remove all the captions containing the specified regular expression, from an input subtitles stream</p>
	 * <p>For each caption, the search is performed on each line. A match on at least one line is enough to remove the caption</p> 
	 * @param input the <code>InputStream</code> on the subtitles source
	 * @param inputType the <code>SubtitlesType</code> of the input
	 * @param output the <code>OutputStream</code> containing the modified subtitles
	 * @param regex the regular expression to which captions are to be matched 
	 * @param caseSensitive true for a case sensitive operation, false otherwise 
	 * @return the <code>OutputStream</code> containing the modified subtitles
	 * @throws Subtitles4jException if an error occurs during the operation
	 * @throws IOException if any IO error occurs
	 */
	private static OutputStream removeCaptions(InputStream input, SubtitlesType inputType, OutputStream output, String regex, boolean caseSensitive) throws Subtitles4jException, IOException
	{
		return transformSubtitles(input, inputType, output, REMOVING_PROCESSING, regex, caseSensitive);
	}
	
	/**
	 * Equivalent to {@link #removeCaptions(InputStream, SubtitlesType, OutputStream, String, boolean)} for a case sensitive search
	 */
	public static OutputStream removeCaptions(InputStream input, SubtitlesType inputType, OutputStream output, String regex) throws Subtitles4jException, IOException
	{
		return removeCaptions(input, inputType, output, regex, true);
	}

	/**
	 * Equivalent to {@link #removeCaptions(InputStream, SubtitlesType, OutputStream, String, boolean)} for a case insensitive search
	 */
	public static OutputStream removeCaptionsIgnoreCase(InputStream input, SubtitlesType inputType, OutputStream output, String regex) throws Subtitles4jException, IOException
	{
		return removeCaptions(input, inputType, output, regex, false);
	}
	
	/**
	 * <p>Remove all the captions containing the specified regular expression, from an input subtitles file</p>
	 * <p>For each caption, the search is performed on each line. A match on at least one line is enough to remove the caption</p>
	 * @param input the input subtitles <code>File</code> 
	 * @param regex the regular expression to which captions are to be matched 
	 * @param caseSensitive true for a case sensitive operation, false otherwise 
	 * @return the modified <code>File</code>, which is the same as the input
	 * @throws Subtitles4jException if an error occurs during the operation
	 * @throws IOException if any IO error occurs
	 */
	private static File removeCaptions(File input, String regex, boolean caseSensitive) throws Subtitles4jException, IOException
	{
		return transformSubtitles(input, REMOVING_PROCESSING, regex, caseSensitive);
	}

	/**
	 * Equivalent to {@link #removeCaptions(File, String, boolean)} for a case sensitive search
	 */
	public static File removeCaptions(File input, String regex) throws Subtitles4jException, IOException
	{
		return removeCaptions(input, regex, true);
	}

	/**
	 * Equivalent to {@link #removeCaptions(File, String, boolean)} for a case insensitive search
	 */
	public static File removeCaptionsIgnoreCase(File input, String regex) throws Subtitles4jException, IOException
	{
		return removeCaptions(input, regex, false);
	}
	
	/**
	 * Do a certain operation on an input subtitles stream
	 * @param input the <code>InputStream</code> on the subtitles source
	 * @param inputType the <code>SubtitlesType</code> of the input
	 * @param output the <code>OutputStream</code> containing the modified subtitles
	 * @param processing the processing to apply
	 * @param params the optional parameters
	 * @return the <code>OutputStream</code> containing the modified subtitles
	 * @throws Subtitles4jException if an error occurs during the operation
	 * @throws IOException if any IO error occurs
	 */
	private static OutputStream transformSubtitles(InputStream input, SubtitlesType inputType, OutputStream output, SubtitlesContainerProcessing processing, Object... params) throws Subtitles4jException, IOException
	{
		Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
		SubtitlesContainer container = factory.fromStream(input, inputType);
		processing.process(container, params);
		return factory.toSubtitlesType(container, output, inputType);
	}
	
	/**
	 * Do a certain operation on an input subtitles file
	 * @param input the input subtitles <code>File</code> 
	 * @param processing the processing to apply
	 * @param params the optional parameters
	 * @return the modified <code>File</code>, which is the same as the input
	 * @throws Subtitles4jException if an error occurs during the operation
	 * @throws IOException if any IO error occurs
	 */
	private static File transformSubtitles(File input, SubtitlesContainerProcessing processing, Object... params) throws Subtitles4jException, IOException
	{
		Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
		SubtitlesContainer container = factory.fromFile(input);
		processing.process(container, params);
		return factory.toSubtitlesType(container, input, getType(input.getName()));
	}
	
	/**
	 * This interface defines a processing on a <code>SubtitlesContainer</code>
	 */
	private interface SubtitlesContainerProcessing
	{
		/**
		 * Do a certain operation on a <code>SubtitlesContainer</code>
		 * @param container The <code>SubtitlesContainer</code> to treat
		 * @param params the optional parameters
		 */
		void process(SubtitlesContainer container, Object... params);
	}
}
