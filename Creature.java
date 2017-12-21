public class Creature
{
    public double mhp;
    public double hp;
    public double atk;
    public double def;
    public int level;
    public Creature(int l) {
        level=l;
        calcStats();
        hp=mhp;
    }
    
    public void calcStats() {
        mhp=10*Math.pow(level,1.1);
        atk=10*Math.pow(level,1.1);
        def=10*Math.pow(level,1.1);
    }
    
    public String toString() {
        return "Level "+level+" Hp: "+hp+"/"+mhp+"( "+((hp/mhp)*100)+"% )";
    }
}