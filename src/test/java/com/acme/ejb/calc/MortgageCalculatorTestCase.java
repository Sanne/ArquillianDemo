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
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MortgageCalculatorTestCase {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(MortgageCalculator.class, MortgageCalculatorBean.class);
    }

    @EJB
    MortgageCalculator calculator;

    @Test
    public void shouldCalculateMonthlyPaymentAccurately() {
        // calculator = new MortgageCalculatorBean();

        double principal = 750000;
        double rate = 7.5;
        int term = 30;
        BigDecimal expected = new BigDecimal(Double.toString(5244.11));

        BigDecimal actual = calculator.calculateMonthlyPayment(principal, rate, term);
        Assert.assertEquals("A banking error has been detected!", expected, actual);

        principal = 2500000;
        rate = 5.5;
        term = 30;
        expected = new BigDecimal(Double.toString(14194.72));

        actual = calculator.calculateMonthlyPayment(principal, rate, term);
        Assert.assertEquals("A banking error has been detected!", expected, actual);
    }
}
