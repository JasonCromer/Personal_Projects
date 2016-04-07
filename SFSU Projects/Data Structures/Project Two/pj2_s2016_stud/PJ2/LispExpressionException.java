/************************************************************************************
 * 
 * Do not modify this file.
 *
 * LispExpressionException 
 *
 * It is used by LispExpressionEvaluator 
 *
 *************************************************************************************/

package PJ2;

public class LispExpressionException extends RuntimeException
{
    public LispExpressionException()
    {
	this("");
    }

    public LispExpressionException(String errorMsg) 
    {
	super(errorMsg);
    }

}
