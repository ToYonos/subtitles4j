package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;
import info.toyonos.subtitles4j.model.SubtitlesContainer.StyleProperty;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

// TODO handle SSA

public class ASSFactory extends AbstractFormatFactory
{
	private static final SimpleDateFormat TIMESTAMPS_SDF = new SimpleDateFormat("H:mm:ss:SSS");
	
	private static final String SCRIPT_INFO 		= "Script Info";
	//private static final String V4_STYLE			= "V4 Styles";
	private static final String V4PLUS_STYLE		= "V4+ Styles";
	private static final String EVENTS 				= "Events";
	private static final String FORMAT				= "Format";
	private static final String DIALOGUE			= "Dialogue";
	private static final String STYLE				= "Style";

	private static final String SCRIPT_INFO_TITLE	= "Title";
	private static final String SCRIPT_INFO_AUTHOR	= "Original Script";
	private static final String SCRIPT_INFO_TYPE	= "ScriptType";
	private static final String SCRIPT_INFO_TIMER	= "Timer";
	
	private static final String FORMAT_LAYER		= "Layer";
	//private static final String FORMAT_MARKED		= "Marked";
	private static final String FORMAT_START		= "Start";
	private static final String FORMAT_END			= "End";
	private static final String FORMAT_STYLE		= "Style";
	private static final String FORMAT_NAME			= "Name";
	private static final String FORMAT_MARGINL		= "MarginL";
	private static final String FORMAT_MARGINR		= "MarginR";
	private static final String FORMAT_MARGINV		= "MarginV";
	private static final String FORMAT_EFFECT		= "Effect";
	private static final String FORMAT_TEXT			= "Text";
	
	private static final String SEPARATOR = ": ";
	private static final String VALUE_SEPARATOR = ",";
	private static final String SCRIPT_TYPE = "v4.00+";
	private static final String DEFAULT_STYLE = "Default";
	private static final String DEFAULT_MARGIN = "0000";

	private static final BiMap<StyleProperty, String> STYLE_MAPPING = HashBiMap.create();
	static
	{
		STYLE_MAPPING.put(StyleProperty.NAME, "Name");
		STYLE_MAPPING.put(StyleProperty.FONT_NAME, "Fontname");
		STYLE_MAPPING.put(StyleProperty.FONT_SIZE, "Fontsize");
		STYLE_MAPPING.put(StyleProperty.PRIMARY_COLOR, "PrimaryColour");
		STYLE_MAPPING.put(StyleProperty.SECONDARY_COLOR, "SecondaryColour");
		STYLE_MAPPING.put(StyleProperty.OUTLINE_COLOR, "OutlineColour");
		STYLE_MAPPING.put(StyleProperty.BACK_COLOR, "BackColour");
		STYLE_MAPPING.put(StyleProperty.BOLD, "Bold");
		STYLE_MAPPING.put(StyleProperty.ITALIC, "Italic");
		STYLE_MAPPING.put(StyleProperty.UNDERLINE, "Underline");
		STYLE_MAPPING.put(StyleProperty.STRIKEOUT, "StrikeOut");
		STYLE_MAPPING.put(StyleProperty.SCALE_X, "ScaleX");
		STYLE_MAPPING.put(StyleProperty.SCALE_Y, "ScaleY");
		STYLE_MAPPING.put(StyleProperty.SPACING, "Spacing");
		STYLE_MAPPING.put(StyleProperty.ANGLE, "ANGLE");
		STYLE_MAPPING.put(StyleProperty.BORDER_STYLE, "BorderStyle");
		STYLE_MAPPING.put(StyleProperty.OUTLINE, "Outline");
		STYLE_MAPPING.put(StyleProperty.SHADOW, "Shadow");
		STYLE_MAPPING.put(StyleProperty.ALIGNMENT, "Alignment");
		STYLE_MAPPING.put(StyleProperty.MARGIN_L, "MarginL");
		STYLE_MAPPING.put(StyleProperty.MARGIN_R, "MarginR");
		STYLE_MAPPING.put(StyleProperty.MARGIN_V, "MarginV");
		STYLE_MAPPING.put(StyleProperty.ENCODING, "Encoding");
	}
	
