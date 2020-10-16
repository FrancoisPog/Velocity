package fr.ufc.l3info.oprog;


import org.omg.CORBA.MARSHAL;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.Iterator;
import java.util.Set;

public class Station {

    private final double latitude;
    private final double longitude;
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

    public void equilibrer(Set<IVelo> velos){

        for(int i = 0 ; i < this.capacite ; ++i){

            if(this.velos[i] != null && ( this.velos[i].estAbime() || this.velos[i].prochaineRevision() <= 0 )){
                velos.add(this.velos[i]);
                this.velos[i].decrocher();
                this.velos[i] = null;
            }
        }

        if(nbBornesLibres() <= Math.floor(capacite/2.0)){
            if(nbBornesLibres() == Math.floor(capacite/2.0)){
                return;
            }else{

                int i = 0;
                while( nbBornesLibres() < Math.floor(capacite/2.0)){

                    if(this.velos[i] == null){
                        i++;
                        continue;
                    }
                    velos.add(this.velos[i]);
                    this.velos[i].decrocher();
                    this.velos[i] = null;
                    i++;
                }
            }
        }

        deposerVelos(velos,false);

        if(nbBornesLibres() <= Math.floor(capacite/2.0)){
            return;
        }

        deposerVelos(velos,true);

    }

    private void deposerVelos(Set<IVelo> nouveaux, boolean poserBesoinRevision){
        Iterator<IVelo> it = nouveaux.iterator();

        while(it.hasNext() && nbBornesLibres() > Math.floor(capacite/2.0)) {
            IVelo velo = it.next();
            if(velo.estAbime() || ( !poserBesoinRevision && velo.prochaineRevision() <= 0 ) ){
                continue;
            }
            int i = 0 ;
            while(this.velos[i] != null){i++;};

            this.velos[i] = velo;
            this.velos[i].arrimer();
        }

        for(int i = 0 ; i < this.capacite ; ++i){
            nouveaux.remove(this.velos[i]);
        }
    }



    public long maintenant() {
        return System.currentTimeMillis();
    }


}
