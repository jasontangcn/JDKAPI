package com.fruits.jdkapi.socket.breakpoint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DownloadState implements Serializable {
	private static final long serialVersionUID = 1L;

	private String resourceURL;
	private String resrouceURLMD5;
	private int threadCount;
	private int contentLength;
	private String fileToSave;
	private List<DownloadRange> ranges = new ArrayList<DownloadRange>();

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getResrouceURLMD5() {
		return resrouceURLMD5;
	}

	public void setResrouceURLMD5(String resrouceURLMD5) {
		this.resrouceURLMD5 = resrouceURLMD5;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public List getRanges() {
		return ranges;
	}

	public void setRanges(List<DownloadRange> ranges) {
		this.ranges = ranges;
	}

	public void addRange(DownloadRange range) {
		this.ranges.add(range);
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getFileToSave() {
		return fileToSave;
	}

	public void setFileToSave(String fileToSave) {
		this.fileToSave = fileToSave;
	}

	public List<DownloadRange> getRangesNotCompleted() {
		List<DownloadRange> rangesNotCompleted = new ArrayList<DownloadRange>();
		for (DownloadRange range : ranges) {
			if (!range.isDownloadedFully())
				rangesNotCompleted.add(range);
		}
		return rangesNotCompleted;
	}

	public boolean isAllRangesCompleted() {
		boolean allCompleted = true;
		for (DownloadRange range : ranges) {
			if (!range.isDownloadedFully())
				return false;
		}
		return true;
	}
}