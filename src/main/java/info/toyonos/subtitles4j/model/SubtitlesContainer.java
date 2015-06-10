package info.toyonos.subtitles4j.model;

import info.toyonos.subtitles4j.factory.FileGenerationException;
import info.toyonos.subtitles4j.factory.SubtitlesVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubtitlesContainer implements Visitable
{
	public enum StyleProperty
	{
		NAME,
		FONT_NAME,
		FONT_SIZE,
		PRIMARY_COLOR,
		SECONDARY_COLOR,
		OUTLINE_COLOR,
		BACK_COLOR,
		BOLD,
		ITALIC,
		UNDERLINE,
		STRIKEOUT,
		SCALE_X,
		SCALE_Y,
		SPACING,
		ANGLE,
		BORDER_STYLE,
		OUTLINE,
		SHADOW,
		ALIGNMENT, // (1-3 sub, 4-6 mid, 7-9 top)
		MARGIN_L,
		MARGIN_R,
		MARGIN_V,
		ENCODING;
	}
	
	
	private String title;
	private String author;
	
	private Map<String, Map<StyleProperty, String>> styles;
	
	private List<Caption> captions;

	public SubtitlesContainer()
	{
		captions = new ArrayList<SubtitlesContainer.Caption>();
		styles = new HashMap<String, Map<SubtitlesContainer.StyleProperty, String>>();
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
	
	public Map<String, Map<StyleProperty, String>> getStyles()
	{
		return styles;
	}

	public void addCaption(long start, long end, List<String> lines)
	{
		captions.add(new Caption(start, end, lines));
	}
	
	public void addCaption(long start, long end, String styleKey, List<String> lines)
	{
		captions.add(new Caption(start, end, styleKey, lines));
	}

	public List<Caption> getCaptions()
	{
		return captions;
	}

	public void shiftTime(int millis)
	{
		for (Caption caption : captions)
		{
			caption.addStart(millis);
			caption.addEnd(millis);
		}
	}
	
	@Override
	public void accept(SubtitlesVisitor visitor) throws FileGenerationException
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
		private String styleKey;
		private List<String> lines;

		public Caption(long start, long end, List<String> lines)
		{
			this.start = start;
			this.end = end;
			this.styleKey = null;
			this.lines = lines;
		}
		
		public Caption(long start, long end, String styleKey, List<String> lines)
		{
			this.start = start;
			this.end = end;
			this.styleKey = styleKey;
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
		
		public void addStart(long millis)
		{
			start += millis;
			if (start < 0) start = 0; 
		}

		public long getEnd()
		{
			return end;
		}

		public void setEnd(long end)
		{
			this.end = end;
		}
		
		public void addEnd(long millis)
		{
			end += millis;
			if (end < 0) end = 0; 
		}

		public String getStyleKey()
		{
			return styleKey;
		}

		public void setStyleKey(String styleKey)
		{
			this.styleKey = styleKey;
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
		public void accept(SubtitlesVisitor visitor) throws FileGenerationException
		{
			visitor.visit(this);
		}
	}
}