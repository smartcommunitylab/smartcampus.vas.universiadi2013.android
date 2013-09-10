package android.smartcampus.template.standalone;

public class ExtendedAnswer {
	private String answer;
	private String question;
	private int totalTag;
	private int usefulTag;
	
	public ExtendedAnswer(String answer, String question, int totalTag,
			int usefulTag) {
		super();
		this.answer = answer;
		this.question = question;
		this.totalTag = totalTag;
		this.usefulTag = usefulTag;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getTotalTag() {
		return totalTag;
	}

	public void setTotalTag(int totalTag) {
		this.totalTag = totalTag;
	}

	public int getUsefulTag() {
		return usefulTag;
	}

	public void setUsefulTag(int usefulTag) {
		this.usefulTag = usefulTag;
	}
}
