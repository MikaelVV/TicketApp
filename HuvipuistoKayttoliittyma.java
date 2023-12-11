import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HuvipuistoKayttoliittyma extends JFrame {
    private static JTextField lapsenNimi;
    private static JTextField huoltajanNumero;
    private static JComboBox<String> alennusRyhmaComboBox;

    public HuvipuistoKayttoliittyma() {
        setTitle("Huvipuiston Rannekesovellus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLayout(new GridLayout(3, 2));

        JButton normaaliButton = new JButton("<html>Normaali<br>Hinta: 22€</html>");
        normaaliButton.setPreferredSize(new Dimension(120, 60));
        normaaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                int normaaliHinta = 22;
            }
        });

        JButton lastenButton = new JButton("<html>Lasten<br>Hinta: 15€</html>");
        lastenButton.setPreferredSize(new Dimension(120, 60));
        lastenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                int lastenHinta = 15;
            }
        });

        JButton alennusButton = new JButton("<html>Alennus<br>Hinta: 20€</html>");
        alennusButton.setPreferredSize(new Dimension(120, 60));

        add(normaaliButton);
        add(lastenButton);
        add(alennusButton);

        lapsenNimi = new JTextField();
        lapsenNimi.setPreferredSize(new Dimension(100, 30));
        lapsenNimi.setBorder(BorderFactory.createTitledBorder("Lapsen etunimi"));

        huoltajanNumero = new JTextField();
        huoltajanNumero.setPreferredSize(new Dimension(150, 30));
        huoltajanNumero.setBorder(BorderFactory.createTitledBorder("Huoltajan puhelinnumero:"));

        add(lapsenNimi);
        add(huoltajanNumero);

        TitledBorder alennusRyhmaBorder = BorderFactory.createTitledBorder("Valitse alennusryhmä");
        alennusRyhmaComboBox = new JComboBox<>(new String[] { "Opiskelija", "Eläkeläinen", "Varusmies" });
        alennusRyhmaComboBox.setBorder(alennusRyhmaBorder);
        add(alennusRyhmaComboBox);

        JButton tulostaButton = new JButton("Tulosta");
        tulostaButton.setPreferredSize(new Dimension(80, 30));
        add(tulostaButton);
        tulostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Kuitti();
            }
        });


        JButton ostaButton = new JButton("Osta");
        ostaButton.setPreferredSize(new Dimension(80, 30));
        add(ostaButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HuvipuistoKayttoliittyma());
    }

    private static void Kuitti(){
        try{
            // Luodaan lipukkeiden tekstitiedosto.
            File myObj = new File("Huvipuistolipukkeet.txt");
            myObj.createNewFile();

            //Luodaan kirjoittaja, jolla päästään kirjoittamaan tekstitiedostoon tekstiä.
            FileWriter kirjoittaja = new FileWriter("Huvipuistolipukkeet.txt");

            // Määritellään tämänhetkinen päivämäärä ja kellon aika.
            LocalDate paivaMaara = LocalDate.now();
            LocalTime aika = LocalTime.now();

            String lapsenNimiteksti = lapsenNimi.getText();
            String huoltajanNumeroteksti = huoltajanNumero.getText();

            String tulostus = "Kuitti\n" + paivaMaara + " "+ aika + "\nLapsen nimi: " + lapsenNimiteksti + "\nHuoltajan puhelin numero: "+ huoltajanNumeroteksti;

            kirjoittaja.write(tulostus);
            kirjoittaja.close();

            if(myObj.createNewFile()){
                System.out.println("Lippukuitti on luotu: " + myObj.getName());
                System.out.println("Tiedosto polku: " + myObj.getAbsolutePath());
            }else{
                System.out.println("Lippu on luotu jo.");
                System.out.println("Tiedosto polku: " + myObj.getAbsolutePath());
            }
        } catch (IOException e){
            System.out.println("Lippukuittia luodessa tapahtui virhe.");
            e.printStackTrace();
        }
    }
}
