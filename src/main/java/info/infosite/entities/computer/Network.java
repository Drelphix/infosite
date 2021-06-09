package info.infosite.entities.computer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Entity
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

    @Column
    private String ipAddress;

    @Column
    private String macAddress;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;


    public List<String> getIpAddresses() {
        List<String> nothing = new ArrayList<>();
        nothing.add("Ip-адреса отсутствуют");
        try {
            return Arrays.asList(ipAddress.split(","));
        } catch (NullPointerException e) {
            return nothing;

        }
    }

    public String getIpFrom10Network() {
        List<String> ips = getIpAddresses();
        for (String ip : ips) {
            if (ip.startsWith("192.168.10.")) {
                return ip;
            }
        }
        return ips.get(0);
    }

}
