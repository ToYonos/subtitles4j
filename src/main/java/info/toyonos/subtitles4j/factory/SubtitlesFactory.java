package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;

public interface SubtitlesFactory
{
	public SubtitlesContainer fromFile(File input) throws MalformedFileException;
	
	public File toFile(SubtitlesContainer container, File output);
}
