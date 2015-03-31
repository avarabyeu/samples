package com.github.avarabyeu.samples.other.lucene;

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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystems;

/**
 * Created by andrey.vorobyov on 3/25/15.
 */
public class LuceneDemo {

    public static final String FILES_TO_INDEX_DIRECTORY = "/Users/andrey.vorobyov/own/workspaces/webdriver-demo/src/test/resources/filesToIndex";
    public static final String INDEX_DIRECTORY = "indexDirectory";

    public static final String FIELD_PATH = "path";
    public static final String FIELD_CONTENTS = "contents";

    public static void main(String[] args) throws Exception {

        createIndex();
        searchIndex("mushrooms");
        searchIndex("steak");
        searchIndex("steak AND cheese");
        searchIndex("steak and cheese");
        searchIndex("bacon OR cheese");

    }

    public static void createIndex() throws IOException {
        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIRECTORY)), iwc);

        File dir = new File(FILES_TO_INDEX_DIRECTORY);
        File[] files = dir.listFiles();
        for (File file : files) {
            Document document = new Document();

            String path = file.getCanonicalPath();
            document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

            Reader reader = new FileReader(file);
            document.add(new TextField(FIELD_CONTENTS, reader));

            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }

    public static void searchIndex(String searchString) throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        System.out.println("Searching for '" + searchString + "'");
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIRECTORY)));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query query = queryParser.parse(searchString);


        TopDocs results = indexSearcher.search(query, 100);
        ScoreDoc[] hits = results.scoreDocs;

        System.out.println("Number of hits: " + hits.length);

        for (ScoreDoc hit : hits) {
            Document document = indexSearcher.doc(hit.doc);

            String path = document.get(FIELD_PATH);
            System.out.println("Hit: " + path);
            System.out.println("DOC:" + document.get(FIELD_CONTENTS));
        }

    }

}
