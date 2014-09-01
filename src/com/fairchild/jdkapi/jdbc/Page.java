package com.fairchild.jdkapi.jdbc;

import java.io.Serializable;
import java.util.*;

public class Page implements Serializable {
	public static final int pageSize = 5;
	private List records;
	int start;
	int totalRowsCount;

	public Page(List records, int start, int totalRowsCount) {
		this.records = records;
		this.start = start;
		this.totalRowsCount = totalRowsCount;
	}

	public int getTotalRowsCount() {
		return this.totalRowsCount;
	}

	public List getRecords() {
		return this.records;
	}

	public int getSize() {
		return records.size();
	}

	public boolean hasPreviousPage() {
		return (start > 0);
	}

	public int getPreviousPageStart() {
		return Math.max(start - pageSize, 0);
	}

	public boolean hasNextPage() {
		return (start + records.size()) < totalRowsCount;
	}

	public int getNextPageStart() {
		return start + records.size();
	}
}
