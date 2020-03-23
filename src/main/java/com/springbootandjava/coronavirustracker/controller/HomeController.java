package com.springbootandjava.coronavirustracker.controller;

import java.awt.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springbootandjava.coronavirustracker.model.LocationStorage;
import com.springbootandjava.coronavirustracker.services.coronaVirusServicesTracker;

@Controller
public class HomeController {
	// Testing Git new 
	@Autowired
	coronaVirusServicesTracker coronaVirusServicesTracker;
	
	@GetMapping("/")
	public String home(Model model) {
		
		java.util.List<LocationStorage> allStats = coronaVirusServicesTracker.getAllStats();
		int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
		// we can also use setAttribute method
		model.addAttribute("locationStats", allStats );
		model.addAttribute("totalReportedCases", totalReportedCases );
		model.addAttribute("totalNewCases", totalNewCases );
		
		// when there is a mapping to / url, it
		// returns home template
		return "home";
	}
}
