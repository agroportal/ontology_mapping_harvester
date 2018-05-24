package fr.lirmm.agroportal.ontologymappingharvester.entities;

public class TargetReference {

    private String target;
    private String atAgroportal;
    private String atBioportal;
    private String atExternal;
    private String linkVerified;
    private String date;

    public TargetReference(String target, String atAgroportal, String atBioportal, String atExternal, String linkVerified, String date) {
        this.target = target;
        this.atAgroportal = atAgroportal;
        this.atBioportal = atBioportal;
        this.atExternal = atExternal;
        this.linkVerified = linkVerified;
        this.date = date;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAtAgroportal() {
        return atAgroportal;
    }

    public void setAtAgroportal(String atAgroportal) {
        this.atAgroportal = atAgroportal;
    }

    public String getAtBioportal() {
        return atBioportal;
    }

    public void setAtBioportal(String atBioportal) {
        this.atBioportal = atBioportal;
    }

    public String getAtExternal() {
        return atExternal;
    }

    public void setAtExternal(String atExternal) {
        this.atExternal = atExternal;
    }

    public String getLinkVerified() {
        return linkVerified;
    }

    public void setLinkVerified(String linkVerified) {
        this.linkVerified = linkVerified;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TargetReference{" +
                "target='" + target + '\'' +
                ", atAgroportal='" + atAgroportal + '\'' +
                ", atBioportal='" + atBioportal + '\'' +
                ", atExternal='" + atExternal + '\'' +
                ", linkVerified='" + linkVerified + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
