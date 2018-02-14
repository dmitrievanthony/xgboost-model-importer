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

import java.util.List;

/**
 * XGBoost model.
 */
public class XGModel {
    /** List of decision trees. */
    private final List<XGNode> trees;

    /**
     * Constructs a new XGBoost model.
     *
     * @param trees List of XGBoost trees.
     */
    public XGModel(List<XGNode> trees) {
        this.trees = trees;
    }

    /**
     * Predicts label for the specified object.
     *
     * @param obj Object.
     * @return Label.
     */
    public double predict(XGObject obj) {
        double res = 0;

        for (XGNode tree : trees)
            res += tree.predict(obj);

        return (1.0 / (1.0 + Math.exp(-res)));
    }
}