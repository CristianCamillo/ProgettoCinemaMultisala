package sharedPackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import exportables.Cinema;
import exportables.Customer;

public class CinemaGraphicsPane
{
	private final static short WIDTH = 650;
	private final static short HEIGHT = 650;
	
	public static Cinema getModified(Cinema cinema, Customer customer)
	{
		if(cinema == null)
			throw new NullPointerException("La sala cinematografica non può essere nulla.");
		
		short tileSize = (short) (cinema.getWidth() >= cinema.getDepth() ? (WIDTH - (WIDTH / 4)) / cinema.getWidth() : (HEIGHT - (HEIGHT / 4)) / cinema.getDepth());
		short xOffset = (short) (cinema.getWidth() >= cinema.getDepth() ? WIDTH / 8 : (WIDTH - (tileSize * cinema.getWidth()) - WIDTH / 4) / 2 + WIDTH / 8);
		short yOffset = (short) (cinema.getDepth() >= cinema.getWidth() ? HEIGHT / 8 : (HEIGHT - (tileSize * cinema.getDepth()) - HEIGHT / 4) / 2 + HEIGHT / 8);
		
		Canvas canvas = new Canvas()
		{
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g)
			{
				g.clearRect(0, 0, WIDTH, HEIGHT);
				
				for(byte y = 0; y < cinema.getDepth(); y++)
					for(byte x = 0; x < cinema.getWidth(); x++)
					{
						if(cinema.isSeatAvailable(x, y))
							g.setColor(Color.GREEN);
						else if(cinema.isSeatUnavailable(x, y))
							g.setColor(Color.GRAY);
						else if(customer != null && cinema.isSeatBookedBy(x, y, customer))
							g.setColor(Color.CYAN);
						else if(customer != null && cinema.isSeatSoldTo(x, y, customer))
							g.setColor(Color.BLUE);
						else if(cinema.isSeatBooked(x, y))
							g.setColor(Color.YELLOW);
						else if(cinema.isSeatSold(x, y))
							g.setColor(Color.RED);
						
						g.fillRect(xOffset + tileSize * x, yOffset + tileSize * y + tileSize / 4, tileSize, tileSize * 3 / 4);
						
						g.setColor(Color.BLACK);
						g.drawRect(xOffset + tileSize * x, yOffset + tileSize * y + tileSize / 4, tileSize, tileSize * 3 / 4);
						g.drawRect(xOffset + tileSize * x + tileSize / 4, yOffset + tileSize * y + tileSize / 4, tileSize / 2, tileSize / 2);
					}
			}
		};
		
		canvas.setSize(WIDTH, HEIGHT);
		
		canvas.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent event)
			{
				byte x = (byte) ((event.getPoint().x - xOffset) / tileSize);
				byte y = (byte) ((event.getPoint().y - yOffset) / tileSize);
				
				if(event.getPoint().x >= xOffset && event.getPoint().y >= yOffset && event.getPoint().x <= WIDTH - xOffset && event.getPoint().y <= HEIGHT - yOffset)
				{
					if(customer != null)
					{
						if(cinema.isSeatAvailable(x, y) && !cinema.isBookedBy(customer) && !cinema.isSoldTo(customer))
						{
							cinema.setSeatBooked(x, y, customer.getEmailAddress());
							canvas.repaint();
						}
						else if(cinema.isSeatBookedBy(x, y, customer))
						{
							cinema.setSeatSold(x, y, customer);
							canvas.repaint();
						}
						else if(cinema.isSeatSoldTo(x, y, customer))
						{
							cinema.removeSold(x, y, customer);
							canvas.repaint();
						}
						else
							JOptionPane.showMessageDialog(null, "Posto " + (x + 1) + "-" + (y + 1) + " non interagibile.", "Errore", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						if(cinema.isSeatAvailable(x, y))
						{
							cinema.setSeatUnavailable(x, y);
							canvas.repaint();
						}
						else if(cinema.isSeatUnavailable(x, y))
						{
							cinema.setSeatAvailable(x, y);
							canvas.repaint();
						}
						else
							JOptionPane.showMessageDialog(null, "Posto " + (x + 1) + "-" + (y + 1) + " non interagibile.", "Errore", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
			public void mouseClicked(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
		
		
		if(JOptionPane.showOptionDialog(null, canvas, "Sala cinematografica " + cinema.getCode(),
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] {"Ok", "Annulla"}, null) == 0)
			return cinema;
		
		return null;
	}	
}
