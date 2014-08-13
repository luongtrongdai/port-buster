package com.port.buster;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.port.entity.EntBillInfo;
import com.port.entity.EntBillResult;
import com.port.entity.EnumType;

public class buster {

	public static final String URL_FORMAT = "http://www.vnpost.vn/TrackandTrace/tabid/130/n/%s/t/%d/s/1/Default.aspx";
	public static final String CANNOT_FOUNT = "N/A";
	
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
			
			List<EntBillResult> lstResult = new ArrayList<EntBillResult>();
			EntBillResult entResult;
			String date;
			String time;
			String status;
			String location;
			for (EntBillInfo bill : lstBill) {
				entResult = new EntBillResult();
				entResult.setCode(bill.getCode());
				
				
				String url = String.format(URL_FORMAT, bill.getCode()
													 , EnumType.getTypeByKey(bill.getType()));
				Document doc = Jsoup.connect(url).get();
				Elements e = doc.select(".mGrid tr:last-child td");

				date = CANNOT_FOUNT;
				time = CANNOT_FOUNT;
				status = CANNOT_FOUNT;
				location = CANNOT_FOUNT;
				
				if (e != null && e.size() > 0) {
					date = e.get(1).text();
					time = e.get(2).text();
					status = e.get(3).text();
					location = e.get(4).text();
				}
				entResult.setDate(date);
				entResult.setTime(time);
				entResult.setLocation(location);
				entResult.setStatus(status);
				lstResult.add(entResult);
			}
			//Write csv file
			File tempFile = new File("result.csv");
	        //Create scheame
	        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
	        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 2048);
	        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
	        
	        CsvSchema write = CsvSchema.builder()
												.addColumn("code")
												.addColumn("date")
												.addColumn("time")
												.addColumn("status")
												.addColumn("location")
												.build();
	        write = write.withHeader();
	        CsvMapper csvWrite = new CsvMapper();
	        ObjectWriter myObjectWriter = csvWrite.writer(write);

	        //Write list data to error file
	        myObjectWriter.writeValue(writerOutputStream, lstResult);
	        //Close stream
	        tempFileOutputStream.close();
	        bufferedOutputStream.close();
	        writerOutputStream.close();
	        
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
