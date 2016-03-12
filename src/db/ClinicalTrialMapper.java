package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import model.ClinicalTrial;

public class ClinicalTrialMapper implements RowMapper<ClinicalTrial> {
	@Override
	public ClinicalTrial mapRow(ResultSet rs, int rowNum) throws SQLException {
		String id = rs.getString("id");
		String title = rs.getString("title");
		String topic = rs.getString("topic");
		return new ClinicalTrial(id, title, topic);
	}
}