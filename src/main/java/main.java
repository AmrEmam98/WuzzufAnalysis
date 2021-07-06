import com.example.Wuzzuf.EDA.DataAnalysis;
import com.example.Wuzzuf.dataPreperation.DataPreProcessor;
import com.example.Wuzzuf.ml.Kmeans;
import com.example.Wuzzuf.models.WuzzufModel;
import org.apache.spark.sql.*;

public class main {
    public static void main(String[] args) {
        String path = "src/main/resources/data/Wuzzuf_Jobs.csv";
        DataPreProcessor dataPreProcessor = new DataPreProcessor();
        Dataset<WuzzufModel> wuzzufData = dataPreProcessor.prepareData(path);
//        wuzzufData.describe().show();
        wuzzufData.printSchema();

        DataAnalysis analysis = new DataAnalysis(wuzzufData);
//        analysis.getSummary();
//        analysis.getJobsPerCompany();
//        analysis.getMostPopularAreas();
//        analysis.getMostPopularJobs();
//        System.out.println(analysis.getMostSkills());
        Kmeans kmeans=new Kmeans(wuzzufData);
        kmeans.applyKmeans();
    }

}
