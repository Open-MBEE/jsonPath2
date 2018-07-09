// Generated from JsonPath2Parser.g4 by ANTLR 4.7.1
package gov.nasa.jpl.jsonPath2;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonPath2Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, QUESTION=2, OPENROUND=3, CLOSEROUND=4, WILDCARD=5, DOLLAR=6, 
		DOT=7, CARET=8, TILDE=9, BANG=10, EQUALS=11, COMMA=12, COLON=13, OPENSQUARE=14, 
		CLOSESQUARE=15, DOC=16, REF=17, HERE=18, LIB=19, IDENTIFIER=20, NATURALNUM=21, 
		WS=22;
	public static final int
		RULE_path = 0, RULE_recursivepath = 1, RULE_recursiveuppath = 2, RULE_element = 3, 
		RULE_tagelement = 4, RULE_roottag = 5, RULE_reftag = 6, RULE_heretag = 7, 
		RULE_indexelement = 8, RULE_index = 9, RULE_attributefilterelement = 10, 
		RULE_attributevalue = 11, RULE_doubleattributefilterelement = 12, RULE_negationfilterelement = 13, 
		RULE_regexfilterelement = 14, RULE_alternationelement = 15, RULE_repeatelement = 16, 
		RULE_branches = 17;
	public static final String[] ruleNames = {
		"path", "recursivepath", "recursiveuppath", "element", "tagelement", "roottag", 
		"reftag", "heretag", "indexelement", "index", "attributefilterelement", 
		"attributevalue", "doubleattributefilterelement", "negationfilterelement", 
		"regexfilterelement", "alternationelement", "repeatelement", "branches"
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

	@Override
	public String getGrammarFileName() { return "JsonPath2Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JsonPath2Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PathContext extends ParserRuleContext {
		public RecursivepathContext recursivepath() {
			return getRuleContext(RecursivepathContext.class,0);
		}
		public RecursiveuppathContext recursiveuppath() {
			return getRuleContext(RecursiveuppathContext.class,0);
		}
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public BranchesContext branches() {
			return getRuleContext(BranchesContext.class,0);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_path);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(36);
				recursivepath();
				}
				break;
			case 2:
				{
				setState(37);
				recursiveuppath();
				}
				break;
			case 3:
				{
				setState(38);
				element();
				}
				break;
			}
			setState(42);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(41);
				branches();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecursivepathContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(JsonPath2Parser.DOT, 0); }
		public List<TerminalNode> WILDCARD() { return getTokens(JsonPath2Parser.WILDCARD); }
		public TerminalNode WILDCARD(int i) {
			return getToken(JsonPath2Parser.WILDCARD, i);
		}
		public RecursivepathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recursivepath; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitRecursivepath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RecursivepathContext recursivepath() throws RecognitionException {
		RecursivepathContext _localctx = new RecursivepathContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_recursivepath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			match(DOT);
			setState(45);
			match(WILDCARD);
			setState(46);
			match(WILDCARD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecursiveuppathContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(JsonPath2Parser.DOT, 0); }
		public List<TerminalNode> CARET() { return getTokens(JsonPath2Parser.CARET); }
		public TerminalNode CARET(int i) {
			return getToken(JsonPath2Parser.CARET, i);
		}
		public RecursiveuppathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recursiveuppath; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitRecursiveuppath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RecursiveuppathContext recursiveuppath() throws RecognitionException {
		RecursiveuppathContext _localctx = new RecursiveuppathContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_recursiveuppath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(DOT);
			setState(49);
			match(CARET);
			setState(50);
			match(CARET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementContext extends ParserRuleContext {
		public TagelementContext tagelement() {
			return getRuleContext(TagelementContext.class,0);
		}
		public IndexelementContext indexelement() {
			return getRuleContext(IndexelementContext.class,0);
		}
		public RegexfilterelementContext regexfilterelement() {
			return getRuleContext(RegexfilterelementContext.class,0);
		}
		public AttributefilterelementContext attributefilterelement() {
			return getRuleContext(AttributefilterelementContext.class,0);
		}
		public DoubleattributefilterelementContext doubleattributefilterelement() {
			return getRuleContext(DoubleattributefilterelementContext.class,0);
		}
		public NegationfilterelementContext negationfilterelement() {
			return getRuleContext(NegationfilterelementContext.class,0);
		}
		public AlternationelementContext alternationelement() {
			return getRuleContext(AlternationelementContext.class,0);
		}
		public RepeatelementContext repeatelement() {
			return getRuleContext(RepeatelementContext.class,0);
		}
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_element);
		try {
			setState(60);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				tagelement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				indexelement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(54);
				regexfilterelement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(55);
				attributefilterelement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(56);
				doubleattributefilterelement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(57);
				negationfilterelement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(58);
				alternationelement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(59);
				repeatelement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TagelementContext extends ParserRuleContext {
		public RoottagContext roottag() {
			return getRuleContext(RoottagContext.class,0);
		}
		public ReftagContext reftag() {
			return getRuleContext(ReftagContext.class,0);
		}
		public HeretagContext heretag() {
			return getRuleContext(HeretagContext.class,0);
		}
		public TerminalNode DOT() { return getToken(JsonPath2Parser.DOT, 0); }
		public TerminalNode CARET() { return getToken(JsonPath2Parser.CARET, 0); }
		public TerminalNode WILDCARD() { return getToken(JsonPath2Parser.WILDCARD, 0); }
		public TerminalNode IDENTIFIER() { return getToken(JsonPath2Parser.IDENTIFIER, 0); }
		public TerminalNode DOC() { return getToken(JsonPath2Parser.DOC, 0); }
		public TerminalNode HERE() { return getToken(JsonPath2Parser.HERE, 0); }
		public TerminalNode REF() { return getToken(JsonPath2Parser.REF, 0); }
		public TerminalNode LIB() { return getToken(JsonPath2Parser.LIB, 0); }
		public TagelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitTagelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagelementContext tagelement() throws RecognitionException {
		TagelementContext _localctx = new TagelementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_tagelement);
		int _la;
		try {
			setState(67);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOLLAR:
			case DOC:
				enterOuterAlt(_localctx, 1);
				{
				setState(62);
				roottag();
				}
				break;
			case CARET:
			case REF:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				reftag();
				}
				break;
			case TILDE:
			case HERE:
				enterOuterAlt(_localctx, 3);
				{
				setState(64);
				heretag();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 4);
				{
				setState(65);
				match(DOT);
				setState(66);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << WILDCARD) | (1L << CARET) | (1L << DOC) | (1L << REF) | (1L << HERE) | (1L << LIB) | (1L << IDENTIFIER))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RoottagContext extends ParserRuleContext {
		public TerminalNode DOLLAR() { return getToken(JsonPath2Parser.DOLLAR, 0); }
		public TerminalNode DOC() { return getToken(JsonPath2Parser.DOC, 0); }
		public TerminalNode COLON() { return getToken(JsonPath2Parser.COLON, 0); }
		public RoottagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_roottag; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitRoottag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RoottagContext roottag() throws RecognitionException {
		RoottagContext _localctx = new RoottagContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_roottag);
		int _la;
		try {
			setState(74);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOLLAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(69);
				match(DOLLAR);
				}
				break;
			case DOC:
				enterOuterAlt(_localctx, 2);
				{
				setState(70);
				match(DOC);
				setState(72);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(71);
					match(COLON);
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReftagContext extends ParserRuleContext {
		public TerminalNode CARET() { return getToken(JsonPath2Parser.CARET, 0); }
		public TerminalNode REF() { return getToken(JsonPath2Parser.REF, 0); }
		public TerminalNode COLON() { return getToken(JsonPath2Parser.COLON, 0); }
		public ReftagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reftag; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitReftag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReftagContext reftag() throws RecognitionException {
		ReftagContext _localctx = new ReftagContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_reftag);
		int _la;
		try {
			setState(81);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CARET:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				match(CARET);
				}
				break;
			case REF:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				match(REF);
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(78);
					match(COLON);
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeretagContext extends ParserRuleContext {
		public TerminalNode TILDE() { return getToken(JsonPath2Parser.TILDE, 0); }
		public TerminalNode HERE() { return getToken(JsonPath2Parser.HERE, 0); }
		public TerminalNode COLON() { return getToken(JsonPath2Parser.COLON, 0); }
		public HeretagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_heretag; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitHeretag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeretagContext heretag() throws RecognitionException {
		HeretagContext _localctx = new HeretagContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_heretag);
		int _la;
		try {
			setState(88);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TILDE:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				match(TILDE);
				}
				break;
			case HERE:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				match(HERE);
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(85);
					match(COLON);
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexelementContext extends ParserRuleContext {
		public TerminalNode OPENSQUARE() { return getToken(JsonPath2Parser.OPENSQUARE, 0); }
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode CLOSESQUARE() { return getToken(JsonPath2Parser.CLOSESQUARE, 0); }
		public IndexelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitIndexelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexelementContext indexelement() throws RecognitionException {
		IndexelementContext _localctx = new IndexelementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_indexelement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(OPENSQUARE);
			setState(91);
			index();
			setState(92);
			match(CLOSESQUARE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexContext extends ParserRuleContext {
		public TerminalNode WILDCARD() { return getToken(JsonPath2Parser.WILDCARD, 0); }
		public TerminalNode NATURALNUM() { return getToken(JsonPath2Parser.NATURALNUM, 0); }
		public IndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexContext index() throws RecognitionException {
		IndexContext _localctx = new IndexContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_index);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			_la = _input.LA(1);
			if ( !(_la==WILDCARD || _la==NATURALNUM) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributefilterelementContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(JsonPath2Parser.QUESTION, 0); }
		public TerminalNode OPENROUND() { return getToken(JsonPath2Parser.OPENROUND, 0); }
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(JsonPath2Parser.EQUALS, 0); }
		public AttributevalueContext attributevalue() {
			return getRuleContext(AttributevalueContext.class,0);
		}
		public TerminalNode CLOSEROUND() { return getToken(JsonPath2Parser.CLOSEROUND, 0); }
		public AttributefilterelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributefilterelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitAttributefilterelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributefilterelementContext attributefilterelement() throws RecognitionException {
		AttributefilterelementContext _localctx = new AttributefilterelementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_attributefilterelement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(QUESTION);
			setState(97);
			match(OPENROUND);
			setState(98);
			path();
			setState(99);
			match(EQUALS);
			setState(100);
			attributevalue();
			setState(101);
			match(CLOSEROUND);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributevalueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsonPath2Parser.STRING, 0); }
		public AttributevalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributevalue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitAttributevalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributevalueContext attributevalue() throws RecognitionException {
		AttributevalueContext _localctx = new AttributevalueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_attributevalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DoubleattributefilterelementContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(JsonPath2Parser.QUESTION, 0); }
		public TerminalNode OPENROUND() { return getToken(JsonPath2Parser.OPENROUND, 0); }
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public TerminalNode EQUALS() { return getToken(JsonPath2Parser.EQUALS, 0); }
		public TerminalNode CLOSEROUND() { return getToken(JsonPath2Parser.CLOSEROUND, 0); }
		public DoubleattributefilterelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubleattributefilterelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitDoubleattributefilterelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubleattributefilterelementContext doubleattributefilterelement() throws RecognitionException {
		DoubleattributefilterelementContext _localctx = new DoubleattributefilterelementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_doubleattributefilterelement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(QUESTION);
			setState(106);
			match(OPENROUND);
			setState(107);
			path();
			setState(108);
			match(EQUALS);
			setState(109);
			path();
			setState(110);
			match(CLOSEROUND);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NegationfilterelementContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(JsonPath2Parser.QUESTION, 0); }
		public TerminalNode OPENROUND() { return getToken(JsonPath2Parser.OPENROUND, 0); }
		public TerminalNode BANG() { return getToken(JsonPath2Parser.BANG, 0); }
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TerminalNode CLOSEROUND() { return getToken(JsonPath2Parser.CLOSEROUND, 0); }
		public NegationfilterelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negationfilterelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitNegationfilterelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NegationfilterelementContext negationfilterelement() throws RecognitionException {
		NegationfilterelementContext _localctx = new NegationfilterelementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_negationfilterelement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(QUESTION);
			setState(113);
			match(OPENROUND);
			setState(114);
			match(BANG);
			setState(115);
			path();
			setState(116);
			match(CLOSEROUND);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RegexfilterelementContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(JsonPath2Parser.QUESTION, 0); }
		public TerminalNode OPENROUND() { return getToken(JsonPath2Parser.OPENROUND, 0); }
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TerminalNode TILDE() { return getToken(JsonPath2Parser.TILDE, 0); }
		public TerminalNode EQUALS() { return getToken(JsonPath2Parser.EQUALS, 0); }
		public AttributevalueContext attributevalue() {
			return getRuleContext(AttributevalueContext.class,0);
		}
		public TerminalNode CLOSEROUND() { return getToken(JsonPath2Parser.CLOSEROUND, 0); }
		public RegexfilterelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regexfilterelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitRegexfilterelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegexfilterelementContext regexfilterelement() throws RecognitionException {
		RegexfilterelementContext _localctx = new RegexfilterelementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_regexfilterelement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(QUESTION);
			setState(119);
			match(OPENROUND);
			setState(120);
			path();
			setState(121);
			match(TILDE);
			setState(122);
			match(EQUALS);
			setState(123);
			attributevalue();
			setState(124);
			match(CLOSEROUND);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AlternationelementContext extends ParserRuleContext {
		public TerminalNode OPENSQUARE() { return getToken(JsonPath2Parser.OPENSQUARE, 0); }
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public TerminalNode CLOSESQUARE() { return getToken(JsonPath2Parser.CLOSESQUARE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JsonPath2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JsonPath2Parser.COMMA, i);
		}
		public AlternationelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alternationelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitAlternationelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlternationelementContext alternationelement() throws RecognitionException {
		AlternationelementContext _localctx = new AlternationelementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_alternationelement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(OPENSQUARE);
			setState(127);
			element();
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(128);
				match(COMMA);
				setState(129);
				element();
				}
				}
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(135);
			match(CLOSESQUARE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RepeatelementContext extends ParserRuleContext {
		public TerminalNode WILDCARD() { return getToken(JsonPath2Parser.WILDCARD, 0); }
		public TerminalNode OPENROUND() { return getToken(JsonPath2Parser.OPENROUND, 0); }
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TerminalNode CLOSEROUND() { return getToken(JsonPath2Parser.CLOSEROUND, 0); }
		public RepeatelementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeatelement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitRepeatelement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RepeatelementContext repeatelement() throws RecognitionException {
		RepeatelementContext _localctx = new RepeatelementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_repeatelement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			match(WILDCARD);
			setState(138);
			match(OPENROUND);
			setState(139);
			path();
			setState(140);
			match(CLOSEROUND);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BranchesContext extends ParserRuleContext {
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public TerminalNode OPENSQUARE() { return getToken(JsonPath2Parser.OPENSQUARE, 0); }
		public TerminalNode CLOSESQUARE() { return getToken(JsonPath2Parser.CLOSESQUARE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(JsonPath2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JsonPath2Parser.COMMA, i);
		}
		public BranchesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_branches; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonPath2ParserVisitor ) return ((JsonPath2ParserVisitor<? extends T>)visitor).visitBranches(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BranchesContext branches() throws RecognitionException {
		BranchesContext _localctx = new BranchesContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_branches);
		int _la;
		try {
			setState(154);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(142);
				path();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(143);
				match(OPENSQUARE);
				setState(144);
				path();
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(145);
					match(COMMA);
					setState(146);
					path();
					}
					}
					setState(151);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(152);
				match(CLOSESQUARE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30\u009f\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\3\2\5\2*\n\2\3\2\5\2-\n\2\3\3\3\3\3\3\3\3\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5?\n\5\3\6\3\6\3\6\3\6\3\6\5"+
		"\6F\n\6\3\7\3\7\3\7\5\7K\n\7\5\7M\n\7\3\b\3\b\3\b\5\bR\n\b\5\bT\n\b\3"+
		"\t\3\t\3\t\5\tY\n\t\5\t[\n\t\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\7\21\u0085\n\21\f\21\16\21\u0088\13\21\3\21\3\21\3\22\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\23\7\23\u0096\n\23\f\23\16\23\u0099\13"+
		"\23\3\23\3\23\5\23\u009d\n\23\3\23\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$\2\4\5\2\7\7\n\n\22\26\4\2\7\7\27\27\2\u00a2\2)\3\2\2\2\4"+
		".\3\2\2\2\6\62\3\2\2\2\b>\3\2\2\2\nE\3\2\2\2\fL\3\2\2\2\16S\3\2\2\2\20"+
		"Z\3\2\2\2\22\\\3\2\2\2\24`\3\2\2\2\26b\3\2\2\2\30i\3\2\2\2\32k\3\2\2\2"+
		"\34r\3\2\2\2\36x\3\2\2\2 \u0080\3\2\2\2\"\u008b\3\2\2\2$\u009c\3\2\2\2"+
		"&*\5\4\3\2\'*\5\6\4\2(*\5\b\5\2)&\3\2\2\2)\'\3\2\2\2)(\3\2\2\2*,\3\2\2"+
		"\2+-\5$\23\2,+\3\2\2\2,-\3\2\2\2-\3\3\2\2\2./\7\t\2\2/\60\7\7\2\2\60\61"+
		"\7\7\2\2\61\5\3\2\2\2\62\63\7\t\2\2\63\64\7\n\2\2\64\65\7\n\2\2\65\7\3"+
		"\2\2\2\66?\5\n\6\2\67?\5\22\n\28?\5\36\20\29?\5\26\f\2:?\5\32\16\2;?\5"+
		"\34\17\2<?\5 \21\2=?\5\"\22\2>\66\3\2\2\2>\67\3\2\2\2>8\3\2\2\2>9\3\2"+
		"\2\2>:\3\2\2\2>;\3\2\2\2><\3\2\2\2>=\3\2\2\2?\t\3\2\2\2@F\5\f\7\2AF\5"+
		"\16\b\2BF\5\20\t\2CD\7\t\2\2DF\t\2\2\2E@\3\2\2\2EA\3\2\2\2EB\3\2\2\2E"+
		"C\3\2\2\2F\13\3\2\2\2GM\7\b\2\2HJ\7\22\2\2IK\7\17\2\2JI\3\2\2\2JK\3\2"+
		"\2\2KM\3\2\2\2LG\3\2\2\2LH\3\2\2\2M\r\3\2\2\2NT\7\n\2\2OQ\7\23\2\2PR\7"+
		"\17\2\2QP\3\2\2\2QR\3\2\2\2RT\3\2\2\2SN\3\2\2\2SO\3\2\2\2T\17\3\2\2\2"+
		"U[\7\13\2\2VX\7\24\2\2WY\7\17\2\2XW\3\2\2\2XY\3\2\2\2Y[\3\2\2\2ZU\3\2"+
		"\2\2ZV\3\2\2\2[\21\3\2\2\2\\]\7\20\2\2]^\5\24\13\2^_\7\21\2\2_\23\3\2"+
		"\2\2`a\t\3\2\2a\25\3\2\2\2bc\7\4\2\2cd\7\5\2\2de\5\2\2\2ef\7\r\2\2fg\5"+
		"\30\r\2gh\7\6\2\2h\27\3\2\2\2ij\7\3\2\2j\31\3\2\2\2kl\7\4\2\2lm\7\5\2"+
		"\2mn\5\2\2\2no\7\r\2\2op\5\2\2\2pq\7\6\2\2q\33\3\2\2\2rs\7\4\2\2st\7\5"+
		"\2\2tu\7\f\2\2uv\5\2\2\2vw\7\6\2\2w\35\3\2\2\2xy\7\4\2\2yz\7\5\2\2z{\5"+
		"\2\2\2{|\7\13\2\2|}\7\r\2\2}~\5\30\r\2~\177\7\6\2\2\177\37\3\2\2\2\u0080"+
		"\u0081\7\20\2\2\u0081\u0086\5\b\5\2\u0082\u0083\7\16\2\2\u0083\u0085\5"+
		"\b\5\2\u0084\u0082\3\2\2\2\u0085\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088\u0086\3\2\2\2\u0089\u008a\7\21"+
		"\2\2\u008a!\3\2\2\2\u008b\u008c\7\7\2\2\u008c\u008d\7\5\2\2\u008d\u008e"+
		"\5\2\2\2\u008e\u008f\7\6\2\2\u008f#\3\2\2\2\u0090\u009d\5\2\2\2\u0091"+
		"\u0092\7\20\2\2\u0092\u0097\5\2\2\2\u0093\u0094\7\16\2\2\u0094\u0096\5"+
		"\2\2\2\u0095\u0093\3\2\2\2\u0096\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0097"+
		"\u0098\3\2\2\2\u0098\u009a\3\2\2\2\u0099\u0097\3\2\2\2\u009a\u009b\7\21"+
		"\2\2\u009b\u009d\3\2\2\2\u009c\u0090\3\2\2\2\u009c\u0091\3\2\2\2\u009d"+
		"%\3\2\2\2\17),>EJLQSXZ\u0086\u0097\u009c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}