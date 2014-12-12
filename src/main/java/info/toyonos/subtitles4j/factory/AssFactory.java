package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class AssFactory extends AbstractFormatFactory
{
	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("H:mm:ss,SS");
	
	private static final String SCRIPT_INFO 	= "Script Info";
	private static final String V4_STYLE		= "V4 Styles";
	private static final String V4PLUS_STYLE	= "V4+ Styles";
	private static final String EVENTS 			= "Events";
	private static final String FORMAT			= "Format";
	private static final String DIALOGUE		= "Dialogue";
	
	private static final String FORMAT_LAYER	= "Layer";
	private static final String FORMAT_MARKED	= "Marked";
	private static final String FORMAT_START	= "Start";
	private static final String FORMAT_END		= "End";
	private static final String FORMAT_STYLE	= "Style";
	private static final String FORMAT_MARGINL	= "MarginL";
	private static final String FORMAT_MARGINR	= "MarginR";
	private static final String FORMAT_MARGINV	= "MarginV";
	private static final String FORMAT_EFFECT	= "Effect";
	private static final String FORMAT_TEXT		= "Text";
	
	//private PrintWriter subtitlesWriter;
	
	@Override
	public SubtitlesContainer fromFile(File input) throws MalformedFileException
	{
		SubtitlesContainer container = new SubtitlesContainer();

	    try
	    {
	    	Ini iniFile = new Ini(new FileReader(input));

	    	// TODO others sections
	    	
	    	// Captions
	    	Section eventsSection = iniFile.get(EVENTS);
	    	List<String> format = Arrays.asList(eventsSection.get(FORMAT).split(","));
	    	for (int i = 0; i < eventsSection.length(DIALOGUE); i++)
	    	{
	    		List<String> dialogue = Arrays.asList(eventsSection.get(DIALOGUE, i).split(","));
	    		long start, end;

	    		// Start index
	    		int idxStart = format.indexOf(FORMAT_START);
	    		if (idxStart == -1)
	    		{
	    			throw new MalformedFileException("Missing start timestamp !"); // TODO where ?
	    		}
	    		start = getMilliseconds(dialogue.get(idxStart));
	    		
	    		// End index
	    		int idxEnd = format.indexOf(FORMAT_END);
	    		if (idxEnd == -1)
	    		{
	    			throw new MalformedFileException("Missing end timestamp !"); // TODO where ?
	    		}
	    		end = getMilliseconds(dialogue.get(idxEnd));
	    	}

		    return container;
	    }
	    catch (IOException e)
	    {
	    	// TODO log
	    	e.printStackTrace();
	    	return null;
	    }
	}

	public File toFile(SubtitlesContainer container, File output)
	{
		// TODO
		return null;
	}
	
	@Override
	public void visit(SubtitlesContainer container)
	{
		// TODO
	}

	@Override
	public void visit(Caption caption)
	{
		// TODO
	}

	@Override
	protected SimpleDateFormat getTimestampDateFormat()
	{
		return TIMESTAMPS_SDF;
	}
}
