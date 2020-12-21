package com.lumen.ifaces;

import java.util.List;

import com.lumen.model.ItemDisplay;
import com.lumen.model.Items;

public interface Service {

	public void buyItem(int itemCode, int quantity);

	public List<ItemDisplay> dailyReport(int day, int month, int noOfItems);

	public List<ItemDisplay> dailyReportByCategory(int day, int month, String category, int noOfItems);

	public List<ItemDisplay> monthlyReport(int month, int noOfItems);

	public List<ItemDisplay> monthlyReportByCategory(int month, String category, int noOfItems);

}
