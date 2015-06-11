package info.toyonos.subtitles4j.factory;

public class MalformedSubtitlesException extends Subtitles4jException
{
	private static final long serialVersionUID = 4714051403733532699L;

	public MalformedSubtitlesException()
	{
		super();
	}

	public MalformedSubtitlesException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MalformedSubtitlesException(String message)
	{
		super(message);
	}

	public MalformedSubtitlesException(Throwable cause)
	{
		super(cause);
	}
}
