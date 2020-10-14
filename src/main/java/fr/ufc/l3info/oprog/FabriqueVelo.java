package fr.ufc.l3info.oprog;

public class FabriqueVelo {

    public static FabriqueVelo INSTANCE = null;

    private FabriqueVelo(){}

    public static FabriqueVelo getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FabriqueVelo();
        }
        return INSTANCE;
    }

    public IVelo construire(char t, String... options){
        IVelo velo = new Velo(t);

        boolean [] flags = {false,false,false,false,false};

        if(options == null){
            return velo;
        }

        for(String opt : options){
            if(opt == null){
                continue;
            }
            switch (opt){
                case "CADRE_ALUMINIUM" : {
                    if(!flags[0]){
                        flags[0] = true;
                        velo = new OptCadreAlu(velo);
                    }
                    break;
                }
                case "SUSPENSION_AVANT" : {
                    if(!flags[1]){
                        flags[1] = true;
                        velo = new OptSuspensionAvant(velo);
                    }
                    break;
                }
                case "SUSPENSION_ARRIERE" : {
                    if(!flags[2]){
                        flags[2] = true;
                        velo = new OptSuspensionArriere(velo);
                    }
                    break;
                }
                case "FREINS_DISQUE" : {
                    if(!flags[3]){
                        flags[3] = true;
                        velo = new OptFreinsDisque(velo);
                    }
                    break;
                }
                case "ASSISTANCE_ELECTRIQUE" : {
                    if(!flags[4]){
                        flags[4] = true;
                        velo = new OptAssistanceElectrique(velo);
                    }
                    break;
                }
                default: {}
            }
        }

        return velo;
    }


}
