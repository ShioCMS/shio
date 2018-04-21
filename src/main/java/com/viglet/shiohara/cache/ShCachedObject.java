package com.viglet.shiohara.cache;

public class ShCachedObject implements ShCacheable {
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/*
	 * This variable will be used to determine if the object is expired.
	 */
	private java.util.Date dateofExpiration = null;
	private Object identifier = null;
	/*
	 * This contains the real "value". This is the object which needs to be shared.
	 */
	public Object object = null;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public ShCachedObject(Object obj, Object id, int minutesToLive) {
		this.object = obj;
		this.identifier = id;
		// minutesToLive of 0 means it lives on indefinitely.
		if (minutesToLive != 0) {
			dateofExpiration = new java.util.Date();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(dateofExpiration);
			cal.add(cal.MINUTE, minutesToLive);
			dateofExpiration = cal.getTime();
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public boolean isExpired() {
		// Remember if the minutes to live is zero then it lives forever!
		if (dateofExpiration != null) {
			// date of expiration is compared.
			if (dateofExpiration.before(new java.util.Date())) {
				/*
				 * System.out.
				 * println("CachedResultSet.isExpired:  Expired from Cache! EXPIRE TIME: " +
				 * dateofExpiration.toString() + " CURRENT TIME: " + (new
				 * java.util.Date()).toString());
				 */
				return true;
			} else {
				// System.out.println("CachedResultSet.isExpired: Expired not from Cache!");
				return false;
			}
		} else // This means it lives forever!
			return false;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public Object getIdentifier() {
		return identifier;
	}
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}