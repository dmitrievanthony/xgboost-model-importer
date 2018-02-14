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

package com.dmitrievanthony.xgboost.model.importer.parser;

import com.dmitrievanthony.xgboost.model.importer.MapBasedXGObject;
import com.dmitrievanthony.xgboost.model.importer.XGModel;
import java.util.Scanner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link XGBoostModelParser}.
 */
public class XGBoostModelParserTest {
    /** Parser. */
    private final XGModelParser parser = new XGModelParser();

    /** End-to-end test for {@code parse()} and {@code predict()} methods. */
    @Test
    public void testParseAndPredict() {
        XGModel mdl = parser.parseResource("agaricus-model.txt");

        Scanner testDataScanner = new Scanner(
            XGBoostModelParserTest.class.getClassLoader()
                .getResourceAsStream("agaricus-test-data.txt")
        );

        Scanner testExpResultsScanner = new Scanner(
            XGBoostModelParserTest.class.getClassLoader()
                .getResourceAsStream("agaricus-test-expected-results.txt")
        );

        while (testDataScanner.hasNextLine()) {
            assertTrue(testExpResultsScanner.hasNextLine());

            String testDataStr = testDataScanner.nextLine();
            String testExpResultsStr = testExpResultsScanner.nextLine();

            MapBasedXGObject testObj = new MapBasedXGObject();

            for (String keyValueString : testDataStr.split(" ")) {
                String[] keyVal = keyValueString.split(":");

                if (keyVal.length == 2)
                    testObj.put("f" + keyVal[0], Double.parseDouble(keyVal[1]));
            }

            double prediction = mdl.predict(testObj);

            double expPrediction = Double.parseDouble(testExpResultsStr);

            assertEquals(expPrediction, prediction, 1e-6);
        }
    }
}