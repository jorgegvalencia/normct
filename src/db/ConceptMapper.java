package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import model.Concept;
import model.ConceptFactory;

public class ConceptMapper implements RowMapper<Concept> {
	@Override
	public Concept mapRow(ResultSet rs, int rowNum) throws SQLException {
		Concept c = ConceptFactory.getConcept(rs.getString("cui"));
		if (c == null) {
			c = new Concept(
					rs.getString("cui"), 
					rs.getString("sctid"), 
					rs.getString("name"),
					rs.getString("semantic_type"),
					rs.getString("normalform"),
					rs.getString("focus_concept"),
					rs.getString("focus_concept_fsn"),
					rs.getString("focus_concept_hierarchy"));
		}
		return c;
	}
}
