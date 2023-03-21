package io.geeksbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import com.skanci.security.entity.AppUser;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelGenerator {
	
	private List<AppUser> users;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public ExcelGenerator(List<AppUser> users) {
		this.users = users;
		this.workbook = new XSSFWorkbook();
	}
	
	private void writeHeader() {
		this.sheet = workbook.createSheet("List of users");
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		
		font.setBold(true);
		font.setFontName("Times New Roman");
		font.setFontHeight(12);
		style.setFont(font);
		
		String[] headers = {"username", "email"};
		
		for(int i = 0; i < headers.length; i++) {
			this.createCell(row, i, headers[i], style);
		}
	}
	
	private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
		this.sheet.autoSizeColumn(columnCount);
		
		Cell cell = row.createCell(columnCount);
		
		if (valueOfCell instanceof Integer) {
			cell.setCellValue((Integer) valueOfCell);
		} else if (valueOfCell instanceof Long) {
			cell.setCellValue((Long) valueOfCell);
		} else if (valueOfCell instanceof String) {
			cell.setCellValue((String) valueOfCell);
		} else if (valueOfCell instanceof Boolean) {
			cell.setCellValue((Boolean) valueOfCell);
		}
		cell.setCellStyle(style);
	}
	
	private void write() {
		int rowCount = 1;
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		font.setFontName("Times New Roman");
		style.setFont(font);
		
		for(AppUser user: this.users) {
			Row row = this.sheet.createRow(rowCount++);
			int columnCount = 0;
			this.createCell(row, columnCount++, user.getUsername(), style);
			this.createCell(row, columnCount++, user.getEmail(), style);
		}
	}
	
	public void generatedExcelFile(HttpServletResponse response) throws IOException {
		this.writeHeader();
		this.write();
		
		ServletOutputStream outputStream = response.getOutputStream();

		this.workbook.write(outputStream); 
		this.workbook.close();
		outputStream.close();
		 
				
		/*
		 * File currDir = new File("."); String path = currDir.getAbsolutePath(); String
		 * fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
		 * System.out.println(fileLocation); FileOutputStream fileOutputStream = new
		 * FileOutputStream(fileLocation); workbook.write(fileOutputStream);
		 * workbook.close();
		 */
	}

}
