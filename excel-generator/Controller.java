package io.geeksbox;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.skanci.security.entity.AppUser;
import com.skanci.security.entity.AppUserRole;
import com.skanci.security.excel.ExcelGenerator;
import com.skanci.security.service.AppUserService;

import jakarta.servlet.http.HttpServletResponse;

DateFormat 
    HttpServletResponse
    IOException
    AppUser
    List

@RestController
@RequestMapping("/api/auth")
public class TestController {

	private IAppUserService appUserService;

	@Autowired
	public TestController(AppUserService appUserService,
		this.appUserService = appUserService;
	}
    
	@GetMapping("/users/export-to-excel")
	public void exportIntoExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
		String fileName = "list-of-users_" + dateFormatter.format(new Date());
		System.out.println(fileName);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName +  ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		List<AppUser> users = this.appUserService.findAll();
		ExcelGenerator generator = new ExcelGenerator(users);
		generator.generatedExcelFile(response);
		
		/*
		 * Resource resource = UrlResource(foundFile.toUri());
		 * 
		 * return ResponseEntity.ok() .contentType(MediaType
		 * .parseMediaType(contentType)) .header(HttpHeaders.CONTENT_DISPOSITION,
		 * headerValue) .body(resource);
		 */ 
	}
	
}
