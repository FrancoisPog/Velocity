package fr.ufc.l3info.oprog;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private double latitude;
    private double longitude;
    private String nom;
    private int capacite;
    private IRegistre registre;
    private IVelo[] velos;


    public Station(String nom, double latitude, double longitude, int capacite) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacite = capacite;
        this.registre = null;

        if (this.capacite < 0) {
            this.velos = new Velo[0];
            return;
        }

        this.velos = new Velo[this.capacite];
        for (int i = 0; i < this.capacite; ++i) {
            velos[i] = null;
        }
    }

    public void setRegistre(IRegistre registre) {
        this.registre = registre;
    }

    public String getNom() {
        return this.nom;
    }

    public int capacite() {
        return this.capacite;
    }

    public int nbBornesLibres() {
        int nbBornesLibre = 0;
        for (int i = 0; i < this.capacite; ++i) {
            if (velos[i] == null) {
                nbBornesLibre++;
            }
        }
        return nbBornesLibre;
    }

    public IVelo veloALaBorne(int b) {
        if (b < 1 || b > this.capacite) {
            return null;
        }
        return velos[b-1];
    }

    public IVelo emprunterVelo(Abonne a, int b) {

        if (a.estBloque() || this.registre == null || this.registre.nbEmpruntsEnCours(a) != 0) {
            return null;
        }
        this.registre.emprunter(a, this.velos[b-1], maintenant());
        IVelo v = veloALaBorne(b);
        if (v != null) {
            this.velos[b-1] = null;
        }
        return v;
    }


    public int arrimerVelo(IVelo v, int b) {
        if (v == null || b < 1 || b > this.capacite) {
            return -1;
        }
        if (this.registre == null || veloALaBorne(b) != null) {
            return -2;
        }

        if (v.arrimer() != 0) {
            return -3;
        }

        if (this.registre.retourner(v, maintenant()) != 0) {
            this.velos[b-1] = v;
            return -4;
        }

        this.velos[b-1] = v;
        return 0;
    }

    public long maintenant() {
        return System.currentTimeMillis();
    }


}
