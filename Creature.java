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
        mhp=100*Math.pow(level,1.1);
        atk=30*Math.pow(level,1.1);
        def=30*Math.pow(level,1.1);
    }
}