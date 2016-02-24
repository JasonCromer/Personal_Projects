package com.example.jason.simplefraction;


import android.support.annotation.NonNull;

@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
public class SimpleFraction implements SimpleFractionInterface, Comparable<SimpleFraction> {

    //Integer numerator and denominator
    private int num;
    private int den;


    //Constructor that sets fraction to 0/1
    public SimpleFraction(){
        num = 0;
        den = 1;
    }


    //Constructor that sets fraction via parameters
    public SimpleFraction(int num, int den){

        //Throw exception in case of zero denominator
        if(den == 0){
            throwException();
        }
        else{
            this.num = num;
            this.den = den;
        }
    }


    @Override
    public int compareTo(@NonNull SimpleFraction other) {

        //Convert SimpleFractions to doubles and compare
        double thisDouble = this.toDouble();
        double otherDouble = other.toDouble();

        double result = Double.compare(thisDouble, otherDouble);

        //Return int value of double (either negative, 0, or positive result)
        return (int) result;
    }


    @Override
    public void setSimpleFraction(int num, int den) {

        //Set the fraction, overwriting old fraction
        if(den == 0){
            throwException();
        }
        else{
            this.num = num;
            this.den = den;
        }
    }


    @Override
    public double toDouble() {

        //return double floating point value
        return (double)(num) / (double)(den);
    }


    @Override
    public SimpleFractionInterface add(SimpleFractionInterface secondFraction) {

        //Used when accessing formatted fraction array
        final int numIndex = 0;
        final int denIndex = 1;

        // Downcast interface object to SimpleFraction object
        SimpleFraction fractionTwo = (SimpleFraction) secondFraction;

        //Convert secondFraction into a string
        String fractionTwoString = fractionTwo.toString();

        //convert current fraction into a string
        String fractionOneString = this.toString();

        //Assign a/b and c/d using fractionOneString and fractionTwoString
        int a = getIntNumerator(fractionOneString);
        int b = getIntDenominator(fractionOneString);
        int c = getIntNumerator(fractionTwoString);
        int d = getIntDenominator(fractionTwoString);

        //Add using formnula: a/b + c/d is (ad + cb)/(bd)
        int newNum = (a * d) + (c * b);
        int newDen = b * d;

        //Get our formatted fraction
        int[] formattedFraction = formatProperFraction(newNum, newDen);

        //Create new Simple fraction object with our formatted num and den
        SimpleFraction resultFraction = new SimpleFraction(formattedFraction[numIndex],
                formattedFraction[denIndex]);

        //Reduce fraction
        resultFraction.reduceSimpleFractionToLowestTerms();

        return resultFraction;
    }


    @Override
    public SimpleFractionInterface subtract(SimpleFractionInterface secondFraction) {

        //Used when accessing formatted fraction array
        final int numIndex = 0;
        final int denIndex = 1;

        //Downcast secondFraction to SimpleFraction
        SimpleFraction fractionTwo = (SimpleFraction) secondFraction;

        //Convert fractions to strings
        String fractionOneString = this.toString();
        String fractionTwoString = fractionTwo.toString();

        //Assign a/b and c/d using fractionOneString and fractionTwoString
        int a = getIntNumerator(fractionOneString);
        int b = getIntDenominator(fractionOneString);
        int c = getIntNumerator(fractionTwoString);
        int d = getIntDenominator(fractionTwoString);

        //Subtract using formula: a/b - c/d is (ad - cb)/(bd)
        int newNum = (a * d) - (c * b);
        int newDen = b * d;

        //Get formatted fraction
        int[] formattedFraction = formatProperFraction(newNum, newDen);

        //Create new simple fraction object with formatted num and den
        SimpleFraction resultFraction = new SimpleFraction(formattedFraction[numIndex],
                formattedFraction[denIndex]);

        //Reduce fraction
        resultFraction.reduceSimpleFractionToLowestTerms();

        return resultFraction;
    }


    @Override
    public SimpleFractionInterface multiply(SimpleFractionInterface secondFraction) {

        //Used when accessing formatted fraction array
        final int numIndex = 0;
        final int denIndex = 1;

        //Downcast secondFraction to a SimpleFraction
        SimpleFraction fractionTwo = (SimpleFraction) secondFraction;

        //Convert fractions to strings
        String fractionOneString = this.toString();
        String fractionTwoString = fractionTwo.toString();

        //Assign a/b and c/d using fractionOneString and fractionTwoString
        int a = getIntNumerator(fractionOneString);
        int b = getIntDenominator(fractionOneString);
        int c = getIntNumerator(fractionTwoString);
        int d = getIntDenominator(fractionTwoString);

        //Multiply using formula: a/b * c/d is (ac)/(bd)
        int newNum = a * c;
        int newDen = b * d;

        //Get formatted fraction
        int[] formattedFraction = formatProperFraction(newNum, newDen);

        //Create new SimpleFraction object with formatted num and den
        SimpleFraction resultFraction = new SimpleFraction(formattedFraction[numIndex],
                formattedFraction[denIndex]);

        //Reduce fraction
        resultFraction.reduceSimpleFractionToLowestTerms();

        return resultFraction;
    }


