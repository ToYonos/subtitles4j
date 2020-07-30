package info.toyonos.subtitles4j.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import info.toyonos.subtitles4j.factory.SubtitlesGenerationException;
import info.toyonos.subtitles4j.factory.SubtitlesVisitor;

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
		styles = new LinkedHashMap<String, Map<SubtitlesContainer.StyleProperty, String>>();
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

	/**
	 * Shift the timestamps of the captions
	 * @param millis the time in millisecond to be added to each caption, can be negative
	 */
	public void shiftTime(int millis)
	{
		for (Caption caption : captions)
		{
			caption.addStart(millis);
			caption.addEnd(millis);
		}
	}
	
	/**
	 * Remove all the captions containing the specified regular expression 
	 * @param regex the regular expression to which captions are to be matched
	 * @param caseSensitive true for a case sensitive operation, false otherwise
	 * @return the number of captions removed
	 */
	public int remove(String regex, boolean caseSensitive)
	{
		int removedItems = 0;
		for (Iterator<Caption> i = captions.iterator(); i.hasNext();)
		{
			for (String line : i.next().lines)
			{
				Pattern p = caseSensitive ? Pattern.compile(regex) : Pattern.compile(regex, Pattern.CASE_INSENSITIVE); 
				if (p.matcher(line).find())
				{
					i.remove();
					removedItems++;
					break;
				}
			}
		}
		return removedItems;
	}
	
	@Override
	public void accept(SubtitlesVisitor visitor) throws SubtitlesGenerationException
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
		public void accept(SubtitlesVisitor visitor) throws SubtitlesGenerationException
		{
			visitor.visit(this);
		}
	}
}