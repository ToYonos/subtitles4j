package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.helper.Subtitles4jUtils;
import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

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

	/**
	 * Private constructor for singleton
	 */
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

	/**
	 * <p>Construct a <code>SubtitlesContainer</code> from an input <code>File</code><p>
	 * <p>The input <code>SubtitlesType</code> is determined through the filename extension<p>
	 * @param input The input <code>File</code> to be parsed
	 * @return the resulting <code>SubtitlesContainer</code>
	 * @throws Subtitles4jException if the input is malformed, the extension not supported
	 * @throws IOException if any IO error occurs
	 */
	public SubtitlesContainer fromFile(File input) throws Subtitles4jException, IOException 
	{
		SubtitlesType inputType = Subtitles4jUtils.getType(input.getName());
		if (inputType != null)
		{
			return factories.get(inputType).fromFile(input);
		}
		else
		{
			throw new UnsupportedSubtitlesExtension(FilenameUtils.getExtension(input.getName()));
		}
	}
	
	/**
	 * <p>Construct a <code>SubtitlesContainer</code> from an <code>InputStream</code><p>
	 * @param input The <code>InputStream</code> to be parsed
	 * @param inputType the <code>SubtitlesType</code> of the input
	 * @return the resulting <code>SubtitlesContainer</code>
	 * @throws MalformedSubtitlesException if the extension not supported
	 * @throws IOException if any IO error occurs
	 */
	public SubtitlesContainer fromStream(InputStream input, SubtitlesType inputType) throws MalformedSubtitlesException, IOException 
	{
		return factories.get(inputType).fromStream(input);
	}
	
	/**
	 * Convert a <code>SubtitlesContainer</code> to a <code>File</code>, for a target <code>SubtitlesType</code> 
	 * @param container the <code>SubtitlesContainer</code> to be converted
	 * @param output the output <code>File</code>
	 * @param type the target <code>SubtitlesType</code> 
	 * @return the generated output <code>File</code>
	 * @throws SubtitlesGenerationException if an error occurs during the generation
	 * @throws IOException if any IO error occurs
	 */
	public File toSubtitlesType(SubtitlesContainer container, File output, SubtitlesType type) throws SubtitlesGenerationException, IOException
	{
		return factories.get(type).toFile(container, output);
	}
	
	/**
	 * Convert a <code>SubtitlesContainer</code> to a <code>OutputStream</code>, for a target <code>SubtitlesType</code> 
	 * @param container the <code>SubtitlesContainer</code> to be converted
	 * @param output the <code>OutputStream</code>
	 * @param type the target <code>SubtitlesType</code> 
	 * @return the generated <code>OutputStream</code>
	 * @throws SubtitlesGenerationException if an error occurs during the generation
	 */
	public OutputStream toSubtitlesType(SubtitlesContainer container, OutputStream output, SubtitlesType type) throws SubtitlesGenerationException
	{
		return factories.get(type).toStream(container, output);
	}
	
	/**
	 * Convert an input <code>File</code> to a target <code>SubtitlesType</code> 
	 * @param input the input <code>File</code>
	 * @param output the output <code>File</code>
	 * @param outputType the new wished <code>SubtitlesType</code> 
	 * @return the modified output <code>File</code> 
	 * @throws Subtitles4jException if an error occurs during the conversion
	 * @throws IOException if any IO error occurs
	 */
	public File toSubtitlesType(File input, File output, SubtitlesType outputType) throws Subtitles4jException, IOException
	{
		String ext = FilenameUtils.getExtension(input.getName());
		if (outputType.hasExtension(ext))
		{
			// Same input/output extension
			return input;
		}
		else
		{
			return factories.get(outputType).toFile(fromFile(input), output);
		}
	}
	
	/**
	 * Convert an <code>InputStream</code> to a target <code>SubtitlesType</code> 
	 * @param input the <code>InputStream</code>
	 * @param inputType the <code>SubtitlesType</code> of the input
	 * @param output the <code>OutputStream</code> 
	 * @param outputType the new wished <code>SubtitlesType</code>
	 * @return the converted code>OutputStream</code>
	 * @throws Subtitles4jException if an error occurs during the conversion
	 * @throws IOException if any IO error occurs
	 */
	public OutputStream toSubtitlesType(InputStream input, SubtitlesType inputType, OutputStream output, SubtitlesType outputType) throws Subtitles4jException, IOException
	{
		if (outputType == inputType)
		{
			// Same input/output type
			IOUtils.copy(input, output);
			return output;
		}
		else
		{
			return factories.get(outputType).toStream(fromStream(input, inputType),	output);
		}
	}
	
	/* Shortcut methods for all types */
	
	/**
	 * Equivalent to {@link #toSubtitlesType(SubtitlesContainer, File, SubtitlesType)} for <code>SubtitlesType.SRT</code>
	 */
	public File toSRT(SubtitlesContainer container, File output) throws SubtitlesGenerationException, IOException
	{
		return factories.get(SubtitlesType.SRT).toFile(container, output);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(SubtitlesContainer, OutputStream, SubtitlesType) } for <code>SubtitlesType.SRT</code>
	 */
	public OutputStream toSRT(SubtitlesContainer container, OutputStream output) throws SubtitlesGenerationException, IOException
	{
		return factories.get(SubtitlesType.SRT).toStream(container, output);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(SubtitlesContainer, File, SubtitlesType)} for <code>SubtitlesType.ASS</code>
	 */
	public File toASS(SubtitlesContainer container, File output) throws SubtitlesGenerationException, IOException
	{
		return factories.get(SubtitlesType.ASS).toFile(container, output);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(SubtitlesContainer, OutputStream, SubtitlesType) } for <code>SubtitlesType.ASS</code>
	 */
	public OutputStream toASS(SubtitlesContainer container, OutputStream output) throws SubtitlesGenerationException, IOException
	{
		return factories.get(SubtitlesType.ASS).toStream(container, output);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(File, File, SubtitlesType)} for <code>SubtitlesType.SRT</code>
	 */
	public File toSRT(File input, File output) throws Subtitles4jException, IOException
	{
		return toSubtitlesType(input, output, SubtitlesType.SRT);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(InputStream, SubtitlesType, OutputStream, SubtitlesType)} for <code>SubtitlesType.SRT</code>
	 */
	public OutputStream toSRT(InputStream input, SubtitlesType inputType, OutputStream output) throws Subtitles4jException, IOException
	{
		return toSubtitlesType(input, inputType, output, SubtitlesType.SRT);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(File, File, SubtitlesType)} for <code>SubtitlesType.ASS</code>
	 */
	public File toASS(File input, File output) throws Subtitles4jException, IOException
	{
		return toSubtitlesType(input, output, SubtitlesType.ASS);
	}
	
	/**
	 * Equivalent to {@link #toSubtitlesType(InputStream, SubtitlesType, OutputStream, SubtitlesType)} for <code>SubtitlesType.ASS</code>
	 */
	public OutputStream toASS(InputStream input, SubtitlesType inputType, OutputStream output) throws Subtitles4jException, IOException
	{
		return toSubtitlesType(input, inputType, output, SubtitlesType.ASS);
	}
}