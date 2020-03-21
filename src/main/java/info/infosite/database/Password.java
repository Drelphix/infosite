package info.infosite.database;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name="passwords")
public class Password {
    @Id
    @Column (name = "id_pass")
    @NotNull
    private int idPass;

    @Column(name = "device")
    @NotNull
    private String device;

    @Column(name = "model")
    @NotNull
    private String model;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAddress")
    private Address address;
    public Password(){

    }
    public Password (String device, String model, String login, String password, String notes, Address address) {
        this.device = device;
        this.model = model;
        this.login = login;
        this.password = password;
        this.notes = notes;
        this.address = address;
    }

    public int getIdPass() {
        return idPass;
    }

    public void setIdPass(int idPass) {
        this.idPass = idPass;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
