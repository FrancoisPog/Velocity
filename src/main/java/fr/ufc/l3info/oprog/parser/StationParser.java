package fr.ufc.l3info.oprog.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser pour lire un fichier décrivant des stations.
 */
public class StationParser {

    private int current = 0;
    private List<Token> tokens = null;

    /**
     * Constructeur privé pour empêcher d'instancier la classe. 
     */
    private StationParser() {    }

    private static StationParser INSTANCE = null;

    public static StationParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StationParser();
        }
        return INSTANCE;
    }

    /**
     * Réalise une analyse syntaxique du fichier passé en paramètre et construit l'arbre de syntaxe abstraite correspondant.
     * @param f le fichier format texte à analyser
     * @return la racine de l'arbre de syntaxe abstraite
     * @throws IOException si le fichier n'a pas pu être lu
     * @throws StationParserException si l'analyse a révélé des erreurs de parsing.
     */
    public ASTNode parse(File f) throws IOException, StationParserException {
        tokens = StationFileTokenizer.tokenize(f);
        current = 0;
        return listeStation();
    }

    /**
     * Lit le token courant et passe au suivant. 
     * @return le token courant
     * @throws StationParserException si la fin du fichier est rencontrée.
     */
    private Token nextToken() throws StationParserException {
        if (current >= tokens.size()) {
            int l = tokens.get(tokens.size()-1).getLigne();
            int c = tokens.get(tokens.size()-1).getColonne();
            throw new StationParserException("Fin de fichier inattendue", l, c);
        }
        Token t = tokens.get(current);
        current++;
        return t;
    }
    
    private ASTNode listeStation() throws StationParserException {
        ASTNode r = new ASTListeStations(1, 1);
        while (current < tokens.size()) {
            ASTNode s = station();
            r.addChild(s);
        }
        return r;
    }

    private ASTNode station() throws StationParserException {

        Token tok = this.nextToken();
        if (! tok.getValeur().equalsIgnoreCase("station")) {
            throw new StationParserException("attendu : \"station\"", tok.getLigne(), tok.getColonne());
        }
        ASTNode r = new ASTStation(tok.getLigne(), tok.getColonne());

        tok = nextToken();
        String nom = tok.getValeur();
        if (!(nom.length() > 1 && nom.charAt(0) == '"' && nom.charAt(nom.length()-1) == '"')) {
            throw new StationParserException("attendu : littéral de chaîne", tok.getLigne(), tok.getColonne());
        }
        r.addChild(new ASTChaine(nom, tok.getLigne(), tok.getColonne()));

        tok = nextToken();
        if (! tok.getValeur().equals("{")) {
            throw new StationParserException("attendu : {", tok.getLigne(), tok.getColonne());
        }

        do {
            ASTNode decl = declaration();
            r.addChild(decl);
            tok = nextToken();
        }
        while (tok.getValeur().equals(";"));

        if (! tok.getValeur().equals("}")) {
            throw new StationParserException("attendu : } ou ;", tok.getLigne(), tok.getColonne());
        }

        return r;
    }

     private ASTNode declaration() throws StationParserException {
        Token tok = this.nextToken();
         ASTNode r = new ASTDeclaration(tok.getLigne(), tok.getColonne());
         if (!tok.getValeur().equalsIgnoreCase("latitude") &&
             !tok.getValeur().equalsIgnoreCase("longitude") &&
             !tok.getValeur().equalsIgnoreCase("capacite")) {
             throw new StationParserException("attendu : \"latitude\" ou \"longitude\" ou \"capacite\"", tok.getLigne(), tok.getColonne());
         }

         ASTIdentificateur id = new ASTIdentificateur(tok.getValeur(), tok.getLigne(), tok.getColonne());
         r.addChild(id);

         tok = this.nextToken();
         if (! tok.getValeur().equals(":")) {
            throw new StationParserException("attendu : \":\"", tok.getLigne(), tok.getColonne());
         }

         tok = this.nextToken();
         if (! tok.getValeur().matches("^\\-?\\d+(\\.\\d+)?$")) {
             throw new StationParserException("attendu : un nombre", tok.getLigne(), tok.getColonne());
         }
         ASTNombre num = new ASTNombre(tok.getValeur(), tok.getLigne(), tok.getColonne());
         r.addChild(num);

         return r;
     }

}



/**
 * Classe utilitaire permettant de découper le fichier en une suite de tokens. 
 */
class StationFileTokenizer {

    /**
     * Transforme le texte d'un fichier en une suite de tokens : identifiants, accolades, chaines, ponctuations, etc.
     * Seuls les espaces et les séparateurs classiques (tabulations, retours à la ligne, etc.) sont ignorés. 
     * @param f le fichier à découper
     * @return une liste de tokens représentant les tokens ainsi identifiés.
     * @throws IOException si le fichier n'a pas pu être lu.
     * @throws StationParserException si une erreur d'analyse lexicale a eu lieu. 
     */
    public static List<Token> tokenize(File f) throws IOException, StationParserException {
        FileReader fr = new FileReader(f);
        List<Token> r = new ArrayList<>();
        int lu, nc = 0, nl = 1;
        Token token = new Token();
        boolean inQuotes = false;
        while ((lu = fr.read()) != -1) {
            char charLu = (char) lu;
            nc++;
            if (charLu == '\n') {
                nc = 0;
                nl++;
            }
            switch (charLu) {
                case '\t':
                case ' ':
                    if (inQuotes) {
                        token.append(charLu, nl, nc);
                        break;
                    }
                case '\n':
                case '\r':
                    if (inQuotes) {
                        throw new StationParserException("Unexpected end of String literal", nl, nc);
                    }
                    if (token.length() > 0) {
                        r.add(token);
                        token = new Token();
                    }
                    break;
                case '{':
                case ':':
                case ';':
                case '}':
                    if (token.length() > 0) {
                        r.add(token);
                        token = new Token();
                    }
                    token.append(charLu, nl, nc);
                    r.add(token);
                    token = new Token();
                    break;
                case '"':
                    if (inQuotes) {
                        int next = fr.read();
                        if (next != -1 && ((char)next) != ' ' && ((char)next != '\t') && ((char)next) != '\r' && ((char)next != '\n') ) {
                            throw new StationParserException("Unexpected end of String literal", nl, nc);
                        }
                        token.append(charLu, nl, nc);
                        r.add(token);
                        token = new Token();
                        inQuotes = false;
                    }
                    else {
                        if (token.length() > 0) {
                            throw new StationParserException("Unexpected beginning of String literal", nl, nc);
                        }
                        token.append(charLu, nl, nc);
                        inQuotes = true;
                    }
                    break;
                default:
                    token.append(charLu, nl, nc);
                    break;
            }
        }
        if (token.length() > 0) {
            r.add(token);
        }
        return r;
    }

}

/**
 * Classe gérant les tokens.
 */
class Token {

    String valeur;
    int l, c;

    public Token() {
        valeur = "";
    }

    public int getLigne() {
        return l;
    }

    public int getColonne() {
        return c;
    }

    public void append(char c, int nl, int nc) {
        if (valeur.length() == 0) {
            this.l = nl;
            this.c = nc;
        }
        valeur += c;
    }

    public int length() {
        return valeur.length();
    }

    public String getValeur() {
        return valeur;
    }

    public String toString() { return "[" + valeur + "|(" + l + "," + c + ")]"; }
}

