package info.toyonos.subtitles4j.factory;

public class UnsupportedSubtitlesExtension extends Subtitles4jException
{
	private static final long serialVersionUID = 4714051403733532699L;

	public UnsupportedSubtitlesExtension()
	{
		super();
	}

	public UnsupportedSubtitlesExtension(String message, Throwable cause)
	{
		super(message, cause);
	}

	public UnsupportedSubtitlesExtension(String message)
	{
		super(message);
	}

	public UnsupportedSubtitlesExtension(Throwable cause)
	{
		super(cause);
	}
}
