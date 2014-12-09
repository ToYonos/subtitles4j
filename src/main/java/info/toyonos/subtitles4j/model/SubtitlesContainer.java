package info.toyonos.subtitles4j.model;

import info.toyonos.subtitles4j.factory.SubtitlesVisitor;

import java.util.ArrayList;
import java.util.List;

public class SubtitlesContainer implements Visitable
{
	private List<Caption> captions;

	public SubtitlesContainer()
	{
		captions = new ArrayList<SubtitlesContainer.Caption>();
	}
	
	public void addCaption(long start, long end, List<String> lines)
	{
		captions.add(new Caption(start, end, lines));
	}

	public List<Caption> getCaptions()
	{
		return captions;
	}

	@Override
	public void accept(SubtitlesVisitor visitor)
	{
		visitor.visit(this);
		for (Caption caption : captions)
		{
			caption.accept(visitor);
		}
	}
	
	public class Caption implements Visitable
	{
		public long start;
		public long end;
		public List<String> lines;
		
		public Caption(long start, long end, List<String> lines)
		{
			this.start = start;
			this.end = end;
			this.lines = lines;
		}

		@Override
		public void accept(SubtitlesVisitor visitor)
		{
			visitor.visit(this);
		}
	}
}