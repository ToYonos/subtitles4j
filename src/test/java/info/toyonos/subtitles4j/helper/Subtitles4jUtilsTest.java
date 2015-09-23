package info.toyonos.subtitles4j.helper;

import info.toyonos.subtitles4j.SubtitlesFileHandler;
import info.toyonos.subtitles4j.SubtitlesFileHandler.SubtitlesFile;
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
			FileUtils.readLines(subtitlesFileHandler.getFile("shifted1")),
			FileUtils.readLines(Subtitles4jUtils.shift(inputFile, 2500))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test1", "shifted2"})
	public void testShiftNegative() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test1"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(subtitlesFileHandler.getFile("shifted2")),
			FileUtils.readLines(Subtitles4jUtils.shift(inputFile, -1000))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test1", "shifted3"})
	public void testShiftNegativeZeroValue() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test1"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(subtitlesFileHandler.getFile("shifted3")),
			FileUtils.readLines(Subtitles4jUtils.shift(inputFile, -2500))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test5", "removed1"})
	public void testRemoveCaptionsSimplePatternCaseSensitive() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test5"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(subtitlesFileHandler.getFile("removed1")),
			FileUtils.readLines(Subtitles4jUtils.removeCaptions(inputFile, "test"))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test5", "removed2"})
	public void testRemoveCaptionsRegularPatternCaseSensitive() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test5"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(subtitlesFileHandler.getFile("removed2")),
			FileUtils.readLines(Subtitles4jUtils.removeCaptions(inputFile, "thi[a-z]+"))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test5", "removed3"})
	public void testRemoveCaptionsSimplePatternCaseInsensitive() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test5"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(subtitlesFileHandler.getFile("removed3")),
			FileUtils.readLines(Subtitles4jUtils.removeCaptionsIgnoreCase(inputFile, "test"))
		);
	}
	
	@Test
	@SubtitlesFile(type=SubtitlesType.SRT, name={"test5", "removed4"})
	public void testRemoveCaptionsRegularPatternCaseInsensitive() throws Subtitles4jException, IOException 
	{
		File inputFile = folder.newFile("tmp.srt");
		FileUtils.copyFile(subtitlesFileHandler.getFile("test5"), inputFile);
		Assert.assertEquals(
			FileUtils.readLines(subtitlesFileHandler.getFile("removed4")),
			FileUtils.readLines(Subtitles4jUtils.removeCaptionsIgnoreCase(inputFile, "thi[a-z]+"))
		);
	}
}