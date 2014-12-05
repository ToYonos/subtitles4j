package info.toyonos.subtitles4j;

import java.util.List;

public class SubtitlesContainer implements Visitable
{
	private List<Caption> captions;

	public void addCaption(int start, int end, List<String> lines)
	{
		captions.add(new Caption(start, end, lines));
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
		public int start;
		public int end;
		public List<String> lines;
		
		public Caption(int start, int end, List<String> lines)
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