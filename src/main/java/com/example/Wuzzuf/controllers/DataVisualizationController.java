package com.example.Wuzzuf.controllers;


import com.example.Wuzzuf.Constants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/charts")
public class DataVisualizationController {

    @GetMapping(path = "mostCompanies")
    ResponseEntity<byte[]> getCompaniesChart(){
        return getImage(Constants.MOST_POPULAR_COMPANIES);
    }
    @GetMapping(path = "mostJobs")
     ResponseEntity<byte[]> getJobsChart(){
        return getImage(Constants.MOST_POPULAR_JOBS);
    }

     @GetMapping(path = "mostAreas")
     ResponseEntity<byte[]> getAreasChart(){
        return getImage(Constants.MOST_POPULAR_AREAS);
    }


    ResponseEntity<byte[]> getImage(String chartName){

        String path="charts/"+ chartName+ ".jpg";
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
