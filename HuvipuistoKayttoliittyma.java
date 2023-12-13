import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Lisää Kokonaismyynnit-luokka
class Kokonaismyynnit {
    public static int laskuri = 0;
}

public class HuvipuistoKayttoliittyma extends JFrame {
    private static JTextField lapsenNimi;
    private static JTextField huoltajanNumero;
    private static JComboBox<String> alennusRyhmaComboBox;
    private static JButton normaaliButton;
    private static JButton lastenButton;

    public HuvipuistoKayttoliittyma() {
        setTitle("Huvipuiston Rannekesovellus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLayout(new GridLayout(3, 2));

        normaaliButton = new JButton("<html>Normaali<br>Hinta: 22€</html>");
        normaaliButton.setPreferredSize(new Dimension(120, 60));
        normaaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == normaaliButton) {
                    int lippujenMaara = kysyLippujenMaara();
                    normaaliButton.setEnabled(false);
                    int normaaliHinta = 22 * lippujenMaara;
                    float vero1 = 5.28f;
                    Kokonaismyynnit.laskuri += normaaliHinta;
                    Kokonaismyynnit.laskuri += vero1;
                }
            }
        });

        lastenButton = new JButton("<html>Lasten<br>Hinta: 15€</html>");
        lastenButton.setPreferredSize(new Dimension(120, 60));
        lastenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == lastenButton) {
                    int lippujenMaara = kysyLippujenMaara();
                    lastenButton.setEnabled(false);
                    int lastenHinta = 15 * lippujenMaara;
                    float vero = 3.6f;
                    Kokonaismyynnit.laskuri += lastenHinta;
                    Kokonaismyynnit.laskuri += vero;
                }
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
        String alennukset[] = {"Ei alennusta", "Opiskelija", "Eläkeläinen", "Varusmies"};
        alennusRyhmaComboBox = new JComboBox<>(alennukset);
        alennusRyhmaComboBox.setBorder(alennusRyhmaBorder);
        add(alennusRyhmaComboBox);

        JButton tulostaButton = new JButton("Tulosta");
        tulostaButton.setPreferredSize(new Dimension(80, 30));
        add(tulostaButton);
        tulostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kuitti();
                Kokonaismyynnit();

                // Nollaa napit ja tekstikentät
                normaaliButton.setEnabled(true);
                lastenButton.setEnabled(true);
                lapsenNimi.setText("");
                huoltajanNumero.setText("");
                alennusRyhmaComboBox.setSelectedIndex(0);
            }
        });

        JButton ostaButton = new JButton("Osta");
        ostaButton.setPreferredSize(new Dimension(80, 30));
        add(ostaButton);
        ostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kokonaismyynnit();
                Kuitti();
            }
        });

        setVisible(true);
    }

    private int kysyLippujenMaara() {
        try {
            String lippujenMaaraStr = JOptionPane.showInputDialog("Syötä lippujen määrä:");
            if (lippujenMaaraStr != null) {
                return Integer.parseInt(lippujenMaaraStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte. Anna lippujen määrä numeroina.");
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HuvipuistoKayttoliittyma());
    }

    private static void Kuitti() {
        try {
            // Luodaan lipukkeiden tekstitiedosto.
            File kuitti = new File("Huvipuistolipukkeet.txt");

            if (kuitti.createNewFile()) {
                System.out.println("Lippukuitti on luotu: " + kuitti.getName());
                System.out.println("Tiedosto polku: " + kuitti.getAbsolutePath());
            } else {
                System.out.println("Lippu on luotu jo.");
                System.out.println("Tiedosto polku: " + kuitti.getAbsolutePath());
            }

            // Luodaan kirjoittaja, jolla päästään kirjoittamaan tekstitiedostoon tekstiä.
            try (FileWriter kirjoittaja = new FileWriter(kuitti)) {
                // Määritellään tämänhetkinen päivämäärä ja kellon aika.
                LocalDate paivaMaara = LocalDate.now();
                LocalTime aika = LocalTime.now();

                String lapsenNimiteksti = lapsenNimi.getText();
                String huoltajanNumeroteksti = huoltajanNumero.getText();
                String laskuriTeksti = Integer.toString(Kokonaismyynnit.laskuri);

                String tulostus = "Kuitti\n" + paivaMaara + " " + aika + "\nLapsen nimi: " + lapsenNimiteksti
                        + "\nHuoltajan puhelin numero: " + huoltajanNumeroteksti + "\nHinta: " + laskuriTeksti + "\nalv24% sisaltyy hintaan";

                kirjoittaja.write(tulostus);
            }
        } catch (IOException e) {
            System.out.println("Lippukuittia luodessa tapahtui virhe.");
            e.printStackTrace();
        }
    }

    private static void Kokonaismyynnit() {
        try {
            File kokonaismyynnit = new File("Kokonaismyynnit.txt");

            if (kokonaismyynnit.createNewFile()) {
                System.out.println("Kokonaismyyntituloste on luotu: " + kokonaismyynnit.getName());
                System.out.println("Tiedosto polku: " + kokonaismyynnit.getAbsolutePath());
            } else {
                System.out.println("Kokonaismyyntituloste on luotu jo.");
                System.out.println("Tiedosto polku: " + kokonaismyynnit.getAbsolutePath());
            }

            // Luodaan kirjoittaja, jolla päästään kirjoittamaan tekstitiedostoon tekstiä.
            try (FileWriter kirjoittaja = new FileWriter(kokonaismyynnit);
                    BufferedWriter lisaaja = new BufferedWriter(kirjoittaja)) {

                LocalDate paivaMaara = LocalDate.now();
                LocalTime aika = LocalTime.now();
                String laskuriTeksti = Integer.toString(Kokonaismyynnit.laskuri);
                String kokonaisTulostus = "Kokonaismyyntitilanne \n" + paivaMaara + " " + aika + "\n" + laskuriTeksti;

                kirjoittaja.write(kokonaisTulostus);
                lisaaja.write(" euroa");
            }
        } catch (IOException e) {
            System.out.println("Kokonaismyyntitulostetta luodessa tapahtui virhe.");
            e.printStackTrace();
        }
    }

    private static void Paivanmyynnit(){
        try{
            File paivanmyynnit = new File("Paivanmyynnit.txt");

            if(paivanmyynnit.createNewFile()){
                System.out.println("Paivanmyyntituloste on luotu: " + paivanmyynnit.getName());
                System.out.println("Tiedosto polku: " + paivanmyynnit.getAbsolutePath());
            } else {
                System.out.println("Paivanmyyntituloste on luotu jo.");
                System.out.println("Tiedosto polku: " + paivanmyynnit.getAbsolutePath());
            }

            PrintWriter lisaaja = new PrintWriter(new FileWriter(paivanmyynnit, true));
            lisaaja.append("testi");
            lisaaja.close();
            
        } catch (IOException e){
            System.out.println("Paivanmyyntitulostetta luodessa tapahtui virhe.");
            e.printStackTrace();
        }
    }
}
