package ctgov;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchOptions{
	private String requestURL = "https://clinicaltrials.gov/ct2/results?";

	private SearchOptions(SearchOptionsBuilder builder){
		try {
			String queryParams =
					"term=" + URLEncoder.encode( builder.searchTerm, "UTF-8")+
					"&cond=" + URLEncoder.encode( builder.condition, "UTF-8")+
					"&recr=" + URLEncoder.encode( builder.recruitment.getParam(), "UTF-8")+
					"&type=" + URLEncoder.encode( builder.type.getParam(), "UTF-8")+
					"&gndr=" + URLEncoder.encode( builder.gender.getParam(), "UTF-8")+
					"&rcv_s=" + URLEncoder.encode( builder.firstReceivedStart, "UTF-8")+
					"&rcv_e=" + URLEncoder.encode( builder.firstReceivedEnd, "UTF-8")+
					"&lup_s=" + URLEncoder.encode( builder.lastUpdatedStart, "UTF-8")+
					"&lup_e=" + URLEncoder.encode( builder.lastUpdatedEnd, "UTF-8");
			requestURL += queryParams;
			requestURL += "&studyxml=true";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
		private String firstReceivedStart = "";
		private String firstReceivedEnd = "";
		private String lastUpdatedStart = "";
		private String lastUpdatedEnd = "";

		public SearchOptionsBuilder(String term){
			this.searchTerm = term;
		}

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
					this.lastUpdatedStart = DATEFORMAT.format(start);
					this.lastUpdatedEnd = DATEFORMAT.format(end);					
				}
				else{
					this.firstReceivedStart = DATEFORMAT.format(start);
					this.firstReceivedEnd = DATEFORMAT.format(end);
				}
			} catch (ParseException e) {
				System.err.println("RequestOptions: Wrong date format");
			}
			return this;
		}

		public void showOptions(){
			System.out.println("term: " + searchTerm);
			System.out.println("cond: " + condition);
			System.out.println("recr: " + recruitment.getParam());
			System.out.println("type: " + type.getParam());
			System.out.println("gndr: " + gender.getParam());
			System.out.println("rcv_s: " + firstReceivedStart);
			System.out.println("rcv_e: " + firstReceivedEnd);
			System.out.println("lup_s: " + lastUpdatedStart);
			System.out.println("lup_e: " + lastUpdatedEnd);
		}
	}
}