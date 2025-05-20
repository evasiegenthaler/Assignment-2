package a1.example.a2;

import javafx.scene.control.Slider;
import model.NormalPet;
import model.Pet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PetManager {
    //private static final List<Pet> petList = new ArrayList<>();

    private static LinkedHashMap<Integer, Pet> petMap = new LinkedHashMap<>();


    public static boolean isDuplicate(Pet currentPet, String name, int happiness, int hunger, int energy) {
        for (Pet pet : petMap.values()) {
            if (pet != currentPet && // Damit das Pet nicht mit sich selbst verglichen wird wenn es durch den loop geht
                    pet.getPetName().equalsIgnoreCase(name) &&
                    pet.getHappiness() == happiness &&
                    pet.getHunger() == hunger &&
                    pet.getEnergy() == energy) {
                return true;
            }
        }
        return false;
    }
    public static boolean addPet (Pet pet) { //Das Pet von z.b savePet wird hier übergeben
        if (isDuplicate(null, pet.getPetName(), pet.getHappiness(), pet.getHunger(), pet.getEnergy())) {
            return false; // Duplikat gefunden – nicht hinzufügen
        }
        // In die HashMap speichern
        petMap.put(pet.getId(), pet);  // Pet wird mit seiner ID als Key gespeichert, Pet object= value
        return true; // Erfolgreich hinzugefügt
    }

    public static void removePetById (Pet pet) {
        petMap.remove(pet.getId()); // Entfernt Pet aus der Liste anhand seiner ID (key)
    }

    public static Collection<Pet> getPetList() {
        return petMap.values();
    }

    //Helper methode hier in Pet Manager um organisiert zu bleiben ich rufe die Methode durch Frame4 auf da dieser Controller mit fxml verknüft ist
    public static void exportPets(String filePath) {
        Workbook workbook = new XSSFWorkbook(); //XSSFWorkbook bedeutet: "OOXML", --> zusammenhang Apache POI, erstellt neue excel Datei
        Sheet sheet = workbook.createSheet("Pets"); // Erstellt excel tabellenblatt mit name Pets

        // ---- HEADER STYLE (violett mit weißer fetter Schrift) ----
        CellStyle headerStyle = workbook.createCellStyle(); //Methode um neuen stil im workbock zu erstellen. Sie gibt Objekt vom typ CellStyle zurück -->assigne objekt in die variable namens headerstyle der Klasse CellStyle=aus POI
        headerStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex()); // wählt Farbe=violetter Hintergrund. setFillForegroundColor() erwartet eine zahl desshalb .getIndex() vom enum VIOLET,
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // füllt die Zelle voll

        Font headerFont = workbook.createFont(); //Erstellt Objekt vom typ Font
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // weißer Text
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12); // setFontHeightInPoints() erwartet datentyp short= kleiner als int 16 BIT, 12= default ist int also cast zu short
        headerStyle.setFont(headerFont); //Verknüft CellStyle=headerstyle mit FontStyle=headerFont

        // ---- DATEN STYLE (hellblau) ----
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex()); // wählt blaue Farbe
        dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // setzt blaue Farbe

        Font dataFont = workbook.createFont();
        dataFont.setColor(IndexedColors.BLACK.getIndex()); // schwarzer Text
        dataStyle.setFont(dataFont); //Verknüft Cellstyle=dataStyle mit FontStyle=dataFont

        // ---- KOPFZEILE ----
        Row header = sheet.createRow(0); //Kreirt die erste Zeile 0 und speichert diese in der variable header
        String[] columns = {"#", "Name", "Happiness", "Hunger", "Energy", "ID"}; // Array vom typ String []= weil Array fixe werte

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i); //Kreiert neue Zelle in der ersten Excel-Zeile 0, header. --> weil ich die Zelle in einer bestimmten Zeile=header erstellen will
            cell.setCellValue(columns[i]); //Setz den wert rein in die Zelle also z.b Name=spalte 1, Happiness Spalte 2
            cell.setCellStyle(headerStyle); // setz für all diese Zellen einen bestimmten stil nähmlich headerStyle
        }

        // ---- DATEN ----
        int rowNum = 1; // Start bei 1 weil Zeile 0= header Zeile
        for (Pet pet : petMap.values()) {
            Row row = sheet.createRow(rowNum++); //Erstellt neue Zeile für das aktuelle Pet und zählt dan hoch
            row.createCell(0).setCellValue(PetManager.getPosition(pet)); //nimmt pet aus der schleife=pet gettet die Position dessen-->kreiert Zelle und setzt wert dort rein
            row.createCell(1).setCellValue(pet.getPetName());
            row.createCell(2).setCellValue(pet.getHappiness());
            row.createCell(3).setCellValue(pet.getHunger());
            row.createCell(4).setCellValue(pet.getEnergy());
            row.createCell(5).setCellValue(pet.getId());

            // Alle Zellen in diesem Row stylen:
            for (int i = 0; i <= 5; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // ---- SPALTENBREITE ANPASSEN ----
        for (int i = 0; i < columns.length; i++) { //colums=Array der mir angibt wieviel Spalten ich habe < und nicht <= weil Array bei o beginnt <= wäre 1 zu viel
            sheet.autoSizeColumn(i);
        }

        // ---- SPEICHERN ----
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) { //filePath=übergebener Speicherort den user ausgewählt hat. Ohne filePath wüsste Java nicht wohin es schreiben soll
            //FilOutputStream= Java Klasse: Öffnet Datei zum schreiben von Daten wie eine Tür durch die hindurch ich dam mein workbook schreiben kann
            workbook.write(fileOut); //Schreibt die excel datei dorthin wo user ausgewählt hat
            workbook.close(); //schliesst excel
        } catch (IOException e) { //Order existiert nicht oder keine schreibrechte
            e.printStackTrace();
        }
    }

    public static void importPets(String filePath) {
        try (FileInputStream fileIn = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0); // Erstes Tabellenblatt

            // 1️⃣ Alle bisherigen Pets löschen
            petMap.clear();

            // ID auf 1 setzen ruft Hilfsmethode aus Pet Klasse auf da nextID private ist
            //Pet.resetIdCounter();

            // 2️⃣ Header-Zeile einlesen (Zeile 0)
            Row header = sheet.getRow(0); //Holt die erste Zeile aus dem Excel-Blatt (Name, Happiness, Hunger, Energy, Position, Index)
            HashMap<String, Integer> columnMap = new HashMap<>(); // Erstellt HashMap, die als Key=String (Name, Happiness, Hunger....) speichert und als value die enstprechende Spalte (0,1,2,3 ...)

            for (Cell cell : header) { //Geht durch jede Zelle in der header Zeile
                columnMap.put(cell.getStringCellValue(), cell.getColumnIndex()); //Holt den String und Holt die Spalte und speichert das in der HasMap
            }

            // Prüfen ob alle benötigten Spalten vorhanden sind
            if (!columnMap.containsKey("Name") ||
                    !columnMap.containsKey("Happiness") ||
                    !columnMap.containsKey("Hunger") ||
                    !columnMap.containsKey("Energy")) {
                throw new IllegalArgumentException("Excel-Format ungültig: Erwarte Spalten 'Name', 'Happiness', 'Hunger', 'Energy'");
            }

            // 3️⃣ Zeile für Zeile einlesen (ab Zeile 1, weil Zeile 0 Header ist)
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) { // 1 weil ich Kopfzeile mit den Titeln überspringe
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    // Die erste Spalte (ID) ignorieren → du vergibst eigene IDs
                    String name = row.getCell(0).getStringCellValue(); //A
                    int happiness = (int) row.getCell(1).getNumericCellValue(); // B
                    int hunger = (int) row.getCell(2).getNumericCellValue(); // C
                    int energy = (int) row.getCell(3).getNumericCellValue(); // D

                    // Neues Pet → ID wird automatisch vergeben in Pet-Konstruktor
                    Pet pet = new NormalPet(name, happiness, hunger, energy); //Upcast zu Pet
                    PetManager.addPet(pet);
                }
            }

            workbook.close();
            System.out.println("Import erfolgreich von: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getPosition(Pet currentPet) { //Für die Anzeige auf der Pet Karte --> setPet(), static dass man sie über die Klasse aufrufen kann
        int pos = 1;
        for (Pet p : petMap.values()) { //p= steht für einzelnes pet aus meiner Liste. Geht durch die Liste bis das pet aus der Liste das gleiche ist wie currentPet
            if (p.equals(currentPet)) { //Vergleicht das pet p aus dem loop mit dem currentPet das in die Methode gegeben wird
                return pos;
            }
            pos++; //Damit es raufzählt damit das zweite dann auch mit 2 beginnt
        }
        return -1; // Falls nicht gefunden gibt methode -1 zurück. Dieser Wert könnte auch -1000 sein. Ich muss einfach einen wert zurückgeben sonst müsste methode void sein
    }
}


