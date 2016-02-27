package ctgov;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchOptions{
	private String requestURL = "https://clinicaltrials.gov/ct2/results/download?down_stds=top100";

	private SearchOptions(SearchOptionsBuilder builder){
		requestURL = requestURL +
				"term=" + builder.searchTerm +
				"cond=" + builder.condition +
				"recr=" + builder.recruitment.getParam() +
				"type=" + builder.type.getParam() +
				"gndr=" + builder.gender.getParam() +
				"rcv_s=" + builder.firstReceivedStart +
				"rcv_e=" + builder.firstReceivedEnd +
				"lup_s=" + builder.lastUpdatedStart +
				"lup_e=" + builder.lastUpdatedEnd;
	}

	public String getRequestURL(){
		return this.requestURL;
	}

	public enum RecruitmentStatus{
		ALL(""), OPEN("Open"), CLOSED("Closed");

		private String param;

		RecruitmentStatus(String q){
			param = q;
		}

		String getParam() {
			return param;
		}
	}

	public enum StudyType{
		ALL(""), INTERVENTIONAL("Intr"), OBSERVATIONAL("Obsr");

		private String param;

		StudyType(String q){
			param = q;
		}

		String getParam() {
			return param;
		}
	}

	public enum Gender{
		BOTH(""), MALE("Male"), FEMALE("Female");

		private String param;

		Gender(String q){
			param = q;
		}

		String getParam() {
			return param;
		}
	}

	public static class SearchOptionsBuilder{
		private final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy");
		private String searchTerm = "";
		private String condition = "";
		private RecruitmentStatus recruitment = RecruitmentStatus.ALL;
		private StudyType type = StudyType.ALL;
		private Gender gender = Gender.BOTH;
		private Date firstReceivedStart = null;
		private Date firstReceivedEnd = null;
		private Date lastUpdatedStart = null;
		private Date lastUpdatedEnd = null;

		public SearchOptions build(){
			return new SearchOptions(this);
		}

		public SearchOptionsBuilder setRecruitmentStatus(RecruitmentStatus recr){
			this.recruitment = recr;
			return this;
		}

		public SearchOptionsBuilder setSearchTerms(String term){
			this.searchTerm = term;
			return this;
		}

		public SearchOptionsBuilder setCondition(String cond){
			this.condition = cond;
			return this;
		}

		public SearchOptionsBuilder setStudyType(StudyType type){
			this.type = type;
			return this;
		}

		public SearchOptionsBuilder setGender(Gender gndr){
			this.gender = gndr;
			return this;
		}

		public SearchOptionsBuilder setDateRange(String from, String to, boolean updated){
			try {
				Date start = DATEFORMAT.parse(from);
				Date end = DATEFORMAT.parse(to);
				if(updated){
					this.lastUpdatedStart = start;
					this.lastUpdatedEnd = end;					
				}
				else{
					this.firstReceivedStart = start;
					this.firstReceivedEnd = end;
				}
			} catch (ParseException e) {
				System.err.println("RequestOptions: Wrong date format");
			}
			return this;
		}
	}
}