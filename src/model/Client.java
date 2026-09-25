package model;

import main.Logable;
import main.Payable;

public class Client extends Person implements Payable {
	private int memberId = MEMBER_ID;
	private Amount balance;

	public static final int MEMBER_ID = 456;
	public static final Amount BALANCE = new Amount(50.00);

	public Client(String name) {
		super(name);
		// FIX: copy BALANCE's value into a new Amount instead of referencing the static
		// Amount object directly — Amount is mutable, so every Client used to share and
		// mutate the same balance, making one client's payment affect every other client.
		this.balance = new Amount(BALANCE.getValue());
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public Amount getBalance() {
		return balance;
	}

	public void setBalance(Amount balance) {
		this.balance = balance;
	}

	/**
	 * @param sale amount to subtract from balance client
	 * @return true if final balance is positive, false if negative
	 */
	@Override
	public boolean pay(Amount amount) {
		// substract amount from balance
		this.balance.setValue(this.balance.getValue()-amount.getValue());
		
		// check final balance
		if (this.balance.getValue()>0) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Client [memberId=" + memberId + ", balance=" + balance + ", name=" + name + "]";
	}
	
	

}
