package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface SubtitlesFactory
{
	public SubtitlesContainer fromFile(File input) throws MalformedSubtitlesException;
	
	public SubtitlesContainer fromStream(InputStream input) throws MalformedSubtitlesException;
	
	public File toFile(SubtitlesContainer container, File output) throws SubtitlesGenerationException;
	
	public OutputStream toStream(SubtitlesContainer container, OutputStream output) throws SubtitlesGenerationException;
}
