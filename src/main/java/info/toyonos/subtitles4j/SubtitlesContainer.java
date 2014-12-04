package info.toyonos.subtitles4j;

import java.util.List;

public class SubtitlesContainer implements Visitable
{
	private List<Caption> captions;

	public void addCaption(int start, int end, String content)
	{
		captions.add(new Caption(start, end, content));
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
		public String content;
		
		public Caption(int start, int end, String content)
		{
			this.start = start;
			this.end = end;
			this.content = content;
		}

		@Override
		public void accept(SubtitlesVisitor visitor)
		{
			visitor.visit(this);
		}
	}
}