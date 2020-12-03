package fr.ufc.l3info.oprog;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;


public class VilleTest {
    private Ville ville;
    private Abonne valentin;

    final String path = "./target/classes/data/";
    final static String VALID_RIB = "12345-98765-12345678912-21";
    final static String INVALID_RIB = "507317-SOLEIL";


    @Before
    public void initVille() throws IOException {
        this.ville = new Ville();
        File f = new File(path + "stationsBesancon.txt");
        ville.initialiser(f);
    }

    @Test
    public void initialiser_mauvaisFichier() throws IOException {
        Ville empty = new Ville();
        File f = new File(path + "parserError/stationsEgal.txt");
        empty.initialiser(f);
        assertFalse(empty.iterator().hasNext());
        assertNull(empty.getStationPlusProche(0, 0));
    }

    @Test
    public void initialiser_bonOrdre() {
        assertTrue(ville.iterator().hasNext());
        String[] stations = {   "Gare Viotte",
                                "Square Bouchot",
                                "Isembart",
                                "Beaux Arts",
                                "Médiathèque",
                                "Bersot",
                                "Morand",
                                "8 Septembre",
                                "Révolution",
                                "Madeleine",
                                "Marulaz",
                                "Belfort",
                                "Place Leclerc",
                                "Xavier Marmier",
                                "La City",
                                "Saint Jacques",
                                "Mairie",
                                "Granvelle",
                                "Victor Hugo",
                                "Jean Cornet",
                                "Tassigny",
                                "Gare de la Mouillère",
                                "Office de tourisme",
                                "Place Flore",
                                "Place de la Liberté",
                                "Jacobins",
                                "Rivotte",
                                "Déportés",
                                "Beauregard",
                                "Gare d'Eau"
        };
        int i = 0;
        for (Station s : ville) {
            assertTrue(i <= 29);
            assertEquals(s.getNom(),stations[i]);
            i++;
        }
        assertEquals( 30,i);

    }

    @Test
    public void initialiser_stationPrincipaleDefaut() {
        Iterator<Station> it = ville.iterator();
        Station first = it.next();
        assertEquals("Gare Viotte", first.getNom());
    }

    @Test
    public void setStationPrincipale_null(){
        ville.setStationPrincipale(ville.getStation("Bersot"));
        ville.setStationPrincipale(null);
        assertEquals("Bersot",ville.iterator().next().getNom());
    }

    @Test
    public void stationPrincipaleChoisie(){
        ville.setStationPrincipale(ville.getStation("8 Septembre"));
        String [] stations = {  "8 Septembre",
                                "Morand",
                                "Médiathèque",
                                "Bersot",
                                "Tassigny",
                                "Gare de la Mouillère",
                                "Office de tourisme",
                                "Beaux Arts",
                                "Isembart",
                                "Square Bouchot",
                                "Gare Viotte",
                                "Place Flore",
                                "Place de la Liberté",
                                "Révolution",
                                "Madeleine",
                                "Marulaz",
                                "Belfort",
                                "Place Leclerc",
                                "Xavier Marmier",
                                "La City",
                                "Saint Jacques",
                                "Mairie",
                                "Granvelle",
                                "Victor Hugo",
                                "Jean Cornet",
                                "Jacobins",
                                "Rivotte",
                                "Déportés",
                                "Beauregard",
                                "Gare d'Eau"
        };

        int i = 0;
        for (Station s : ville) {
            assertTrue(i <= 29);
            assertEquals(s.getNom(),stations[i]);
            i++;
        }
        assertEquals( 30,i);
    }

    @Test
    public void getStation() {
        assertNotNull(ville.getStation("Gare Viotte"));
        assertNotNull(ville.getStation("Saint Jacques"));
        assertNotNull(ville.getStation("Granvelle"));
        assertNotNull(ville.getStation("Victor Hugo"));
        assertNotNull(ville.getStation("Mairie"));
        assertNotNull(ville.getStation("Jean Cornet"));
        assertNull(ville.getStation("Time Square"));
        assertNull(ville.getStation("Musée Ghibli"));
        assertNull(ville.getStation(null));
    }

    @Test
    public void creerAbonne_ok() {
        Abonne a = ville.creerAbonne("Valentin Jobs", VALID_RIB);
        assertNotNull(a);

        assertFalse(a.estBloque());
    }

