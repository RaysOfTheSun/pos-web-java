package com.raysofthesun.poswebjava.customer.exceptions;

public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException() {
		super("The given customer Id had no associated customers");
	}
}