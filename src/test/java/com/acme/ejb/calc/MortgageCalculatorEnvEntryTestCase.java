/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.ejb.calc;

import java.math.BigDecimal;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MortgageCalculatorEnvEntryTestCase {
    @Deployment
    public static Archive<?> createDeployment() {
        // we have to create a war because ejb-jar.xml must be put in WEB-INF
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(MortgageCalculator.class, MortgageCalculatorBean.class)
                .addAsWebInfResource("interest-rate-ejb-jar.xml", "ejb-jar.xml");
    }

    @EJB
    MortgageCalculator calculator;

    @Test
    public void shouldCalculateMonthlyPaymentAccuratelyWithBuiltInRate() {
        Assert.assertEquals("Interest rate should be set by ejb-jar.xml", 5.5, calculator.getCurrentInterestRate());

        double principal = 750000;
        int term = 30;
        BigDecimal expected = new BigDecimal(Double.toString(4258.42));

        BigDecimal actual = calculator.calculateMonthlyPayment(principal, term);
        Assert.assertEquals("A banking error has been detected!", expected, actual);
    }
}
