import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Map;
import java.awt.*;
import javax.swing.table.*;


public class BikeShopMainGUI{

    private JTextField searchField;
    private JComboBox<String> columnSelect;
    private JComboBox<String> tableSelect;
    private JTable dataTable;
    private JScrollPane scroll;
    private String username;
    private String password;
    private JFrame frame;
    private GridBagConstraints gbc;
   
    public BikeShopMainGUI(String username, String password) {
        frame = new JFrame("Bike Shop Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1090, 600);
        frame.setLocationRelativeTo(null);
        
        this.username = username;
        this.password = password;
        frame.setLayout(new GridBagLayout());
        initializeUI();
        frame.setVisible(true);
    }
 
    private void initializeUI(){

        String[] tables = {"bike_model","bike_shipment","bike_type","customer","manufacturer","purchase"};
       
        gbc = new GridBagConstraints();
        tableSelect = new JComboBox<String>(tables);
        columnSelect = new JComboBox<String>(BikeShopSQL.getColumns("purchase", username, password));
        searchField = new JTextField();
        JButton searchB = new JButton("Search");
        
        dataTable = new JTable(BikeShopSQL.tableLookup("customer", "","", username, password));
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = dataTable.getColumnModel();
        dataTable.setColumnSelectionAllowed(false);
        for(int i = 0; i < dataTable.getColumnCount(); i++){
            int estimate = ((String)dataTable.getModel().getValueAt(1, i)).length();
    
            if (estimate < 15){
                columnModel.getColumn(i).setPreferredWidth(150);
            }
            else{
                columnModel.getColumn(i).setPreferredWidth(300);
            }
        
        }
        System.out.println(columnSelect);
        scroll = new JScrollPane(dataTable);
        scroll.setVisible(true);
        // scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JLabel resultsLabel = new JLabel("Results:");
        JLabel searchLabel = new JLabel("Enter Query:");
        JLabel columnLabel = new JLabel("Select Table:");
        JLabel tableLabel = new JLabel("Select Column:");
        JButton addB = new JButton("Add Data");
        JButton removeB = new JButton("Remove Data");
        JButton updateB = new JButton("Update Data");
        JButton refreshB = new JButton("Refresh Data");
        
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 0;
        frame.add(tableLabel, gbc);
        gbc.gridx = 1;  
        gbc.gridy = 0;
        frame.add(columnLabel, gbc);
        gbc.gridx = 2;  
        gbc.gridy = 0;
        frame.add(searchLabel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        frame.add(tableSelect, gbc);
        gbc.gridx = 1;  
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        frame.add(columnSelect, gbc);
        gbc.gridx = 2;  
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        frame.add(searchField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 3;  
        gbc.gridy = 1;
        frame.add(searchB, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        gbc.gridx = 0;  
        gbc.gridy = 2;
        frame.add(resultsLabel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        frame.add(scroll, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.gridx = 0;  
        gbc.gridy = 16;
        frame.add(updateB, gbc);
        gbc.gridx = 1;  
        gbc.gridy = 16;
        frame.add(addB, gbc);
        gbc.gridx = 2;  
        gbc.gridy = 16;
        frame.add(removeB, gbc);
        gbc.gridx = 3;  
        gbc.gridy = 16;
        
        frame.add(refreshB, gbc);
        gbc.gridx = 5;
        gbc.gridy = 16;
        

      

        searchB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        tableSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        refreshB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        addB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        removeB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });

      
        updateB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void insertTable(){
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = dataTable.getColumnModel();
        for(int i = 0; i < dataTable.getColumnCount(); i++){
            if(dataTable.getRowCount() <= 0){
                columnModel.getColumn(i).setPreferredWidth(150);
            }
            else{
                int estimate = ((String)dataTable.getModel().getValueAt(0, i)).length();
                if (estimate < 15){
                    columnModel.getColumn(i).setPreferredWidth(150);
                }
                else{
                    columnModel.getColumn(i).setPreferredWidth(300);
                }
            }
        }
        scroll = new JScrollPane(dataTable);
        scroll.setVisible(true);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        frame.add(scroll, gbc);
    }
   
    public void refresh(){
            String table = tableSelect.getSelectedItem().toString();
           
            frame.remove(this.scroll);
            frame.remove(this.columnSelect);
            columnSelect = new JComboBox<String>(BikeShopSQL.getColumns(table, username, password));
            columnSelect.setBounds(180,100,120,40);
            dataTable = new JTable(BikeShopSQL.tableLookup(table, "", "", username, password));
            gbc.gridx = 1;  
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            frame.add(columnSelect, gbc);
            insertTable();
            frame.revalidate();
            frame.repaint();
    }

    public void search(){
        String keyword = searchField.getText();
        String column = columnSelect.getSelectedItem().toString();
        String table = tableSelect.getSelectedItem().toString();
    
        try{
            frame.remove(this.scroll);
            dataTable = new JTable(BikeShopSQL.tableLookup(table, column, keyword, username, password));
            insertTable();
            frame.revalidate();
            frame.repaint();
        }
        catch(Exception e){
            searchField.setText("Error searching for items: " + e.getMessage());
            refresh();
        }
    }

    public void add(){
        
        JFrame addFrame = new JFrame("Insert Into Database");
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.setSize(400, 650);
        addFrame.setLocationRelativeTo(null);
        addFrame.setLayout(new GridBagLayout());
        addFrame.setVisible(true);
    }

    public void remove(){
        String table = tableSelect.getSelectedItem().toString();
        int[] rows = dataTable.getSelectedRows();
        String column = columnSelect.getItemAt(0);
        for(int row: rows){
            String value = (String) dataTable.getValueAt(row, 0);
            BikeShopSQL.remove(table, column, value, username, password);
        }
        refresh();
        
    }

    //Create JFrame
    public void service(String patientID) {
    JFrame serviceFrame = new JFrame("Select a Telehealth service");
    serviceFrame.setSize(400, 400);
    serviceFrame.setLocationRelativeTo(null);
    serviceFrame.setLayout(new GridBagLayout());

    GridBagConstraints gbcservice = new GridBagConstraints();
    gbcservice.fill = GridBagConstraints.HORIZONTAL;
    gbcservice.insets = new Insets(10, 10, 10, 10);
    
    //Display the patient information
    JLabel information = new JLabel("Patient information:");
    gbcservice.gridx = 0;
    gbcservice.gridy = 0;
    gbcservice.gridwidth = 2;
    serviceFrame.add(information, gbcservice);

    try {
        //Retrieve the patient information
        Map<String, String> data = BikeShopSQL.getPatientColumn(patientID, username,password); 
        if (data != null) {
            int row = 1;
            //Map to iterate through key, value pairs
           for (Map.Entry<String, String> entry : data.entrySet()) {
                gbcservice.gridwidth = 1; 
                gbcservice.gridx = 0;
                gbcservice.gridy = row;
                serviceFrame.add(new JLabel(entry.getKey() + ":"), gbcservice);
                gbcservice.gridx = 1;
                serviceFrame.add(new JLabel(entry.getValue()), gbcservice);
                row++;
            }

        } else {
            gbcservice.gridwidth = 2;
            gbcservice.gridx = 0;
            gbcservice.gridy = 1;
            serviceFrame.add(new JLabel("No patient found with ID: " + patientID), gbcservice);
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        gbcservice.gridwidth = 2;
        gbcservice.gridx = 0;
        gbcservice.gridy = 1;
        serviceFrame.add(new JLabel("Error fetching patient data"), gbcservice);
    }
    //Button to assign a Telehealth Service
    JButton addAppointmentButton = new JButton("Add Appointment");
    addAppointmentButton.addActionListener(e -> {
        
    });
    
    gbcservice.gridwidth = 2;
    gbcservice.gridx = 0;
    gbcservice.gridy++;
    serviceFrame.add(addAppointmentButton, gbcservice);
    serviceFrame.setVisible(true);
}}

