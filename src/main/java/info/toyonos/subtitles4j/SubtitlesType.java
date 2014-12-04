package info.toyonos.subtitles4j;

public enum SubtitlesType
{
	SRT (SRTFactory.class);
	
	private Class<? extends SubtitlesFactory> clazz;
	
	private SubtitlesType(Class<? extends SubtitlesFactory> clazz)
	{
		this.clazz = clazz;
	}

	public Class<? extends SubtitlesFactory> getClazz()
	{
		return clazz;
	}
}
