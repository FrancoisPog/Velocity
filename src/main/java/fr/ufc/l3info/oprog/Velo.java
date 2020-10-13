package fr.ufc.l3info.oprog;

import java.util.Locale;

public class Velo implements IVelo {

    private final static double TARIF = 2.0;
    private final static double DISTANCE_MAX_ENTRE_REVISIONS = 500.0;

    private boolean estAbime;
    private boolean estDecroche;
    private final char type;
    private double kilometrage;
    private double kmDerniereRevision;

    public Velo() {
        this('m');
    }

    public Velo(char t) {
        this.estAbime = false;
        this.estDecroche = true;
        this.kilometrage = 0;

        t = Character.toLowerCase(t);

        if (t == 'h') {
            this.type = 'h';
        } else if (t == 'f') {
            this.type = 'f';
        } else {
            this.type = 'm';
        }

    }

    @Override
    public double kilometrage() {
        return this.kilometrage;
    }

    @Override
    public double prochaineRevision() {
        return DISTANCE_MAX_ENTRE_REVISIONS - (this.kilometrage() - this.kmDerniereRevision);
    }

    @Override
    public void parcourir(double km) {
        if (this.estDecroche && km > 0) {
            this.kilometrage += km;
        }
    }

    @Override
    public double tarif() {
        return TARIF;
    }

    @Override
    public int decrocher() {
        if (this.estDecroche) {
            return -1;
        } else {
            this.estDecroche = true;
            return 0;
        }
    }

    @Override

    public int arrimer() {
        if (this.estDecroche) {
            this.estDecroche = false;
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void abimer() {
        this.estAbime = true;
    }

    @Override
    public boolean estAbime() {
        return this.estAbime;
    }

    @Override
    public int reviser() {
        if (!this.estDecroche) {
            return -1;
        }

        this.estAbime = false;
        this.kmDerniereRevision = this.kilometrage;
        return 0;
    }

    @Override
    public int reparer() {
        if (!this.estDecroche) {
            return -1;
        }
        if (this.estAbime) {
            this.estAbime = false;
            return 0;
        }

        return -2;

    }

    public String toString() {

        String res = "Vélo cadre ";

        if (this.type == 'f') {
            res += "femme - ";
        } else if (this.type == 'h') {
            res += "homme - ";
        } else {
            res += "mixte - ";
        }

        res += String.format(Locale.ENGLISH,"%.1f km", this.kilometrage);

        if (prochaineRevision() <= 0) {
            res += " (révision nécessaire)";
        }

        return res;
    }
}
