parser grammar JsonPath2Parser;

options { tokenVocab=JsonPath2Lexer; }

/**
 * Defines the grammar for the variant of JsonPath used by the Path class.
 * 
 * Grammar based off of Steven Alexander's JsonPath grammar, found at
 * https://github.com/stevenalexander/antlr4-jsonpath-grammar/blob/master/JsonPath.g4
 */

path     : ( recursivepath | recursiveuppath | element ) branches?
         ;

recursivepath : DOT WILDCARD WILDCARD ;

recursiveuppath : DOT CARET CARET ;

element  : tagelement
         | indexelement
         | regexfilterelement
         | attributefilterelement
         | doubleattributefilterelement
         | negationfilterelement
         | alternationelement
         | repeatelement
         ;

tagelement : roottag
           | reftag
           | heretag
           | DOT (CARET | WILDCARD | IDENTIFIER | DOC | HERE | REF | LIB) // DOC, HERE, etc. in this context don't actually mean anything special
           ;

roottag : DOLLAR
        | DOC COLON?
        ;

reftag  : CARET
        | REF COLON?
        ;

heretag : TILDE
        | HERE COLON?
        ;

indexelement : OPENSQUARE index CLOSESQUARE ;

index : WILDCARD
      | NATURALNUM
      ;


attributefilterelement : QUESTION OPENROUND path EQUALS attributevalue CLOSEROUND ;

attributevalue : STRING ;

doubleattributefilterelement : QUESTION OPENROUND path EQUALS path CLOSEROUND ;

negationfilterelement : QUESTION OPENROUND BANG path CLOSEROUND ;

regexfilterelement : QUESTION OPENROUND path TILDE EQUALS attributevalue CLOSEROUND ;

alternationelement : OPENSQUARE element (COMMA element)* CLOSESQUARE ;

repeatelement : WILDCARD OPENROUND path CLOSEROUND ;

branches : path
         | OPENSQUARE path (COMMA path)* CLOSESQUARE
         ;
