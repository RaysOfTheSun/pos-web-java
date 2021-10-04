package com.raysofthesun.poswebjava.agent.exception;

public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException() {
		super("The given customer Id had no associated customers");
	}
}
