package info.toyonos.subtitles4j.factory;

public class MalformedFileException extends Subtitles4jException
{
	private static final long serialVersionUID = 4714051403733532699L;

	public MalformedFileException()
	{
		super();
	}

	public MalformedFileException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MalformedFileException(String message)
	{
		super(message);
	}

	public MalformedFileException(Throwable cause)
	{
		super(cause);
	}
}
