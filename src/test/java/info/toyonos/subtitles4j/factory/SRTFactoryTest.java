package info.toyonos.subtitles4j.factory;

import info.toyonos.subtitles4j.model.SubtitlesContainer;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SRTFactoryTest
{
	private SRTFactory factory;

	private File testFile1;

	@Before
	public void setUp() throws Exception 
	{
		// TODO better resource management
		factory = new SRTFactory();
		testFile1 = new File("src/test/resources/test1.srt");
	}

	@Test
	public void testFromFile() throws MalformedFileException
	{
		SubtitlesContainer container = factory.fromFile(testFile1);
		Assert.assertNotNull(container);
		Assert.assertNotNull(container.getCaptions());
		
		Assert.assertEquals(3, container.getCaptions().size());
		Assert.assertEquals(1234, container.getCaptions().get(0).start);
		Assert.assertEquals(5678, container.getCaptions().get(0).end);
		Assert.assertEquals(1, container.getCaptions().get(0).lines.size());
		Assert.assertEquals("Line 1", container.getCaptions().get(0).lines.get(0));
	}
}