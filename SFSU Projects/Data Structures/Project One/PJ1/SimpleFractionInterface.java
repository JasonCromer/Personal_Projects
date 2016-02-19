/* This file specifies methods for SimpleFractionInterface	*/
/* Do not modify this file!!                  			*/

package PJ1;

public interface SimpleFractionInterface 
{
    /** Task: Sets a fraction to a given value.
     *  @param num is the integer numerator
     *  @param den is the integer denominator
     *  @throws ArithmeticException if denominator is 0  */
    public void setSimpleFraction(int num, int den);

	/** Task: convert a fraction to double value
	 *  @return the double floating point value of a fraction */
	public double toDouble();

	/** Task: Adds two fractions.
	 *  @param secondFraction is a fraction that is the second operand of the addition
	 *  @return a fraction which is the sum of the invoking fraction and the secondFraction */
	public SimpleFractionInterface add(SimpleFractionInterface secondFraction);

	/** Task: Subtracts two fractions.
	 *  @param secondFraction a fraction that is the second operand of the subtraction
	 *  @return a fraction which is the difference of the invoking fraction and the second operand */
	public SimpleFractionInterface subtract(SimpleFractionInterface secondFraction);

	/** Task: Multiplies two fractions.
	 *  @param secondFraction a fraction that is the second operand of the multiplication
	 *  @return a fraction which is the product of the invoking fraction and the secondFraction*/
	public SimpleFractionInterface multiply(SimpleFractionInterface secondFraction);

	/** Task: Divides two fractions.
	 *  @param secondFraction a fraction that is the second operand of the division
	 *  @return a fraction which the quotient of the invoking fraction and the secondFraction
         *  @throws FractionException if secondFraction is 0 */
	public SimpleFractionInterface divide(SimpleFractionInterface secondFraction);

	/** Task: Get's the fraction's reciprocal
	 *  @return the reciprocal of the invoking fraction 
         *  @throws FractionException if the new number with denominator is 0*/
	public SimpleFractionInterface getReciprocal();

}
