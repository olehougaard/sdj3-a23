package dk.via.tasks;

import java.io.Serializable;

public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String text;
	private long timeEstimate;
	
	public Task(String text, long timeEstimate) {
		this.text = text;
		this.timeEstimate = timeEstimate;
	}

	public String getText() {
		return text;
	}

	public long getTimeEstimate() {
		return timeEstimate;
	}
	
	@Override
	public String toString() {
		return String.format("Task(%s, estimate=%d)", text, timeEstimate);
	}
}
