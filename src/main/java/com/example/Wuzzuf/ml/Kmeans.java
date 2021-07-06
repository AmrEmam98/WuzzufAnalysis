package com.example.Wuzzuf.ml;

import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;

public class Kmeans {
    Dataset<Row> df;
    public Kmeans(Dataset<WuzzufModel> df){
       this.df=df.toDF();
    }
    public Dataset<Row> factorize(Dataset<Row>rowDataset,String inputColumn, String outputColumn){

        StringIndexer indexer = new StringIndexer().setInputCol(inputColumn).setOutputCol(outputColumn);
        return indexer.fit(rowDataset).transform(rowDataset);
    }

    public void applyKmeans(){
        Dataset<Row> trainningData=prepareData();
        Dataset<Row>[] splits = trainningData.randomSplit(new double[] { 0.8, 0.2 },42);
        Dataset<Row> trainingFeaturesData = splits[0];
        Dataset<Row> testFeaturesData = splits[1];
        KMeans kMeans= new KMeans().setFeaturesCol("Features").setK(5);
        KMeansModel model = kMeans.fit(trainningData);
        System.out.println(Arrays.toString(model.clusterCenters()));
//        model.transform()

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
}
