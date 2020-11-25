package fr.ufc.l3info.oprog;

import java.util.List;

/**
 * Interface décrivant les registres. Ceux-ci enregistrent les emprunts de vélos,
 * avec une date de début et une date de fin.
 */
public interface IRegistre {

    /**
     * Enregistre l'emprunt du vélo v pour l'abonné a avec la date d'emprunt d
     * @param a l'abonné qui réalise l'emprunt
     * @param v le vélo qui est emprunté par l'abonné
     * @param d la date d'emprunt considérée (en ms)
     * @return   0 pour indiquer que l'emprunt a bien été réalisé.
     *          -1 pour signaler un paramètre incorrect (valeur null d'abonné ou de vélo)
     *          -2 pour signaler que le vélo est déjà emprunté ou a déjà été emprunté à cette date
     */
    int emprunter(Abonne a, IVelo v, long d);

    /**
     * Enregistre le retour du vélo v qui était en cours d'emprunt
     * @param v le vélo qui est retourné
     * @param d la date de retour du vélo (en ms)
     * @return  0 pour indiquer que l'emprunt a correctement été clôturé.
     *         -1 pour signaler un paramère incorrect (vélo null)
     *         -2 pour signaler que le vélo n'était pas emprunté
     *         -3 pour signaler si la date de retour est incorrecte (antérieure à la date d'emprunt ou chevauchant une autre période d'emprunt du même vélo)
     */
    int retourner(IVelo v, long d);

    /**
     * Permet de calculer le nombre d'emprunts en cours de l'abonné passé en paramètre.
     * Potentiellement l'abonné peut réaliser plusieurs emprunts en même temps, même si
     * dans la pratique, cela lui sera interdit.
     * @param a l'abonné considéré.
     * @return une valeur >= 0 qui indique le nombre d'emprunts en cours de a
     */
    int nbEmpruntsEnCours(Abonne a);

    /**
     * Permet de calculer le montant de la facturation de l'abonné en paramètre pour
     * tous les emprunts qui se sont clôturés entre la date de début et la date de fin
     * passées en paramètre.
     * @param a     l'abonné considéré
     * @param debut la date de début de la période considérée
     * @param fin   la date de fin de la période considérée
     * @return la somme à facturer à l'abonné en fonction des vélos empruntés et du temps
     *          d'utilisation de chaque vélo.
     */
    double facturation(Abonne a, long debut, long fin);

    /**
     * Permet de connaitre l'emprunteur d'un vélo
     * @param v Le vélo emprunté
     * @return L'emprunteur courant du vélo passé en paramètre (null si le vélo n'est pas en cours d'emprunt)
     */
    Abonne emprunteur(IVelo v);
}