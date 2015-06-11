package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

public interface SubtitlesVisitor
{
	public void visit(SubtitlesContainer container) throws SubtitlesGenerationException;
	
	public void visit(SubtitlesContainer.Caption caption) throws SubtitlesGenerationException;
}
