package calcx;

public class BoolToken extends Token {
	public Boolean BoolValue ;

	public BoolToken(Boolean value,TokenType T) {
		super(TokenType.BOOLVAL);
			BoolValue = value;
		
		
	}

	public Boolean getValue() {
		return BoolValue;
	}

	public void print() {
		System.out.println("Bool Token: " + BoolValue);
	}
}
