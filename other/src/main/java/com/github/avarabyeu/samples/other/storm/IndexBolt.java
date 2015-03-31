package com.github.avarabyeu.samples.other.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.common.base.Stopwatch;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import twitter4j.Status;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author avarabyeu
 */
public class IndexBolt extends BaseRichBolt {

    public static final String INDEX_DIRECTORY = "indexDirectory";


    private IndexWriter indexWriter;


    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try {
            indexWriter = new IndexWriter(FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIRECTORY)), iwc);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void cleanup() {
        try {
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Tuple input) {
        Status tweet = (Status) input.getValueByField("tweet");
        String lang = tweet.getUser().getLang();
        String text = tweet.getText().replaceAll("\\p{Punct}", " ").toLowerCase();

        Stopwatch timer = Stopwatch.createStarted();
        Document document = new Document();
        System.out.println("NAME:" + tweet.getUser().getName());
        document.add(new StringField("user", tweet.getUser().getName(), Field.Store.YES));
        document.add(new StringField("lang", lang, Field.Store.YES));


        document.add(new TextField("content", text, Field.Store.NO));
        try {
            indexWriter.addDocument(document);
            indexWriter.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Took: " + timer.elapsed(TimeUnit.MILLISECONDS));

        timer.reset().start();

        searchIndex("germanwings");
        System.out.println("Search Took: " + timer.elapsed(TimeUnit.SECONDS));


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public static void searchIndex(String searchString) {
        try {
            System.out.println("Searching for '" + searchString + "'");
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIRECTORY)));
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser("content", analyzer);
            Query query = queryParser.parse(searchString);


            TopDocs results = indexSearcher.search(query, 100);
            ScoreDoc[] hits = results.scoreDocs;

            System.out.println("Number of hits: " + hits.length);

            for (ScoreDoc hit : hits) {
                Document document = indexSearcher.doc(hit.doc);

                String user = document.get("user");
//                System.out.print("Hit: " + user + " ");
//                System.out.print("Lang:" + document.get("lang") + " ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
