package util;

public class DocPair {
	/*
	 * Meta class for grouping storing document di-dj pair information.
	 */
	public int qryId;
	public int d1; // Document 1 -- the relevant document.
	public int d2; // Document 2 -- Irrelevant document.
	public DocPair(int qryId, int d1, int d2) {
		// TODO Auto-generated constructor stub
		this.qryId = qryId;
		this.d1 = d1;
		this.d2 = d2;
	}

}
