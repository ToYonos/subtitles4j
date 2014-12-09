package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

public interface SubtitlesVisitor
{
	public void visit(SubtitlesContainer container);
	
	public void visit(SubtitlesContainer.Caption caption);
}
