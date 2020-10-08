package fr.ufc.l3info.oprog;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Classe représentant un abonné au service VéloCité.
 */
public class Abonne {

    private static int nombreInstances;

    private final String nom;
    private String rib;
    private final int id;
    private boolean estBloque;



    /**
     * Créé un abonné dont le nom est passé en paramètre, sans informations bancaires.
     * Si le nom de l'abonné n'est pas correct (vide ou ne contenant pas des lettres éventuellement séparées par des espaces ou des traits d'union), le constructeur déclenchera l'exception IncorrectNameException.
     * On notera que le nom peut contenir des espaces inutiles en début et en fin, mais ceux-ci seront retirés pour enregistrer cette donnée.
     *
     * @param nom le nom du nouvel abonné.
     * @throws IncorrectNameException si le nom de l'abonné n'est pas correct.
     */
    public Abonne(String nom) throws IncorrectNameException {
        if(nom == null){
            throw new IncorrectNameException();
        }
        nom = nom.trim();
        if (nom.length() == 0 || !Pattern.matches("^[\\p{L}]+([ -][\\p{L}]+)*$", nom)) {
            throw new IncorrectNameException();
        }

        this.nom = nom;
        this.estBloque = false;
        this.rib = null;
        this.id = Abonne.nombreInstances++;

    }

    /**
     * Créé un abonné dont le nom est passé en paramètre, avec les informations bancaires spécifiées dans le second paramètre.
     * Le comportement attendu est le même que celui du constructeur précédent. Le RIB n'est enregistré que si celui-ci est valide.
     *
     * @param nom le nom du nouvel abonné.
     * @param rib le RIB
     * @throws IncorrectNameException si le nom de l'abonné n'est pas correct.
     */
    public Abonne(String nom, String rib) throws IncorrectNameException {
        this(nom);

        if (ribEstValide(rib)) {
            this.rib = rib;
        }
    }

    /**
     * Renvoie l'identifiant de l'abonné, généré autoamtiquement à sa création.
     *
     * @return l'identifiant de l'abonné.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Renvoie le nom de l'abonné.
     *
     * @return le nom de l'abonné, sans les éventuels espace en début et en fin de chaîne.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Met à jour l'ancien RIB pour un nouveau. Si le nouveau RIB n'est pas valide, l'abonné conserve ses anciennes coordonnées bancaires.
     *
     * @param rib nouveau RIB pour la mise à jour.
     */
    public void miseAJourRIB(String rib) {
        if (ribEstValide(rib)) {
            this.rib = rib;
        }

    }

    /**
     * Permet de bloquer volontairement un abonné.
     */
    public void bloquer() {
        this.estBloque = true;
    }

    /**
     * Permet de débloquer un abonné.
     */
    public void debloquer() {
        this.estBloque = false;
    }

    /**
     * Vérifie si un abonné est bloqué. Celui-ci peut être bloqué volontairement ou parce que ses coordonnées bancaires sont invalides.
     *
     * @return true si l'abonné est considéré comme bloqué, false sinon.
     */
    public boolean estBloque() {
        return this.estBloque || rib == null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Abonne abonne = (Abonne) o;
        return id == abonne.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Vérifie qu'un rib est valide
     *
     * @param rib Le rib
     * @return True si le rib est valide, faux sinon
     */
    private boolean ribEstValide(String rib) {
        if (rib == null || !Pattern.matches("^(\\d{5}-){2}\\d{11}-\\d{2}$", rib)) {
            return false;
        }
        String[] ribData = rib.split("-");
        long key = 97 - ((89 * Long.parseLong(ribData[0]) + 15 * Long.parseLong(ribData[1]) + 3 * Long.parseLong(ribData[2])) % 97);
        return key == Long.parseLong(ribData[3]);

    }

}

class IncorrectNameException extends Exception {
    public IncorrectNameException() {
        super("Le nom fourni n'est pas correct.");
    }
}

