package calcx;

public class BoolVarToken extends VarToken{
	public Boolean value;

	public BoolVarToken(String identName) {
		super(identName, TokenType.BOOL);
		value = null;
	}

	public String getValue() {
		return String.valueOf(value);
	}

	public void setValue(String newValue) {
		value = Boolean.parseBoolean(newValue);
	}

	public void print() {
		System.out.println("Boolean Variable Token: " + identifierName);
	}
}
