	package managerPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import exportables.Cinema;
import exportables.Discount;
import exportables.Show;
import exportables.WeeklyProgram;
import sharedPackage.ShowPane;
import sharedPackage.SharedTableFrame;

public final class ManagerMainFrame extends SharedTableFrame implements ActionListener
{
	private static final long serialVersionUID = 8783148160448937226L;
	
	private final JButton createWeeklyButton = new JButton("Crea un nuovo programma settimanale");
	private final JButton addCinemaButton = new JButton("Inserisci una nuova sala");
	private final JButton addDiscountButton = new JButton("Inserisci una nuova politica di sconto");
	private final JButton changeDiscActButton = new JButton("Attiva/Disattiva politiche di sconto");
	private final JButton deleteDiscountsButton = new JButton("Elimina tutte le politiche di sconto");
	private final JButton showEarningsButton = new JButton("Mostra guadagni");

	private ArrayList<Cinema> cinemas;
	private ArrayList<Discount> discounts;
	
	private ArrayList<Object[]> showData = null;
	
	public ManagerMainFrame(ArrayList<Cinema> cinemas, ArrayList<Discount> discounts, WeeklyProgram program)
	{
		super("Progetto Multisala - Gestore", program);
		
		if(cinemas == null)
			throw new NullPointerException("La lista delle sale cinematografiche può essere nulla.");
		if(discounts == null)
			throw new NullPointerException("La lista delle politiche di sconto non può essere nulla.");
		
		this.cinemas = cinemas;
		this.discounts = discounts;
		
 		buttonPanel.add(createWeeklyButton);
 		buttonPanel.add(new JLabel(" "));
 		buttonPanel.add(addCinemaButton);
 		buttonPanel.add(new JLabel(" "));
 		buttonPanel.add(addDiscountButton);
 		buttonPanel.add(changeDiscActButton);
 		buttonPanel.add(deleteDiscountsButton);
 		buttonPanel.add(new JLabel(" "));
 		buttonPanel.add(showEarningsButton);
 		
		createWeeklyButton.addActionListener(this);
		addCinemaButton.addActionListener(this);
		addDiscountButton.addActionListener(this);
		changeDiscActButton.addActionListener(this);
		deleteDiscountsButton.addActionListener(this);
		showEarningsButton.addActionListener(this);		
		
		updateTableData();		
		initializeTable();
		updateTable();
		
		display();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource().equals(createWeeklyButton))
		{
			if(JOptionPane.showOptionDialog(null, "Vuoi eliminare il corrente programma "
					+ "settimanale e crearne uno nuovo?", "Avviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Si", "No"}, null) == 0) 
			{
				try
				{
					CreateWeeklyProgramPane.create(cinemas, program);
					updateTableData();
					updateTable();
				}
				catch(IllegalArgumentException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(event.getSource().equals(addCinemaButton))
		{
			Cinema cinema = CreateCinemaPane.create(cinemas);
			if(cinema != null)
				cinemas.add(cinema);
		}
		else if(event.getSource().equals(addDiscountButton))
		{
			Discount discount = CreateDiscountPane.create(discounts);
			if(discount != null)
				discounts.add(discount);
		}
		else if(event.getSource().equals(changeDiscActButton))
		{
			if(program.discountEnabled())
				program.disableDiscount();
			else
				program.enableDiscount();
			
			JOptionPane.showMessageDialog(null, "Politiche di sconto " + (program.discountEnabled() ? "attivate." : "disattivate."), "Informazione", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(event.getSource().equals(deleteDiscountsButton))
		{
			if(JOptionPane.showOptionDialog(null, "Vuoi eliminare tutte le politiche di sconto?", "Avviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Si", "No"}, null) == 0)
				discounts.clear();
		}
		else if(event.getSource().equals(showEarningsButton))
		{	
			if(program.getNumberOfShows() != 0)
			{
				JPanel panel = new JPanel(new BorderLayout());
				JTextArea area = new JTextArea(10, 20);	
				JScrollPane pane = new JScrollPane(area);
				
				area.setEditable(false);
				
				panel.add(new JLabel("Guadagno settimanale totale: " + program.getTotalEarning() + " €"), BorderLayout.NORTH);
				panel.add(new JLabel(" "), BorderLayout.CENTER);
				panel.add(pane, BorderLayout.SOUTH);
				
				ArrayList<?>[] results = program.getEarningsByShow();
				
				for(short i = 0; i < program.getNumberOfShows(); i++)
					area.append(results[0].get(i) + ": " + results[1].get(i) + " €\n");
				
				JOptionPane.showMessageDialog(null, panel, "Guadagno settimanale", JOptionPane.PLAIN_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(null, "Nessun programma spettimanale registrato.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void updateTableData()
	{
		showData = new ArrayList<Object[]>();
		for(short i = 0; i < program.getNumberOfShows(); i++)
			showData.add(new Object[] {program.get(i).getTitle(), program.get(i).getCinema().getCode(), program.get(i).getCinema().getFreeSeatsCount()});
	}
	
	private void initializeTable()
	{		
		orderShows(OrderBy.TITLE);
		
		showTable = new JTable(program.getNumberOfShows() < 25 ? 25 : program.getNumberOfShows(), 3)
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
	        {                
	                return false;               
	        };
	    };
	    
	    JTableHeader tableHeader = showTable.getTableHeader();
	    TableColumnModel tcm = tableHeader.getColumnModel();
	    tcm.getColumn(0).setHeaderValue("Titolo");
	    tcm.getColumn(1).setHeaderValue("N.Sala");
	    tcm.getColumn(2).setHeaderValue("N.Posti liberi in sala");
	    tableHeader.repaint();
	    
		((DefaultTableCellRenderer) tableHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
		tableHeader.setReorderingAllowed(false);
		tableHeader.setResizingAllowed(false);
	    showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showTable.setRowHeight(20);
	    showTable.getColumnModel().getColumn(0).setPreferredWidth(203);
	    showTable.getColumnModel().getColumn(1).setPreferredWidth(45);
	    showTable.getColumnModel().getColumn(2).setPreferredWidth(202);
	    
	    tableHeader.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
				switch(showTable.columnAtPoint(e.getPoint()))
				{
					case 0: orderShows(OrderBy.TITLE); break;
					case 1: orderShows(OrderBy.CINEMA_CODE); break;
					case 2: orderShows(OrderBy.AVAIL_SEATS); break;
				}
				
				updateTable();
			}

			public void mouseClicked(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){} 
		});
	    
	    showTable.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
				int row = showTable.rowAtPoint(e.getPoint());
				String title = (String)showTable.getModel().getValueAt(row, 0);
				
				if(title != null && !title.isBlank())
				{
					int index = program.indexOf(new Show(title, 1f, new GregorianCalendar(), (short)1, new Cinema((byte)1, (byte)1, (byte)1), ""));
					new ShowPane(program.get(index)).showManagerPane();
					showTable.getModel().setValueAt(program.get(index).getCinema().getFreeSeatsCount(), row, 2);
				}
			}

			public void mouseClicked(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){} 
		});
	    
	    showPane = new JScrollPane(showTable);
	    showPane.setPreferredSize(new Dimension(450, 523));
	    panel.add(showPane, BorderLayout.EAST);
	}
	
	private void orderShows(OrderBy order)
	{
		short n = (short) (program.getNumberOfShows() - 1);
		short lastChanged = program.getNumberOfShows();
		while (lastChanged > 0)
		{
			lastChanged = 0;
		    for(short i = 0; i < n; i++)
		    {	
		    	int cond = 0;
		    	
		    	String title0 = ((String)showData.get(i)[0]).toLowerCase();
		    	String title1 = ((String)showData.get(i + 1)[0]).toLowerCase();
		    	byte code0 = (byte)showData.get(i)[1];
		    	byte code1 = (byte)showData.get(i + 1)[1];
		    	short free0 = (short)showData.get(i)[2];
		    	short free1 = (short)showData.get(i + 1)[2];
		    	
		    	switch(order)
		    	{
		    		case TITLE:
		    			cond = title0.compareTo(title1);
		    			if(cond == 0)
		    			{	
		    				cond = Byte.compare(code0, code1);
		    				if(cond == 0)
		    					cond = Short.compare(free0, free1);
		    			}
		    			break;
		    		case CINEMA_CODE:
		    			cond = Byte.compare(code0, code1);
		    			if(cond == 0)
		    			{
		    				cond = title0.compareTo(title1);
		    				if(cond == 0)
		    					cond = Short.compare(free0, free1);
		    			}
		    			break;
		    		case AVAIL_SEATS:
		    			cond = Short.compare(free0, free1);
		    			if(cond == 0)
		    			{
		    				cond = title0.compareTo(title1);
		    				if(cond == 0)
		    					cond = Byte.compare(code0, code1);
		    			}
		    		default:
		    	}
		    	
		    	if(cond > 0)
		    	{
		    		Object[] buffer = showData.get(i);
		    		showData.set(i, showData.get(i + 1));
		    		showData.set(i + 1, buffer);
		    		lastChanged = i;
		    	}
		    }
		    n = lastChanged;
		}
	}
	
	private void updateTable()
	{		
		TableModel tableModel = showTable.getModel();
		
		for(short i = 0; i < tableModel.getRowCount(); i++)
		{
			tableModel.setValueAt("", i, 0);
			tableModel.setValueAt("", i, 1);
			tableModel.setValueAt("", i, 2);
		}
		
		for(short i = 0; i < program.getNumberOfShows(); i++)
		{
			tableModel.setValueAt((String)showData.get(i)[0], i, 0);
			tableModel.setValueAt((byte)showData.get(i)[1] + "", i, 1);
			tableModel.setValueAt((short)showData.get(i)[2] + "", i, 2);
		}
		
		showTable.getSelectionModel().clearSelection();
	}
}
