package info.toyonos.subtitles4j.factory;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;

import java.io.File;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class SRTFactoryTest
{
	private SRTFactory factory;

	private File testFile1;

	@Before
	public void setUp() throws Exception 
	{
		// TODO better resource management
		factory = new SRTFactory();
		testFile1 = new File("src/test/resources/srt/test1.srt");
	}

	@Test
	public void testFromFile() throws MalformedFileException
	{
		SubtitlesContainer container = factory.fromFile(testFile1);

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
}