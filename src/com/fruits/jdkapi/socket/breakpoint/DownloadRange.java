package com.fruits.jdkapi.socket.breakpoint;
import java.io.Serializable;

public class DownloadRange implements Serializable {
	private static final long serialVersionUID = 1L;

	private String rangeName;
	private int startPosition;
	private int endPosition;
	private int bytesDownloaded;

	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public int getBytesDownloaded() {
		return bytesDownloaded;
	}

	public void setBytesDownloaded(int bytesDownloaded) {
		this.bytesDownloaded = bytesDownloaded;
	}

	public boolean isDownloadedFully() {
		return ((endPosition - startPosition + 1) == bytesDownloaded);
	}

	@Override
	public String toString() {
		return "DownloadRange [rangeName=" + rangeName + ", startPosition=" + startPosition + ", endPosition=" + endPosition + ", bytesTotally="
				+ (endPosition - startPosition + 1) + ", bytesDownloaded=" + bytesDownloaded + ", bytesLeft:" + (endPosition - startPosition + 1 - bytesDownloaded)
				+ "]\n";
	}
}