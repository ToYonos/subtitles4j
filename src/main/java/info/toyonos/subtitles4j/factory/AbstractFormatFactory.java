package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;
import info.toyonos.subtitles4j.model.SubtitlesContainer.StyleProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFormatFactory implements SubtitlesVisitor, SubtitlesFactory
{
	private static final String UTF8_BOM = "\uFEFF";
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractFormatFactory.class);
	
	protected PrintWriter subtitlesWriter;
	
	/**
	 * @return The <code>SimpleDateFormat</code> using by timestamps
	 */
	protected abstract SimpleDateFormat getTimestampDateFormat();
	
	protected MalformedSubtitlesException malformedFileException(String content, Integer lineNumber, Object... args)
	{
		return lineNumber != null ?
			new MalformedSubtitlesException(String.format(content + " at line %d", ArrayUtils.addAll(args, lineNumber))) :
			new MalformedSubtitlesException(String.format(content, args));
	}
	
	protected long getMilliseconds(String timestamp, Integer lineNumber) throws MalformedSubtitlesException
	{
		try
		{
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(getTimestampDateFormat().parse(timestamp)); 
			return calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
		}
		catch (ParseException e)
		{
			throw malformedFileException("Malformed timestamp '%s'", lineNumber, timestamp);
		}
	}
	
	protected long getMilliseconds(String timestamp) throws MalformedSubtitlesException
	{
		return getMilliseconds(timestamp, null);
	}
	
	protected String formatMilliseconds(long millis)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(millis - calendar.get(Calendar.ZONE_OFFSET));
		return getTimestampDateFormat().format(calendar.getTime());
	}
	
	protected String removeUTF8BOM(String s)
	{
		if (s.startsWith(UTF8_BOM))
		{
			s = s.substring(1);
		}
		return s;
	}
	
	@Override
	public SubtitlesContainer fromFile(File input) throws MalformedSubtitlesException, IOException
	{
		return fromStream(new FileInputStream(input));
	}
	
	@Override
	public File toFile(SubtitlesContainer container, File output) throws SubtitlesGenerationException, IOException
	{
		toStream(container, new FileOutputStream(output));
		return output;
	}
	
	public OutputStream toStream(SubtitlesContainer container, OutputStream output) throws SubtitlesGenerationException
	{
		try
		{
			subtitlesWriter = new PrintWriter(output);
			container.accept(this);
			return output;
	    }
		finally
		{
			logger.trace("SubtitlesContainer has been transformed with success");
			subtitlesWriter.close();
			subtitlesWriter = null;
	    } 
	}
	
	@Override
	public void visit(SubtitlesContainer container) throws SubtitlesGenerationException
	{
		logger.trace("Visiting SubtitlesContainer");
		if (subtitlesWriter == null) throw new IllegalStateException("You can't call visit directly from " + this.getClass().getSimpleName());
	}

	@Override
	public void visit(Caption caption) throws SubtitlesGenerationException
	{
		logger.trace("Visiting Caption");
		if (subtitlesWriter == null) throw new IllegalStateException("You can't call visit directly from " + this.getClass().getSimpleName());
	}
	
	protected static class StyleMapping
	{
		public String name;
		public boolean mandatory;
		public String defaultValue;
		public StyleProperty mirroredProperty;

		public StyleMapping(String name, String defaultValue)
		{
			this.name = name;
			this.mandatory = false;
			this.defaultValue = defaultValue;
			this.mirroredProperty = null;
		}
		
		public StyleMapping(String name, StyleProperty mirroredProperty)
		{
			this.name = name;
			this.mandatory = false;
			this.defaultValue = null;
			this.mirroredProperty = mirroredProperty;
		}
		
		public StyleMapping(String name)
		{
			this.name = name;
			this.mandatory = true;
			this.defaultValue = null;
			this.mirroredProperty = null;
		}

		@Override
		public String toString()
		{
			return name;
		}
		
		@Override
		public int hashCode()
		{
			return name.hashCode();
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null) return false;
			
			if (obj instanceof StyleMapping)
			{
				StyleMapping mapping = (StyleMapping) obj;
				return name == null ? mapping.name == null : name.equals( mapping.name);
			}
			else if (obj instanceof String)
			{
				String mapping = (String) obj;
				return name == null ? mapping == null : name.equals(mapping);
			}
			return false;
		}	
	}
}
