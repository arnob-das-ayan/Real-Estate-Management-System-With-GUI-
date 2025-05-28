import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

class PropertyBase {
    String address, status;
    double price, area;
    ArrayList<String> amenities;

    public PropertyBase(String address, double price, double area, String status, ArrayList<String> amenities) {
        this.address = address;
        this.price = price;
        this.area = area;
        this.status = status;
        this.amenities = amenities;
    }

    public String getDetails() {
        return "Address: " + address + "<br>Price: $" + price + "<br>Area: " + area + " sqft<br>Status: " + status +
                "<br>Amenities: " + String.join(", ", amenities);
    }
}

class ResidentialProperty extends PropertyBase {
    public ResidentialProperty(String address, double price, double area, String status, ArrayList<String> amenities) {
        super(address, price, area, status, amenities);
    }

    @Override
    public String getDetails() {
        return "<b>Residential Property:</b><br>" + super.getDetails();
    }
}

class CommercialProperty extends PropertyBase {
    public CommercialProperty(String address, double price, double area, String status, ArrayList<String> amenities) {
        super(address, price, area, status, amenities);
    }

    @Override
    public String getDetails() {
        return "<b>Commercial Property:</b><br>" + super.getDetails();
    }
}

class Person {
    String name, contactInfo;

    public Person(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public String getDetails() {
        return "Name: " + name + "<br>Contact Info: " + contactInfo;
    }
}

class Tenant extends Person {
    ArrayList<Lease> leases;

    public Tenant(String name, String contactInfo) {
        super(name, contactInfo);
        this.leases = new ArrayList<>();
    }

    public void addLease(Lease lease) {
        leases.add(lease);
    }

    @Override
    public String getDetails() {
        StringBuilder sb = new StringBuilder("<b>Tenant:</b><br>" + super.getDetails());
        for (Lease lease : leases) {
            sb.append("<br><br>").append(lease.getDetails());
        }
        return sb.toString();
    }
}

class Lease {
    Date startDate, endDate;
    double rentAmount, deposit;
    boolean paymentStatus;

    public Lease(Date startDate, Date endDate, double rentAmount, double deposit) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentAmount = rentAmount;
        this.deposit = deposit;
        this.paymentStatus = false;
    }

    public void markPayment() {
        paymentStatus = true;
    }

    public String getDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return "<b>Lease:</b><br>Start: " + sdf.format(startDate) + "<br>End: " + sdf.format(endDate) +
                "<br>Rent: $" + rentAmount + "<br>Deposit: $" + deposit + "<br>Payment: "
                + (paymentStatus ? "Paid" : "Due");
    }
}

public class RealEstateManagementGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Real Estate Management");
            frame.setSize(600, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Inputs
            JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            JTextField addressField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField areaField = new JTextField();
            JTextField statusField = new JTextField();
            JTextField amenitiesField = new JTextField();
            JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Residential", "Commercial" });

            JTextField tenantNameField = new JTextField();
            JTextField contactField = new JTextField();
            JTextField rentField = new JTextField();
            JTextField depositField = new JTextField();

            inputPanel.add(new JLabel("Property Type:"));
            inputPanel.add(typeCombo);
            inputPanel.add(new JLabel("Address:"));
            inputPanel.add(addressField);
            inputPanel.add(new JLabel("Price:"));
            inputPanel.add(priceField);
            inputPanel.add(new JLabel("Area (sqft):"));
            inputPanel.add(areaField);
            inputPanel.add(new JLabel("Status:"));
            inputPanel.add(statusField);
            inputPanel.add(new JLabel("Amenities (comma-separated):"));
            inputPanel.add(amenitiesField);
            inputPanel.add(new JLabel("Tenant Name:"));
            inputPanel.add(tenantNameField);
            inputPanel.add(new JLabel("Tenant Contact:"));
            inputPanel.add(contactField);
            inputPanel.add(new JLabel("Rent Amount:"));
            inputPanel.add(rentField);
            inputPanel.add(new JLabel("Deposit Amount:"));
            inputPanel.add(depositField);

            JButton createButton = new JButton("Create Property & Lease");
            JButton payButton = new JButton("Mark Payment as Paid");

            JLabel outputLabel = new JLabel();
            outputLabel.setVerticalAlignment(SwingConstants.TOP);
            JScrollPane scrollPane = new JScrollPane(outputLabel);
            scrollPane.setPreferredSize(new Dimension(580, 300));

            // Action Listeners
            final Lease[] leaseRef = new Lease[1];
            final Tenant[] tenantRef = new Tenant[1];

            createButton.addActionListener(e -> {
                try {
                    String address = addressField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    double area = Double.parseDouble(areaField.getText());
                    String status = statusField.getText();
                    ArrayList<String> amenities = new ArrayList<>(Arrays.asList(amenitiesField.getText().split(",")));

                    PropertyBase property;
                    if (typeCombo.getSelectedItem().equals("Residential")) {
                        property = new ResidentialProperty(address, price, area, status, amenities);
                    } else {
                        property = new CommercialProperty(address, price, area, status, amenities);
                    }

                    String tenantName = tenantNameField.getText();
                    String contact = contactField.getText();
                    Tenant tenant = new Tenant(tenantName, contact);

                    Calendar cal = Calendar.getInstance();
                    Date startDate = cal.getTime();
                    cal.add(Calendar.YEAR, 1);
                    Date endDate = cal.getTime();
                    double rent = Double.parseDouble(rentField.getText());
                    double deposit = Double.parseDouble(depositField.getText());

                    Lease lease = new Lease(startDate, endDate, rent, deposit);
                    tenant.addLease(lease);

                    tenantRef[0] = tenant;
                    leaseRef[0] = lease;

                    outputLabel
                            .setText("<html>" + property.getDetails() + "<br><br>" + tenant.getDetails() + "</html>");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: Invalid input. Please check your data.");
                }
            });

            payButton.addActionListener(e -> {
                if (leaseRef[0] != null) {
                    leaseRef[0].markPayment();
                    outputLabel.setText("<html>" + tenantRef[0].getDetails() + "</html>");
                    JOptionPane.showMessageDialog(frame, "Payment marked as Paid!");
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(createButton);
            buttonPanel.add(payButton);

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(buttonPanel, BorderLayout.CENTER);
            frame.add(scrollPane, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}