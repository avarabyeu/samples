package com.github.avarabyeu.samples.other;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.github.avarabyeu.samples.other.storm.IndexBolt;
import com.github.avarabyeu.samples.other.storm.TwitterSampleSpout;


/**
 * Created by andrey.vorobyov on 3/24/15.
 */
public class StormTest {
    static final String TOPOLOGY_NAME = "storm-twitter-word-count";


    public static void main(String... args) {
        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);

        final LocalCluster cluster = new LocalCluster();
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("TwitterSampleSpout", new TwitterSampleSpout());
        topologyBuilder.setBolt("IndexBolt", new IndexBolt()).shuffleGrouping("TwitterSampleSpout");
        topologyBuilder.setBolt("WordSplitterBolt", new com.github.avarabyeu.samples.other.WordSplitterBolt(5)).shuffleGrouping("TwitterSampleSpout");
        topologyBuilder.setBolt("IgnoreWordsBolt", new com.github.avarabyeu.samples.other.IgnoreWordsBolt()).shuffleGrouping("WordSplitterBolt");
        topologyBuilder.setBolt("WordCounterBolt", new com.github.avarabyeu.samples.other.WordCounterBolt(10, 5 * 60, 50)).shuffleGrouping("IgnoreWordsBolt");


        cluster.submitTopology(TOPOLOGY_NAME, conf, topologyBuilder.createTopology());


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cluster.killTopology(TOPOLOGY_NAME);
                cluster.shutdown();
            }
        });


    }
}
