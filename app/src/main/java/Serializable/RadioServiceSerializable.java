package Serializable;

/**
 * Created by TI on 30/04/2017.
 */

public class RadioServiceSerializable {

    private int id_radioservice;
    private String situ_radioservice;

    public RadioServiceSerializable() {

        this.id_radioservice = 0;
        this.situ_radioservice = "";
    }

    public int getId_radioservice() {
        return id_radioservice;
    }

    public void setId_radioservice(int id_radioservice) {
        this.id_radioservice = id_radioservice;
    }

    public String getSitu_radioservice() {
        return situ_radioservice;
    }

    public void setSitu_radioservice(String situ_radioservice) {
        this.situ_radioservice = situ_radioservice;
    }
}
