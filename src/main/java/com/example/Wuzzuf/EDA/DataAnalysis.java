package com.example.Wuzzuf.EDA;

import com.example.Wuzzuf.Constants;
import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.scalatest.Entry;

import java.util.*;
import java.util.stream.Collectors;

public class DataAnalysis {
    Dataset<WuzzufModel> df;
    SparkSession sparkSession;
    DataVisualization dataVisualization;
    public DataAnalysis(Dataset<WuzzufModel> df) {
        this.df = df;
        dataVisualization=new DataVisualization();
        sparkSession=SparkSession.builder().appName("Wuzzuf").master("local[3]").getOrCreate();
    }

    public List<Map.Entry<String, Long>> getMostSkills(){
        Map<String, Long> skills = df.select("Skills").as(Encoders.STRING()).filter(StringUtils::isNotBlank)
                .flatMap(s -> Arrays.asList(s.toLowerCase()
                        .trim()
                        .split(",")).iterator(), Encoders.STRING()).collectAsList().stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        List<Map.Entry<String, Long>> sorted = skills.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
        return sorted;
    }


    public List<Map.Entry<String ,Integer>> getJobsPerCompany() {
        List<Map.Entry<String ,Integer>> columnValuesCount = getColumnValuesCount("Company");
        dataVisualization.pieChart(columnValuesCount, Constants.MOST_POPULAR_COMPANIES);
        return columnValuesCount;
    }

    public List<Map.Entry<String ,Integer>> getMostPopularJobs(){
        List<Map.Entry<String ,Integer>>columnValuesCount = getColumnValuesCount("Title");
        dataVisualization.categoryChart(columnValuesCount,Constants.MOST_POPULAR_JOBS,"jobs");
        return columnValuesCount;

    }
      public List<Map.Entry<String ,Integer>> getMostPopularAreas(){
          List<Map.Entry<String ,Integer>> columnValuesCount = getColumnValuesCount("Location");
          dataVisualization.categoryChart(columnValuesCount,Constants.MOST_POPULAR_AREAS,"Areas");
          return columnValuesCount;

    }


    private List<Map.Entry<String ,Integer>> getColumnValuesCount(String columnName){

        df.createOrReplaceTempView("wuzzuf");
        Dataset<Row> sql = sparkSession.sql("select "+columnName+ ",CAST(count(*) AS INT) as count from wuzzuf group by "+columnName+" order by count DESC ");
        List<String> columnValues = sql.select(columnName).as(Encoders.STRING()).collectAsList();
        List<Integer> count = sql.select("count").as(Encoders.INT()).collectAsList();
        return concatTwoLists(columnValues,count);
    }


    public <T>List<Map.Entry<String ,T>> concatTwoLists(List<String> colValues, List<T> count) {
        List<Map.Entry<String ,T>> outList = new ArrayList<>();
        for (int i = 0; i < colValues.size(); i++) {
            outList.add(new Entry(colValues.get(i), count.get(i)));
        }
        return outList;
    }

    private Map<String, String> convertDFToMap(List<String> rowName, List<String> stringColumn) {
        Map<String, String> colSummary = new HashMap<>();
        for (int i = 0; i < stringColumn.size(); i++) {
            colSummary.put(rowName.get(i), stringColumn.get(i));
        }
        return colSummary;
    }

    public Map<String, Map<String, String>> getSummary() {

        Dataset<Row> summaryDf = df.describe();
        Map<String, Map<String, String>> summaryMap = new HashMap<>();
        List<String> rowName = new ArrayList<>();
        for (String colName : summaryDf.columns()) {
            List<String> stringColumn = summaryDf.select(colName).as(Encoders.STRING()).collectAsList();
            //if first column [count,mean,min,max,stddev] save it in rowName
            if (colName.equals("summary")) {
                rowName = stringColumn;
                continue;
            }
            Map<String, String> colSummary = convertDFToMap(rowName, stringColumn);
            summaryMap.put(colName, colSummary);
        }
        return summaryMap;

    }

}
