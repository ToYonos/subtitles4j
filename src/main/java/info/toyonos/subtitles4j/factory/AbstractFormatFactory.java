package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractFormatFactory implements SubtitlesVisitor, SubtitlesFactory
{
	private static final String UTF8_BOM = "\uFEFF";
	
	protected PrintWriter subtitlesWriter;
	
	/**
	 * @return The <code>SimpleDateFormat</code> using by timestamps
	 */
	protected abstract SimpleDateFormat getTimestampDateFormat();
	
	protected MalformedFileException malformedFileException(LineNumberReader reader, String content, Object... args)
	{
		return reader != null ?
			new MalformedFileException(String.format(content + " at line %d", ArrayUtils.addAll(args, reader.getLineNumber()))) :
			new MalformedFileException(String.format(content, args));
	}

	protected MalformedFileException unexpectedEndOfFile(String message) 
	{
		return new MalformedFileException("Unexpected end of file : " + message);
	}
	
	protected long getMilliseconds(LineNumberReader reader, String timestamp) throws MalformedFileException
	{
		try
		{
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(getTimestampDateFormat().parse(timestamp)); 
			return calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
		}
		catch (ParseException e)
		{
			throw malformedFileException(reader, "Malformed timestamp '%s'", timestamp);
		}
	}
	
	protected long getMilliseconds(String timestamp) throws MalformedFileException
	{
		return getMilliseconds(null, timestamp);
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
