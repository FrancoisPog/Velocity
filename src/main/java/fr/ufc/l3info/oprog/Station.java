package fr.ufc.l3info.oprog;


import org.omg.CORBA.MARSHAL;

public class Station {

    private double latitude;
    private double longitude;
    private final String nom;
    private final int capacite;
    private IRegistre registre;
    private final IVelo[] velos;


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
        if(this.registre.emprunter(a, this.velos[b-1], maintenant()) != 0 ){
            return null;
        }
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

    public double distance(Station s){
        double r = 6371e3;
        double p1 = this.latitude * Math.PI/180;
        double p2 = s.latitude * Math.PI/180;
        double dP = (s.latitude-this.latitude) * Math.PI / 180;
        double dL = (s.longitude - this.longitude) * Math.PI / 180;

        double a =  Math.sin(dP/2) * Math.sin(dP/2) +
                    Math.cos(p1) * Math.cos(p2) *
                    Math.sin(dL/2) * Math.sin(dL/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return Math.round(r * c);

    }

    public long maintenant() {
        return System.currentTimeMillis();
    }


}