	@Override
	public SubtitlesContainer fromFile(File input) throws MalformedFileException
	{
		SubtitlesContainer container = new SubtitlesContainer();

	    try
	    {
	    	Ini iniFile = new Ini();
	    	Config conf = new Config();
	    	conf.setEscape(false);
	    	iniFile.setConfig(conf);
	    	iniFile.load(new FileReader(input));

	    	// ### Script Info section : metadata ###
	    	Section scriptInfoSection = getSection(iniFile, SCRIPT_INFO);
	    	
	    	// Title
	    	container.setTitle(scriptInfoSection.get(SCRIPT_INFO_TITLE));
	    	
	    	// Author
	    	container.setTitle(scriptInfoSection.get(SCRIPT_INFO_AUTHOR));
	    	
	    	// Type verification
	    	if (!SCRIPT_TYPE.equals(scriptInfoSection.get(SCRIPT_INFO_TYPE)))
	    	{
	    		throw new MalformedFileException(String.format(
	    			"[Script Info] : invalid value for %s, expected '%s', found '%s',",
	    			SCRIPT_INFO_TYPE,
	    			SCRIPT_TYPE,
	    			scriptInfoSection.get(SCRIPT_INFO_TYPE)));
	    	}	    	
	    	
	    	// Timer
	    	float timer = Float.parseFloat(scriptInfoSection.get(SCRIPT_INFO_TIMER, "100").replace(',', '.'));
	    	
	    	// ### V4+ Styles section ###
	    	Section stylesSection = getSection(iniFile, V4PLUS_STYLE);
	    	String[] styleFormat = stylesSection.get(FORMAT).split("\\s*,\\s*");
	    	
	    	BiMap<String, StyleProperty> mapping = STYLE_MAPPING.inverse();
	    	// For each defined style
	  		for (int i = 0; i < stylesSection.length(STYLE); i++)
	    	{
	  			String[] styleValues = stylesSection.get(STYLE, i).split(",");
	  			Map<StyleProperty, String> styleValuesMap = null;
		    	// For each value of this style
		    	for (int j = 0; j < styleFormat.length; j++)
		    	{
		    		StyleProperty property = mapping.get(styleFormat[j]);
		    		if (property != null)
		    		{
		    			if (property == StyleProperty.NAME)
		    			{
		    				// The style's key
		    				styleValuesMap = new HashMap<SubtitlesContainer.StyleProperty, String>();
		    				container.getStyles().put(styleValues[j], styleValuesMap);
		    			}
		    			else
		    			{
		    				// Regular property
		    				if (styleValuesMap != null)
		    				{
		    					styleValuesMap.put(property, styleValues[j]);
		    				}
		    				else
		    				{
		    					// Key has not been initialized
		    					// TODO log undefined style, no key
		    					break;
		    				}
		    			}
		    		}
		    		else
		    		{
		    			// TODO log unknow property, ignored
		    		}
		    	}
	    	}
	    	
	    	
	    	// ### Event section : captions ###
	    	Section eventsSection = getSection(iniFile, EVENTS);
	    	List<String> eventFormat = Arrays.asList(eventsSection.get(FORMAT).split("\\s*,\\s*"));

    		// Start index
    		int idxStart = getIndex(eventFormat, FORMAT_START);
  
    		// End index
    		int idxEnd = getIndex(eventFormat, FORMAT_END);
    		
    		// Text
    		int idxText = getIndex(eventFormat, FORMAT_TEXT);
    		
    		for (int i = 0; i < eventsSection.length(DIALOGUE); i++)
	    	{
	    		List<String> dialogue = Arrays.asList(eventsSection.get(DIALOGUE, i).split(","));
	    		long start = (long) (getMilliseconds(dialogue.get(idxStart) + "0") * timer / 100);
	    		long end = (long) (getMilliseconds(dialogue.get(idxEnd) + "0") * timer / 100);
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
	
	private Section getSection(Ini iniFile, String sectionName) throws MalformedFileException 
	{
		Section section = iniFile.get(sectionName);
		if (section == null) throw new MalformedFileException("Missing section [" + sectionName + "]");
		return section;
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
		subtitlesWriter.print(DIALOGUE + SEPARATOR);
		subtitlesWriter.print(0); // Layer
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(StringUtils.chop(formatMilliseconds(caption.getStart()))); // Start
		subtitlesWriter.print(VALUE_SEPARATOR);
		subtitlesWriter.print(StringUtils.chop(formatMilliseconds(caption.getEnd()))); // End
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
		subtitlesWriter.println(StringUtils.join(caption.getLines(), "\\N"));
	}

	@Override
	protected SimpleDateFormat getTimestampDateFormat()
	{
		return TIMESTAMPS_SDF;
	}
	
	@Override
	protected Map<StyleProperty, String> getStyleMapping()
	{
		return STYLE_MAPPING;
	}
}