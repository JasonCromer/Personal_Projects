/************************************************************************************
 * Do not modify this file.
 * SimpleFractionException class. It is used by SimpleFraction class
 *************************************************************************************/

package PJ1;

public class SimpleFractionException extends RuntimeException
{
    public SimpleFractionException()
    {
	this("");
    }

    public SimpleFractionException(String errorMsg) 
    {
	super(errorMsg);
    }

}
