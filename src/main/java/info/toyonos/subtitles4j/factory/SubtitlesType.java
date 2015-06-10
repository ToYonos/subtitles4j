package info.toyonos.subtitles4j.factory;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;


public enum SubtitlesType
{
	SRT (SRTFactory.class, "srt"),
	ASS (ASSFactory.class, "ass", "ssa");
	
	private Class<? extends SubtitlesFactory> clazz;
	private Set<String> extensions;
	
	private SubtitlesType(Class<? extends SubtitlesFactory> clazz, String... extensions)
	{
		this.clazz = clazz;
		this.extensions = new LinkedHashSet<String>(Arrays.asList(extensions));
	}

	public Class<? extends SubtitlesFactory> getClazz()
	{
		return clazz;
	}

	public Set<String> getExtensions()
	{
		return extensions;
	}
	
	public boolean hasExtension(String extension)
	{
		return extensions.contains(extension);
	}
	
	public static SubtitlesType fromExtension(String extension)
	{
		for (SubtitlesType type : SubtitlesType.values())
		{
			if (type.hasExtension(extension)) return type;
		}
		return null;
	}
}
