package fr.ufc.l3info.oprog.parser;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Visiteur réalisant des vérifications sur l'AST du fichier de stations.
 */
public class ASTCheckerVisitor implements ASTNodeVisitor {
    private final Map<String, ERROR_KIND> errors;
    private final Set<String> stations;

    public ASTCheckerVisitor() {
        this.errors = new HashMap<>();
        this.stations = new HashSet<>();
    }

    public Map<String, ERROR_KIND> getErrors() {
        return errors;
    }

    @Override
    public Object visit(ASTNode n) {
        assert (false);
        return null;
    }

    @Override
    public Object visit(ASTListeStations n) {

        if (n.children.size() == 0) {
            errors.put(n.getLCPrefix() + " La liste de station est vide", ERROR_KIND.EMPTY_LIST);
        }

        for (ASTNode child : n) {
            child.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(ASTStation n) {
        String name = (String) n.getChild(0).accept(this);
        if (stations.contains(name)) {
            errors.put(n.getLCPrefix() + " Duplication de la station '"+name+"'"  , ERROR_KIND.DUPLICATE_STATION_NAME);
        }
        stations.add(name);

        boolean haveCapacite = false;
        boolean haveLatitude = false;
        boolean haveLongitude = false;

        for (int i = 1; i < n.getNumChildren(); ++i) {
            String childName = (String) n.getChild(i).accept(this);

            switch (childName) {
                case "capacite": {
                    if (haveCapacite) {
                        errors.put(n.getChild(i).getLCPrefix()+" Duplication de la déclaration '"+childName+"'" , ERROR_KIND.DUPLICATE_DECLARATION);
                    }
                    haveCapacite = true;
                    break;
                }
                case "latitude" : {
                    if (haveLatitude) {
                        errors.put(n.getChild(i).getLCPrefix()+" Duplication de la déclaration '"+childName+"'" , ERROR_KIND.DUPLICATE_DECLARATION);
                    }
                    haveLatitude = true;
                    break;
                }
                case "longitude" : {
                    if (haveLongitude) {
                        errors.put(n.getChild(i).getLCPrefix()+" Duplication de la déclaration '"+childName+"'" , ERROR_KIND.DUPLICATE_DECLARATION);
                    }
                    haveLongitude = true;
                    break;
                }
                default:
                    assert(false);
            }
        }

        if(!haveCapacite){
            errors.put(n.getLCPrefix()+" Déclaration 'capacite' manquante",ERROR_KIND.MISSING_DECLARATION);
        }
        if(!haveLatitude){
            errors.put(n.getLCPrefix()+" Déclaration 'latitude' manquante",ERROR_KIND.MISSING_DECLARATION);
        }
        if(!haveLongitude){
            errors.put(n.getLCPrefix()+" Déclaration 'longitude' manquante",ERROR_KIND.MISSING_DECLARATION);
        }

        return null;
    }

    @Override
    public Object visit(ASTDeclaration n) {
        String key = (String) n.getChild(0).accept(this);
        String value = (String) n.getChild(1).accept(this);

        if (key.equals("capacite")) {
            if (value.contains(".")) {
                this.errors.put(n.getLCPrefix() + " La capacité doit être un entier", ERROR_KIND.WRONG_NUMBER_VALUE);
            }
            if (Double.parseDouble(value) <= 0) {
                this.errors.put(n.getLCPrefix() + " La capacité doit être strictement positive", ERROR_KIND.WRONG_NUMBER_VALUE);
            }


        }

        return key;
    }

    @Override
    public Object visit(ASTChaine n) {
        String chaine = n.toString().substring(1,n.toString().length() - 1);

        if(chaine.trim().length() == 0){
            errors.put(n.getLCPrefix()+" La chaine est vide",ERROR_KIND.EMPTY_STATION_NAME);
        }
        return chaine;
    }

    @Override
    public Object visit(ASTIdentificateur n) {
        return n.value;
    }

    @Override
    public Object visit(ASTNombre n) {
        return n.value;
    }
}

enum ERROR_KIND {
    EMPTY_LIST,
    EMPTY_STATION_NAME,
    DUPLICATE_STATION_NAME,
    MISSING_DECLARATION,
    DUPLICATE_DECLARATION,
    WRONG_NUMBER_VALUE
}