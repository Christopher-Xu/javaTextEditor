import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import javax.swing.AbstractAction.*;
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
        /*
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            e.printStackTrace();
        }    
        */
        setTitle(currentFile);
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Color for future reference
        
        Color darkOrange = new Color (214,170,84);
        
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
        
        //setting up for redo and undo        
        UndoManager undoManager = new UndoManager();

        
        area.getDocument().addUndoableEditListener((UndoableEditEvent e) ->{
            

                System.out.println("Add edit");
                undoManager.addEdit(e.getEdit());

            });
        
        //creating menus and menuItems.
        JMenu file = new JMenu("File");
            JMenuItem quit = new JMenuItem("Quit");
                KeyStroke ctrlQ = KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK);
                quit.setAccelerator(ctrlQ);
                quit.setToolTipText("closes the program (ctrl+q)");
        
            JMenuItem open = new JMenuItem("Open");
            open.setToolTipText("opens a text document from hard drive");
                KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK);
                open.setAccelerator(ctrlO);
                open.setToolTipText("opens a file from file system (ctrl+o)");
        
            JMenuItem save = new JMenuItem("Save");
                KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK);
                save.setAccelerator(ctrlS);
                save.setToolTipText("saves current file (ctrl+s)");
        
            JMenuItem saveAs = new JMenuItem("Save As");
                KeyStroke ctrlShiftS = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK);
                saveAs.setAccelerator(ctrlShiftS);
                saveAs.setToolTipText("saves file to specific part of hard drive (ctrl+shift+s");
        
            file.add(quit);
            file.add(open);
            file.add(save);
            file.add(saveAs);
        
        JMenu themes = new JMenu("themes");
            JMenuItem light = new JMenuItem("light");
                light.setToolTipText("enables light theme");
            JMenuItem dark = new JMenuItem("dark");
                dark.setToolTipText("enables dark theme");
            themes.add(light);
            themes.add(dark);
        
        JMenu edit = new JMenu("edit");
            JMenuItem redo = new JMenuItem("redo");
                KeyStroke ctrlShiftZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK);
                redo.setAccelerator(ctrlShiftZ);
                redo.setToolTipText("Redoes last undo (Ctrl+Shift+Z)");
            JMenuItem undo = new JMenuItem("undo");
                KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK);
                undo.setAccelerator(ctrlZ);
                undo.setToolTipText("Undoes last edit (Ctrl+Z)");
            edit.add(redo);
            edit.add(undo);
        //creating other components for the menubar        
        
        JComboBox<String> fontComboBox = new JComboBox <String> (fonts);
            fontComboBox.setSelectedItem("FreeSans");
        JComboBox <String> fontSize = new JComboBox<String>(new String[]{"2","4","6","8","10","12","14","16","18","20","24","30","40","50"});
            fontSize.setSelectedItem("18");
        
        JCheckBox ital = new JCheckBox("Ital");
        JCheckBox bold = new JCheckBox("Bold");
        
        
        //adding actionListeners to everything.
        
        undo.addActionListener((ActionEvent event) -> {
            try{
                if(undoManager.canUndo()){
                    undoManager.undo();
                }
            }
            catch(CannotUndoException exp){
                exp.printStackTrace();
            }
        });
        
        redo.addActionListener((ActionEvent event) -> {
            try{
                if(undoManager.canRedo()){
                    undoManager.redo();
                }
            }
            catch(CannotRedoException exp){
                exp.printStackTrace();
            }
        });
        
        light.addActionListener((ActionEvent event) -> {
            area.setForeground(Color.black);
            area.setBackground(Color.white);
            area.setCaretColor(Color.black);
           
            menuBar.setBackground(Color.white);
            ital.setBackground(Color.white);
            bold.setBackground(Color.white);
            fontSize.setBackground(Color.white);
            fontComboBox.setBackground(Color.white);
        });
        dark.addActionListener((ActionEvent event) -> {
            area.setForeground(Color.white);
            area.setBackground(Color.darkGray);
            area.setCaretColor(Color.white);
            
            menuBar.setBackground(darkOrange);
            ital.setBackground(darkOrange);
            bold.setBackground(darkOrange);
            fontSize.setBackground(darkOrange);
            fontComboBox.setBackground(darkOrange);
        });
        
        quit.addActionListener((ActionEvent event) -> {
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
        menuBar.add(edit);
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

 
