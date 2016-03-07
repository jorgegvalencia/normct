package test.metamap;

import java.util.List;

import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapElement;

/**
 * An interface for representing a MetaMap map term.  Essentially, a
 * score and a list of Ev terms. 
 * <p>
 * Created: Thu May 21 17:00:23 2009
 *
 * @author <a href="mailto:wrogers@nlm.nih.gov">Willie Rogers</a>
 * @version 1.0
 */
public interface Map extends MetaMapElement, Mapping {
}
