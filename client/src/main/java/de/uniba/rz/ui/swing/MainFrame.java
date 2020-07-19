package de.uniba.rz.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;

public class MainFrame extends JFrame implements Observer{
	private static final long serialVersionUID = -6236283098582578310L;
	private SwingMainController controller;
	private SwingMainModel mainModel;
	private int currentId;
	private TicketTableModel tableModel;
	private JPanel ticketPanel;
	private JTextArea descArea;
	private JComboBox<de.uniba.rz.entities.Type> typeBox;
	private JComboBox<Priority> prioBox;
	private JTextField repField;
	private JTextField topicField;
	private JLabel numberLabel;
	private JButton acceptButton;
	private JButton rejectButton;
	private JButton closeButton;
	private JButton saveButton;
	private JLabel statusLabel;

	public MainFrame(SwingMainController controller, SwingMainModel mainModel) {
		this.controller = controller;
		this.mainModel = mainModel;
		mainModel.addObserver(this);
	}

	public void showUI() {
		try {
			init();
			this.setVisible(true);
		} catch (TicketException e) {
			showErrorDialog(
			      "Unable to create UI as there were problems connecting to the backend! Shutting down system...", e
			);
			controller.triggerApplicationShutdown();
		}
	}

	private void init() throws TicketException {
		this.setTitle("Ticket Management System 5000");
		this.setResizable(false);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		JPanel newRefreshPanel = new JPanel();
		JButton newButton = new JButton("Create new Ticket");
		newButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				prepareCreationOfTicket();
			}
		});
		// newButton.setSize(580, 35);
		newRefreshPanel.add(newButton);
		JButton refreshButton = new JButton("Refresh List");
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.refreshTicketList();
			}
		});
		newRefreshPanel.add(refreshButton);
		JPanel searchAndTablePanel = new JPanel();
		searchAndTablePanel.setLayout(new BorderLayout());
		searchAndTablePanel.add(createSearchPanel(), BorderLayout.NORTH);
		searchAndTablePanel.add(createTicketTablePanel(), BorderLayout.CENTER);
		panel.add(searchAndTablePanel, BorderLayout.NORTH);
		panel.add(newRefreshPanel, BorderLayout.CENTER);
		createTicketPanel();
		panel.add(ticketPanel, BorderLayout.SOUTH);
		this.addWindowListener(new WindowListener(){
			@Override
			public void windowOpened(WindowEvent e) {
				// do nothing
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// do nothing
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// do nothing
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// do nothing
			}

			@Override
			public void windowClosing(WindowEvent e) {
				controller.triggerApplicationShutdown();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
		});
		pack();
	}

	private void showSaveButton() {
		saveButton.setVisible(true);
		rejectButton.setVisible(false);
		acceptButton.setVisible(false);
		closeButton.setVisible(false);
	}

	private void createTicketPanel() {
		ticketPanel = new JPanel();
		ticketPanel.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		numberLabel = new JLabel("#X");
		ticketPanel.add(numberLabel, c1);
		topicField = new JTextField();
		topicField.setColumns(50);
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 1;
		c2.gridy = 0;
		c2.gridwidth = 5;
		ticketPanel.add(topicField, c2);
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 0;
		c3.gridy = 1;
		ticketPanel.add(new JLabel("Reporter:"), c3);
		repField = new JTextField();
		repField.setColumns(15);
		c3 = new GridBagConstraints();
		c3.gridx = 1;
		c3.gridy = 1;
		ticketPanel.add(repField, c3);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		ticketPanel.add(new JLabel("Priority:"), c);
		prioBox = new JComboBox<>(Priority.values());
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		ticketPanel.add(prioBox, c);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 1;
		ticketPanel.add(new JLabel("Type:"), c);
		typeBox = new JComboBox<>(de.uniba.rz.entities.Type.values());
		typeBox.setEnabled(false);
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 1;
		ticketPanel.add(typeBox, c);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		ticketPanel.add(new JLabel("Description:"), c);
		descArea = new JTextArea();
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 5;
		descArea.setColumns(50);
		descArea.setRows(5);
		ticketPanel.add(new JScrollPane(descArea), c);
		statusLabel = new JLabel("Update Status:");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		ticketPanel.add(statusLabel, c);
		acceptButton = new JButton("Accept");
		acceptButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.acceptTicket(currentId);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		ticketPanel.add(acceptButton, c);
		rejectButton = new JButton("Reject");
		rejectButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.rejectTicket(currentId);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;
		ticketPanel.add(rejectButton, c);
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.closeTicket(currentId);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 3;
		ticketPanel.add(closeButton, c);
		saveButton = new JButton("Save New Ticket");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.createNewTicket(
				      repField.getText(), topicField.getText(), descArea.getText(),
				      (de.uniba.rz.entities.Type) typeBox.getSelectedItem(), (Priority) prioBox.getSelectedItem()
				);
			}
		});
		saveButton.setVisible(false);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 5;
		c.gridwidth = 2;
		ticketPanel.add(saveButton, c);
		topicField.setEditable(false);
		repField.setEditable(false);
		prioBox.setEnabled(false);
		typeBox.setEnabled(false);
		descArea.setEditable(false);
		acceptButton.setEnabled(false);
		rejectButton.setEnabled(false);
		closeButton.setVisible(false);
		saveButton.setVisible(false);
	}

	public void showTicketDetails(Ticket ticket) {
		currentId = ticket.getId();
		numberLabel.setText("#" + ticket.getId());
		topicField.setText(ticket.getTopic());
		topicField.setEditable(false);
		repField.setText(ticket.getReporter());
		repField.setEditable(false);
		prioBox.setSelectedItem(ticket.getPriority());
		prioBox.setEnabled(false);
		typeBox.setSelectedItem(ticket.getType());
		typeBox.setEnabled(false);
		descArea.setText(ticket.getDescription());
		descArea.setEditable(false);
		showButtonsDependendOnStatus(ticket.getStatus());
	}

	public void showErrorDialog(String msg, Exception exec) {
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
		textArea.setEditable(false);
		StringWriter writer = new StringWriter();
		exec.printStackTrace(new PrintWriter(writer));
		textArea.setText(writer.toString());
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(350, 150));
		// pass the scrollpane to the joptionpane.
		JOptionPane.showMessageDialog(this, scrollPane, msg, JOptionPane.ERROR_MESSAGE);
	}

	private JPanel createTicketTablePanel() throws TicketException {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add(new JLabel("Tickets currently available:"), BorderLayout.NORTH);
		tableModel = new TicketTableModel();
		tableModel.updateData(mainModel.getAllTickets());
		final JTable table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(580, 250));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		ListSelectionModel listSelect = table.getSelectionModel();
		listSelect.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting() && table.getSelectedRow() != -1) {
					controller.getAndShowTicketById((Integer) table.getValueAt(table.getSelectedRow(), 1));
				}
			}
		});
		table.setSelectionModel(listSelect);
		JScrollPane scrollPane = new JScrollPane(table);
		pane.add(scrollPane, BorderLayout.CENTER);
		return pane;
	}

	private JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		searchPanel.add(new JLabel("Search:"));
		JTextField searchField = new JTextField();
		searchField.setColumns(15);
		searchPanel.add(searchField);
		searchPanel.add(new JLabel("Type:"));
		ArrayList<de.uniba.rz.entities.Type> types = new ArrayList<>();
		types.add(null);
		types.addAll(Arrays.asList(de.uniba.rz.entities.Type.values()));
		JComboBox<de.uniba.rz.entities.Type> typeBox = new JComboBox<de.uniba.rz.entities.Type>(
		      types.toArray(new de.uniba.rz.entities.Type[types.size()])
		);
		searchPanel.add(typeBox);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				de.uniba.rz.entities.Type type = (de.uniba.rz.entities.Type) typeBox.getSelectedItem();
				controller.searchTicket(searchField.getText(), (de.uniba.rz.entities.Type) typeBox.getSelectedItem());
			}
		});
		searchPanel.add(searchButton);
		return searchPanel;
	}

	private void showButtonsDependendOnStatus(Status status) {
		if (Status.NEW == status) {
			rejectButton.setVisible(true);
			rejectButton.setEnabled(true);
			acceptButton.setVisible(true);
			acceptButton.setEnabled(true);
			closeButton.setVisible(false);
			saveButton.setVisible(false);
		} else if (Status.ACCEPTED == status) {
			closeButton.setVisible(true);
			closeButton.setEnabled(true);
			rejectButton.setVisible(false);
			acceptButton.setVisible(false);
			saveButton.setVisible(false);
		} else if (Status.REJECTED == status || Status.CLOSED == status) {
			closeButton.setVisible(true);
			closeButton.setEnabled(false);
			rejectButton.setVisible(false);
			acceptButton.setVisible(false);
			saveButton.setVisible(false);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			tableModel.updateData(mainModel.getAllTickets());
		} catch (TicketException e) {
			showErrorDialog("Updating ticket data failed!", e);
		}
	}

	public void updateTable(List<Ticket> allTickets) {
		tableModel.updateData(allTickets);
	}

	public void clearTicketDetails() {
		numberLabel.setText("");
		descArea.setText("");
		topicField.setText("");
		repField.setText("");
		prioBox.setEnabled(true);
		typeBox.setEnabled(true);
	}

	private void prepareCreationOfTicket() {
		clearTicketDetails();
		numberLabel.setText("Topic:");
		statusLabel.setVisible(false);
		prioBox.setEnabled(true);
		typeBox.setEnabled(true);
		repField.setEditable(true);
		topicField.setEditable(true);
		descArea.setEditable(true);
		showSaveButton();
	}

	private class TicketTableModel extends AbstractTableModel{
		private static final long serialVersionUID = -2733923133633436528L;
		private String[] columns = { "Status", "#", "Topic", "Reporter", "Prio", "Type" };
		private List<Ticket> ticketsToShow;

		@Override
		public String getColumnName(int i) {
			return columns[i];
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			return ticketsToShow.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Ticket ticket = ticketsToShow.get(arg0);
			if (arg1 == 0) {
				return ticket.getStatus();
			} else if (arg1 == 1) {
				return ticket.getId();
			} else if (arg1 == 2) {
				return ticket.getTopic();
			} else if (arg1 == 3) {
				return ticket.getReporter();
			} else if (arg1 == 4) {
				return ticket.getPriority();
			} else if (arg1 == 5) {
				return ticket.getType();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public void updateData(List<Ticket> allTickets) {
			ticketsToShow = allTickets;
			this.fireTableDataChanged();
		}
	}
}
