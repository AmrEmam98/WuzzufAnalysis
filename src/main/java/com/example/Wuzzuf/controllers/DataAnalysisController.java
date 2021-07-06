package com.example.Wuzzuf.controllers;


import com.example.Wuzzuf.EDA.DataAnalysis;
import com.example.Wuzzuf.dataPreperation.DataPreProcessor;
import com.example.Wuzzuf.ml.Kmeans;
import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/analysis")
public class DataAnalysisController {
    String path="src/main/resources/data/Wuzzuf_Jobs.csv";
    DataPreProcessor dataPreProcessor;
    Dataset<WuzzufModel> wuzzufData;
    DataAnalysis analysis;
    Kmeans kmeans;
    DataAnalysisController(){
        dataPreProcessor=new DataPreProcessor();
        wuzzufData=dataPreProcessor.prepareData(path);
        analysis=new DataAnalysis(wuzzufData);
        kmeans=new Kmeans(wuzzufData);
    }

    @GetMapping("summary")
    Map<String, Map<String,String>> getSummary(){
        return  analysis.getSummary();
    }
    @GetMapping("schema")
    String getSchema(){
        return wuzzufData.schema().toString();
    }
    @GetMapping("mostSkills")
    List<Map.Entry<String, Long>> getMostSkills(){
        return analysis.getMostSkills();
    }
    @GetMapping("mostArea")
    List<Map.Entry<String, Integer>> getMostArea(){
        return analysis.getMostPopularAreas();
    }
    @GetMapping("mostJobs")
    List<Map.Entry<String, Integer>> getMostJobs(){
        return analysis.getMostPopularJobs();
    }
    @GetMapping("mostCompanies")
    List<Map.Entry<String, Integer>> getMostCompanies(){
        return analysis.getJobsPerCompany();
    }

    @GetMapping("factorize")
    List<Map.Entry<String, Double>> factorizeYearsExp(){
        Dataset<Row> factorizeYearsExp = kmeans.factorizeYearsExp();
        List<String> yearsExp = factorizeYearsExp.select("YearsExp").as(Encoders.STRING()).collectAsList();
        factorizeYearsExp.printSchema();
        factorizeYearsExp.show();
        List<Double> yearsExpEncoded = factorizeYearsExp.select("YearsExpEncoded").as(Encoders.DOUBLE()).collectAsList();
       return analysis.concatTwoLists(yearsExp,yearsExpEncoded);
    }


    @GetMapping("kmeans")
    Map<String, String> applyKmeans(){
        return kmeans.applyKmeans();
    }



}
