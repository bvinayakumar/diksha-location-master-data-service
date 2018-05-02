package com.MDS.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opencsv.bean.CsvToBean;

import org.apache.http.entity.StringEntity;
//import com.csvreader.CsvReader;
import org.apache.http.entity.mime.MultipartEntityBuilder;

@Controller

public class MDSController {
	/*@Autowired
	private Environment environment;
	@Autowired
	StateHeaders stateHeaders;*/
	final String baseUrl = "https://dev.open-sunbird.org/api/data/v1";
	final String APIKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkMTc1MDIwNDdlODc0ODZjOTM0ZDQ1ODdlYTQ4MmM3MyJ9.7LWocwCn5rrCScFQYOne8_Op2EOo-xTCK5JCFarHKSs";

	@PostMapping("/location/state/upload")
	@ResponseBody
	public ResponseEntity createStateMDS(@RequestParam("file") MultipartFile file) {
		String response = null;
		List<Location> locations = new ArrayList<Location>();
		final String type = "state";
		//String stateNaem = environment.getProperty("stateCode");
		//System.out.println("state header neame::" + stateNaem);
		if (!file.isEmpty()) {
			try {

				String[] stateLocationHeaders = { "state_name", "udise_state_code" };
				createCsvFileLocation(file, stateLocationHeaders, type);
				response = executeRequestLocationBulkUpload(baseUrl, type);
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

				String[] districtLocationHeaders = { "district_name", "udise_district_code", "udise_state_code" };
				createCsvFileLocation(file, districtLocationHeaders, type);
				response = executeRequestLocationBulkUpload(baseUrl, type);
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
				String[] blockLocationHeaders = { "edu_block_name", "udise_edu_block_code", "udise_dist_code",
						"udise_state_code" };
				createCsvFileLocation(file, blockLocationHeaders, type);
				response = executeRequestLocationBulkUpload(baseUrl, type);
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
				String[] blockLocationHeaders = { "cluname", "clucd", "inityear" };
				createCsvFileLocation(file, blockLocationHeaders, type);
				response = executeRequestLocationBulkUpload(baseUrl, type);
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
	public ResponseEntity createSchool(@RequestParam("file") MultipartFile file) throws Exception {
		String response = null;
		final String type = "school";
		if (!file.isEmpty()) {
			try {
				String[] schoolHeaders = { "schcd", "schname", "blkcd", "distcd" };
				CSVParser records = CSVFormat.DEFAULT.withHeader(schoolHeaders).withFirstRecordAsHeader()
						.parse(new InputStreamReader(file.getInputStream()));
				//record.get(headers[1])
				String blockCode = records.getRecords().get(0).get(schoolHeaders[2]);
				String districtId = getDistrictId(blockCode);
				String stateID = getStateId(districtId);
				String stateName = getStateCode(stateID);
				createCsvFileOrg(file, schoolHeaders, "");
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);

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
	 * @param baseURL
	 *            the urlString to which the file needs to be uploaded
	 * @return server response as <code>String</code>
	 */
	public String executeRequestLocationBulkUpload(String baseURL, String type)
			throws ClientProtocolException, IOException, HTTPException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String URL = baseURL + "/location/upload";
		HttpPost uploadFile = new HttpPost(URL);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		// builder.ad

		// This attaches the file to the POST:
		File f = new File("locations.csv");
		builder.addBinaryBody("file", new FileInputStream(f),
				org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, f.getName());
		builder.addTextBody("type", type);
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		// uploadFile.setHeader("x-location-type", type);
		uploadFile.setHeader("Authorization", "bearer " + APIKey);
		HttpResponse response = httpClient.execute(uploadFile);
		// HttpResponse response = client.execute(httpGet);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String body = handler.handleResponse(response);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200)
			return body;
		else
			return "error calling upload service";
	}

	public void createCsvFileOrg(MultipartFile file, String[] headers,String channel) throws IOException {
		String newline = System.getProperty("line.separator");
		String csvFile = "orgs.csv";
		FileWriter writer = new FileWriter(csvFile);
		String[] HEADERS = headers;
		// { StateHeaders.state_name.name(), StateHeaders.udise_state_code.name() };
		CSVParser records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader()
				.parse(new InputStreamReader(file.getInputStream()));
		CSVUtils.writeLine(writer,
				Arrays.asList("orgName", "isRootOrg", "channel", "externalId", "provider", "locationCodes", "latitude",
						"longitude	", "educationLevel", "schoolType", "noOfTeachers", "schoolWithSpecialNeed",
						"schoolInchargeCode", "location", "schoolInchargeName", "schoolInchargeMobile", "description",
						"homeUrl", "orgCode", "orgType", "preferredLanguage", "theme", "contactDetail"));
		writer.append(newline);

		for (CSVRecord record : records) {
		//	String[] schoolHeaders = { "schcd", "schname", "blkcd", "distcd" };
				CSVUtils.writeLine(writer, Arrays.asList(record.get(headers[1]),"false", channel,record.get(headers[0]), "", "todo","","","","","",
						"false","","","","","","",record.get(headers[0]),"School","","",""));
			
				
			writer.append(newline);
		}
		writer.flush();
		writer.close();
		// locations = createMasterDataStoreLocation(type, file);

	}

	public void createCsvFileLocation(MultipartFile file, String[] headers, String type) throws IOException {
		String newline = System.getProperty("line.separator");
		String csvFile = "locations.csv";
		FileWriter writer = new FileWriter(csvFile);
		String[] HEADERS = headers;
		// { StateHeaders.state_name.name(), StateHeaders.udise_state_code.name() };
		CSVParser records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader()
				.parse(new InputStreamReader(file.getInputStream()));
		CSVUtils.writeLine(writer, Arrays.asList("name", "code", "parentCode", "parentId"));
		writer.append(newline);

		for (CSVRecord record : records) {
			if (type.equalsIgnoreCase("state") || type.equalsIgnoreCase("cluster")) {
				CSVUtils.writeLine(writer, Arrays.asList(record.get(headers[0]), record.get(headers[1]), "", ""));
			} else {
				CSVUtils.writeLine(writer,
						Arrays.asList(record.get(headers[0]), record.get(headers[1]), record.get(headers[2]), ""));
			}
			writer.append(newline);
		}
		writer.flush();
		writer.close();
		// locations = createMasterDataStoreLocation(type, file);

	}

	private String getStateForBlock(String blockCode) throws Exception {

		String districtId = getDistrictId(blockCode);
		String stateID = getStateId(districtId);
		String stateName = getStateCode(stateID);

		return stateName;

	}

	private String getDistrictId(String blockCode) throws Exception {

		JsonArray locationSearchResponse = null;
		String districtId = null;
		JsonObject requestfilters = new JsonObject();
		requestfilters.addProperty("code", blockCode);
		locationSearchResponse = callLocationSearchAPI(baseUrl, requestfilters);
		System.out.println("getDistrictId body:::" + locationSearchResponse);
		districtId = locationSearchResponse.get(0).getAsJsonObject().get("parentId").getAsString();

		return districtId;

	}

	private String getStateId(String districtId) throws Exception {

		JsonArray locationSearchResponse = null;
		String stateId = null;
		JsonObject requestfilters = new JsonObject();
		requestfilters.addProperty("id", districtId);
		locationSearchResponse = callLocationSearchAPI(baseUrl, requestfilters);
		System.out.println("getStateId body:::" + locationSearchResponse);
		stateId = locationSearchResponse.get(0).getAsJsonObject().get("parentId").getAsString();
		// requestfilters.addProperty("stateId:::", districtId);
		return stateId;

	}

	private String getStateCode(String stateId) throws Exception {

		JsonArray locationSearchResponse = null;
		String stateCode = null;
		JsonObject requestfilters = new JsonObject();
		requestfilters.addProperty("id", stateId);
		locationSearchResponse = callLocationSearchAPI(baseUrl, requestfilters);
		System.out.println("getStateName body:::" + locationSearchResponse);
		stateCode = locationSearchResponse.get(0).getAsJsonObject().get("code").toString();
		requestfilters.addProperty("stateName:::", stateId);
		return stateCode;

	}

	private boolean ifStateRootORg(String stateCd) throws Exception {

		boolean isRootOrg = false;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JsonObject orgSearchReqBody = new JsonObject();
		JsonObject orgSearchReqst = new JsonObject();
		JsonObject requestfilters = new JsonObject();
		requestfilters.addProperty("channel", stateCd);
		requestfilters.addProperty("isrootOrg", true);
		orgSearchReqst.add("filters", requestfilters);
		orgSearchReqBody.add("request", orgSearchReqst);
		System.out.println("request---" + orgSearchReqBody.getAsJsonObject().toString());
		String URL = "https://dev.open-sunbird.org/api/org/v1/search";
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost districtSearch = new HttpPost(URL);
		districtSearch.addHeader("Authorization", "bearer " + APIKey);
		districtSearch.addHeader("Content-Type", "Application/json");
		districtSearch.setEntity(new StringEntity(orgSearchReqBody.getAsJsonObject().toString()));
		HttpResponse response = httpClient.execute(districtSearch);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String resString = handler.handleResponse(response);
		Gson gson = new Gson();
		JsonElement jelem = gson.fromJson(resString, JsonElement.class);
		JsonObject jobj = jelem.getAsJsonObject();
		int count = ((JsonObject) jobj.get("result")).get("response").getAsJsonObject().get("count").getAsInt();
		if (count > 0)
			isRootOrg = true;
		return isRootOrg;

	}

	private JsonArray callLocationSearchAPI(String baseURL, JsonObject requestfilters) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JsonObject locationSearchReqBody = new JsonObject();
		JsonObject locationSearchReqst = new JsonObject();
		locationSearchReqst.add("filters", requestfilters);
		locationSearchReqBody.add("request", locationSearchReqst);
		System.out.println("request---" + locationSearchReqBody.getAsJsonObject().toString());
		String URL = baseURL + "/location/search";
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost districtSearch = new HttpPost(URL);
		districtSearch.addHeader("Authorization", "bearer " + APIKey);
		districtSearch.addHeader("Content-Type", "Application/json");
		districtSearch.setEntity(new StringEntity(locationSearchReqBody.getAsJsonObject().toString()));
		HttpResponse response = httpClient.execute(districtSearch);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String resString = handler.handleResponse(response);
		Gson gson = new Gson();
		JsonElement jelem = gson.fromJson(resString, JsonElement.class);
		JsonObject jobj = jelem.getAsJsonObject();
		JsonElement getres = ((JsonObject) jobj.get("result")).get("response");
		return getres.getAsJsonArray();
	}
	
	public String executeRequestOrgBulkUpload(String baseURL, String type)
			throws ClientProtocolException, IOException, HTTPException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String URL = baseURL + "/org/upload";
		HttpPost uploadFile = new HttpPost(URL);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		// builder.ad

		// This attaches the file to the POST:
		File f = new File("orgs.csv");
		builder.addBinaryBody("file", new FileInputStream(f),
				org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, f.getName());
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		// uploadFile.setHeader("x-location-type", type);
		uploadFile.setHeader("Authorization", "bearer " + APIKey);
		HttpResponse response = httpClient.execute(uploadFile);
		// HttpResponse response = client.execute(httpGet);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String body = handler.handleResponse(response);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200)
			return body;
		else
			return "error calling upload ORG service";
	}

}
