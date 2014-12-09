package info.toyonos.subtitles4j;

/**
 * Interface that defines a class can be visited by a <code>SubtitlesVisitor</code>
 * 
 * @see info.toyonos.subtitles4j.SubtitlesVisitor
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
