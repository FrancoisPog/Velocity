package fr.ufc.l3info.oprog.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ASTCheckerVisitorTest {

    /** Chemin vers les fichiers de test */
    final String path = "./target/classes/data/checkerError/";

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();


    @Test
    public void stationOK() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "../stationsOK.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);
    }

    @Test
    public void listeVide() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "fichierVide.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.EMPTY_LIST));
    }

    @Test
    public void duplicationStations() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "duplicationStations.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_STATION_NAME));
    }

    @Test
    public void capaciteDouble() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "capaciteDouble.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
    }

    @Test
    public void capaciteNegative() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "capaciteNegative.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
    }

}
