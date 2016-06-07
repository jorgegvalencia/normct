package db.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class TConceptsJDBCTemplate {
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public List<TrialConceptRecord> listTrialConcepts(String nctid) {
		String sql = "SELECT clinical_trial.nctid AS TRIAL, concept.sctid AS SCTID,"
				+ "concept.cui AS CUI, concept.fsn AS CONCEPT, phrase AS PHRASE FROM concept, cmatch, clinical_trial "
				+ "WHERE clinical_trial.nctid = cmatch.trial AND cmatch.sctid = concept.sctid"
				+ " AND clinical_trial.nctid= " + "'" + nctid + "'";
		List<TrialConceptRecord> records = jdbcTemplateObject.query(sql, new TrialConceptsMapper());
		return records;
	}

	public static class TrialConceptsMapper implements RowMapper<TrialConceptRecord> {
		@Override
		public TrialConceptRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			TrialConceptRecord tcr = new TrialConceptRecord();
			tcr.setConcept(rs.getString("CONCEPT"));
			tcr.setCui(rs.getString("CUI"));
			tcr.setSctid(rs.getString("SCTID"));
			tcr.setTrial(rs.getString("TRIAL"));
			tcr.setPhrase(rs.getString("PHRASE"));
			return tcr;
		}

	}
}
