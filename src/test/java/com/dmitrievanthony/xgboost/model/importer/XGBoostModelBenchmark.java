/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmitrievanthony.xgboost.model.importer;

import com.dmitrievanthony.xgboost.model.importer.parser.XGModelParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * Benchmarks for {@link XGModel}.
 */
public class XGBoostModelBenchmark {
    /** Launch benchmark. */
    @Test
    public void launchBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(this.getClass().getName() + ".*")
            .mode(Mode.Throughput)
            .timeUnit(TimeUnit.SECONDS)
            .warmupTime(TimeValue.seconds(1))
            .warmupIterations(5)
            .measurementTime(TimeValue.seconds(1))
            .measurementIterations(10)
            .threads(4)
            .forks(2)
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .build();

        new Runner(opt).run();
    }

    /** Benchmark. */
    @Benchmark
    public void benchmark(XGBoostModelBenchmarkState state, Blackhole blackhole) {
        int objIdx = state.random.nextInt(state.objects.size());
        blackhole.consume(state.mdl.predict(state.objects.get(objIdx)));
    }

    /** Benchmark state. */
    @State(Scope.Thread)
    public static class XGBoostModelBenchmarkState {
        /** XGBoost model. */
        private XGModel mdl;

        /** Test objects. */
        private final List<XGObject> objects = new ArrayList<>();

        /** Random. */
        private final Random random = new Random(0);

        /** Initializes scope. */
        @Setup(Level.Trial)
        public void initialize() {
            mdl = new XGModelParser().parseResource("agaricus-model.txt");

            Scanner testDataScanner = new Scanner(
                XGBoostModelBenchmark.class.getClassLoader().getResourceAsStream("agaricus-test-data.txt")
            );

            while (testDataScanner.hasNextLine()) {
                String testDataStr = testDataScanner.nextLine();

                MapBasedXGObject testObj = new MapBasedXGObject();

                for (String keyValueString : testDataStr.split(" ")) {
                    String[] keyVal = keyValueString.split(":");

                    if (keyVal.length == 2)
                        testObj.put("f" + keyVal[0], Double.parseDouble(keyVal[1]));
                }

                objects.add(testObj);
            }
        }
    }
}
