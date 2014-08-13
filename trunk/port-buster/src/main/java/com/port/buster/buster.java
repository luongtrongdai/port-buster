package com.port.buster;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.port.entity.EntBillInfo;
import com.port.entity.EnumType;

public class buster {

	public static final String URL_FORMAT = "http://www.vnpost.vn/TrackandTrace/tabid/130/n/%s/t/%d/s/1/Default.aspx";
	
	public static void main(String[] args) {
		try {
			CsvSchema bootstrap = CsvSchema.builder()
						.addColumn("code")
						.addColumn("type")
						.build();
	
			bootstrap =	bootstrap
								.withSkipFirstDataRow(true)
								.withoutHeader();
	
			CsvMapper csvMapper = new CsvMapper();
			File csvFile = new File("info.csv");
			MappingIterator<EntBillInfo> mappingIterator = csvMapper.reader(EntBillInfo.class).with(bootstrap).readValues(csvFile);
			List<EntBillInfo>  lstBill = mappingIterator.readAll();
			
			for (EntBillInfo bill : lstBill) {
				String url = String.format(URL_FORMAT, bill.getCode()
													 , EnumType.getTypeByKey(bill.getType()));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
