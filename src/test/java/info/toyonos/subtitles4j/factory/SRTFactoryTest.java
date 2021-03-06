package info.toyonos.subtitles4j.factory;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import info.toyonos.subtitles4j.SubtitlesFileHandler;
import info.toyonos.subtitles4j.SubtitlesFileHandler.SubtitlesFile;
import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("unchecked")
public class SRTFactoryTest
{
	private SRTFactory factory;

	@Rule
	public SubtitlesFileHandler subtitlesFileHandler = new SubtitlesFileHandler();

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Before
	public void setUp() throws Exception 
	{
		factory = new SRTFactory();
	}

	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name="test1")
	public void testFromFileOk() throws MalformedSubtitlesException, IOException
	{
		SubtitlesContainer container = factory.fromFile(subtitlesFileHandler.getFile());

		Assert.assertNotNull(container);
		Assert.assertNotNull(container.getCaptions());
		Assert.assertThat(container.getCaptions(), allOf(
			not(empty()),
			contains(
				allOf(
					instanceOf(Caption.class),
					hasProperty("start", is(1234L)),
					hasProperty("end", is(5678L)),
					hasProperty("lines", allOf(
						not(Matchers.<String>empty()),
						Matchers.<String>contains("Line 1")
					))
				),
				allOf(
					instanceOf(Caption.class),
					hasProperty("start", is(2345L)),
					hasProperty("end", is(6789L)),
					hasProperty("lines", allOf(
						not(Matchers.<String>empty()),
						Matchers.<String>contains("Line 1", "Line 2")
					))
				),
				allOf(
					instanceOf(Caption.class),
					hasProperty("start", is(3456L)),
					hasProperty("end", is(7890L)),
					hasProperty("lines", allOf(
						not(Matchers.<String>empty()),
						Matchers.<String>contains("Line 1", "Line 2", "Line 3")
					))
				)
			)
		));
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.SRT, name="test2")
	public void testFromFileKoBadIndex() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.SRT, name="test3")
	public void testFromFileKoBadTimestamp() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.SRT, name="test4")
	public void testFromFileKoEndOfFile() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"expected1", "expected2"})
	public void testToFileOk() throws IOException, SubtitlesGenerationException
	{
		SubtitlesContainer container = new SubtitlesContainer();
		container.addCaption(0, 123, Arrays.asList("This", "is", "a", "test"));
		File actual = factory.toFile(container, folder.newFile("output1.srt"));
		
		Assert.assertTrue(FileUtils.contentEquals(actual, subtitlesFileHandler.getFile("expected1")));
		
		container = new SubtitlesContainer();
		container.addCaption(0, 1234, Arrays.asList("First one"));
		container.addCaption(5000, 6000, Arrays.asList("Second", "One"));
		container.addCaption(61888, 62001, Arrays.asList("And", "The", "Last", "One !"));
		actual = factory.toFile(container, folder.newFile("output2.srt"));
		
		Assert.assertTrue(FileUtils.contentEquals(actual, subtitlesFileHandler.getFile("expected2")));
	}
}