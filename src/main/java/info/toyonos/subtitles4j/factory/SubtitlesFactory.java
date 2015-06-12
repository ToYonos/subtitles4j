package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SubtitlesFactory
{
	public SubtitlesContainer fromFile(File input) throws MalformedSubtitlesException, IOException;
	
	public SubtitlesContainer fromStream(InputStream input) throws MalformedSubtitlesException, IOException;
	
	public File toFile(SubtitlesContainer container, File output) throws SubtitlesGenerationException, IOException;
	
	public OutputStream toStream(SubtitlesContainer container, OutputStream output) throws SubtitlesGenerationException;
	
	// TODO javadoc
}
