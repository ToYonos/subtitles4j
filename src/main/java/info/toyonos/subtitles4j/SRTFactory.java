package info.toyonos.subtitles4j;

import info.toyonos.subtitles4j.SubtitlesContainer.Caption;

import java.io.File;


public class SRTFactory implements SubtitlesVisitor, SubtitlesFactory
{
	@Override
	public SubtitlesContainer fromFile(File input)
	{
		return null;
	}
	
	public File toFile(SubtitlesContainer container)
	{
		// TODO use visitor
		return null;
	}
	
	@Override
	public void visit(SubtitlesContainer container)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Caption caption)
	{
		// TODO Auto-generated method stub
	}
}
