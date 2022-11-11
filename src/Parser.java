
package calcx;

import java.util.Hashtable;
import java.util.ArrayList;

public class Parser {
	public static int x;
	public ArrayList<Token> tokens; // Array of tokens to be parsed
	public int position;// Current position in array
	
	Hashtable<String, VarToken> vars; // Hashtable of all identifiers

	// Constructor for Parser class. Sequence of tokens and a hashtable of
	// identifiers should be passed.
	public Parser(ArrayList<Token> tokenSeq, Hashtable<String, VarToken> variables) {
		tokens = tokenSeq;
		position = 0; // Current position in sequence
		vars = variables;
	}

	// This method is called when a parse error occurs on some token
	private void parseError() {
		System.out.print("Parse Error on:");
		tokens.get(position).print();
	}

	// This method checks if the next token is of type tokType and if so moves on to
	// the next token
	private void match(TokenType tokType) {
		if (tokens.get(position).returnType() != tokType) {
			System.out.println(position);
			parseError();
		}
		position++;
	}

	// This method determines if the next token is of type tokType (returning a
	// boolean)
	private boolean check(TokenType tokType) {
		if (tokens.get(position).returnType() != tokType) {
			return false;
		}
		return true;
	}

	/*
	 * These next methods implement the Context Free Grammar / Syntax Directed
	 * Translation Scheme. There is a method for each nonterminal of the grammar and
	 * production rules determine the operations to be carried out.
	 */
	
	// Start to parse the program
	public void progAll() {
		if (check(TokenType.BOOL_KEYWORD)) {
			progB();
		}else if(check(TokenType.INT_KEYWORD)) {
			prog();
		}
	}
	public void prog() {
		
		int i = 1;
		// First parse declarations
		decls();
		match(TokenType.COLON);
		int value = expr();
		
		if (check(TokenType.COLON)) {
			while (check(TokenType.COLON)) {
				match(TokenType.COLON);
				int newValue = expr();
				i++;
				System.out.println(value + ":" + newValue + ":" + "END");
			}
		} else {
			System.out.println("Value of expression "+value);
			match(TokenType.END_OF_FILE);
		}

	}
	public void progB() {

		int i = 1;
		// First parse declarations
		decls();
		match(TokenType.COLON);
		String value = String.valueOf(exprB());
		
		if (check(TokenType.COLON)) {
			while (check(TokenType.COLON)) {
				match(TokenType.COLON);
				String newValue = String.valueOf(exprB());
				i++;
				System.out.println(value + ":" + newValue + ":" + "END");
			}
		} else {
			System.out.println("Value of expression "+value);
			match(TokenType.END_OF_FILE);
		}

	}

	// INT x = 5;INT y = 7;:x+y:y-x$
	public void decls() {
		if (check(TokenType.INT_KEYWORD)) {
			decl();
			decls();
		} else if (check(TokenType.BOOL_KEYWORD)) {
			declB();
			decls();
		} else {
			// Do nothing, epsilon production in CFG
		}
	}

	public void declB() {
		if (check(TokenType.BOOL_KEYWORD)) {
			match(TokenType.BOOL_KEYWORD);
			BoolVarToken nextBoolVar = (BoolVarToken) tokens.get(position);
			match(TokenType.BOOL);
			match(TokenType.EQUALS);
			BoolToken nextBool = (BoolToken) tokens.get(position);
			match(TokenType.BOOLVAL);
			match(TokenType.SEMICOLON);
			// do s
			nextBoolVar.setValue(String.valueOf(nextBool.getValue()));
		} else {
			System.out.print("Error, Invalid sequence of declaration");
		}
	}

	public void decl() {
		if (check(TokenType.INT_KEYWORD)) {
			match(TokenType.INT_KEYWORD);
			IntVarToken nextIntVar = (IntVarToken) tokens.get(position);
			match(TokenType.INT_VAR);
			match(TokenType.EQUALS);
			NumToken nextInteger = (NumToken) tokens.get(position);
			match(TokenType.NUM);
			match(TokenType.SEMICOLON);
			// Now do the semantic action
			nextIntVar.setValue(nextInteger.getValue());
		} else {
			System.out.print("Error, Invalid sequence of declaration");
		}
	}

	public Boolean exprB() {
		Boolean val = true;
		if (tokens.get(position).returnType() == TokenType.BOOL) {
			val = Boolean.parseBoolean(((BoolVarToken) tokens.get(position)).getValue());
		}
		if (tokens.get(position).returnType() == TokenType.COLON) {
			match(TokenType.COLON);
			exprB();
		}
		match(TokenType.BOOL);
		switch (tokens.get(position).returnType()) {
		case AND:
			op();
			Boolean value2 = exprB();
			val = val && value2;
			break; // Semantic action
		case NOT:
			op();
			Boolean value2b =  exprB();
			val = !val || !value2b;
			break;
		case OR:
			op();
			Boolean value2c =  exprB();
			val = val || value2c;
			;

			break;// Semantic action

		}

		return val;

	}

	public int expr() {
		int value = 0;
		if (tokens.get(position).returnType() == TokenType.INT_VAR) {
			value = ((IntVarToken) tokens.get(position)).getValue();
		}
		if (tokens.get(position).returnType() == TokenType.COLON) {
			match(TokenType.COLON);
			expr();
		}
		match(TokenType.INT_VAR);
		switch (tokens.get(position).returnType()) {
		case PLUS:
			op();
			int value2 = expr();
			value = value + value2;
			break; // Semantic action
		case MINUS:
			op();
			int value2b = expr();
			value = value - value2b;
			break;
		case DIVIDE:
			op();
			int value2c = expr();
			value = Math.round(value / value2c);
			;

			break;// Semantic action
		case MULTIPLY:
			op();
			int value2d = expr();
			value = value * value2d;
			break;
		case MODULUS:
			op();
			int value2e = expr();
			value = value % value2e;
			break;
		case POWER:
			op();
			int value2f = expr();
			value = (int) Math.pow(value, value2f);
			break;
		}

		return value;

	}

	public void op() {
		if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
		} else if (check(TokenType.PLUS)) {
			match(TokenType.PLUS);
		} else if (check(TokenType.DIVIDE)) {
			match(TokenType.DIVIDE);
		} else if (check(TokenType.MULTIPLY)) {
			match(TokenType.MULTIPLY);
		} else if (check(TokenType.POWER)) {
			match(TokenType.POWER);
		} else if (check(TokenType.MODULUS)) {
			match(TokenType.MODULUS);
		} else if (check(TokenType.AND)) {
			match(TokenType.AND);
		} else if (check(TokenType.AND)) {
			match(TokenType.AND);
		} else if (check(TokenType.AND)) {
			match(TokenType.AND);
		} else {
			System.out.print("Error, Calculation sign expected"); // op should be "+" or "-"
		}
	}
}
