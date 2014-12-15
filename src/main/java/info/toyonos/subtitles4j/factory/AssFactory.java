package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

// TODO handle SSA

public class AssFactory extends AbstractFormatFactory
{
	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("H:mm:ss,SS");
	
	private static final String SCRIPT_INFO 	= "Script Info";
	//private static final String V4_STYLE		= "V4 Styles";
	private static final String V4PLUS_STYLE	= "V4+ Styles";
	private static final String EVENTS 			= "Events";
	private static final String FORMAT			= "Format";
	private static final String DIALOGUE		= "Dialogue";
	
	private static final String FORMAT_LAYER	= "Layer";
	//private static final String FORMAT_MARKED	= "Marked";
	private static final String FORMAT_START	= "Start";
	private static final String FORMAT_END		= "End";
	private static final String FORMAT_STYLE	= "Style";
	private static final String FORMAT_NAME		= "Name";
	private static final String FORMAT_MARGINL	= "MarginL";
	private static final String FORMAT_MARGINR	= "MarginR";
	private static final String FORMAT_MARGINV	= "MarginV";
	private static final String FORMAT_EFFECT	= "Effect";
	private static final String FORMAT_TEXT		= "Text";
	
	private static final String SEPARATOR = ": ";
	private static final String VALUE_SEPARATOR = ",";
	private static final String DEFAULT_STYLE = "Default";
	private static final String DEFAULT_MARGIN = "0000";
	
	private Ini iniFile = null;
	
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

    		// Start index
    		int idxStart = getIndex(format, FORMAT_START);
  
    		// End index
    		int idxEnd = getIndex(format, FORMAT_END);
    		
    		// Text
    		int idxText = getIndex(format, FORMAT_TEXT);
    		
    		for (int i = 0; i < eventsSection.length(DIALOGUE); i++)
	    	{
	    		List<String> dialogue = Arrays.asList(eventsSection.get(DIALOGUE, i).split(","));
	    		long start = getMilliseconds(dialogue.get(idxStart));
	    		long end = getMilliseconds(dialogue.get(idxEnd));
	    		List<String> subtitlesLines = Arrays.asList(dialogue.get(idxText).replaceAll("\\{.*?\\}", "").split("\\\\n|\\\\N"));
	    		
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
	}
	
	private int getIndex(List<String> format, String key) throws MalformedFileException
	{
		int idx = format.indexOf(key);
		if (idx == -1)
		{
			throw new MalformedFileException("[Events] : missing '" + key.toLowerCase() + "' key in format");
		}
		return idx;
	}

	@Override
	public void visit(SubtitlesContainer container)
	{
		subtitlesWriter.println("[" + EVENTS + "]");
		subtitlesWriter.println(
			FORMAT +
			SEPARATOR +
			StringUtils.join(new String[] {
				FORMAT_LAYER,
				FORMAT_START,
				FORMAT_END,
				FORMAT_STYLE,
				FORMAT_NAME,
				FORMAT_MARGINL,
				FORMAT_MARGINR, 
				FORMAT_MARGINV,
				FORMAT_EFFECT,
				FORMAT_TEXT},
			", ")
		);
	}

	@Override
	public void visit(Caption caption)
	{
		subtitlesWriter.println(DIALOGUE + SEPARATOR);
		subtitlesWriter.print(0); // Layer
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(formatMilliseconds(caption.getStart())); // Start
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(formatMilliseconds(caption.getEnd())); // End
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(DEFAULT_STYLE); // Style
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(""); // Name
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(DEFAULT_MARGIN); // MarginL
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(DEFAULT_MARGIN); // MarginR
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(DEFAULT_MARGIN); // MarginV
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(""); // Effect
		subtitlesWriter.print(VALUE_SEPARATOR);
		for (String line : caption.getLines()) subtitlesWriter.print(line + "\\n");
	}

	@Override
	protected SimpleDateFormat getTimestampDateFormat()
	{
		return TIMESTAMPS_SDF;
	}
}