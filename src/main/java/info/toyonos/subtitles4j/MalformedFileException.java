package info.toyonos.subtitles4j;

public class MalformedFileException extends Exception
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
