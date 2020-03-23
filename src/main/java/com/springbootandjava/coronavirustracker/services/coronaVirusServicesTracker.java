package com.springbootandjava.coronavirustracker.services;

import java.awt.List;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.springbootandjava.coronavirustracker.model.LocationStorage;


@Service
public class coronaVirusServicesTracker {
	
	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
	
	// create an instances of locationstorage and populate it
	private java.util.List<LocationStorage> allStats = new ArrayList<>();
	
	
	
	public java.util.List<LocationStorage> getAllStats() {
		return allStats;
	}


	public static String getVIRUS_DATA_URL() {
		return VIRUS_DATA_URL;
	}



	@PostConstruct
	@Scheduled(cron = "* 1 * * * *" )
	public void fetchVirusData() throws IOException, InterruptedException {
		java.util.List<LocationStorage> newStats = new ArrayList<>();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(VIRUS_DATA_URL))
				.build();
		HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		
		//send the response
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
		   
			LocationStorage locationStat = new LocationStorage();
			locationStat.setState(record.get("Province/State"));
			locationStat.setCountry(record.get("Country/Region"));
			locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
			
			
			newStats.add(locationStat);
		}
		this.allStats = newStats;
		        
		
	}
}
