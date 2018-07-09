// Generated from JsonPath2Lexer.g4 by ANTLR 4.7.1
package gov.nasa.jpl.jsonPath2;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonPath2Lexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, QUESTION=2, OPENROUND=3, CLOSEROUND=4, WILDCARD=5, DOLLAR=6, 
		DOT=7, CARET=8, TILDE=9, BANG=10, EQUALS=11, COMMA=12, COLON=13, OPENSQUARE=14, 
		CLOSESQUARE=15, DOC=16, REF=17, HERE=18, LIB=19, IDENTIFIER=20, NATURALNUM=21, 
		WS=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"STRING", "QUESTION", "OPENROUND", "CLOSEROUND", "WILDCARD", "DOLLAR", 
		"DOT", "CARET", "TILDE", "BANG", "EQUALS", "COMMA", "COLON", "OPENSQUARE", 
		"CLOSESQUARE", "DOC", "REF", "HERE", "LIB", "IDENTIFIER", "NATURALNUM", 
		"WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'?'", "'('", "')'", "'*'", "'$'", "'.'", "'^'", "'~'", "'!'", 
		"'='", "','", "':'", "'['", "']'", "'DOC'", "'REF'", "'HERE'", "'LIB'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "STRING", "QUESTION", "OPENROUND", "CLOSEROUND", "WILDCARD", "DOLLAR", 
		"DOT", "CARET", "TILDE", "BANG", "EQUALS", "COMMA", "COLON", "OPENSQUARE", 
		"CLOSESQUARE", "DOC", "REF", "HERE", "LIB", "IDENTIFIER", "NATURALNUM", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public JsonPath2Lexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JsonPath2Lexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\177\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3\2\7\2"+
		"\64\n\2\f\2\16\2\67\13\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3"+
		"\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\7\25j\n\25\f\25\16\25m\13\25\3"+
		"\26\3\26\3\26\7\26r\n\26\f\26\16\26u\13\26\5\26w\n\26\3\27\6\27z\n\27"+
		"\r\27\16\27{\3\27\3\27\3\65\2\30\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30\3\2"+
		"\t\3\2^^\3\2\f\f\5\2C\\aac|\6\2\62;C\\aac|\3\2\63;\3\2\62;\5\2\13\f\17"+
		"\17\"\"\2\u0084\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\3/\3\2\2\2\5:\3\2\2\2\7<\3\2\2\2\t>\3\2\2\2\13@\3\2\2\2\rB\3\2\2"+
		"\2\17D\3\2\2\2\21F\3\2\2\2\23H\3\2\2\2\25J\3\2\2\2\27L\3\2\2\2\31N\3\2"+
		"\2\2\33P\3\2\2\2\35R\3\2\2\2\37T\3\2\2\2!V\3\2\2\2#Z\3\2\2\2%^\3\2\2\2"+
		"\'c\3\2\2\2)g\3\2\2\2+v\3\2\2\2-y\3\2\2\2/\65\7$\2\2\60\64\n\2\2\2\61"+
		"\62\7^\2\2\62\64\n\3\2\2\63\60\3\2\2\2\63\61\3\2\2\2\64\67\3\2\2\2\65"+
		"\66\3\2\2\2\65\63\3\2\2\2\668\3\2\2\2\67\65\3\2\2\289\7$\2\29\4\3\2\2"+
		"\2:;\7A\2\2;\6\3\2\2\2<=\7*\2\2=\b\3\2\2\2>?\7+\2\2?\n\3\2\2\2@A\7,\2"+
		"\2A\f\3\2\2\2BC\7&\2\2C\16\3\2\2\2DE\7\60\2\2E\20\3\2\2\2FG\7`\2\2G\22"+
		"\3\2\2\2HI\7\u0080\2\2I\24\3\2\2\2JK\7#\2\2K\26\3\2\2\2LM\7?\2\2M\30\3"+
		"\2\2\2NO\7.\2\2O\32\3\2\2\2PQ\7<\2\2Q\34\3\2\2\2RS\7]\2\2S\36\3\2\2\2"+
		"TU\7_\2\2U \3\2\2\2VW\7F\2\2WX\7Q\2\2XY\7E\2\2Y\"\3\2\2\2Z[\7T\2\2[\\"+
		"\7G\2\2\\]\7H\2\2]$\3\2\2\2^_\7J\2\2_`\7G\2\2`a\7T\2\2ab\7G\2\2b&\3\2"+
		"\2\2cd\7N\2\2de\7K\2\2ef\7D\2\2f(\3\2\2\2gk\t\4\2\2hj\t\5\2\2ih\3\2\2"+
		"\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2l*\3\2\2\2mk\3\2\2\2nw\7\62\2\2os\t\6"+
		"\2\2pr\t\7\2\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2tw\3\2\2\2us\3\2"+
		"\2\2vn\3\2\2\2vo\3\2\2\2w,\3\2\2\2xz\t\b\2\2yx\3\2\2\2z{\3\2\2\2{y\3\2"+
		"\2\2{|\3\2\2\2|}\3\2\2\2}~\b\27\2\2~.\3\2\2\2\t\2\63\65ksv{\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}