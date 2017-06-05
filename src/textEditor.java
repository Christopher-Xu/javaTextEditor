import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class textEditor extends JFrame{

    private String [] fonts;

    private String currentFile = "untitled";
    //Panel panel = new JPanel();
    private UndoManager undoManager = new UndoManager();
    
    public textEditor(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            e.printStackTrace();
        }    

        setTitle(currentFile);
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        //Creates Components
        
        JTextPane area = new JTextPane();
        
        area.setPreferredSize(new Dimension(500,500));
        
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts = g.getAvailableFontFamilyNames();
        Font Carlito = new Font("FreeSans", Font.PLAIN, 18);
        area.setFont(Carlito);
        

       
        
        
        JScrollPane scrollPane = new JScrollPane(area);
        JMenuBar menuBar = new JMenuBar();
        JFileChooser chooser = new JFileChooser();
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT file", "txt");
        chooser.addChoosableFileFilter(filter);
        

        
        //editing menuBar
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setToolTipText("closes the program");
        JMenuItem open = new JMenuItem("Open");
        open.setToolTipText("opens a text document from hard drive");
        JMenuItem save = new JMenuItem("Save");
        save.setToolTipText("saves file to hard drive");
        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.setToolTipText("saves file to specific part of hard drive");
        
        
            file.add(exit);
            file.add(open);
            file.add(save);
            file.add(saveAs);
        
        JMenu themes = new JMenu("themes");
            JMenuItem light = new JMenuItem("light");
            JMenuItem dark = new JMenuItem("dark");
            themes.add(light);
            themes.add(dark);
        
        JMenu edit = new JMenu("edit");
            JMenuItem redo = new JMenuItem("redo");
        
        
        
        JComboBox<String> fontComboBox = new JComboBox <String> (fonts);
            fontComboBox.setSelectedItem("FreeSans");
        JComboBox <String> fontSize = new JComboBox<String>(new String[]{"2","4","6","8","10","12","14","16","18","20","24","30","40","50"});
            fontSize.setSelectedItem("18");
        
        JCheckBox ital = new JCheckBox("Ital");
        JCheckBox bold = new JCheckBox("Bold");
        
        //adding actionListeners to JMenuItems
        redo.addActionListener((ActionEvent event) -> {
            if(undoManager.canRedo()){
                undoManager.redo();
            }
        });
        
        light.addActionListener((ActionEvent event) -> {
            area.setForeground(Color.black);
            area.setBackground(Color.white);
        });
        dark.addActionListener((ActionEvent event) -> {
            area.setForeground(Color.white);
            area.setBackground(Color.darkGray);
        });
        
        exit.addActionListener((ActionEvent event) -> {
                System.exit(0);
            
        });
        

        
        open.addActionListener((ActionEvent event) -> {
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                String fileName = f.getAbsolutePath();
                currentFile = fileName;
                try{
                    FileReader reader = new FileReader(fileName);
                    BufferedReader br = new BufferedReader(reader);
                    area.read(br,null);
                    setTitle(currentFile);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex);
                }
                        
        });
        
        save.addActionListener((ActionEvent event) -> {
                if(currentFile.equals("untitled")){
                    chooser.showSaveDialog(null);
                    File f = chooser.getSelectedFile();
                    String fileName = f.getAbsolutePath();
                    try{
                        FileWriter writer = new FileWriter(fileName);
                        area.write(writer);
                        writer.close();
                        currentFile = fileName;
                        setTitle(currentFile);
                    }
                    catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
                else{
                    try{
                        FileWriter writer = new FileWriter(currentFile);
                        area.write(writer);
                    }
                    catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            
        });
        
        saveAs.addActionListener((ActionEvent event) -> {
                    chooser.showSaveDialog(null);
                    File f = chooser.getSelectedFile();
                    String fileName = f.getAbsolutePath();
                    try{
                        FileWriter writer = new FileWriter(fileName);
                        area.write(writer);
                        writer.close();
                        currentFile = fileName;
                        setTitle(currentFile);
                    }
                    catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex);
                    }
            
        });
        fontComboBox.addActionListener((ActionEvent event) -> {
                updateUI(area, fontComboBox, fontSize, bold, ital);
            
        });
        fontSize.addActionListener((ActionEvent event) -> {
                updateUI(area, fontComboBox, fontSize, bold, ital);
            
        });
        bold.addActionListener((ActionEvent event) -> {
                updateUI(area, fontComboBox, fontSize, bold, ital);
            
        });
        ital.addActionListener((ActionEvent event) -> {
                updateUI(area, fontComboBox, fontSize, bold, ital);
            
        });
        
        
        //adding items to menuBar
        menuBar.add(file);
        menuBar.add(themes);
        menuBar.add(fontComboBox);
        menuBar.add(fontSize);
        menuBar.add(ital);
        menuBar.add(bold);
        //creates the container upon which we will add the quit button.
        Container pane = getContentPane();
        GridLayout gl = new GridLayout(1,1);//the layout of this pane is a GridLayout with 1 row, 1 columns
        pane.setLayout(gl);
        pane.add(scrollPane);
        //pane.add(fonts);
        setJMenuBar(menuBar);
    }
    private void updateUI(JTextPane area, JComboBox fontComboBox, JComboBox fontSize, JCheckBox bold, JCheckBox ital){
            String fontName = (String)fontComboBox.getSelectedItem();
            int size = Integer.parseInt((String)fontSize.getSelectedItem());

            
            int style;
            if(bold.isSelected() && ital.isSelected()){
                style = Font.BOLD | Font.ITALIC;
            }
            else if(bold.isSelected()){
                style = Font.BOLD;
            }
            else if(ital.isSelected()){
                style = Font.ITALIC;
            }
            else{
                style = Font.PLAIN;
            }
            
            area.setFont(new Font(fontName, style, size));
        }
    public static void main(String [] args){
        textEditor helloWorld = new textEditor();
        helloWorld.pack();
        helloWorld.setVisible(true);
    }
}

 
