package smartcampus.android.template.standalone.Utilities;

public class ElementDescRoute {
	private String distance;
	private String duration;
	private String description;
	private int image;

	public ElementDescRoute(String distance, String duration,
			String description, int img) {
		super();
		this.distance = distance;
		this.duration = duration;
		this.description = description;
		image = img;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
