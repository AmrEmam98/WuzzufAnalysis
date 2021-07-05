package com.example.Wuzzuf.dataPreperation;

import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.sql.*;

public class DataPreProcessor {

    public Dataset<WuzzufModel> prepareData(String path) {
        Dataset<Row> rowDataset = readData(path);
        return cleanData(rowDataset);
    }

    private Dataset<Row> readData(String path) {
        WuzzufDAO wuzzufDAO = new WuzzufDAO();
        return wuzzufDAO.readData(path);
    }

    private Dataset<WuzzufModel> cleanData(Dataset<Row> rowDataset) {
        return rowDataset.na().drop().dropDuplicates().as(Encoders.bean(WuzzufModel.class));
    }


}
