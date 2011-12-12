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
import java.math.MathContext;
import java.math.RoundingMode;
import javax.annotation.Resource;
import javax.ejb.Stateless;

@Stateless
public class MortgageCalculatorBean implements MortgageCalculator {

	private static final BigDecimal TWELVE = new BigDecimal( 12 );
	private static final BigDecimal ONE_HUNDRED = new BigDecimal( 100 );
	private static final int CURRENCY_DECIMALS = 2;

	@Resource(name = "interestRate")
	private Double interestRate;

	/**
	 * a = [ P(1 + r)^Y * r ] / [ (1 + r)^Y - 1 ]
	 */
	@Override
	public BigDecimal calculateMonthlyPayment(double principal, double interestRate, int termYears) {
		BigDecimal p = new BigDecimal( principal );
		int divisionScale = p.precision() + CURRENCY_DECIMALS;
		BigDecimal r = new BigDecimal( interestRate ).divide( ONE_HUNDRED, MathContext.UNLIMITED ).divide( TWELVE, divisionScale,
				RoundingMode.HALF_EVEN );
		BigDecimal z = r.add( BigDecimal.ONE );
		BigDecimal tr = new BigDecimal( Math.pow( z.doubleValue(), termYears * 12 ) );
		return p.multiply( tr ).multiply( r ).divide( tr.subtract( BigDecimal.ONE ), divisionScale, RoundingMode.HALF_EVEN )
				.setScale( CURRENCY_DECIMALS, RoundingMode.HALF_EVEN );
	}

	@Override
	public BigDecimal calculateMonthlyPayment(double principal, int termYears) {
		if ( interestRate == null ) {
			throw new IllegalStateException( "No interest rate has been specified." );
		}
		return calculateMonthlyPayment( principal, interestRate, termYears );
	}

	@Override
	public double getCurrentInterestRate() {
		if ( interestRate == null ) {
			throw new IllegalStateException( "No interest rate has been specified." );
		}
		return interestRate;
	}

}
