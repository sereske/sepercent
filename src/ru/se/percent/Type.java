package ru.se.percent;

public enum Type {
	//PERCENT_PAID, 
	//PERCENT_RECEIVED, 
	MAIN_PAID, 
	MAIN_RECEIVED;

	@Override
	public String toString() {
		String result = "";
		switch (this) {
		/*
		case PERCENT_PAID:
			result = "Проценты уплаченные";
			break;
		case PERCENT_RECEIVED:
			result = "Проценты полученные";
			break;
			*/
		case MAIN_PAID:
			result = "Займ уплаченный";
			break;
		case MAIN_RECEIVED:
			result = "Займ полученный";
			break;
		}
		return result;
	}
}
