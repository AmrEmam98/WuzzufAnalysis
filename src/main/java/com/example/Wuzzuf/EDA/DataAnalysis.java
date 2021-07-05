package com.example.Wuzzuf.EDA;

import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

public class DataAnalysis {
    Dataset<WuzzufModel> df;
    public DataAnalysis(Dataset<WuzzufModel> df){
        this.df=df;
    }

    public  void getSummary(){
       Dataset<Row> summaryDf=df.describe();
       for (String colName:summaryDf.columns()){
           System.out.println(colName.trim());
           summaryDf.select(colName).as(Encoders.STRING());
       }
    }

}
