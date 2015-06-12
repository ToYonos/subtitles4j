package info.toyonos.subtitles4j.factory;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import info.toyonos.subtitles4J.SubtitlesFileHandler;
import info.toyonos.subtitles4J.SubtitlesFileHandler.SubtitlesFile;
import info.toyonos.subtitles4j.model.SubtitlesContainer;
import info.toyonos.subtitles4j.model.SubtitlesContainer.Caption;
import info.toyonos.subtitles4j.model.SubtitlesContainer.StyleProperty;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("unchecked")
public class ASSFactoryTest
{
	private ASSFactory factory;

	@Rule
	public SubtitlesFileHandler subtitlesFileHandler = new SubtitlesFileHandler();

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Before
	public void setUp() throws Exception 
	{
		factory = new ASSFactory();
	}

	@Test
	@SubtitlesFile(type=SubtitlesType.ASS, name="test1")
	public void testFromFileOk() throws Exception
	{		
		SubtitlesContainer container = factory.fromFile(subtitlesFileHandler.getFile());

		Assert.assertNotNull(container);

		Assert.assertEquals(container.getTitle(), "A title");
		Assert.assertEquals(container.getAuthor(), "Toyo");
		
		Assert.assertNotNull(container.getStyles());
		Assert.assertNotNull(container.getStyles().get("Default"));
		Assert.assertThat(container.getStyles().get("Default"), allOf(
			hasEntry(is(StyleProperty.FONT_NAME), is("Arial")),
			hasEntry(is(StyleProperty.FONT_SIZE), is("25")),
			hasEntry(is(StyleProperty.PRIMARY_COLOR), is("&H00FFFFFF")),
			hasEntry(is(StyleProperty.SECONDARY_COLOR), is("&H000000FF")),
			hasEntry(is(StyleProperty.OUTLINE_COLOR), is("&H00000000")),
			hasEntry(is(StyleProperty.BACK_COLOR), is("&H00000000")),
			hasEntry(is(StyleProperty.BOLD), is("0")),
			hasEntry(is(StyleProperty.ITALIC), is("0")),
			hasEntry(is(StyleProperty.UNDERLINE), is("0")),
			hasEntry(is(StyleProperty.STRIKEOUT), is("0")),
			hasEntry(is(StyleProperty.SCALE_X), is("100")),
			hasEntry(is(StyleProperty.SCALE_Y), is("100")),
			hasEntry(is(StyleProperty.SPACING), is("0")),
			hasEntry(is(StyleProperty.ANGLE), is("0")),
			hasEntry(is(StyleProperty.BORDER_STYLE), is("1")),
			hasEntry(is(StyleProperty.OUTLINE), is("2")),
			hasEntry(is(StyleProperty.SHADOW), is("1")),
			hasEntry(is(StyleProperty.ALIGNMENT), is("2")),
			hasEntry(is(StyleProperty.MARGIN_L), is("10")),
			hasEntry(is(StyleProperty.MARGIN_R), is("10")),
			hasEntry(is(StyleProperty.MARGIN_V), is("10")),
			hasEntry(is(StyleProperty.ENCODING), is("1"))
		));
		
		Assert.assertNotNull(container.getCaptions());
		Assert.assertThat(container.getCaptions(), allOf(
			not(empty()),
			contains(
				allOf(
					instanceOf(Caption.class),
					hasProperty("start", is(1230L)),
					hasProperty("end", is(4560L)),
					hasProperty("lines", allOf(
						not(Matchers.<String>empty()),
						Matchers.<String>contains("Line 1")
					))
				),
				allOf(
					instanceOf(Caption.class),
					hasProperty("start", is(2340L)),
					hasProperty("end", is(5670L)),
					hasProperty("lines", allOf(
						not(Matchers.<String>empty()),
						Matchers.<String>contains("Line 1", "Line 2")
					))
				),
				allOf(
					instanceOf(Caption.class),
					hasProperty("start", is(3450L)),
					hasProperty("end", is(6780L)),
					hasProperty("lines", allOf(
						not(Matchers.<String>empty()),
						Matchers.<String>contains("Line 1", "Line 2", "Line 3")
					))
				)
			)
		));
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test2")
	public void testFromFileKoMissingInfoSection() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test3")
	public void testFromFileKoMissingStyleSection() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test4")
	public void testFromFileKoMissingEventSection() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test5")
	public void testFromFileKoMissingEventKey() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test6")
	public void testFromFileKoUndefinedStyle() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test7")
	public void testFromFileKoBadTimestamp() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}
	
	@Test(expected=MalformedSubtitlesException.class)
	@SubtitlesFile(type=SubtitlesType.ASS, name="test8")
	public void testFromFileKoInvalidScriptType() throws MalformedSubtitlesException, IOException
	{
		factory.fromFile(subtitlesFileHandler.getFile());
	}

