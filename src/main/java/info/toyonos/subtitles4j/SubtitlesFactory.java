package info.toyonos.subtitles4j;

import java.io.File;

public interface SubtitlesFactory
{
	public SubtitlesContainer fromFile(File input) throws MalformedFileException;
	
	public File toFile(SubtitlesContainer container);
}
