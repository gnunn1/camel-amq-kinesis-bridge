/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.demo;

import org.apache.camel.CamelContext;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Random;

/**
 * To generate random orders
 */
@Component
public class TransactionGenerator {

    private int count = 1;
    private Random random = new Random();

    public InputStream generateTransaction(CamelContext camelContext) {
        int number = random.nextInt(5) + 1;

        String name = "data/transaction" + number + ".json";

        return camelContext.getClassResolver().loadResourceAsStream(name);
    }

    public String generateFileName() {
        return "transaction" + count++ + ".json";
    }
}