    @Test
    public void creerAbonne_mauvaisRib() {
        Abonne a = ville.creerAbonne("Valentin Gates", INVALID_RIB);
        assertNotNull(a);
        assertTrue(a.estBloque());
    }

    @Test
    public void creerAbonne_mauvaisNom() {
        Abonne a = ville.creerAbonne("P0G", VALID_RIB);
        assertNull(a);
    }

    @Test
    public void creerAbonne_nomNull(){
        assertNull(ville.creerAbonne(null,VALID_RIB));
    }

    @Test
    public void creerAbonne_ribNull(){
        Abonne a = ville.creerAbonne("Maradona",null);
        assertTrue(a.estBloque());
    }

    @Test
    public void getStationPlusProche(){
        assertEquals("Gare de la Mouillère",ville.getStationPlusProche(47.23982496297786,6.033786686212529).getNom());
        assertEquals("Xavier Marmier",ville.getStationPlusProche(47,5).getNom());
    }

    public void emprunterABersot(){
        Station bersot = Mockito.spy(ville.getStation("Bersot"));
        assertEquals(11,bersot.capacite());
        bersot.arrimerVelo(new Velo('h'),6);
        this.valentin = ville.creerAbonne("Valentin",VALID_RIB);
        Mockito.when(bersot.maintenant()).thenReturn(1608114888000L);// 16 décembre 2020 à 10h34m48s
        IVelo velo = bersot.emprunterVelo(this.valentin,6);
        Mockito.when(bersot.maintenant()).thenReturn(1608118668000L);// 16 décembre 2020 à 11h37m48s
        bersot.arrimerVelo(velo,2);
    }

    @Test
    public void facturation_0emprunt(){
        emprunterABersot();
        Map<Abonne, Double> fact = ville.facturation(11,2020);
        assertEquals(1,fact.size());
        assertTrue(fact.containsValue(0.0));
        assertTrue(fact.containsKey(this.valentin));
    }



    @Test
    public void facturation_mauvaisMois(){
        emprunterABersot();

        Map<Abonne, Double> fact = ville.facturation(0,2020);
        assertEquals(1,fact.size());
        assertTrue(fact.containsValue(0.0));
        assertTrue(fact.containsKey(this.valentin));

        fact = ville.facturation(13,2020);
        assertEquals(1,fact.size());
        assertTrue(fact.containsValue(0.0));
        assertTrue(fact.containsKey(this.valentin));
    }

    @Test
    public void facturation(){
        emprunterABersot();
        Abonne pog = ville.creerAbonne("Pog",INVALID_RIB);
        Map<Abonne, Double> fact = ville.facturation(12,2020);
        assertEquals(2,fact.size());
        assertEquals(fact.get(pog),0.0,1e-3);
        assertEquals(fact.get(this.valentin),2.1,1e-3);
    }

    @Test
    public void facturation_2emprunts(){
        emprunterABersot();

        Station city = Mockito.spy(ville.getStation("La City"));
        IVelo velo = new Velo('f');
        velo = new OptAssistanceElectrique(velo);
        city.arrimerVelo(velo,1);
        Mockito.when(city.maintenant()).thenReturn(1608463987000L);// 20 décembre 2020 à 12h33m07s
        velo = city.emprunterVelo(this.valentin,1);
        Mockito.when(city.maintenant()).thenReturn(1608472567000L);// 20 décembre 2020 à 14h56m07s
        city.arrimerVelo(velo,2);

        Map<Abonne,Double> fact = ville.facturation(12,2020);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.valentin),11.633,1e-3);
    }

    @Test
    public void facturation_empruntEnCourt(){
        emprunterABersot();

        Station city = Mockito.spy(ville.getStation("La City"));
        IVelo velo = new Velo('f');
        velo = new OptAssistanceElectrique(velo);
        city.arrimerVelo(velo,1);
        Mockito.when(city.maintenant()).thenReturn(1609435897000L);// 31 décembre 2020 à 18h31m37s
        velo = city.emprunterVelo(this.valentin,1);

        Map<Abonne,Double> fact = ville.facturation(12,2020);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.valentin),2.1,1e-3);

        Mockito.when(city.maintenant()).thenReturn(1609500787000L);// 01 janvier 2021 à 12h33m07s
        city.arrimerVelo(velo,1);

        fact = ville.facturation(12,2020);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.valentin),2.1,1e-3);

        fact = ville.facturation(1,2021);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.valentin),72.066,1e-3);
    }


}
