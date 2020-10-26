package fr.ufc.l3info.oprog;


import java.util.ArrayList;
import java.util.List;
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
        return velos[b - 1];
    }

    public IVelo emprunterVelo(Abonne a, int b) {

        if (a.estBloque() || this.registre == null || this.registre.nbEmpruntsEnCours(a) != 0) {
            return null;
        }
        if (this.registre.emprunter(a, this.velos[b - 1], maintenant()) != 0) {
            return null;
        }
        IVelo v = veloALaBorne(b);
        if (v != null) {
            this.velos[b - 1] = null;
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
            this.velos[b - 1] = v;
            return -4;
        }

        this.velos[b - 1] = v;
        return 0;
    }

    public double distance(Station s) {
        double r = 6371e3;
        double p1 = this.latitude * Math.PI / 180;
        double p2 = s.latitude * Math.PI / 180;
        double dP = (s.latitude - this.latitude) * Math.PI / 180;
        double dL = (s.longitude - this.longitude) * Math.PI / 180;

        double a = Math.sin(dP / 2) * Math.sin(dP / 2) +
                Math.cos(p1) * Math.cos(p2) *
                        Math.sin(dL / 2) * Math.sin(dL / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(r * c);

    }

    public void equilibrer(Set<IVelo> velos) {

        // Remplacement des vélos abimés
        remplacerAbime(velos);

        // On retire les vélos ayant besoin de révisions
        viderBesoinDeRevision(velos);

        // On remplit la station avec le bon nombre de vélos en bon état
        remplirBonEtat(velos);

        if (nbBornesLibres() < Math.floor(capacite / 2.0)) {

            // Si il reste trop de vélos on en récupère
            int i = 0;
            while (nbBornesLibres() < Math.floor(capacite / 2.0)) {

                if (this.velos[i] == null) {
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

    /**
     * Remplace les vélos abimés par des vélos en bon état, si possible
     *
     * @param nouveaux L'ensemble de vélos dispo
     */
    private void remplacerAbime(Set<IVelo> nouveaux) {
        int i = 0;
        int count = 0;
        for (IVelo velo : this.velos) {
            if (velo != null && velo.estAbime()) {
                velo.decrocher();
                velos[i] = null;
                nouveaux.add(velo);
                count++;
            }
            i++;
        }

        i = 0;
        for (IVelo velo : nouveaux) {
            if (count == 0) {
                break;
            }
            if (!velo.estAbime() && velo.prochaineRevision() > 0) {
                while (velos[i] != null) {
                    i++;
                }
                velos[i] = velo;
                velo.arrimer();
                count--;
            }
        }

        for (IVelo velo : velos) {
            nouveaux.remove(velo);
        }
    }

    /**
     * Retire les vélos ayant besoin de révision, en s'assurant d'avoir assez de vélos en bon état dispo pour les remplacer et avoir le bon nombre de vélos à la fin
     *
     * @param nouveaux L'ensemble de vélos dispo
     */
    private void viderBesoinDeRevision(Set<IVelo> nouveaux) {
        List<IVelo> retires = new ArrayList<>();


        // Compte des vélos en bon état
        int nbVelosBonEtat = 0;
        for (IVelo velo : nouveaux) {
            if (!velo.estAbime() && velo.prochaineRevision() > 0) {
                nbVelosBonEtat++;
            }
        }

        int nbVelosEnTrop = (int) ((this.capacite - this.nbBornesLibres()) - Math.ceil(capacite / 2.0));
        if (nbVelosEnTrop < 0) {
            nbVelosEnTrop = 0;
        }


        // On peut retirer autant de vélos qu'il y a en trop + le nombre de vélos qui peuvent etre remplacé par un bon état
        int nbVelosAEnlever = nbVelosEnTrop + nbVelosBonEtat;

        //System.out.println("Bon etat  "+nbVelosBonEtat);
        //System.out.println("A enlever  "+nbVelosAEnlever);
        //System.out.println("En trop  "+nbVelosEnTrop);

        // Décrochage des vélos qui ont besoin de révision
        for (int i = 0; i < this.capacite; ++i) {
            if (velos[i] != null && velos[i].prochaineRevision() <= 0 && (nbVelosAEnlever > 0)) {
                velos[i].decrocher();
                retires.add(velos[i]);
                velos[i] = null;
                nbVelosAEnlever--;
            }
            if (nbVelosBonEtat == 0) {
                break;
            }
        }

        // Ajout des vélos retirés dans le set
        nouveaux.addAll(retires);
    }

    /**
     * Rempli la station avec des vélos en bon état pour que la moitié des bornes soit occupé, si possibles
     *
     * @param nouveaux L'ensemble de vélos dispo
     */
    private void remplirBonEtat(Set<IVelo> nouveaux) {
        // Ajout de vélos en bon état tant que
        int i = 0;
        for (IVelo velo : nouveaux) {
            if (this.nbBornesLibres() <= Math.floor(capacite / 2.0)) {
                break;
            }
            if (!velo.estAbime() && velo.prochaineRevision() > 0) {
                while (velos[i] != null) {
                    i++;
                }
                velos[i] = velo;
                velo.arrimer();
            }
        }

        for (IVelo velo : velos) {
            nouveaux.remove(velo);
        }
    }


    public long maintenant() {
        return System.currentTimeMillis();
    }


}
