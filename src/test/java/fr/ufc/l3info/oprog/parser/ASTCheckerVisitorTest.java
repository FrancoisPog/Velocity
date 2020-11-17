package fr.ufc.l3info.oprog.parser;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(0,errors.size());
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
        //print_errors(errors);
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
        //print_errors(errors);
    }

    @Test
    public void capaciteAvecVirgule() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "capaciteDouble.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
        //print_errors(errors);
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
        //print_errors(errors);
    }

    @Test
    public void nomStationVide() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "stationsNomVide.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
        //print_errors(errors);
    }

    @Test
    public void declarationManquante() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "declarationManquante.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
        //print_errors(errors);
    }

    @Test
    public void declarationEnDouble() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "declarationEnDouble.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(1,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
        //print_errors(errors);
    }

    @Test
    public void toutFaux() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "toutFaux.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(6,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
        assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
        assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
        assertTrue(errors.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
        assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_STATION_NAME));
        assertEquals(Collections.frequency(errors.values(),ERROR_KIND.WRONG_NUMBER_VALUE),2);
        //print_errors(errors);
    }

    @Test
    public void declarationsManquantes() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "declarationsManquantes.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(4,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
        assertEquals(Collections.frequency(errors.values(),ERROR_KIND.MISSING_DECLARATION),4);
        //print_errors(errors);
    }

    @Test
    public void declarationManquanteEtDeclarationEnDouble() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "declarationManquanteEtDeclarationEnDouble.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(2,errors.size());
        assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
        assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
        //print_errors(errors);
    }

    @Test
    public void capaciteNegativeAvecVirgule() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "capaciteNegativeAvecVirgule.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        assertEquals(2,errors.size());
        assertEquals(2,Collections.frequency(errors.values(),ERROR_KIND.WRONG_NUMBER_VALUE));
        //print_errors(errors);
    }

    @Test
    public void onzeErreurs() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "onzeErreurs.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        //print_errors(errors);
        assertEquals(11,errors.size());
        assertEquals(2,Collections.frequency(errors.values(),ERROR_KIND.WRONG_NUMBER_VALUE));
        assertEquals(1,Collections.frequency(errors.values(),ERROR_KIND.DUPLICATE_STATION_NAME));
        assertEquals(2,Collections.frequency(errors.values(),ERROR_KIND.MISSING_DECLARATION));
        assertEquals(4,Collections.frequency(errors.values(),ERROR_KIND.DUPLICATE_DECLARATION));
        assertEquals(2,Collections.frequency(errors.values(),ERROR_KIND.EMPTY_STATION_NAME));
    }


    @Test
    public void duplicationDeclarations() throws IOException, StationParserException {
        ASTNode root = parser.parse(new File(path + "duplicationDeclarations.txt"));
        ASTStationBuilder builder = new ASTStationBuilder();
        root.accept(builder);
        ASTCheckerVisitor checker = new ASTCheckerVisitor();
        root.accept(checker);

        Map<String,ERROR_KIND> errors = checker.getErrors();
        //print_errors(errors);
        assertEquals(6,errors.size());
        assertEquals(6,Collections.frequency(errors.values(),ERROR_KIND.DUPLICATE_DECLARATION));

    }

    public void print_errors(Map<String,ERROR_KIND> errors){
        for(String error : errors.keySet()){
            System.out.println(errors.get(error) +" - "+ error);
        }
    }


}
