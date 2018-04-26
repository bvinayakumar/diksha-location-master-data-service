package com.MDS.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.MDS.Model.Location;
import com.MDS.Model.School;
import com.MDS.Utils.CSVUtils;
import com.MDS.Utils.LocationHeaders;
import com.MDS.Utils.StateHeaders;
import com.opencsv.bean.CsvToBean;
//import com.csvreader.CsvReader;
import org.apache.http.entity.mime.MultipartEntityBuilder;

@Controller

public class MDSController {
	@Autowired
    private Environment environment;
	@Autowired
	StateHeaders stateHeaders;
	final String uploadUrl = "https://dev.open-sunbird.org/api/data/v1/location/upload";
	final String APIKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkMTc1MDIwNDdlODc0ODZjOTM0ZDQ1ODdlYTQ4MmM3MyJ9.7LWocwCn5rrCScFQYOne8_Op2EOo-xTCK5JCFarHKSs";
	@PostMapping("/location/state/upload")
	@ResponseBody
	public ResponseEntity createStateMDS(@RequestParam("file") MultipartFile file) {
		String response = null;
		List<Location> locations = new ArrayList<Location>();
		final String type = "state";
		String stateNaem = environment.getProperty("stateCode");
		 System.out.println("state header neame::"+ stateNaem);
		if (!file.isEmpty()) {
			try {
				
				String[] stateLocationHeaders = { "state_name",
						"udise_state_code" };
				createCsvFile(file, stateLocationHeaders,type);
				 response =executeMultiPartRequest(uploadUrl, type);
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
	
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@PostMapping("/location/district/upload")
	@ResponseBody
	public ResponseEntity createDistrictMDS(@RequestParam("file") MultipartFile file) {
		List<Location> locations = new ArrayList<Location>();
		String response = null;
		final String type = "district";
		if (!file.isEmpty()) {
		
				try {
					
					String[] districtLocationHeaders = { 
							"district_name","udise_district_code","udise_state_code" };
					createCsvFile(file, districtLocationHeaders,type);
					 response =executeMultiPartRequest(uploadUrl, type);
				} catch (IOException e) {
					return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

				}
			
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@PostMapping("/location/block/upload")
	@ResponseBody
	public ResponseEntity createBlockMDS(@RequestParam("file") MultipartFile file) {
		String response = null;
		List<Location> locations = new ArrayList<Location>();
		final String type = "block";
		if (!file.isEmpty()) {
			try {
				String[] blockLocationHeaders = { 
						"edu_block_name", "udise_edu_block_code","udise_dist_code","udise_state_code"};
				createCsvFile(file, blockLocationHeaders,type);
				 response =executeMultiPartRequest(uploadUrl, type);
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@PostMapping("/location/cluster/upload")
	@ResponseBody
	public ResponseEntity createClusterMDS(@RequestParam("file") MultipartFile file) {
		List<Location> locations = new ArrayList<Location>();
		String response = null;
		final String type = "cluster";
		if (!file.isEmpty()) {
			try {
				String[] blockLocationHeaders = {"cluname","clucd","inityear"};
				createCsvFile(file, blockLocationHeaders,type);
				 response =executeMultiPartRequest(uploadUrl, type);
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@PostMapping("/school/upload")
	@ResponseBody
	public ResponseEntity createSchool(@RequestParam("file") MultipartFile file) {
		List<School> schools = new ArrayList<School>();
		final String type = "school";
		if (!file.isEmpty()) {
			try {
				schools = createMasterDataSchool(type, file);

			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<List<School>>(schools, HttpStatus.OK);

	}

	private List<School> createMasterDataSchool(String type, MultipartFile file) throws IOException {
		List<School> schools = new ArrayList<School>();
		InputStream inputStream = file.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		CsvToBean<School> reader = new CsvToBean<School>();
		String line = br.readLine();
		// Reading header, Ignoring
		while ((line = br.readLine()) != null && !line.isEmpty()) {

			String[] fields = line.split(",");

			String schoolCode = fields[0];
			String name = fields[1];
			String latlong = fields[2];
			String educationLevel = fields[3];
			String schoolType = fields[4];
			String noOfTeachers = fields[5];
			String clusterCode = fields[6];
			boolean schoolWithSpecialNeed = Boolean.parseBoolean(fields[7]);
			String schoolInchargeCode = fields[8];
			String schoolInchargeName = fields[9];
			String schoolInchargeMobile = fields[10];
			String address = fields[11];
			String externalId = fields[12];
			String idProvider = fields[13];
			String idProviderType = fields[14];

			School school = new School(schoolCode, name, latlong, educationLevel, schoolType, noOfTeachers, clusterCode,
					schoolWithSpecialNeed, schoolInchargeCode, schoolInchargeName, schoolInchargeMobile, address,
					externalId, idProvider, idProviderType);
			schools.add(school);
		}
		br.close();
		return schools;
	}

	private List<Location> createMasterDataStoreLocation(String type, List<Location> locations) throws IOException {

		String csvFile = "locations.csv";
		FileWriter writer = new FileWriter(csvFile);

		// for header
		CSVUtils.writeLine(writer, Arrays.asList(LocationHeaders.code.name(), LocationHeaders.parentCode.name(),
				LocationHeaders.name.name()));

		for (Location loc : locations) {
			List<String> list = new ArrayList<>();
			list.add(loc.getCode());
			list.add(loc.getParentCode());
			list.add(loc.getName());
			;
			CSVUtils.writeLine(writer, list);
		}

		writer.flush();
		writer.close();

		return locations;
	}

	/**
	 * Method that builds the multi-part form data request
	 * 
	 * @param urlString
	 *            the urlString to which the file needs to be uploaded
	 * @return server response as <code>String</code>
	 */
	public String executeMultiPartRequest(String urlString, String type) throws ClientProtocolException, IOException,HTTPException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost(urlString);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		// builder.ad

		// This attaches the file to the POST:
		File f = new File("locations.csv");
		builder.addBinaryBody("file", new FileInputStream(f),
				org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, f.getName());
		builder.addTextBody("type", type);
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		//uploadFile.setHeader("x-location-type", type);
		uploadFile.setHeader("Authorization", "bearer " + APIKey);
		HttpResponse response = httpClient.execute(uploadFile);
		//HttpResponse response = client.execute(httpGet);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String body = handler.handleResponse(response);
		int code = response.getStatusLine().getStatusCode();
		if(code==200)
		return body;
		else 
			return "error calling upload service";
	}

	

	public void createCsvFile(MultipartFile file, String[] headers, String type) throws IOException {
		String newline = System.getProperty("line.separator");
		String csvFile = "locations.csv";
		FileWriter writer = new FileWriter(csvFile);
		String[] HEADERS = headers;
		// { StateHeaders.state_name.name(), StateHeaders.udise_state_code.name() };
		CSVParser records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader()
				.parse(new InputStreamReader(file.getInputStream()));
		CSVUtils.writeLine(writer, Arrays.asList("name","code",
				"parentCode","parentId"));
		writer.append(newline);
	
		for (CSVRecord record : records) {
			if (type.equalsIgnoreCase("state")||type.equalsIgnoreCase("cluster")) {
				CSVUtils.writeLine(writer, Arrays.asList(record.get(headers[0]),  record.get(headers[1]),"",""));
			} else {
				CSVUtils.writeLine(writer,
						Arrays.asList(record.get(headers[0]), record.get(headers[1]), record.get(headers[2]),""));
			}
			writer.append(newline);
		}
		writer.flush();
		writer.close();
		// locations = createMasterDataStoreLocation(type, file);

	}
}
