package fr.ufc.l3info.oprog;

/**
 * Interface décrivant les services offerts par un vélo.
 */
public interface IVelo {

    /**
     * Indique le nombre de kilomètres total qu'a déjà parcouru le vélo.
     * @return le kilométrage du vélo.
     */
    double kilometrage();

    /**
     * Indique le nombre de kilomètres qu'il reste au vélo avant la prochaine révision.
     * Une valeur négative ou nulle indiquera qu'il est temps d'effectuer la révision.
     * @return le nombre de kilomètres avant la prochaine révision.
     */
    double prochaineRevision();

    /**
     * Fait parcourir au vélo en cours d'emprunt le nombre de kilomètres passé en paramètre.
     * Si le vélo n'est pas emprunté, cette méthode est sans effet.
     * @param km le nombre de kilomètres parcourus.
     */
    void parcourir(double km);

    /**
     * Tarif horaire pour le vélo.
     * @return Le tarif de location pour une heure de vélo (positif ou nul)
     */
    double tarif();

    /**
     * Permet de décrocher le vélo de sa borne.
     * @return  0 si le vélo a effectivement pu être décroché,
     *          -1 si le vélo est déjà décroché.
     */
    int decrocher();

    /**
     * Permet de raccrocher le vélo à une borne.
     * @return  0 si le vélo a effectivement pu être accroché,
     *          -1 si le vélo est déjà accroché.
     */
    int arrimer();

    /**
     * Abime le vélo, quelque soit son état d'origine.
     */
    void abimer();

    /**
     * Indique si le vélo est en bon état ou abimé.
     * @return true si le vélo est abimé, false sinon.
     */
    boolean estAbime();

    /**
     * Permet de réviser le vélo lorsque celui-ci est décroché. Cette action a pour effet
     * de réinitialiser le décompte des kilomètres à parcourir avant la prochaine révision.
     * Si le vélo était abimé, la révision a pour effet de le réparer.
     * @return  0 si la révision a pu être effectuée,
     *          -1 sinon (le vélo est encore accroché).
     */
    int reviser();

    /**
     * Permet de réparer un vélo. La réparation s'effectue sur un vélo abimé qui n'est pas accroché.
     * @return  0 si le vélo a pu être réparé,
     *          -1 si le vélo est accroché,
     *          -2 si le vélo est décroché, mais qu'il n'est pas abimé.
     */
    int reparer();

    /**
     * Génère une chaîne de caractères décrivant le vélo.
     * @return une chaîne décrivant le vélo.
     */
    String toString();

}
