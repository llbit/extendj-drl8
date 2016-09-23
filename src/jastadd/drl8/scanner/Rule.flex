<YYINITIAL> {
  ":="   { return sym(Terminals.UNIF); }
  "rule" { return sym(Terminals.RULE); }
  "when" { return sym(Terminals.WHEN); }
//  "then" { return sym(Terminals.THEN); }
}
