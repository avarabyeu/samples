package com.github.avarabyeu.samples.other;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrey.vorobyov on 3/24/15.
 */
public class WordCounterBolt extends BaseRichBolt {
    private static final Logger logger = LoggerFactory.getLogger(WordCounterBolt.class);
    /**
     * Number of milliseconds before the top list will be logged to stdout.
     */
    private final long logInterval;
    /**
     * Number of milliseconds before the top list will be cleared.
     */
    private final long clearInterval;
    /**
     * Number of top words to store in stats.
     */
    private final int topListSize;

    private Map<String, Long> counter;

    private Stopwatch logStopwatch;
    private Stopwatch clearStopwatch;

    public WordCounterBolt(long logIntervalSec, long clearIntervalSec, int topListSize) {
        this.logInterval = TimeUnit.SECONDS.toMillis(logIntervalSec);
        this.clearInterval = TimeUnit.SECONDS.toMillis(clearIntervalSec);
        this.topListSize = topListSize;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        counter = new HashMap<>();

        logStopwatch = Stopwatch.createStarted();
        clearStopwatch = Stopwatch.createStarted();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    @Override
    public void execute(Tuple input) {
        String word = (String) input.getValueByField("word");
        Long count = counter.get(word);

        counter.put(word, count == null ? 1L : count + 1);


        if (logStopwatch.elapsed(TimeUnit.MILLISECONDS) > logInterval) {
            logger.info("Word count: " + counter.size());

            publishTopList();
            logStopwatch.reset().start();
        }
    }

    private void publishTopList() {
        // calculate top list:
        SortedMap<Long, String> top = new TreeMap<>();
        for (Map.Entry<String, Long> entry : counter.entrySet()) {
            long count = entry.getValue();
            String word = entry.getKey();

            top.put(count, word);
            if (top.size() > topListSize) {
                top.remove(top.firstKey());
            }
        }


        // Output top list:
        for (Map.Entry<Long, String> entry : top.entrySet()) {
            logger.info(new StringBuilder("top - ").append(entry.getValue()).append('>').append(entry.getKey()).toString());
        }

        if (clearStopwatch.elapsed(TimeUnit.MILLISECONDS) > clearInterval) {
            counter.clear();
            clearStopwatch.reset().start();
        }
    }
}
