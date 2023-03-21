package com.skanci.security.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.skanci.security.dto.UserBasic;
import com.skanci.security.dto.UserRoleDto;
import com.skanci.security.entity.AppRole;
import com.skanci.security.entity.AppUser;
import com.skanci.security.entity.AppUserRole;
import com.skanci.security.excel.ExcelGenerator;
import com.skanci.security.jwt.JwtAuthResponse;
import com.skanci.security.jwt.JwtTokenProvider;
import com.skanci.security.service.AppRoleService;
import com.skanci.security.service.AppUserRoleService;
import com.skanci.security.service.AppUserService;
import com.skanci.security.service.IAppRoleService;
import com.skanci.security.service.IAppUserRoleService;
import com.skanci.security.service.IAppUserService;
import com.skanci.security.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class TestController {

	private AuthenticationManager authenticationManager;
	private IAppUserService appUserService;
	private IAppRoleService appRoleService;
	private IAppUserRoleService appUserRoleService;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider jwtTokenProvider;
	private ReportService reportService;
	
	@Autowired
	public TestController(AuthenticationManager authenticationManager,
						  AppUserService appUserService,
						  AppRoleService appRoleService,
						  AppUserRoleService appUserRoleService,
						  PasswordEncoder passwordEncoder,
						  JwtTokenProvider jwtTokenProvider,
						  ReportService reportService) {
		this.authenticationManager = authenticationManager;
		this.appUserService = appUserService;
		this.appRoleService = appRoleService;
		this.passwordEncoder = passwordEncoder;
		this.appUserRoleService = appUserRoleService;
		this.jwtTokenProvider =  jwtTokenProvider;
		this.reportService = reportService;
	}

	@GetMapping
	public String hello(Principal principal) {
		return "Hello world : " + principal.getName();
	}
	
	@GetMapping("user")
	public String helloUser() {
		return "Hello User !";
	}
	
	@GetMapping("admin")
	public String helloAdmin() {
		return "Hello Admin !";
	}

	// USERS END-POINT

	@PostMapping("sign-in")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody UserBasic userBasic) {
		System.out.println("start sign-in !");
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						userBasic.getUsername(),
						userBasic.getPassword()
				)
		);
		System.out.println("after authentication !");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		System.out.println("after security context !");
		String token = this.jwtTokenProvider.generateToken(authentication);
		System.out.println("after token generation !");
		JwtAuthResponse response = new JwtAuthResponse(token);
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	@GetMapping("users")
	public List<AppUser> listUser(){
		return this.appUserService.findAll();
	}
	
	@PostMapping("users/create")
	public ResponseEntity<String> storeUser(@RequestBody AppUser appUser) {
		this.appUserService.save(new AppUser(
				appUser.getUsername(),
				this.passwordEncoder.encode(appUser.getPassword())
		));
		return new ResponseEntity<>("User registered success !", HttpStatus.CREATED);
	}

	// ROLES END-POINT
	@GetMapping("roles")
	public List<AppRole> listRoles(){
		return this.appRoleService.findAll();
	}
	
	@PostMapping("roles/create")
	public ResponseEntity<String> storeRole(@RequestBody AppRole appRole) {
		this.appRoleService.save(appRole);
		return new ResponseEntity<>("Role created successfully !", HttpStatus.CREATED);
	}
	
	@PostMapping("add-user-roles")
	public ResponseEntity<String> associateUserRole(@RequestBody UserRoleDto userRole) {
		System.out.println(userRole.getUserId());
		System.out.println(userRole.getRoleId());
		this.appUserRoleService.save(
				new AppUserRole(
						this.appUserService.findById(userRole.getUserId()), 
						this.appRoleService.findById(userRole.getRoleId())
				) 
		);
		return new ResponseEntity<>("User has been associated !", HttpStatus.CREATED);
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
	
	@GetMapping("/export-report")
	public void generateReport(HttpServletResponse response
			) throws FileNotFoundException, JRException, IOException {
		// response.setContentType("application/pdf");
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
		String fileName = "users-report_" + dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName +  ".pdf";
		response.setHeader(headerKey, headerValue);
		this.reportService.exportReport(response);
	}
	
}
