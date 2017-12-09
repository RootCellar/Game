 
import java.util.ArrayList;
public class Class
{
    private String name;
    private double bhp;
    private double bmana;
    private double batk;
    private double bdef;
    private double bmatk;
    private boolean avail;
    private ArrayList<Ability> abilities;
    public Class(String n,double hp, double mana, double atk, double def, double matk, boolean a) {
        name=n;
        bhp=hp;
        bmana=mana;
        batk=atk;
        bdef=def;
        bmatk=matk;
        avail=a;
        abilities=new ArrayList<Ability>();
    }
    
    public boolean isAvail() {
        return avail;
    }
    
    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
    
    public void remAbility(String n) {
        for(int i=0; i<abilities.size(); i++) {
            if(abilities.get(i).getName().equals(n)) {
                abilities.remove(i);
            }
        }
    }
    
    public void addAbility(Ability a) {
        abilities.add(a);
    }
    
    public String toString() {
        String temp="";
        for(int i=0; i<abilities.size(); i++) {
            temp+=abilities.get(i).toString();
        }
        return "Name: "+name+"\n"+
               "Base HP: "+bhp+"\n"+
               "Base Mana: "+bmana+"\n"+
               "Base Attack: "+batk+"\n"+
               "Base Defense: "+bdef+"\n"+
               "Base Magic Attack: "+bmatk+"\n"+
               "Abilities: \n"+temp;
    }
    
    public String getName() {
        return name;
    }
    
    public double getBHp() {
        return bhp;
    }
    
    public double getBMana() {
        return bmana;
    }
    
    public double getBAtk() {
        return batk;
    }
    
    public double getBDef() {
        return bdef;
    }
    
    public double getBMatk() {
        return bmatk;
    }
}