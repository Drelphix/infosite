package info.infosite.functions;


import info.infosite.entities.xml.Backup;
import info.infosite.entities.xml.Computer;
import info.infosite.entities.xml.Disk;
import lombok.Getter;
import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class XMLReader {
    Document doc;
    private final Computer computer;
    @PersistenceContext
    private EntityManager entityManager;

    public XMLReader(String path) throws IOException, SAXException, ParserConfigurationException {

        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        //Root node analysis
        this.computer = new Computer();
        computer.setName(GetFirstElement("NameComp"));
        computer.setTime(GetFirstElement("Time"));
        computer.setDate(GetFirstElement("Date"));
        //Disks node analysis
        NodeList xmlDisks = doc.getElementsByTagName("Disks").item(0).getChildNodes();
        List<Disk> disks = new ArrayList<>();
        for (int i = 0; i < xmlDisks.getLength(); i++) {
            Node node = xmlDisks.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Disk disk = new Disk();
                Element elementDisk = (Element) node;
                disk.setLetter(node.getNodeName());
                //Disk name analysis
                NodeList xmlDisk = node.getChildNodes();
                for (int j = 0; j < xmlDisk.getLength(); j++) {
                    Node nodeDisk = xmlDisk.item(j);
                    if (nodeDisk.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementDiskName = (Element) xmlDisk;
                        disk.setName(GetFirstElement("VolumeName", elementDiskName));
                        disk.setTotalSize(CalcSize(GetFirstElement("TotalSize", elementDiskName)));
                        disk.setFreeSpaceGb(Math.round(Double.parseDouble(GetFirstElement("FreeSpace", elementDiskName)) / 1024 / 1024) / 100 * 100);
                        disk.setFreeSpace(CalcSize(GetFirstElement("FreeSpace", elementDiskName)));

                    }
                }
                disks.add(disk);
            }
        }
        computer.setDisks(disks);
        //Backup node analysis
        NodeList xmlBackupList = doc.getElementsByTagName("Backup").item(0).getChildNodes();
        List<Backup> backups = new ArrayList<>();
        for (int i = 0; i < xmlBackupList.getLength(); i++) {
            Node nodeBackup = xmlBackupList.item(i);
            if (nodeBackup.getNodeType() == Node.ELEMENT_NODE) {
                Backup backup = new Backup();
                //Path name analysis
                NodeList xmlPath = nodeBackup.getChildNodes();
                List<info.infosite.entities.xml.File> files = new ArrayList<>();
                for (int j = 0; j < xmlPath.getLength(); j++) {
                    Node nodePath = xmlPath.item(j);
                    if (nodePath.getNodeType() == Node.ELEMENT_NODE) {
                        if (nodePath.getNodeName().equals("itempaths")) {
                            backup.setPath(nodePath.getFirstChild().getTextContent());
                        } else {
                            //Files name analysis
                            NodeList xmlFiles = nodePath.getChildNodes();
                            info.infosite.entities.xml.File file = new info.infosite.entities.xml.File();
                            for (int k = 0; k < xmlFiles.getLength(); k++) {
                                Node nodeFile = xmlFiles.item(k);
                                if (nodeFile.getNodeType() == Node.ELEMENT_NODE) {
                                    if (nodeFile.getNodeName().equals("Name")) {
                                        file.setName(nodeFile.getFirstChild().getTextContent());
                                    } else if (nodeFile.getNodeName().equals("DateLastModified")) {
                                        file.setLastDate(nodeFile.getFirstChild().getTextContent());
                                    } else if (nodeFile.getNodeName().equals("Size")) {
                                        file.setSize(CalcSize(nodeFile.getFirstChild().getTextContent()));
                                    }
                                }
                            }
                            files.add(file);
                        }
                        backup.setFiles(files);
                    }
                }
                backups.add(backup);
            }
        }
        computer.setBackups(backups);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public String GetFirstElement(String param) {
        return doc.getDocumentElement().getElementsByTagName(param).item(0).getTextContent();
    }

    public String GetFirstElement(String param, Element element) {
        return element.getElementsByTagName(param).item(0).getTextContent();
    }

    private String CalcSize(String size) {

        long byteSize = Long.parseLong(size);
        double sizeKb = byteSize / 1024;
        double sizeMb = sizeKb / 1024;
        double sizeGb = sizeMb / 1024;
        if (sizeKb <= 1024) {
            size = sizeKb + " кб";
        } else if (sizeKb >= 1024 && sizeMb <= 1024) {
            size = Math.round(sizeMb * 100) / 100 + " мб";
        } else if (sizeMb >= 1024) {
            size = Double.valueOf(Math.round(sizeGb * 100)) / 100 + " гб";
        }
        return size;
    }
}


