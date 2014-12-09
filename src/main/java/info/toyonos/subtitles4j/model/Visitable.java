package info.toyonos.subtitles4j.model;

import info.toyonos.subtitles4j.factory.SubtitlesVisitor;

/**
 * Interface that defines a class can be visited by a <code>SubtitlesVisitor</code>
 * 
 * @see info.toyonos.subtitles4j.factory.SubtitlesVisitor
 * @author ToYonos
 */
public interface Visitable
{
	/**
	 * Accept the visitor within the object
	 * @param visitor The <code>SubtitlesVisitor</code> instance
	 */
	public void accept(SubtitlesVisitor visitor);
}
