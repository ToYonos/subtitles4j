package info.toyonos.subtitles4j.factory;

public abstract class Subtitles4jException extends Exception
{
	private static final long serialVersionUID = 4714051403733532699L;

	public Subtitles4jException()
	{
		super();
	}

	public Subtitles4jException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public Subtitles4jException(String message)
	{
		super(message);
	}

	public Subtitles4jException(Throwable cause)
	{
		super(cause);
	}
}
