package hashing;

import java.util.Arrays;

public class ISBN10 {

	private char[] isbn;

	public ISBN10(String isbn) {
		if (isbn.length() != 10)
			throw new IllegalArgumentException("Wrong length, must be 10");
		if (!checkDigit(isbn))
			throw new IllegalArgumentException("Not a valid isbn 10");
		this.isbn = isbn.toCharArray();
	}

	private boolean checkDigit(String isbn) {
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += Character.getNumericValue(isbn.charAt(i)) * (10 - i);
		}
		int checkDigit = (11 - (sum % 11)) % 11;

		return isbn.endsWith(checkDigit == 10 ? "X" : "" + checkDigit);
	}


	@Override
	public int hashCode() {
		int primeNr = 17;
		int hashVal = 0;
		int isbnNumber = 0;

		for (int i = 0; i <= isbn.length-1; i++) {
			int digit = isbn[i] - '0';
			isbnNumber = isbnNumber * 10 + digit;
		}

		hashVal += primeNr * isbnNumber;
		if (hashVal < 0) {
			//hashVal &= 0xFFFFFFF;
			hashVal *= -1;  // Obs vi ville ha en bitoperation här men VPL gav oss utslag för det så vi ändrade till -1 istället
		}
		return hashVal;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ISBN10) {
			return Arrays.equals(this.isbn, ((ISBN10) obj).isbn);
		}
		return false;
	}

	@Override
	public String toString() {
		return new String(isbn);
	}
}
