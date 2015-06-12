package info.toyonos.subtitles4j.helper;

import info.toyonos.subtitles4J.SubtitlesFileHandler;
import info.toyonos.subtitles4J.SubtitlesFileHandler.SubtitlesFile;
import info.toyonos.subtitles4j.factory.Subtitles4jException;
import info.toyonos.subtitles4j.factory.SubtitlesType;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class Subtitles4jUtilsTest
{
	@Rule
	public SubtitlesFileHandler subtitlesFileHandler = new SubtitlesFileHandler();
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test1", "shifted1"})
	public void testShiftPositive() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test1"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(Subtitles4jUtils.shift(inputFile, 2500)),
			FileUtils.readLines(subtitlesFileHandler.getFile("shifted1"))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test1", "shifted2"})
	public void testShiftNegative() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test1"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(Subtitles4jUtils.shift(inputFile, -1000)),
			FileUtils.readLines(subtitlesFileHandler.getFile("shifted2"))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test1", "shifted3"})
	public void testShiftNegativeZeroValue() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test1"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(Subtitles4jUtils.shift(inputFile, -2500)),
			FileUtils.readLines(subtitlesFileHandler.getFile("shifted3"))
		);
	}
}