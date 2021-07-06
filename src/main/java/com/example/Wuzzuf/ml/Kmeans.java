package com.example.Wuzzuf.ml;

import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Kmeans {
    Dataset<Row> df;
    public Kmeans(Dataset<WuzzufModel> df){
       this.df=df.toDF();
    }
    public Dataset<Row> factorize(Dataset<Row>rowDataset,String inputColumn, String outputColumn){

        StringIndexer indexer = new StringIndexer().setInputCol(inputColumn).setOutputCol(outputColumn);
        return indexer.fit(rowDataset).transform(rowDataset);
    }

    public Map<String, String> applyKmeans(){
        Dataset<Row> trainningData=prepareData();
        KMeans kMeans= new KMeans().setFeaturesCol("Features").setK(5);
        KMeansModel model = kMeans.fit(trainningData);
        double wsse = model.computeCost(trainningData);
         String cluster=Arrays.toString(model.clusterCenters());

        Map<String,String> out=new HashMap<>();
        out.put("Sum Of square Error", Double.toString(wsse));
        out.put("Cluster Centers",cluster);
        return out;

    }

    private Dataset<Row> prepareData() {
        Dataset<Row> factorizedCompany=factorize(df,"Company","CompanyEncoded");
        Dataset<Row> factorizedTitle = factorize(factorizedCompany, "Title", "TitleEncoded");
        VectorAssembler vectorAssembler=new VectorAssembler();
        String[] inputColumns={"CompanyEncoded","TitleEncoded"};
        vectorAssembler.setInputCols(inputColumns);
        vectorAssembler.setOutputCol("Features");
        return vectorAssembler.transform(factorizedTitle);
    }

    public Dataset<Row> factorizeYearsExp(){

        return factorize(this.df,"YearsExp","YearsExpEncoded");
    }


}
