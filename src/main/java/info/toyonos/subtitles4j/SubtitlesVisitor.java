package info.toyonos.subtitles4j;

public interface SubtitlesVisitor
{
	public void visit(SubtitlesContainer container);
	
	public void visit(SubtitlesContainer.Caption caption);
}
