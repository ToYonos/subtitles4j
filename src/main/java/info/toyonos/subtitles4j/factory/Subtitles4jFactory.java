package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.helper.Subtitles4jUtils;
import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.ByteArrayOutputStream;
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
	
	/**
	 * <p>Construct a <code>SubtitlesContainer</code> from an <code>InputStream</code><p>
	 * @param input The <code>InputStream</code> to be parsed
	 * @param inputType the <code>SubtitlesType</code> of the input
	 * @return the resulting <code>SubtitlesContainer</code>
	 * @throws Subtitles4jException if the input is malformed, the extension not supported
	 * @throws IOException if any IO error occurs
	 */
	public SubtitlesContainer fromStream(InputStream input, SubtitlesType inputType) throws MalformedSubtitlesException, IOException 
	{
		if (inputType != null)
		{
			return factories.get(inputType).fromStream(input);
		}
		return null;
	}
	
	/**
	 * Convert a <code>SubtitlesContainer</code> to a <code>File</code>, for a target <code>SubtitlesType</code> 
	 * @param container the <code>SubtitlesContainer</code> to be converted
	 * @param ouput the output <code>File</code>
	 * @param type the target <code>SubtitlesType</code> 
	 * @return the generated output <code>File</code>
	 * @throws Subtitles4jException if an error occurs during the generation
	 * @throws IOException if any IO error occurs
	 */
	public File toSubtitlesType(SubtitlesContainer container, File ouput, SubtitlesType type) throws SubtitlesGenerationException, IOException
	{
		return factories.get(type).toFile(container, ouput);
	}
	
	/**
	 * Convert a <code>SubtitlesContainer</code> to a <code>OutputStream</code>, for a target <code>SubtitlesType</code> 
	 * @param container the <code>SubtitlesContainer</code> to be converted
	 * @param ouput the <code>OutputStream</code>
	 * @param type the target <code>SubtitlesType</code> 
	 * @return the generated <code>OutputStream</code>
	 * @throws Subtitles4jException if an error occurs during the generation
	 */
	public OutputStream toSubtitlesType(SubtitlesContainer container, OutputStream ouput, SubtitlesType type) throws SubtitlesGenerationException
	{
		return factories.get(type).toStream(container, ouput);
	}
	
	/**
	 * Convert an input <code>File</code> to a target <code>SubtitlesType</code> 
	 * @param input the input <code>File</code>
	 * @param outputType the new wished <code>SubtitlesType</code> 
	 * @return the modified <code>File</code> 
	 * @throws Subtitles4jException if an error occurs during the conversion
	 * @throws IOException if any IO error occurs
	 */
	public File toSubtitlesType(File input, SubtitlesType outputType) throws Subtitles4jException, IOException
	{
		String ext = FilenameUtils.getExtension(input.getName());
		if (outputType.hasExtension(ext))
		{
			// Same input/output extension
			return input;
		}
		else
		{
			return factories.get(outputType).toFile(
				fromFile(input),
				new File(
					input.getParent(),
					FilenameUtils.getBaseName(input.getName()) + "." + outputType.getExtensions().iterator().next()
				)
			);
		}
	}
	
	/**
	 * Convert an <code>InputStream</code> to a target <code>SubtitlesType</code> 
	 * @param input the <code>InputStream</code>
	 * @param inputType the <code>SubtitlesType</code> of the input 
	 * @param outputType the new wished <code>SubtitlesType</code>
	 * @return the converted code>OutputStream</code>
	 * @throws Subtitles4jException if an error occurs during the conversion
	 * @throws IOException if any IO error occurs
	 */
	public OutputStream toSubtitlesType(InputStream input, SubtitlesType inputType, SubtitlesType outputType) throws Subtitles4jException, IOException
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
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
	
	public File toSRT(SubtitlesContainer container, File ouput) throws SubtitlesGenerationException, IOException
	{
		return factories.get(SubtitlesType.SRT).toFile(container, ouput);
	}
	
	public File toASS(SubtitlesContainer container, File ouput) throws SubtitlesGenerationException, IOException
	{
		return factories.get(SubtitlesType.ASS).toFile(container, ouput);
	}
	
	public File toSRT(File input) throws Subtitles4jException, IOException
	{
		return toSubtitlesType(input, SubtitlesType.SRT);
	}
	
	public File toASS(File input) throws Subtitles4jException, IOException
	{
		return toSubtitlesType(input, SubtitlesType.ASS);
	}
	
	// TODO stream methods
}