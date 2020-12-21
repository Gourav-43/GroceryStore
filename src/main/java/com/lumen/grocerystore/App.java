package com.lumen.grocerystore;

import java.sql.Connection;
import java.util.Formatter;
import java.util.List;

import com.lumen.model.ItemDisplay;
import com.lumen.services.ItemsService;
import com.lumen.utility.ConnectionUtils;

public class App {
	public static void main(String[] args) {
		Connection con = ConnectionUtils.getMyConnection();

		ItemsService service = new ItemsService(con);
		List<ItemDisplay> items = service.dailyReport(11,11,3);
		display(items);
	}

	public static void display(List<ItemDisplay> items) {
		Formatter f = new Formatter();
		f.format("%15s %15s %15s %15s %15s %15s","S.No", "Item_Name", "Category", "Item_Code",
				"Total_Quantity", "Total_Cost");
		System.out.println(f);
		System.out.println();
		int i=1;
		for (ItemDisplay item : items) {
			Formatter f1 = new Formatter();
			f1.format("%15s %15s %15s %15s %15s %15s",i++, item.getItemName(), item.getCategory(), item.getItemCode(),
					item.getTotalQuantity(), item.getTotalCost());
			System.out.println(f1);
		}
		

	}

}
