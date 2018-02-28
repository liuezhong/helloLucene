package org.itat.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2018/2/27.
 */
public class HelloLucene {
    /*
    * 建立索引
    * */
    public void index(){
        IndexWriter writer = null;
        try {
//        1、创建Directory(确定索引建立在什么地方，内存或硬盘上）
//            Directory directory = new RAMDirectory();//建立在内存中
            Directory directory = FSDirectory.open(new File("D:/GitHub_workspace/lucene/index01"));//创建在硬盘上
//        2、创建IndexWriter（写索引）
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35));
            writer = new IndexWriter(directory,iwc);
//        3、创建Document对象（索引一篇文档或数据库表）
            Document doc = null;
//        4、为Document添加field（field是文件的属性）
            File f = new File("D:/GitHub_workspace/mylucene/helloLucene/document");
            for(File file:f.listFiles()){
                doc = new Document();
//                文件内容
                doc.add(new Field("content",new FileReader(file)));

//             文件名
//                 第二个参数： 是否把文件的全名存入硬盘，
//                        Field.Store.YES或NO
//                设置为YES表示会把这个域中的内容完全存储到文件中，方便进行文本的还原
//                设置为NO表示会把这个域中的内容不存储到文件中，但是可以被索引，此时内容无法完全还原（doc.get)
//                还原即指通过doc.get获得
//                  第三个参数：是否对文件名进行分词
                doc.add(new Field("filename",file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
//                文件路径
                doc.add(new Field("path",file.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
//        5、通过IndexWriter添加文档到索引中
                writer.addDocument(doc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    * 搜索
    * */
    public void searcher(){
        try {
//        1、创建Directory(去什么地方搜索）
            Directory directory = FSDirectory.open(new File("D:/GitHub_workspace/lucene/index01"));
//        2、创建IndexReader(读取索引）
            IndexReader reader = IndexReader.open(directory);
//        3、根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);
//        4、创建搜索的query
//            创建parser来确定要搜索文件的内容，第二个参数表示搜索的域
            QueryParser parser = new QueryParser(Version.LUCENE_35,"content",new StandardAnalyzer(Version.LUCENE_35));
//              创建query，表示搜索域为content中包含java的文档
            Query query = parser.parse("java");
//        5、根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query,10);
//        6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds){
//        7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document d = searcher.doc(sd.doc);
//        8、根据Document对象获取需要的值
                System.out.println(d.get("filename")+"["+d.get("path")+"]");
            }

//        9、关闭reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
