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

import com.dmitrievanthony.xgboost.model.importer.XGModel;
import com.dmitrievanthony.xgboost.model.importer.parser.visitor.XGModelVisitor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/** XGBoost model parser. */
public class XGModelParser {
    /**
     * Parses specified resource with XGBoost model.
     *
     * @param rsrcName Resource name.
     * @return XGBoost model.
     */
    public XGModel parseResource(String rsrcName) {
        try (InputStream is = XGModelParser.class.getClassLoader().getResourceAsStream(rsrcName)) {
            return parse(is);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses specified file with XGBoost model.
     *
     * @param fileName File name.
     * @return XGBoost model.
     */
    public XGModel parseFile(String fileName) {
        try (InputStream is = new FileInputStream(fileName)) {
            return parse(is);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses specified input stream with XGBoost model.
     *
     * @param inputStream Input stream.
     * @return XGBoost model.
     */
    private XGModel parse(InputStream inputStream) {
        try {
            CharStream cStream = CharStreams.fromStream(inputStream);
            XGBoostModelLexer lexer = new XGBoostModelLexer(cStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            XGBoostModelParser parser = new XGBoostModelParser(tokens);

            XGModelVisitor visitor = new XGModelVisitor();

            return visitor.visit(parser.xgModel());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
