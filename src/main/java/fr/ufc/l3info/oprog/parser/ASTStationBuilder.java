package fr.ufc.l3info.oprog.parser;

import fr.ufc.l3info.oprog.Station;

import java.util.HashSet;
import java.util.Set;

/**
 *  Visiteur permettant de construire un ensemble de stations à partir d'un AST
 */
public class ASTStationBuilder implements ASTNodeVisitor {

    private Set<Station> stations;

    public Set<Station> getStations() {
        return new HashSet<>(stations);
    }

    public ASTStationBuilder() {
        this.reset();
    }

    public void reset() {
        stations = new HashSet<>();
    }

    @Override
    public Object visit(ASTNode n) {
        return null;
    }

    @Override
    public Object visit(ASTListeStations n) {
        for (ASTNode child : n) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ASTStation n) {
        String name = (String) n.getChild(0).accept(this);
        double lon = 0.0, lat = 0.0;
        int cap = 0;
        for (int i=1; i < n.getNumChildren(); i++) {
            Object[] decl = (Object[]) n.getChild(i).accept(this);
            switch ((String) decl[0]) {
                case "capacite":
                    cap = ((Double) decl[1]).intValue();
                    break;
                case "longitude":
                    lon = (double) decl[1];
                    break;
                default:
                    lat = (double) decl[1];
                    break;
            }
        }
        stations.add(new Station(name, lat, lon, cap));
        return null;
    }

    @Override
    public Object visit(ASTDeclaration n) {
        String key = (String) n.getChild(0).accept(this);
        Double value = (Double) n.getChild(1).accept(this);
        return new Object[] { key, value };
    }

    @Override
    public Object visit(ASTChaine n) {
        return n.toString().substring(1, n.toString().length()-1);
    }

    @Override
    public Object visit(ASTIdentificateur n) {
        return n.toString();
    }

    @Override
    public Object visit(ASTNombre n) {
        return n.getNumberValue();
    }
}
