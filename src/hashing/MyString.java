package hashing;

public class MyString {

	private char[] data;
	
	public MyString(String title) {
		data = title.toCharArray();
	}

	public Object length() {
		return data.length;
	}

	public int charAt(int i) {
		char c = data[i];
		return (int) c;
	}
	@Override
	public String toString() {
		return new String(data);
	}

}
