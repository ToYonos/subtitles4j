package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.StyleProperty;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractFormatFactory implements SubtitlesVisitor, SubtitlesFactory
{
	private static final String UTF8_BOM = "\uFEFF";
	
	protected PrintWriter subtitlesWriter;
	
	/**
	 * @return The <code>SimpleDateFormat</code> using by timestamps
	 */
	protected abstract SimpleDateFormat getTimestampDateFormat();
	
	/**
	 * @return The <code>Map</code> which associate a <code>StyleProperty</code> with a label
	 */
	protected Map<StyleProperty, String> getStyleMapping()
	{
		// No style by default
		return null;
	}
	
	protected MalformedFileException malformedFileException(String content, Integer lineNumber, Object... args)
	{
		return lineNumber != null ?
			new MalformedFileException(String.format(content + " at line %d", ArrayUtils.addAll(args, lineNumber))) :
			new MalformedFileException(String.format(content, args));
	}
	
	protected long getMilliseconds(String timestamp, Integer lineNumber) throws MalformedFileException
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
	
	protected long getMilliseconds(String timestamp) throws MalformedFileException
	{
		return getMilliseconds(timestamp, null);
	}
	
	protected String formatMilliseconds(long millis)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(millis - calendar.getTimeZone().getOffset(calendar.getTimeInMillis()));
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
	
	public File toFile(SubtitlesContainer container, File output)
	{
		try
		{
			subtitlesWriter = new PrintWriter(output);
			container.accept(this);
			return output;
	    }
	    catch (IOException e)
	    {
	    	// TODO log
	    	return null;
	    }
		finally
		{
			subtitlesWriter.close();
			subtitlesWriter = null;
	    } 
	}
}
