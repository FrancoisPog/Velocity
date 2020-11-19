package fr.ufc.l3info.oprog.parser;

import org.junit.Assert;
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
    final String pathExtra = "./target/classes/data/extraError/";

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

    /* ---------- Station de vélo correcte ---------- */

    @Test
    public void testStation_OK() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "stationsOK.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    /* ---------- ListeStation ---------- */

    @Test
    public void testListeStations_Vide() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_vide.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.EMPTY_LIST));
    }

    /* ---------- Nom de la station ---------- */

    @Test
    public void testNomStation_Vide() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_nomvide.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
    }

    @Test
    public void testNomStation_FauxVide() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "stations_nomfauxvide.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
    }

    @Test
    public void testNomStation_Double() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "stations_nomdouble.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_STATION_NAME));
    }

    /* ---------- Identificateurs : coordonnées ---------- */

    @Test
    public void testIdentificateurs_LongitudeNon() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_longitudenon.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
    }

    @Test
    public void testIdentificateurs_LongitudeDouble() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_longitudedouble.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
    }

    @Test
    public void testIdentificateurs_CoordNegative() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_coordnegative.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    /* ---------- Identificateurs : capacité ---------- */

    @Test
    public void testIdentificateurs_CapaciteNegative() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_capacitenegative.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
    }

    @Test
    public void testIdentificateurs_CapaciteDouble() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_capacitedouble.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
    }

    @Test
    public void testIdentificateurs_CapaciteNegativeDouble() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_capacitenegativedouble.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
    }

    @Test
    public void testIdentificateurs_CapaciteManquante() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_capacitepasla.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
    }

    /* ---------- Plusieurs erreurs ---------- */

    @Test
    public void testErreurs_PlusieursIdentificateursDouble() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_identificateursdouble.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
    }

    @Test
    public void testErreurs_MemeErreur2Fois() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_erreursdeuxfois.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
    }

    @Test
    public void testErreurs_PlusieursErreur() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "station_erreursplusieurs.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
        Assert.assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
    }

    @Test
    public void testErreurs_RienNeVa() throws StationParserException, IOException {
        ASTNode node = parser.parse(new File(pathExtra + "stations_erreursrienneva.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        node.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertEquals(6, errors.size());
        Assert.assertTrue(errors.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
        Assert.assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_STATION_NAME));
        Assert.assertTrue(errors.containsValue(ERROR_KIND.MISSING_DECLARATION));
        Assert.assertTrue(errors.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
        Assert.assertTrue(errors.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
    }

}
