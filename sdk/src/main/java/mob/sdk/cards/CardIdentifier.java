package mob.sdk.cards;

public enum  CardIdentifier {
    PARFOES("parfoes de tovenaar"),    //tovenaar
    KEVIN("kevin de knecht"),      //knecht
    BARRY("barry de barbaar"),      //barbaar
    AVANIUS("koning avanius")     //koning
    ;
    //TODO add more cards
    private String name;


    CardIdentifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
