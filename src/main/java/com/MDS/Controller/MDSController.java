package com.MDS.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.MDS.Model.Location;
import com.MDS.Model.School;

@Controller

public class MDSController {
	@PostMapping("/location/state/upload")
	@ResponseBody
	public ResponseEntity createStateMDS(@RequestParam("file") MultipartFile file) {
		List<Location> locations = new ArrayList<Location>();
		final String type = "state";
		if (!file.isEmpty()) {
			try {
				locations = createmasterDataStoreLocation(type, file);
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<List<Location>>(locations, HttpStatus.OK);

	}

	@PostMapping("/location/district/upload")
	@ResponseBody
	public ResponseEntity createDistrictMDS(@RequestParam("file") MultipartFile file) {
		List<Location> locations = new ArrayList<Location>();
		final String type = "district";
		if (!file.isEmpty()) {
			try {
				locations = createmasterDataStoreLocation(type, file);
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<List<Location>>(locations, HttpStatus.OK);

	}

	@PostMapping("/location/block/upload")
	@ResponseBody
	public ResponseEntity createBlockMDS(@RequestParam("file") MultipartFile file) {
		List<Location> locations = new ArrayList<Location>();
		final String type = "block";
		if (!file.isEmpty()) {
			try {
				locations = createmasterDataStoreLocation(type, file);
			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<List<Location>>(locations, HttpStatus.OK);

	}

	@PostMapping("/location/cluster/upload")
	@ResponseBody
	public ResponseEntity createClusterMDS(@RequestParam("file") MultipartFile file) {
		List<Location> locations = new ArrayList<Location>();
		final String type = "cluster";
		if (!file.isEmpty()) {
			try {
				locations = createmasterDataStoreLocation(type, file);

			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<List<Location>>(locations, HttpStatus.OK);

	}

	@PostMapping("/school/upload")
	@ResponseBody
	public ResponseEntity createSchool(@RequestParam("file") MultipartFile file) {
		List<School> schools = new ArrayList<School>();
		final String type = "school";
		if (!file.isEmpty()) {
			try {
				schools = createmasterDataSchool(type, file);

			} catch (IOException e) {
				return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);

			}
		} else {

			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<List<School>>(schools, HttpStatus.OK);

	}

	private List<School> createmasterDataSchool(String type, MultipartFile file) throws IOException {
		List<School> schools = new ArrayList<School>();
		InputStream inputStream = file.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

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

	private List<Location> createmasterDataStoreLocation(String type, MultipartFile file) throws IOException {
		List<Location> locations = new ArrayList<Location>();

		InputStream inputStream = file.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		String line = br.readLine();
		// Reading header, Ignoring
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			String[] fields = line.split(",");
			String code = fields[0];
			String parentCode = fields[1];
			String name = fields[2];
			Location loc = new Location(code, parentCode, name, type);
			locations.add(loc);
		}
		br.close();
		return locations;
	}

}
