package com.example.Wuzzuf.dataPreperation;

import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.*;

public class WuzzufDAO {
    public Dataset<Row> readData(String path){
        Logger.getLogger ("org").setLevel (Level.ERROR);
        final SparkSession sparkSession= SparkSession.builder().appName("new Words Count App").master("local[3]")
                .getOrCreate();
        DataFrameReader reader =sparkSession.read();
        reader.option ("header", "true");
        return reader.csv(path);
    }
}
