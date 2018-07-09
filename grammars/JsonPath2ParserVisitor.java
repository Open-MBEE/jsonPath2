// Generated from JsonPath2Parser.g4 by ANTLR 4.7.1
package gov.nasa.jpl.jsonPath2;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JsonPath2Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JsonPath2ParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(JsonPath2Parser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#recursivepath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecursivepath(JsonPath2Parser.RecursivepathContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#recursiveuppath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecursiveuppath(JsonPath2Parser.RecursiveuppathContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(JsonPath2Parser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#tagelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagelement(JsonPath2Parser.TagelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#roottag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoottag(JsonPath2Parser.RoottagContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#reftag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReftag(JsonPath2Parser.ReftagContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#heretag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeretag(JsonPath2Parser.HeretagContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#indexelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexelement(JsonPath2Parser.IndexelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(JsonPath2Parser.IndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#attributefilterelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributefilterelement(JsonPath2Parser.AttributefilterelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#attributevalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributevalue(JsonPath2Parser.AttributevalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#doubleattributefilterelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleattributefilterelement(JsonPath2Parser.DoubleattributefilterelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#negationfilterelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegationfilterelement(JsonPath2Parser.NegationfilterelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#regexfilterelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegexfilterelement(JsonPath2Parser.RegexfilterelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#alternationelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternationelement(JsonPath2Parser.AlternationelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#repeatelement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatelement(JsonPath2Parser.RepeatelementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonPath2Parser#branches}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBranches(JsonPath2Parser.BranchesContext ctx);
}