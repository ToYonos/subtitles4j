package info.toyonos.subtitles4J;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SubtitlesFileHandler implements TestRule
{
	private Map<String, File> files = new HashMap<String, File>();
	
	/**
	 * @param name
	 * @return a declared <code>File</code> according to its name
	 */
	public File getFile(String name)
	{
		return files.get(name);
	}
	
	/**
	 * 
	 * @return the first declared <code>File</code> 
	 */
	public File getFile()
	{
		return files.values().iterator().next();
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement()
		{
			@Override
			public void evaluate() throws Throwable
			{
				// Getting the annotation
				SubtitlesFile subtitlesFile = description.getAnnotation(SubtitlesFile.class);

				if (subtitlesFile != null)
				{
					// Fetching Files
					for (String name : subtitlesFile.name())
					{
						files.put(name, new File(getClass().getClassLoader()
							.getResource(
								subtitlesFile.type().name().toLowerCase() +
								File.separator +
								name +
								"." +
								subtitlesFile.type().name().toLowerCase()
							)
							.getFile()
						));	
					}
				}

				try
				{
					base.evaluate();
				}
				finally
				{
					files.clear();
				}
			}
		};
	}
	
	@Retention( value = RetentionPolicy.RUNTIME)
	@Target(value = {ElementType.METHOD})
	public @interface SubtitlesFile
	{
		public enum Type {SRT, ASS};

		public String[] name();
		
		public Type type();
	}
}
