package info.toyonos.subtitles4j.model;

import info.toyonos.subtitles4j.factory.SubtitlesVisitor;

import java.util.ArrayList;
import java.util.List;

public class SubtitlesContainer implements Visitable
{
	private String title;
	private String author;
	
	private List<Caption> captions;

	public SubtitlesContainer()
	{
		captions = new ArrayList<SubtitlesContainer.Caption>();
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
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
		private long start;
		private long end;
		private List<String> lines;

		public Caption(long start, long end, List<String> lines)
		{
			this.start = start;
			this.end = end;
			this.lines = lines;
		}
		
		public long getStart()
		{
			return start;
		}

		public void setStart(long start)
		{
			this.start = start;
		}

		public long getEnd()
		{
			return end;
		}

		public void setEnd(long end)
		{
			this.end = end;
		}

		public List<String> getLines()
		{
			return lines;
		}

		public void setLines(List<String> lines)
		{
			this.lines = lines;
		}

		@Override
		public void accept(SubtitlesVisitor visitor)
		{
			visitor.visit(this);
		}
	}
}