package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SRTFactory extends AbstractFormatFactory
{
	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("HH:mm:ss,SSS");

	private static final String TIMESTAMPS_SEPARATOR = " --> ";
	
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
	
	@Override
	public void visit(SubtitlesContainer container)
	{
		if (subtitlesWriter == null) throw new IllegalStateException("You can't call visit directly from " + this.getClass().getSimpleName());
		
		// The index starts at 1
		index = 1;
	}

	@Override
	public void visit(Caption caption)
	{
		if (subtitlesWriter == null) throw new IllegalStateException("You can't call visit directly from " + this.getClass().getSimpleName());
		
		// Writing the caption
		subtitlesWriter.append(String.valueOf(index++))
		.append(System.getProperty("line.separator"))
		.append(formatMilliseconds(caption.getStart()))
		.append(TIMESTAMPS_SEPARATOR)
		.append(formatMilliseconds(caption.getEnd()))
		.append(System.getProperty("line.separator"));
		for (String line : caption.getLines()) subtitlesWriter.println(line);
		subtitlesWriter.println("");
	}

	@Override
	protected SimpleDateFormat getTimestampDateFormat()
	{
		return TIMESTAMPS_SDF;
	}
}
