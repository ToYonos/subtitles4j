package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

public class SRTFactory implements SubtitlesVisitor, SubtitlesFactory
{
	private static final String TIMESTAMPS_SEPARATOR = Pattern.quote(" --> ");
	private static final String UTF8_BOM = "\uFEFF";

	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("HH:mm:ss,SSS");
	
	private PrintWriter subtitlesWriter;
	private int index;
	
	@Override
	public SubtitlesContainer fromFile(File input) throws MalformedFileException
	{
		SubtitlesContainer container = new SubtitlesContainer();
		LineNumberReader reader = null;
	    try
	    {
	    	reader = new LineNumberReader(new FileReader(input));

	        String line = null;
	        int i = 1;
	        
	        while ((line = reader.readLine()) != null)
	        {
	        	long start, end;
	        	line = removeUTF8BOM(line.trim());
	        	
	        	// Empty line : ignored
	        	if (line.isEmpty()) continue;
	        	
	        	// Index verification
	        	if (line.matches("[0-9]+"))
	        	{
	        		int foundIndex = Integer.parseInt(line);
	        		if (foundIndex != i++) throw malformedFileException(reader, "Unexpected index %d", foundIndex);
	        	}
	        	
	        	// Timestamps
	        	String timestamps = reader.readLine();
	        	if (timestamps == null) throw unexpectedEndOfFile("timestamps declaration was expected here");

	        	String[] timestampsArray = timestamps.split(TIMESTAMPS_SEPARATOR);
	        	start = getMilliseconds(reader, timestampsArray[0]);
	        	end = getMilliseconds(reader, timestampsArray[1]);
	        	
	        	// Text content
	        	List<String> subtitlesLines = new ArrayList<String>();
	        	String subtitlesLine = null;
	        	while ((subtitlesLine = reader.readLine()) != null && !subtitlesLine.trim().isEmpty())
	        	{
	        		subtitlesLines.add(subtitlesLine.trim());
	        	}
	        	
	        	// Adding the caption
	        	container.addCaption(start, end, subtitlesLines);
	        }

		    return container;
	    }
	    catch (IOException e)
	    {
	    	// TODO log
	    	e.printStackTrace();
	    	return null;
	    }
	    finally
	    {
	       try { if (reader != null) reader.close(); } catch (IOException e) {}
	    } 
	}
	
	private MalformedFileException malformedFileException(LineNumberReader reader, String content, Object... args)
	{
		return new MalformedFileException(String.format(content + " at line %d", ArrayUtils.addAll(args, reader.getLineNumber())));
	}

	private MalformedFileException unexpectedEndOfFile(String message) 
	{
		return new MalformedFileException("Unexpected end of file : " + message);
	}
	
	private long getMilliseconds(LineNumberReader reader, String timestamp) throws MalformedFileException
	{
		try
		{
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(TIMESTAMPS_SDF.parse(timestamp)); 
			return calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
		}
		catch (ParseException e)
		{
			throw malformedFileException(reader, "Malformed timestamp '%s'", e, timestamp);
		}
	}
	
	private String formatMilliseconds(long millis)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(millis - calendar.getTimeZone().getOffset(calendar.getTimeInMillis()));
		return TIMESTAMPS_SDF.format(calendar.getTime());
	}
	
	private String removeUTF8BOM(String s)
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
			index = 1;
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
			index = 0;
	    } 
	}
	
	@Override
	public void visit(SubtitlesContainer container)
	{
		if (subtitlesWriter == null) throw new IllegalStateException("You can't call visit directly from " + this.getClass().getSimpleName());
		
		// Nothing to do here
	}

	@Override
	public void visit(Caption caption)
	{
		if (subtitlesWriter == null) throw new IllegalStateException("You can't call visit directly from " + this.getClass().getSimpleName());
		
		// Writing the caption
		subtitlesWriter.append(String.valueOf(index++))
		.append(formatMilliseconds(caption.getStart()))
		.append(TIMESTAMPS_SEPARATOR)
		.append(formatMilliseconds(caption.getEnd()));
		for (String line : caption.getLines()) subtitlesWriter.println(line);
		subtitlesWriter.println("");
	}
}
