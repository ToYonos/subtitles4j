package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SubtitlesFactory
{
	/**
	 * <p>Construct a <code>SubtitlesContainer</code> from an input <code>File</code><p>
	 * @param input The input <code>File</code> to be parsed
	 * @return the resulting <code>SubtitlesContainer</code>
	 * @throws MalformedSubtitlesException if the input is malformed
	 * @throws IOException if any IO error occurs
	 */
	public SubtitlesContainer fromFile(File input) throws MalformedSubtitlesException, IOException;
	
	/**
	 * <p>Construct a <code>SubtitlesContainer</code> from an <code>InputStream</code><p>
	 * @param input The <code>InputStream</code> to be parsed
	 * @return the resulting <code>SubtitlesContainer</code>
	 * @throws Subtitles4jException if the input is malformed
	 * @throws IOException if any IO error occurs
	 */
	public SubtitlesContainer fromStream(InputStream input) throws MalformedSubtitlesException, IOException;
	
	/**
	 * Convert a <code>SubtitlesContainer</code> to a <code>File</code> 
	 * @param container the <code>SubtitlesContainer</code> to be converted
	 * @param output the output <code>File</code> 
	 * @return the generated output <code>File</code>
	 * @throws SubtitlesGenerationException if an error occurs during the generation
	 * @throws IOException if any IO error occurs
	 */
	public File toFile(SubtitlesContainer container, File output) throws SubtitlesGenerationException, IOException;
	
	/**
	 * Convert a <code>SubtitlesContainer</code> to a <code>OutputStream</code> 
	 * @param container the <code>SubtitlesContainer</code> to be converted
	 * @param output the <code>OutputStream</code> 
	 * @return the generated <code>OutputStream</code>
	 * @throws SubtitlesGenerationException if an error occurs during the generation
	 */
	public OutputStream toStream(SubtitlesContainer container, OutputStream output) throws SubtitlesGenerationException;
}
