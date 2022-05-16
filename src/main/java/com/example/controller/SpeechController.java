package com.example.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.response.SpeechResponse;

@RestController
@RequestMapping("/evaluation")
public class SpeechController {

	@GetMapping("/")
	public ResponseEntity<SpeechResponse> generateResponse(@RequestParam Map<String, String> params){
		
		Set<String> paramsKeys = params.keySet();
		
		Map<String,Integer> speakerVsSpeechesMap = new HashMap<>();
        Map<String,Integer> speakerVsTopicMap = new HashMap<>();
        Map<String, Integer> speakerVsWordsMap = new HashMap<>();
        Entry<String, Integer> leastWordy = null;
        int mostSpeechesIn2013 = 0;
        int mostSecuritySpeeches = 0;
        String mostSpeechesSpeaker = null;
        String mostSecuritySpeaker = null;
        
        SpeechResponse response = new SpeechResponse();
		
		for(String key : paramsKeys) {
			
			String value = params.get(key);
			try {
				URL url = new URL(value);
				CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
				try(CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat)) {
					
		            for(CSVRecord csvRecord : csvParser) {
		                String speaker = csvRecord.get("Speaker");
		                String topic = csvRecord.get("Topic");
		                String date = csvRecord.get("Date");
		                String words = csvRecord.get("Words");

//		                System.out.println(speaker + "," + topic + "," + date + "," + words);
		                
		                //Question-1
		                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		                LocalDate dateObj = LocalDate.parse(date, formatter);
		                if(dateObj.getYear() == 2013) {
		                	if(speakerVsSpeechesMap.containsKey(speaker)) {
		                		int speechCount = speakerVsSpeechesMap.get(speaker);
		                		speakerVsSpeechesMap.put(speaker, speechCount++);
		                	}
		                	else {
		                		speakerVsSpeechesMap.put(speaker, 1);
		                	}
		                	if(mostSpeechesIn2013 < speakerVsSpeechesMap.get(speaker)) {
			                	mostSpeechesIn2013 = speakerVsSpeechesMap.get(speaker);
			                	mostSpeechesSpeaker = speaker;
			                }
		                }
		                
		                //Question-2
		                if(topic.equals("Internal Security")) {
		                	if(speakerVsTopicMap.containsKey(speaker)) {
		                		int topicCount = speakerVsTopicMap.get(speaker);
		                		speakerVsTopicMap.put(speaker, topicCount++);
		                	}
		                	else {
		                		speakerVsTopicMap.put(speaker, 1);
		                	}
		                	if(mostSecuritySpeeches < speakerVsTopicMap.get(speaker)) {
		                		mostSecuritySpeeches = speakerVsTopicMap.get(speaker);
		                		mostSecuritySpeaker = speaker;
		                	}
		                }
		                
		                //Question-3
		                if(speakerVsWordsMap.containsKey(speaker)) {
		                	int wordCount = speakerVsWordsMap.get(speaker);
		                	wordCount = wordCount + Integer.parseInt(words);
		                	speakerVsWordsMap.put(speaker, wordCount);
		                }
		                else {
		                	speakerVsWordsMap.put(speaker, Integer.parseInt(words));
		                }
		            }
		            leastWordy = Collections.min(speakerVsWordsMap.entrySet(), Comparator.comparing(Entry::getValue));
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		try {
			response.setMostSpeeches(mostSpeechesSpeaker);
	        response.setMostSecurity(mostSecuritySpeaker);
	        response.setLeastWordy(leastWordy.getKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
