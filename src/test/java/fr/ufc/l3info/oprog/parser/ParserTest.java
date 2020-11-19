package fr.ufc.l3info.oprog.parser;

import fr.ufc.l3info.oprog.Station;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *  Quelques tests pour le package parser.
 */
public class ParserTest {

    /** Chemin vers les fichiers de test */
    final String path = "./target/classes/data/parserError/";
    final String pathExtra = "./target/classes/data/extraError/";

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();

    @Test
    public void testTokenizer() throws StationParserException, IOException {
        List<Token> tokens = StationFileTokenizer.tokenize(new File(path + "../stationsOK.txt"));
        assertEquals(30, tokens.size());
        String[] expected = { "station", "\"21 - Avenue Fontaine Argent, Boulevard Diderot\"", "{",
                "latitude", ":", "47.2477615", ";", "longitude", ":", "5.9835995", ";",
                "capacite", ":", "12", "}", "station", "\"Avenue du Maréchal Foch\"", "{",
                "capacite", ":", "10", ";", "longitude", ":", "6.022671", ";", "latitude", ":", "47.246511", "}" };
        for (int i=0; i < expected.length; i++) {
            assertEquals(expected[i], tokens.get(i).getValeur());
        }
        assertEquals(1, tokens.get(0).getLigne());
        assertEquals(1, tokens.get(0).getColonne());
        assertEquals(10, tokens.get(tokens.size()-2).getLigne());
        assertEquals(16, tokens.get(tokens.size()-2).getColonne());
    }


    @Test
    public void testParserOK() throws StationParserException, IOException {
        ASTNode n = parser.parse(new File(path + "../stationsOK.txt"));
        assertTrue(n instanceof ASTListeStations);
        assertEquals(2, n.getNumChildren());
        for (ASTNode n1 : n) {
            assertTrue(n1 instanceof ASTStation);
            assertEquals(4, n1.getNumChildren());
            // premier petit fils -> ASTChaine
            assertTrue(n1.getChild(0) instanceof ASTChaine);
            // 2e, 3e et 4e fils -> ASTDeclaration avec 2 enfants
            for (int i = 1; i < 4; i++) {
                assertTrue(n1.getChild(i) instanceof ASTDeclaration);
                assertEquals(2, n1.getChild(i).getNumChildren());
                assertTrue(n1.getChild(i).getChild(0) instanceof ASTIdentificateur);
                assertTrue(n1.getChild(i).getChild(1) instanceof ASTNombre);
            }
        }
    }


    @Test
    public void testStationBuilder() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "../stationsOK.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        n.accept(builder);
        assertEquals(2, builder.getStations().size());
        int nb = 0;
        for (Station s : builder.getStations()) {
            if (s.getNom().equals("21 - Avenue Fontaine Argent, Boulevard Diderot")) {
                assertEquals(12, s.capacite());
                nb = nb | 1;
            }
            else if (s.getNom().equals("Avenue du Maréchal Foch")) {
                assertEquals(10, s.capacite());
                nb = nb | 2;
            }
        }
        assertEquals(3, nb);
    }

    @Test (expected = IOException.class)
    public void mauvaisFichier() throws IOException, StationParserException {
        parser.parse(new File(path+"mauvais.txt"));
    }

    @Test (expected = StationParserException.class)
    public void sansNom() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsSansNom.txt"));
    }

    @Test (expected = StationParserException.class)
    public void sansPointVirgule() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsErreurPointVirgule.txt"));
    }

    @Test (expected = StationParserException.class)
    public void sansAccolades() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsErreurAccolade.txt"));
    }

    @Test (expected = StationParserException.class)
    public void egual() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsEgal.txt"));
    }

    @Test (expected = StationParserException.class)
    public void mauvaisId() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsMauvaisIdentificateur.txt"));
    }

    @Test (expected = StationParserException.class)
    public void nombreVirgule() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsMauvaisNombre.txt"));
    }

    @Test (expected = StationParserException.class)
    public void valeurLettres() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsValeursLettres.txt"));
    }

    @Test (expected = StationParserException.class)
    public void doubleVirgule() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsNombreDoubleVirgule.txt"));
    }

    @Test (expected = StationParserException.class)
    public void nomEnChiffres() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsNomEnChiffres.txt"));
    }

    @Test (expected = StationParserException.class)
    public void mauvaiseStructure() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsMauvaiseStructure.txt"));
    }

    @Test (expected = StationParserException.class)
    public void stationsVide() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsVides.txt"));
    }

    @Test (expected = StationParserException.class)
    public void nomSimpleQuote() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsNomSimpleQuote.txt"));
    }

    @Test (expected = StationParserException.class)
    public void virgule() throws IOException, StationParserException {
        parser.parse(new File(path+"stationsVirgule.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_Guillemet() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_chaine.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_Guillemet2() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_singlequotes.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_Accolade() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_accolade.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_PasString() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_passtring.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_Backslash() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_backslash.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_Identificateur() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_identificateur.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_IdentificateurOneMoreThing() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_identificateuronemorething.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_PointVirgule() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_pointvirgule.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_PointVirguleDouble() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_pointvirguledouble.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_PointVirguleDernier() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_pointvirguledernier.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_DeuxPoints() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_deuxpoints.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_SansNombre() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_sansnombre.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_NombreVirgules() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_nombrevirgules.txt"));
    }

    @Test(expected = StationParserException.class)
    public void testParser_Stazion() throws IOException, StationParserException {
        parser.parse(new File(pathExtra + "station_stazion.txt"));
    }

}
