package com.github.avarabyeu.samples.other;

import com.google.common.base.Charsets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andrey.vorobyov on 3/24/15.
 */
public class FlightTest {

    public static void main(String... args) throws IOException {
        List<FlightData> flightData = Resources.asCharSource(new URL("http://www.flightradar24.com/GWI18G_20150324.csv"), Charsets.UTF_8).readLines(new LineProcessor<List<FlightData>>() {

            private List<FlightData> flightData = new ArrayList<FlightData>();

            private AtomicInteger currentLine = new AtomicInteger(0);

            @Override
            public boolean processLine(String line) throws IOException {
                if (1 == currentLine.incrementAndGet()) {
                    return true;
                }
                String[] data = line.split(",");
                if (data.length <= 1){
                    return true;
                }
                FlightData fd = new FlightData(
                        data[0],
                        data[1],
                        data[2],
                        Float.valueOf(data[3]),
                        Float.valueOf(data[4]),
                        data[5],
                        Long.valueOf(data[6]),
                        Long.valueOf(data[7]),
                        Long.valueOf(data[8]),
                        Long.valueOf(data[9]),
                        Long.valueOf(data[10]),
                        data[11],
                        Long.valueOf(data[12]));
                return flightData.add(fd);
            }

            @Override
            public List<FlightData> getResult() {
                return flightData;
            }
        });

        Gson gson = new Gson();
        System.out.println(gson.toJson(flightData));
    }
}
