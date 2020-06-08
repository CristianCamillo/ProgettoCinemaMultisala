package customerPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import exportables.Cinema;
import exportables.Customer;
import exportables.Discount;
import exportables.Show;
import exportables.WeeklyProgram;
import sharedPackage.SharedTableFrame;
import sharedPackage.ShowPane;

public final class CustomerMainFrame extends SharedTableFrame implements ActionListener
{
	private static final long serialVersionUID = -918450217047492896L;
	
	private final JButton showButton = new JButton("Mostra");
	private JComboBox<String> cinemaBox;
	private final JRadioButton showPastShows = new JRadioButton("Mostra spettacoli passati");
	
	private Customer customer;
	private ArrayList<Discount> discounts;

	private ArrayList<Object[]> showData = new ArrayList<Object[]>();
	
	public CustomerMainFrame(Customer customer, ArrayList<Discount> discounts, WeeklyProgram program)
	{	
		super("Progetto Multisala - Cliente", program); 
		
		if(customer == null)
			throw new NullPointerException("Il cliente non può essere nullo.");
		if(discounts == null)
			throw new NullPointerException("La lista delle politiche di sconto non può essere nulla.");
		
		this.customer = customer;
		this.discounts = discounts;
		
		byte max = 0;		
		for(short i = 0; i < program.getNumberOfShows(); i++)
		{
			Show currentShow = program.get(i);
			
			if(max < currentShow.getCinema().getCode())
				max =  currentShow.getCinema().getCode();
			
			showData.add(i, new Object[] {currentShow.getTitle(), currentShow.getDate(), currentShow.getCinema().getCode()});
		}

		String[] cinemas = new String[max + 1];
		cinemas[0] = "Tutte le sale";
		for(byte i = 1; i <= max; i++)
			cinemas[i] = "Sala " + i;		
		
		cinemaBox = new JComboBox<String>(cinemas);	
	    
 		buttonPanel.add(showButton);
 		buttonPanel.add(new JLabel(" "));
 		buttonPanel.add(cinemaBox);
 		buttonPanel.add(showPastShows);
 		
 		initializeTable();
 		updateTable((byte)0);
		
		showButton.addActionListener(this);
		
		display();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		updateTable((byte)cinemaBox.getSelectedIndex());
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
	    tcm.getColumn(1).setHeaderValue("Data");
	    tcm.getColumn(2).setHeaderValue("N.Sala");
	    tableHeader.repaint();
	    
		((DefaultTableCellRenderer) tableHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
		tableHeader.setReorderingAllowed(false);
		tableHeader.setResizingAllowed(false);
	    showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showTable.setRowHeight(20);
	    showTable.getColumnModel().getColumn(0).setPreferredWidth(265);
	    showTable.getColumnModel().getColumn(1).setPreferredWidth(140);
	    showTable.getColumnModel().getColumn(2).setPreferredWidth(45);
	    
	    tableHeader.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
				switch(showTable.columnAtPoint(e.getPoint()))
				{
					case 0: orderShows(OrderBy.TITLE); break;
					case 1: orderShows(OrderBy.DATE); break;
					case 2: orderShows(OrderBy.CINEMA_CODE);
				}
				
				updateTable((byte)cinemaBox.getSelectedIndex());
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
				
				if(showTable.getModel().getValueAt(row, 0) != null)
				{
					Show compare = new Show(showTable.getModel().getValueAt(row, 0).toString(), 1f, new GregorianCalendar(), (short) 1, new Cinema((byte)1, (byte)1, (byte)1), "");
				
					for(short i = 0; i < program.getNumberOfShows(); i++)
						if(compare.equals(program.get(i)))
						{
							new ShowPane(program.get(i)).showCustomerPane(customer, discounts, program);
							break;
						}
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
		short lastChanged = (short) showData.size();
		while (lastChanged > 0)
		{
			lastChanged = 0;
		    for(short i = 0; i < n; i++)
		    {	
		    	int cond = 0;
		    	
		    	String title0 = ((String)showData.get(i)[0]).toLowerCase();
		    	String title1 = ((String)showData.get(i + 1)[0]).toLowerCase();
		    	GregorianCalendar date0 = (GregorianCalendar)showData.get(i)[1];
		    	GregorianCalendar date1 = (GregorianCalendar)showData.get(i + 1)[1];
		    	byte code0 = (byte)showData.get(i)[2];
		    	byte code1 = (byte)showData.get(i + 1)[2];
		    	
		    	switch(order)
		    	{
		    		case TITLE:
		    			cond = title0.compareTo(title1);
		    			if(cond == 0)
		    			{
		    				cond = date0.compareTo(date1);
		    				if(cond == 0)
		    					cond = Byte.compare(code0, code1);
		    			}
		    			break;
		    		case DATE:
		    			cond = date0.compareTo(date1);
		    			if(cond == 0)
		    			{
		    				cond = title0.compareTo(title1);
		    				if(cond == 0)
		    					cond = Byte.compare(code0, code1);
		    			}
		    			break;
		    		case CINEMA_CODE:
		    			cond = Byte.compare(code0, code1);
		    			if(cond == 0)
		    			{	
		    				cond = title0.compareTo(title1);;
		    				if(cond == 0)
		    					cond = date0.compareTo(date1);
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
	
	private void updateTable(byte cinemaCode)
	{	
		TableModel tableModel = showTable.getModel();
		
		for(short i = 0; i < program.getNumberOfShows(); i++)
		{
			tableModel.setValueAt("", i, 0);
			tableModel.setValueAt("", i, 1);
			tableModel.setValueAt("", i, 2);
		}
		
		short j = 0;
		for(short i = 0; i < showData.size(); i++)
			if((cinemaCode == 0 || cinemaCode == (byte)showData.get(i)[2]) && (showPastShows.isSelected() || new GregorianCalendar().compareTo((GregorianCalendar) showData.get(i)[1]) < 0))
			{
				tableModel.setValueAt(showData.get(i)[0], j, 0);
				tableModel.setValueAt(getSimpleDate((GregorianCalendar)showData.get(i)[1]), j, 1);
				tableModel.setValueAt(showData.get(i)[2] + "", j, 2);
				j++;
			}
		
		showTable.getSelectionModel().clearSelection();
	}
}
