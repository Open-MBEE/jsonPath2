#!/bin/bash

function makeGrammar() {
    antlr4 $1Parser.g4 $1Lexer.g4 -visitor -no-listener -package gov.nasa.jpl.jsonPath2 &&
    cp $1*.java $1*.tokens ../src/gov/nasa/jpl/jsonPath2/
}

makeGrammar JsonPath2

