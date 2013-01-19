package com.android.iliConnect.handler;

import java.util.ArrayList;
import java.util.List;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.modification.NotificationData;
import com.android.iliConnect.models.modification.NotificationItem;
import com.android.iliConnect.models.modification.NotificationMessage;

public class ModificationHandler {
	public boolean isNotificationMarked(String ref_id) {
		NotificationData notiData = MainActivity.instance.localDataProvider.appData.getNotificationData();

		int itemIndex = loadNotificationItemIdx(notiData, ref_id);
		if (itemIndex != -1) {
			NotificationItem item = notiData.NotificationItems.get(itemIndex);
			if (item != null) {
				return true;
			}
		}

		return false;
	}

	public boolean isNotificationShown(String ref_id) {
		NotificationData notiData = MainActivity.instance.localDataProvider.appData.getNotificationData();

		int itemIndex = loadNotificationMessageIdx(notiData, ref_id);
		if (itemIndex != -1) {
			NotificationMessage message = notiData.NotificationMessages.get(itemIndex);
			if (message != null) {
				return true;
			}
		}

		return false;
	}

	public void setNotificationMarked(String ref_id) {
		NotificationData notiData = MainActivity.instance.localDataProvider.appData.getNotificationData();
		if (notiData == null) {
			notiData = new NotificationData();
		}

		List<NotificationItem> notificationList;
		if (notiData.NotificationItems == null) {
			notificationList = new ArrayList<NotificationItem>();
			notiData.NotificationItems = notificationList;
		}

		NotificationItem item = new NotificationItem();
		item.setRef_id(ref_id);
		notiData.NotificationItems.add(item);

		MainActivity.instance.localDataProvider.appData.save();
	}

	public void setModificationUnmarked(String ref_id) {
		NotificationData notiData = MainActivity.instance.localDataProvider.appData.getNotificationData();

		int index = loadNotificationItemIdx(notiData, ref_id);
		if (index > -1) {
			notiData.NotificationItems.remove(index);
		}
		
		setModificationUnshown(ref_id);

		MainActivity.instance.localDataProvider.appData.save();
	}

	private int loadNotificationItemIdx(NotificationData data, String ref_id) {
		int itemIndex = -1;
		if (data != null) {
			if (data.NotificationItems != null && !data.NotificationItems.isEmpty()) {
				for (int i = 0; i < data.NotificationItems.size(); i++) {
					if (data.NotificationItems.get(i).getRef_id().equals(ref_id)) {
						itemIndex = i;
						break;
					}
				}
			}
		}

		return itemIndex;
	}
	
	public void setNotificationShown(String ref_id) {
		NotificationData notiData = MainActivity.instance.localDataProvider.appData.getNotificationData();
		if (notiData == null) {
			notiData = new NotificationData();
		}

		List<NotificationMessage> notificationList;
		if (notiData.NotificationMessages == null) {
			notificationList = new ArrayList<NotificationMessage>();
			notiData.NotificationMessages = notificationList;
		}

		NotificationMessage message = new NotificationMessage();
		message.setRef_id(ref_id);
		notiData.NotificationMessages.add(message);

		MainActivity.instance.localDataProvider.appData.save();
	}
	
	private void setModificationUnshown(String ref_id) {
		NotificationData notiData = MainActivity.instance.localDataProvider.appData.getNotificationData();

		int index = loadNotificationMessageIdx(notiData, ref_id);
		if (index > -1) {
			notiData.NotificationMessages.remove(index);
		}

		MainActivity.instance.localDataProvider.appData.save();
	}

	private int loadNotificationMessageIdx(NotificationData data, String ref_id) {
		int itemIndex = -1;
		if (data != null) {
			if (data.NotificationMessages != null && !data.NotificationMessages.isEmpty()) {
				for (int i = 0; i < data.NotificationMessages.size(); i++) {
					if (data.NotificationMessages.get(i).getRef_id().equals(ref_id)) {
						itemIndex = i;
						break;
					}
				}
			}
		}

		return itemIndex;
	}
}
