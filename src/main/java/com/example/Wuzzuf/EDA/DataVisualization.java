package com.example.Wuzzuf.EDA;

import org.knowm.xchart.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataVisualization {


 public  void pieChart(List<Map.Entry<String ,Integer>>columnValuesCount,String chartTitle){
     PieChart chart=new PieChartBuilder().height(700).width(1020)
             .title(chartTitle).build();
     int counter=0;
     for(Map.Entry<String,Integer> mapEntry:columnValuesCount){
         if(counter==10)
             break;
         chart.addSeries( mapEntry.getKey(),mapEntry.getValue());
         counter++;
     }
     new SwingWrapper<PieChart>(chart).displayChart();
     try {
         String path="D:\\iti\\5 Java & UML_Amr Elshafey\\project\\Wuzzuf\\src\\main\\resources\\charts\\";
         BitmapEncoder.saveBitmap(chart,path+chartTitle, BitmapEncoder.BitmapFormat.JPG);
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
 public  void categoryChart(List<Map.Entry<String ,Integer>>columnValuesCount,String chartTitle,String colName)
 {
     CategoryChart chart=new CategoryChartBuilder().height(700).width(1020)
             .title(chartTitle).xAxisTitle(colName).yAxisTitle("count")
             .build();

     List<String> names = columnValuesCount.stream().map(Map.Entry::getKey).limit(10).collect(Collectors.toList());
     List<Integer> count = columnValuesCount.stream().map(Map.Entry::getValue).limit(10).collect(Collectors.toList());
     chart.addSeries(colName,names,count);
     new SwingWrapper<CategoryChart>(chart).displayChart();
     try {
         String path="D:\\iti\\5 Java & UML_Amr Elshafey\\project\\Wuzzuf\\src\\main\\resources\\charts\\";
         BitmapEncoder.saveBitmap(chart,path+chartTitle, BitmapEncoder.BitmapFormat.JPG);
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

}
