package fr.ufc.l3info.oprog;

public abstract class Option implements IVelo {
    IVelo _original;
    double tarifOption;
    String description;

    public Option(IVelo original, double tarif, String description){
        if(original == null){
            original = new Velo();
        }
        this._original = original;
        this.tarifOption = tarif;
        this.description = description;
    }

    @Override
    public double tarif(){
        return this._original.tarif() + this.tarifOption;
    }

    @Override
    public double kilometrage() {
        return this._original.kilometrage();
    }

    @Override
    public double prochaineRevision() {
        return this._original.prochaineRevision();
    }

    @Override
    public void parcourir(double km) {
        this._original.parcourir(km);
    }


    @Override
    public int decrocher() {
        return this._original.decrocher();
    }

    @Override
    public int arrimer() {
        return this._original.arrimer();
    }

    @Override
    public void abimer() {
        this._original.abimer();
    }

    @Override
    public boolean estAbime() {
        return this._original.estAbime();
    }

    @Override
    public int reviser() {
        return this._original.reviser();
    }

    @Override
    public int reparer() {
        return this._original.reparer();
    }

    @Override
    public String toString(){
        String [] res = this._original.toString().split(" - ");

        return res[0] + ", " + this.description + " - " + res[1];
    }
}

class OptCadreAlu extends Option{
    public OptCadreAlu(IVelo original) {
        super(original, 0.2, "cadre aluminium");
    }
}

class OptFreinsDisque extends Option{
    public OptFreinsDisque(IVelo original){
        super(original,0.3,"freins à disque");
    }
}

class OptSuspensionAvant extends  Option{
    public OptSuspensionAvant(IVelo original){
        super(original,0.5,"suspension avant");
    }
}

class OptSuspensionArriere extends Option{
    public OptSuspensionArriere(IVelo original){
        super(original,0.5,"suspension arrière");
    }
}

class OptAssistanceElectrique extends Option{
    public OptAssistanceElectrique(IVelo original){
        super(original,2.0,"assistance électrique");
    }
}
