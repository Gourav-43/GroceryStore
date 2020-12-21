package com.lumen.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.lumen.utility.Constants;
import com.lumen.ifaces.Service;
import com.lumen.model.ItemDisplay;
import com.lumen.model.Items;
import java.sql.Date;

public class ItemsService implements Service {

	private static final String MAPPING_TABLE_NAME = "Mapping";
	private Connection connection;

	public ItemsService(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void buyItem(int itemCode, int quantity) {
		// fetch the category for the item code - getCategoryForItemCode
		// fetch the entry for the specific itemCode and category
		// add this entry to the Sale Table -

		String category = getCategoryForItemCode(itemCode);
		System.out.println(category);
		Items item = getItemDetail(category, itemCode);
		System.out.println(item.getItemCode());
		System.out.println(item.getItemName());

		addItemToSaleTable(item, quantity, category);

	}

	private String getCategoryForItemCode(int itemCode) {
		PreparedStatement statement;
		String category = null;
		String query = "Select category from " + MAPPING_TABLE_NAME + " where itemCode= ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, itemCode);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				category = rs.getString("category");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return category;
	}

	private Items getItemDetail(String category, int itemCode) {
		PreparedStatement statement;
		Items result = null;
		String query = "Select * from " + Constants.CATEGORY_MAPPING.get(category) + " where itemCode = ? ;";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, itemCode);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				String itemName = rs.getString("itemName");
				double unitPrice = rs.getDouble("unitPrice");
				result = new Items();
				result.setItemCode(itemCode);
				result.setItemName(itemName);
				result.setUnitPrice(unitPrice);
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private boolean addItemToSaleTable(Items entry, int quantity, String category) {
		boolean result = false;
		double unitPrice = entry.getUnitPrice();
		String itemName = entry.getItemName();
		int itemCode = entry.getItemCode();
		double total = quantity * unitPrice;
		Date currentDate = new Date(System.currentTimeMillis());

		PreparedStatement statement;

		try {
			String query = "Insert into Sale(itemCode,category,itemName,unitPrice,quantity,total,date) Values(?,?,?,?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1, itemCode);
			statement.setString(2, category);
			statement.setString(3, itemName);
			statement.setDouble(4, unitPrice);
			statement.setInt(5, quantity);
			statement.setDouble(6, total);
			statement.setDate(7, currentDate);
			int row = statement.executeUpdate();
			if (row == 1) {
				result = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public List<ItemDisplay> dailyReport(int day, int month, int noOfItems) {
		List<ItemDisplay> result = null;
		String query = "select sum(quantity) as quantitySum,sum(total) as totalSum,itemCode,itemName,category,unitPrice from sale where day(date) ="
				+ day + " and month(date) = " + month + " group by itemCode order by sum(quantity) desc";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			result = addItemFromResultSet(rs, noOfItems);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;

	}

	@Override
	public List<ItemDisplay> dailyReportByCategory(int day, int month, String category, int noOfItems) {
		// TODO Auto-generated method stub
		List<ItemDisplay> result = null;
		String query = "select sum(quantity) as quantitySum,sum(total) as totalSum,itemCode,itemName,category,unitPrice from sale where day(date) = "
				+ day + " and month(date) =" + month + " and category = " + "\"" + category + "\""
				+ " and date = ? group by itemCode order by sum(quantity) desc";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			result = addItemFromResultSet(rs, noOfItems);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public List<ItemDisplay> monthlyReport(int month, int noOfItems) {
		// TODO Auto-generated method stub
		List<ItemDisplay> result = null;
		String query = "select sum(quantity) as quantitySum,sum(total) as totalSum,itemCode,itemName,category,unitPrice from sale where month(date) = "
				+ month + " group by itemCode order by sum(quantity) desc";
		PreparedStatement statement;
		int i = 0;
		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			result = addItemFromResultSet(rs, noOfItems);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public List<ItemDisplay> monthlyReportByCategory(int month, String category, int noOfItems) {
		// TODO Auto-generated method stub
		List<ItemDisplay> result = null;
		String query = "select sum(quantity) as quantitySum,sum(total) as totalSum,itemCode,itemName,category,unitPrice from sale where category = "
				+ "\"" + category + "\"" + " and month(date) = " + month
				+ " group by itemCode order by sum(quantity) desc";
		PreparedStatement statement;
		int i = 0;
		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			result = addItemFromResultSet(rs, noOfItems);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	private List<ItemDisplay> addItemFromResultSet(ResultSet rs, int counter) {

		List<ItemDisplay> list = new ArrayList<ItemDisplay>();
		int i = 0;
		try {
			while (rs.next() && i < counter) {
				ItemDisplay entry = new ItemDisplay();
				entry.setItemCode(rs.getInt("itemCode"));
				entry.setItemName(rs.getString("itemName"));
				entry.setUnitPrice((double) rs.getDouble("unitPrice"));
				entry.setCategory(rs.getString("category"));
				entry.setTotalQuantity(rs.getInt("quantitySum"));
				entry.setTotalCost(rs.getDouble("totalSum"));
				list.add(entry);
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

}