	@Test
	@SubtitlesFile(type=SubtitlesType.ASS, name={"expected1", "expected2"})
	public void testToFileOk() throws IOException, SubtitlesGenerationException
	{
		SubtitlesContainer container = new SubtitlesContainer();
		container.setTitle("Test title");
		container.setAuthor("Test author");

		container.getStyles().put("Default", new HashMap<SubtitlesContainer.StyleProperty, String>());
		container.getStyles().get("Default").put(StyleProperty.NAME, "Default");
		container.getStyles().get("Default").put(StyleProperty.FONT_NAME, "Arial");
		container.getStyles().get("Default").put(StyleProperty.FONT_SIZE, "12");
		container.getStyles().get("Default").put(StyleProperty.PRIMARY_COLOR, "&H00FFFFFF");
		container.getStyles().get("Default").put(StyleProperty.BACK_COLOR, "&H00000000");
		
		container.addCaption(0, 123, Arrays.asList("This", "is", "a", "test"));
		File actual = factory.toFile(container, folder.newFile("output1.ass"));
		
		Assert.assertTrue(FileUtils.contentEquals(actual, subtitlesFileHandler.getFile("expected1")));
		
		container = new SubtitlesContainer();
		container.setTitle("Test title");
		container.setAuthor("Test author");

		container.getStyles().put("Default", new HashMap<SubtitlesContainer.StyleProperty, String>());
		container.getStyles().get("Default").put(StyleProperty.NAME, "Default");
		container.getStyles().get("Default").put(StyleProperty.FONT_NAME, "Arial");
		container.getStyles().get("Default").put(StyleProperty.FONT_SIZE, "12");
		container.getStyles().get("Default").put(StyleProperty.PRIMARY_COLOR, "&H00FFFFFF");
		container.getStyles().get("Default").put(StyleProperty.BACK_COLOR, "&H00000000");
		container.getStyles().put("Style1", new HashMap<SubtitlesContainer.StyleProperty, String>());
		container.getStyles().get("Style1").put(StyleProperty.NAME, "Style1");
		container.getStyles().get("Style1").put(StyleProperty.FONT_NAME, "Arial");
		container.getStyles().get("Style1").put(StyleProperty.FONT_SIZE, "14");
		container.getStyles().get("Style1").put(StyleProperty.PRIMARY_COLOR, "&H00FFFFFF");
		container.getStyles().get("Style1").put(StyleProperty.BACK_COLOR, "&H00000000");
		container.getStyles().get("Style1").put(StyleProperty.BOLD, "1");
		container.getStyles().put("Style2", new HashMap<SubtitlesContainer.StyleProperty, String>());
		container.getStyles().get("Style2").put(StyleProperty.NAME, "Style2");
		container.getStyles().get("Style2").put(StyleProperty.FONT_NAME, "Arial");
		container.getStyles().get("Style2").put(StyleProperty.FONT_SIZE, "16");
		container.getStyles().get("Style2").put(StyleProperty.PRIMARY_COLOR, "&H00FFFFFF");
		container.getStyles().get("Style2").put(StyleProperty.BACK_COLOR, "&H00000000");
		container.getStyles().get("Style2").put(StyleProperty.ITALIC, "1");
		
		container.addCaption(0, 1234, Arrays.asList("First one"));
		container.addCaption(5000, 6000, "Style1", Arrays.asList("Second", "One"));
		container.addCaption(61888, 62001, "Style2", Arrays.asList("And", "The", "Last", "One !"));
		actual = factory.toFile(container, folder.newFile("output2.ass"));
		
		Assert.assertTrue(FileUtils.contentEquals(actual, subtitlesFileHandler.getFile("expected2")));
	}

	@Test(expected=SubtitlesGenerationException.class)
	public void testToFileKoMandatoryFieldNoValue() throws IOException, SubtitlesGenerationException
	{
		SubtitlesContainer container = new SubtitlesContainer();
		container.setTitle("Test title");
		container.setAuthor("Test author");

		container.getStyles().put("Default", new HashMap<SubtitlesContainer.StyleProperty, String>());
		container.getStyles().get("Default").put(StyleProperty.NAME, "Default");
		container.getStyles().get("Default").put(StyleProperty.FONT_SIZE, "12");
		container.getStyles().get("Default").put(StyleProperty.PRIMARY_COLOR, "&H00FFFFFF");
		container.getStyles().get("Default").put(StyleProperty.BACK_COLOR, "&H00000000");
		
		container.addCaption(0, 123, Arrays.asList("This", "is", "a", "test"));
		factory.toFile(container, folder.newFile("output3.ass"));
	}
}