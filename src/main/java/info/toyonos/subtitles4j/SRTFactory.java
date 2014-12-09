package info.toyonos.subtitles4j;

import info.toyonos.subtitles4j.SubtitlesContainer.Caption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

public class SRTFactory implements SubtitlesVisitor, SubtitlesFactory
{
	private static final String TIMESTAMPS_SEPARATOR = Pattern.quote("-->");

	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("HH:mm:ss,SSS");
	
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
	        	int start, end;
	        	line = line.trim();
	        	
	        	// Empty line : ignored
	        	if (line.isEmpty()) continue;
	        	
	        	// Index verification
	        	if (line.matches("[0-9]+)"))
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
	    	return null;
	    }
	    finally
	    {
	       try { if (reader != null) reader.close(); } catch (IOException e) {}
	    } 
	}
	
	private MalformedFileException malformedFileException(LineNumberReader reader, String content, Object... args)
	{
		return new MalformedFileException(String.format(content + " at line %d", args, reader.getLineNumber() + 1));
	}

	private MalformedFileException unexpectedEndOfFile(String message) 
	{
		return new MalformedFileException("Unexpected end of file : " + message);
	}
	
	private int getMilliseconds(LineNumberReader reader, String timestamp) throws MalformedFileException
	{
		try
		{
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(TIMESTAMPS_SDF.parse(timestamp)); 
			return calendar.get(Calendar.MILLISECOND);
		}
		catch (ParseException e)
		{
			throw malformedFileException(reader, "Malformed timestamp '%s'", e, timestamp);
		}
	}
	
	public File toFile(SubtitlesContainer container)
	{
		// TODO use visitor
		return null;
	}
	
	@Override
	public void visit(SubtitlesContainer container)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Caption caption)
	{
		// TODO Auto-generated method stub
	}
}
