package normalization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.JAXBElement;

import normalization.coredataset.Concept;
import normalization.coredataset.CoreDatasetService;
import normalization.coredataset.CoreDatasetServicePortType;
import normalization.coredataset.KinshipConcepts;
import normalization.coredataset.NormalizedExpression;
import normalization.coredataset.SnomedRelationship;

public class CoreDatasetServiceClient {
	private CoreDatasetService service;
	private CoreDatasetServicePortType port;
	private static HashMap<String, String> index = new HashMap<>(); // scui,normalForm
	private static HashMap<String, String> indexFull = new HashMap<>(); // scui,normalForm

	private CoreDatasetServiceClient() {
		service = new CoreDatasetService();
		port = service.getCoreDatasetServiceHttpsSoap12Endpoint();
	}
	
	private static class SingletonHelper {
		private static final CoreDatasetServiceClient INSTANCE = new CoreDatasetServiceClient();
	}

	public static CoreDatasetServiceClient  getInstance() {
		return SingletonHelper.INSTANCE;
	}
	
	public String getNFFocusConcept(String sctid){
		NormalizedExpression expression = port.getShortNormalForm(sctid);
		String focusConceptCode = expression.getFocusConcept().getValue();
		return focusConceptCode;
	}
	
	public String getNFFocusConceptFSN(String sctid){
		NormalizedExpression expression = port.getShortNormalForm(sctid);
		String focusConceptFsn = expression.getFocusConceptTitle().getValue();
		return focusConceptFsn;
	}
	
	public HashMap<String, String> getNFRefinements(String scui){
		HashMap<String, String> refinements = new HashMap<>();
		NormalizedExpression expression = port.getShortNormalForm(scui);
		List<SnomedRelationship> relationshipList = expression.getRelationships();
		if (!relationshipList.isEmpty()) {
			for (int i = 0; i < relationshipList.size(); i++) {
				List<String> pair = getNormalizedExpressionRefinement(relationshipList.get(i));
				refinements.put(pair.get(0), pair.get(1));
			}
		}
		if(refinements.isEmpty())
			refinements = null;
		return refinements;
	}

	public String getNormalFormAsString(String scui) {
		return getNormalFormAsString(scui, true);
	}

	public String getNormalFormAsString(String scui, boolean term) {
		if (term) {
			if (indexFull.containsKey(scui))
				return indexFull.get(scui);
		} else {
			if (index.containsKey(scui))
				return index.get(scui);
		}
		NormalizedExpression expression = port.getShortNormalForm(scui);
		StringBuilder sb = new StringBuilder();
		String focusConceptCode = expression.getFocusConcept().getValue();
		String focusConcept = expression.getFocusConceptTitle().getValue();
		if (focusConcept != null) {
			if (term) {
				sb.append(focusConceptCode + "|" + focusConcept + "|");
			} else {
				sb.append(focusConceptCode);
			}
			List<SnomedRelationship> relationshipList = expression.getRelationships();
			if (!relationshipList.isEmpty()) {
				sb.append(":");
				for (int i = 0; i < relationshipList.size(); i++) {
					sb.append(getSnomedRelationshipAsString(relationshipList.get(i), term));
					if (i < relationshipList.size() - 1) {
						sb.append(",");
					}
				}
			}
		} else {
			sb.append("-");
		}
		if (term) {
			indexFull.put(scui, sb.toString());
		} else {
			index.put(scui, sb.toString());
		}
		return sb.toString();
	}

	@Deprecated
	public String getFSN(String scui) {
		JAXBElement<String> label = port.cd2CDMNEW(scui).getCode().getValue().getLabel();
		if (label != null)
			return label.getValue();
		else
			return "";
	}

	public String getRootConcept(String term) {
		return port.getRootConcept(term);
	}

	public List<String> getParents(String term) {
		return port.getParents(term);
	}
	
	public List<String> getGranparents(String term){
		return port.getTillGranparents(term);
	}
	
	public List<String> getNextGen(String term){
		List<KinshipConcepts> list = port.getNextGen(new ArrayList<String>(Arrays.asList(term)));
		List<Concept> concepts = list.get(0).getSiblings();
		List<String> ids = new ArrayList<String>();
		for(Concept c: concepts){
			ids.add(c.getCode().getValue());
		}
		return ids;
	}

	public boolean isParent(String parent, String child) {
		return getParents(child).contains(parent);
	}

	public List<String> getBestMatches(List<String> terms) {
		List<String> l = new ArrayList<>();
		List<String> aux = new CopyOnWriteArrayList<>(terms);
		for (String parent : terms) {
			Iterator<String> it = aux.iterator();
			while (it.hasNext()) {
				String child = it.next();
				if (isParent(parent, child)) {
					l.add(parent);
					aux.remove(child);
				}
			}
		}
		return l;
	}



	private String getSnomedRelationshipAsString(SnomedRelationship snrel, boolean term) {
		String relCode = snrel.getRelationship().getValue();
		String relTerm = snrel.getRelationshipTitle().getValue();
		String relValue = getNormalizedExpressionAsString(snrel.getRelationshipValue().getValue(), term);
		String result;
		if (term) {
			result = relCode + "|" + relTerm + "|=" + relValue;
		} else {
			result = relCode + "=" + relValue;
		}
		return result;
	}



	private String getNormalizedExpressionAsString(NormalizedExpression expression, boolean term) {
		String focusConceptCode = expression.getFocusConcept().getValue();
		String focusConcept = expression.getFocusConceptTitle().getValue();
		String result;
		if (term) {
			result = focusConceptCode + "|" + focusConcept + "|";
		} else {
			result = focusConceptCode;
		}
		List<SnomedRelationship> relationshipList = expression.getRelationships();
		if (!relationshipList.isEmpty()) {
			for (int i = 0; i < relationshipList.size(); i++) {
				result += getSnomedRelationshipAsString(relationshipList.get(i), term);
				if (i < relationshipList.size() - 1) {
					result += ",";
				}
			}
		}
		return result;
	}
	
	private List<String> getNormalizedExpressionRefinement(SnomedRelationship expression){
		List<String> pair = new ArrayList<>();
		// only one relationship value
		if(expression.getRelationship() != null){
			pair.add(0, expression.getRelationship().getValue());
			pair.add(1, expression.getRelationshipValue().getValue().getFocusConcept().getValue());
		} else {
			pair = null;
		}
		return pair;
	}

}
