package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SRTFactory extends AbstractFormatFactory
{
	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("HH:mm:ss,SSS");

	private static final String TIMESTAMPS_SEPARATOR = " --> ";
	
	private int index;
	
	protected SRTFactory() {}
	
	@Override
	public SubtitlesContainer fromStream(InputStream input) throws MalformedSubtitlesException, IOException
	{
		SubtitlesContainer container = new SubtitlesContainer();

	    try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(input)))
	    {
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
	        		if (foundIndex != i++) throw malformedFileException("Unexpected index %d", reader.getLineNumber(), foundIndex);
	        	}

	        	// Timestamps
	        	String timestamps = reader.readLine();
	        	if (timestamps == null) throw unexpectedEndOfFile("timestamps declaration was expected here");

	        	String[] timestampsArray = timestamps.split(TIMESTAMPS_SEPARATOR);
	        	start = getMilliseconds(timestampsArray[0], reader.getLineNumber());
	        	end = getMilliseconds(timestampsArray[1], reader.getLineNumber());
	        	
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

	        logger.trace("The subtitles source has been read with success, {} captions read", container.getCaptions().size());
	        
		    return container;
	    }
	}
		
	private MalformedSubtitlesException unexpectedEndOfFile(String message) 
	{
		return new MalformedSubtitlesException("Unexpected end of file : " + message);
	}
	
	@Override
	public void visit(SubtitlesContainer container) throws SubtitlesGenerationException
	{
		super.visit(container);

		// The index starts at 1
		index = 1;
	}

	@Override
	public void visit(Caption caption) throws SubtitlesGenerationException
	{
		super.visit(caption);
		
		// Writing the caption
		subtitlesWriter.append(String.valueOf(index++))
		.append(System.getProperty("line.separator"))
		.append(formatMilliseconds(caption.getStart()))
		.append(TIMESTAMPS_SEPARATOR)
		.append(formatMilliseconds(caption.getEnd()))
		.append(System.getProperty("line.separator"));
		subtitlesWriter.println(StringUtils.join(caption.getLines(), '\n'));
		subtitlesWriter.println();
	}

	@Override
	protected SimpleDateFormat getTimestampDateFormat()
	{
		return TIMESTAMPS_SDF;
	}
}
