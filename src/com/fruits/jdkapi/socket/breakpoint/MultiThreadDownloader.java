package com.fruits.jdkapi.socket.breakpoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadDownloader {
	private static final String TEMP_DOWNLOAD_DIR = "D:\\MTD";
	
	private DownloadState downloadState;

	public DownloadState getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(DownloadState downloadState) {
		this.downloadState = downloadState;
	}

	public MultiThreadDownloader(String resourceURL, String fileToSave, int threadCount) throws Exception {
		String resourceURLMD5 = Utils.getMD5(resourceURL);

		downloadState = restoreDownloadStateFromDisk(resourceURLMD5);

		if (null == downloadState) {
			int contentLength = getResourceContentLength(resourceURL);
			int size = contentLength / threadCount;

			downloadState = new DownloadState();
			downloadState.setResourceURL(resourceURL);
			downloadState.setResrouceURLMD5(resourceURLMD5);
			downloadState.setThreadCount(threadCount);
			downloadState.setContentLength(contentLength);
			downloadState.setFileToSave(fileToSave);

			for (int i = 0; i < threadCount; i++) {
				DownloadRange range = new DownloadRange();
				int startPos = i * size;
				int endPos = -1;
				if (i != (threadCount - 1)) {
					endPos = (i + 1) * size - 1;
				} else {
					endPos = contentLength - 1;
				}
				range.setStartPosition(startPos);
				range.setEndPosition(endPos);
				range.setRangeName("range#" + i);
				downloadState.addRange(range);
			}
		}
	}

	private DownloadState restoreDownloadStateFromDisk(String resourceURLMD5) throws Exception {
		File downloadStateFile = new File(TEMP_DOWNLOAD_DIR + "\\" + resourceURLMD5 + ".mtd");
		if (downloadStateFile.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(downloadStateFile));
			DownloadState state = (DownloadState) ois.readObject();
			ois.close();
			return state;
		}
		return null;
	}

	public void download() throws Exception {
		List<DownloadRange> ranges = downloadState.getRangesNotCompleted();

		System.out.println("ranges not completed: [" + ranges + "]");

		ExecutorService executor = Executors.newFixedThreadPool(ranges.size());
		final CountDownLatch counter = new CountDownLatch(ranges.size());

		long startTime = new Date().getTime();

		for (DownloadRange range : ranges) {
			final int startPos = range.getStartPosition() + range.getBytesDownloaded();
			final int endPos = range.getEndPosition();
			final String rangeNumber = range.getRangeName();

			executor.execute(new Thread() {
				public void run() {
					try {
						System.out
								.println("thread for " + rangeNumber + " start to download range[" + startPos + "-" + ((-1 == endPos) ? "" : endPos) + "].");
						int bytesRead = downloadFileAndSave(downloadState.getResourceURL(), startPos, endPos, downloadState.getFileToSave());
						range.setBytesDownloaded(range.getBytesDownloaded() + bytesRead);

						counter.countDown();
						System.out.println("thread for " + rangeNumber + " has downloaded " + bytesRead + " bytes");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}

		counter.await();

		File downloadStateFile = new File(TEMP_DOWNLOAD_DIR + "\\" + downloadState.getResrouceURLMD5() + ".mtd");
		if (downloadStateFile.exists())
			downloadStateFile.delete();

		if (!downloadState.isAllRangesCompleted()) {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEMP_DOWNLOAD_DIR + "\\" + downloadState.getResrouceURLMD5() + ".mtd"));
			oos.writeObject(downloadState);
			oos.close();
		}

		System.out.println("downloading completed, costed time : " + (new Date().getTime() - startTime) + " ms.");
	}

	private int downloadFileAndSave(String resourceURL, int startPos, int endPos, String fileToSave) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(resourceURL).openConnection();
		if (endPos != -1) {
			conn.setRequestProperty("RANGE", "bytes=" + startPos + "-" + endPos);
		} else {
			conn.setRequestProperty("RANGE", "bytes=" + startPos + "-");
		}
		conn.setDoInput(true);
		conn.connect();
		InputStream is = conn.getInputStream();

		RandomAccessFile file = new RandomAccessFile(fileToSave, "rw");
		file.seek(startPos);

		int n;
		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while (-1 != (n = is.read(buffer))) {
			file.write(buffer, 0, n);
			bytesRead += n;
		}

		file.close();
		is.close();

		return bytesRead;
	}

	private static int getResourceContentLength(String resourceURL) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(resourceURL).openConnection();
		conn.connect();
		return conn.getContentLength();
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(getResourceContentLength("http://localhost:8080/examples/webstorm.exe"));

		MultiThreadDownloader mtd = new MultiThreadDownloader("https://dldir1.qq.com/qqfile/qq/QQ9.0.5/23816/QQ9.0.5.exe", TEMP_DOWNLOAD_DIR + "\\" + "QQ.exe", 5);
		mtd.download();
	}
}