    @Override
    public SimpleFractionInterface divide(SimpleFractionInterface secondFraction) {

        //Used when accessing formatted fraction array
        final int numIndex = 0;
        final int denIndex = 1;

        //Downcase secondFraction to SimpleFraction
        SimpleFraction fractionTwo = (SimpleFraction) secondFraction;

        //Convert fractions to strings
        String fractionOneString = this.toString();
        String fractionTwoString = fractionTwo.toString();

        //Assign a/b and c/d using fractionOneString and fractionTwoString
        int a = getIntNumerator(fractionOneString);
        int b = getIntDenominator(fractionOneString);
        int c = getIntNumerator(fractionTwoString);
        int d = getIntDenominator(fractionTwoString);

        //Check to see if denominator is 0. If so, throw exception
        if(b == 0|| d == 0){
            throwException();
        }

        //Divide using formula: a/b / c/d is (ad)/(bc)
        int newNum = a * d;
        int newDen = b * c;

        //Get formatted fraction
        int[] formattedFraction = formatProperFraction(newNum, newDen);

        //Create new SimpleFraction object with formatted num and den
        SimpleFraction resultFraction = new SimpleFraction(formattedFraction[numIndex],
                formattedFraction[denIndex]);

        //Reduce fraction
        resultFraction.reduceSimpleFractionToLowestTerms();

        return resultFraction;
    }


    @Override
    public SimpleFractionInterface getReciprocal() {

        //Create ints with switched numerator and denominator
        int recipNum = this.den;
        int recipDen = this.num;

        //Create new SimpleFraction object using reciprocated values

        return new SimpleFraction(recipNum, recipDen);
    }


    public boolean equals(Object other){

        //Cast other to SimpleFraction
        SimpleFraction otherFraction = (SimpleFraction) other;

        //Reduce fractions
        this.reduceSimpleFractionToLowestTerms();
        otherFraction.reduceSimpleFractionToLowestTerms();

        //Return the string values of each fraction
        return this.toString().equals(otherFraction.toString());
    }


    public String toString(){
        return num + "/" + den;
    }


    /*
    This method uses the backlash as a delimiter to pull out only the denominator
    of the String parameter, which is a SimpleFraction.toString() object.
*/
    private int getIntDenominator(String fraction){

        //Delimit where the denominator starts
        int delimiter = fraction.indexOf("/");

        //Get only the denominator of the string, first param is inclusive, so we add one
        //To exclude the backslash
        String stringResult = fraction.substring(delimiter + 1, fraction.length());

        return Integer.parseInt(stringResult);
    }


    /*
    This method uses the backlash as a delimiter to pull out only the Numerator
    of the String parameter.
    @param fraction 	a SimpleFraction.toString() String
    @return 			a String numerator
*/
    private int getIntNumerator(String fraction){

        //Set start index of fraction
        final int fractionStart = 0;

        //Delimit where the denominator starts
        int delimiter = fraction.indexOf("/");

        //Get only the numerator of the string
        String stringResult = fraction.substring(fractionStart, delimiter);

        //Convert to integer

        return Integer.parseInt(stringResult);
    }



    /*
        This method switches the negative sign from the denominator to the
        numerator if one exists, and returns the result as an array of
        two integers (numerator and denominator).
        @param num 			an Integer symbolizing the numerator
        @param den 			an Integre symbolizing the denominator
        @return 			an int[] array
    */
    private int[] formatProperFraction(int num, int den){
        //Store fraction as [num, den]
        int[] fractionArray = new int[2];

        if(den < 0){
            //Force positive
            den = Math.abs(den);

            //Make num negative instead of den
            fractionArray[0] = -1 * num;
            fractionArray[1] = den;

            return fractionArray;
        }
        else{
            fractionArray[0] = num;
            fractionArray[1] = den;

            return fractionArray;
        }

    }


    private void reduceSimpleFractionToLowestTerms()
    {
        // implement this method!
        //
        // Outline:
        // compute GCD of num & den
        // GCD works for + numbers.
        // So, you should eliminate - sign
        // then reduce numbers : num/GCD and den/GCD

        int gcd = GCD(Math.abs(num), Math.abs(den));

        num = num / gcd;

        den = den / gcd;

    }


    /** Task: Computes the greatest common divisor of two integers.
     *  @param integerOne	 an integer
     *  @param integerTwo	 another integer
     *  @return the greatest common divisor of the two integers */
    private int GCD(int integerOne, int integerTwo)
    {
        int result;

        if (integerOne % integerTwo == 0)
            result = integerTwo;
        else
            result = GCD(integerTwo, integerOne % integerTwo);

        return result;
    }	// end GCD


    private void throwException(){
        throw new SimpleFractionException("Denominator cannot be zero!");
    }
}
