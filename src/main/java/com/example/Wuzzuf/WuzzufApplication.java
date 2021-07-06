package com.example.Wuzzuf;

import com.example.Wuzzuf.EDA.DataAnalysis;
import com.example.Wuzzuf.dataPreperation.DataPreProcessor;
import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.sql.Dataset;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController

public class WuzzufApplication {

	public static void main(String[] args) {
		SpringApplication.run(WuzzufApplication.class, args);
	}
	@GetMapping(path = "api/summary")
	List<Map.Entry<String,Integer>> getSummary(){
		String path="src/main/resources/data/Wuzzuf_Jobs.csv";
		DataPreProcessor dataPreProcessor=new DataPreProcessor();
		Dataset<WuzzufModel> wuzzufData=dataPreProcessor.prepareData(path);
		wuzzufData.printSchema();
		DataAnalysis analysis=new DataAnalysis(wuzzufData);
		return  analysis.getMostPopularAreas();
	}
	@GetMapping(path = "api/charts")
	ResponseEntity<byte[]> getImage(){

		String path="charts/Most popular jobs.jpg";
		ClassPathResource imgFile = new ClassPathResource(path);
		byte[] bytes = new byte[0];
		try {
			bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity
				.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(bytes);
	}
}
