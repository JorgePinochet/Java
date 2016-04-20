/**
 * Apr 6, 2009
 *
 */
package chapterFourTest;

import chapterFour.CashRegister;

/**
 *
 * @author 432873
 * Jorge Pinochet
 *
 * version 1.0
 *
 * This class tests then CashRegister class.
 */
public class CashRegisterTester
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		CashRegister register = new CashRegister();
		
		register.recordPurchase(0.75);
		register.recordPurchase(1.50);
		register.enterPayment(2, 0, 5, 0, 0);
		System.out.print("Change: ");
		System.out.println(register.giveChange());
		System.out.println("Expected: 0.25");
		
		register.recordPurchase(2.25);
		register.recordPurchase(19.25);
		register.enterPayment(23, 2, 0, 0, 0);
		System.out.print("Change: ");
		System.out.println(register.giveChange());
		System.out.println("Expected: 2.0");
	}

}
