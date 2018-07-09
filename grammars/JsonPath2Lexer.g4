lexer grammar JsonPath2Lexer;

STRING      : '"' (~'\\' | '\\' ~'\n')*? '"'; // STRING adapted from Bart Kiers, https://stackoverflow.com/questions/23799285/parsing-quoted-string-with-escape-chars
QUESTION    : '?'   ;
OPENROUND   : '('   ;
CLOSEROUND  : ')'   ;
WILDCARD    : '*'   ;
DOLLAR      : '$'   ;
DOT         : '.'   ;
CARET       : '^'   ;
TILDE       : '~'   ;
BANG        : '!'   ;
EQUALS      : '='   ;
COMMA       : ','   ;
COLON       : ':'   ;
OPENSQUARE  : '['   ;
CLOSESQUARE : ']'   ;
DOC         : 'DOC' ;
REF         : 'REF' ;
HERE        : 'HERE';
LIB         : 'LIB' ;
IDENTIFIER  : [a-zA-Z_][a-zA-Z0-9_]* ;
NATURALNUM  : '0' | [1-9][0-9]* ;
WS : [ \t\n\r]+ -> skip ;