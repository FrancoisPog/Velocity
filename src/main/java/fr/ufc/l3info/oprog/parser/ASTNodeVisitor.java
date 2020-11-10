package fr.ufc.l3info.oprog.parser;

/**
 * Interface de visiteur pour les arbres de syntaxe abstraite utilisant des noeuds ASTNode.
 */
public interface ASTNodeVisitor {
    
    public Object visit(ASTNode n);
    public Object visit(ASTListeStations n);
    public Object visit(ASTStation n);
    public Object visit(ASTDeclaration n);
    public Object visit(ASTChaine n);
    public Object visit(ASTIdentificateur n);
    public Object visit(ASTNombre n);

}
