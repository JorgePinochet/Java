<<<<<<< HEAD
package clientSide;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class BSClient 
{   
	
	int i,j;//coordinates
	static int	x;
	static int	y;
	int	endp;
	boolean gameover=false;
	String servername=" ";
	String start=" ";
	Socket bsSocket = null;
	static PrintWriter out = null;
	BufferedReader in = null;
		
	/**
	 * Constructor sets up the battleship game server, different from the chat server.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public BSClient () throws IOException 
	{	
		try 
		{
			servername = JOptionPane.showInputDialog(null,"Input the name of" + " the server you wish to connect to.\n(ie. PC3873.princeton.edu)", "Server Name",JOptionPane.PLAIN_MESSAGE);
			if (servername == null)
			{
				servername=" ";
			}
			System.out.println("Server Name: "+servername);
            bsSocket = new Socket(servername, 4443);
            this.out = new PrintWriter(bsSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(bsSocket.getInputStream()));
        } 
		catch (UnknownHostException e) 
		{
            JOptionPane.showMessageDialog(null,"Don't know about host: " + servername + ".","Error",JOptionPane.WARNING_MESSAGE);
			servername="invalid";  
        } 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,"Couldn't get I/O for the connection to: "+servername+".","Error",JOptionPane.WARNING_MESSAGE);
			servername="invalid";           
        }	
    }
	
	/**
	 * Method listens to the observable from the battleship class.
	 * 
	 * @throws IOException 
	 */
	public void listen() throws IOException 
	{		
		while(!(start = this.in.readLine()).equals("play"))
		{			
			System.out.println("start =" + start);
			if (start.equals("getmove"))
				break;
			else if (start.equals("opponent"))
			{
				Battleship.getPlayers(1).setUser(in.readLine());	
			}				
			System.out.println("Still in the loop.");
		}
		if (!start.equals("getmove"))			
		{
			Battleship.getPlayers(0).setMove(true);
			JOptionPane.showMessageDialog(null,Battleship.getPlayers(0).getUser() + " move.", "", JOptionPane.INFORMATION_MESSAGE);
		}
	}	
	
	/**
	 * Method handles the results of a shot fired.
	 */
	public void results()
	{
		String results = null;
		
		try
		{
			while(!(results = in.readLine()).equals("results"))
			{	
				
			}
			results = in.readLine();
			System.out.println("results:  " + results);			
		}
		catch (IOException e)
		{ 
			e.printStackTrace();
			System.out.println("Nothing's there.");
		}
		if (results.startsWith("miss"))
		{
			Battleship.getPlayers(1).setBboard(x,y,Color.blue);
			Battleship.getPlayers(0).setShots();
		}
		else if (results.startsWith("hit"))
		{
			Battleship.getPlayers(0).setHits();			
			results=results.substring(results.indexOf(" ") + 1);			
			if (results.startsWith("shipsunk"))
			{
				Battleship.getPlayers(1).setShipsLeft();
				results=results.substring(results.indexOf(" ")+1);
				if (!results.startsWith("Patrol"))
					JOptionPane.showMessageDialog(null,"You sank the " + results.substring(0,results.indexOf(" ")) + "!","Good Job!",				
					JOptionPane.INFORMATION_MESSAGE);
				else
				{
				  	JOptionPane.showMessageDialog(null,"You sank the Patrol Boa" + "t!","Good Job!", JOptionPane.INFORMATION_MESSAGE);
					results=results.substring(results.indexOf(" ")+1);
				}
				results = results.substring(results.indexOf(" ")+1);
				x = Integer.parseInt(results.substring(0,1));
				y = Integer.parseInt(results.substring(2,3));
				endp = Integer.parseInt(results.substring(6,7));				
				if (Integer.parseInt(results.substring(4,5)) == 0)
				{					
					for (i = x; i <= endp; i++)
					{
						Battleship.getPlayers(1).setBboard(i,y,Color.black);
					}
				}
				else
				{
					for (i = y; i <= endp; i++)
					{
						Battleship.getPlayers(1).setBboard(x,i,Color.black);
					}					
				}
			}			
			else
			{			
				Battleship.getPlayers(1).setBboard(x,y,Color.orange);
			}
			Battleship.getPlayers(0).setShots();			
		}
		else if (results.startsWith("theirshot"))
		{
			results = results.substring(results.indexOf(" ")+1);
			System.out.println("results:  "+results);
			x = Integer.parseInt(results.substring(0,1));
			y = Integer.parseInt(results.substring(2,3));
			if (!Battleship.getPlayers(0).getWhatShip(x,y).equals(" "))
			{
				Battleship.getPlayers(1).setHits();
				if (!Battleship.getPlayers(0).isSunk(x,y,Battleship.getPlayers(0).getWhatShip(x,y)))
				{
					Battleship.getPlayers(0).setBboard(x,y,Color.orange);
				}
			}
			else
			{
			  Battleship.getPlayers(0).setBboard(x,y,Color.blue);
			}
			Battleship.getPlayers(1).setShots();				
		}
		else if (results.startsWith("wastedturn"))
		{			
			System.out.println("Turn missed due to infraction");
		}		
		else if (results.startsWith("lostturn"))
		{
			JOptionPane.showMessageDialog(null, "You took too long!",
			"Lost Turn", JOptionPane.INFORMATION_MESSAGE);				
		}	
		try
		{
		  if (in.readLine().equals("gameover"))
		  {
		  		Battleship.setGameover(true);
				if (Battleship.getPlayers(0).getShipsLeft()!=0)
				{
					JOptionPane.showMessageDialog(null,"YOU WON!", "It's A Celebration!",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You Lost","Sorry!", JOptionPane.INFORMATION_MESSAGE);
				}
		  }
		}
		catch (IOException g)
		{
			g.printStackTrace();
			System.out.println("Problem with input out exception");
		}
		Player.isStatsOpen();
		System.out.println("results received");		
	}
	
	public String getServerName()
	{
		return servername;	
	}
		
	/**
	 * Method says where a shot was fired.
	 * 
	 * @param a, x coordinate from the shot fired
	 * @param b, y coordinate from the shot fired
	 */
	public static void fireShot(int a, int b)
	{
		x = a;
		y = b;		
		out.println(x);
		out.println(y);
		System.out.println("shot fired:  " + Battleship.getCletters(x + 1) + "," + Battleship.getCnumbers(y + 1));		
	}
	
	/**
	 * Method that checks for the fire shot.
	 */
	public static void fireShot()
	{
		out.println("wastedturn");		
	}
	
	/**
	 * Method sends the position of ships to the enemy grid.
	 */
	public void sendShips()
	{		
		out.println(Battleship.getPlayers(Battleship.getYou()).getUser());			
		for ( i = 0; i < 10; i++)
			for (j = 0; j < 10; j++)
				out.println(Battleship.getPlayers(Battleship.getYou()).getWhatShip(i,j));		
	}
=======
package clientSide;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class BSClient 
{   
	
	int i,j;//coordinates
	static int	x;
	static int	y;
	int	endp;
	boolean gameover=false;
	String servername=" ";
	String start=" ";
	Socket bsSocket = null;
	static PrintWriter out = null;
	BufferedReader in = null;
		
	/**
	 * Constructor sets up the battleship game server, different from the chat server.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public BSClient () throws IOException 
	{	
		try 
		{
			servername = JOptionPane.showInputDialog(null,"Input the name of" + " the server you wish to connect to.\n(ie. PC3873.princeton.edu)", "Server Name",JOptionPane.PLAIN_MESSAGE);
			if (servername == null)
			{
				servername=" ";
			}
			System.out.println("Server Name: "+servername);
            bsSocket = new Socket(servername, 4443);
            this.out = new PrintWriter(bsSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(bsSocket.getInputStream()));
        } 
		catch (UnknownHostException e) 
		{
            JOptionPane.showMessageDialog(null,"Don't know about host: " + servername + ".","Error",JOptionPane.WARNING_MESSAGE);
			servername="invalid";  
        } 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,"Couldn't get I/O for the connection to: "+servername+".","Error",JOptionPane.WARNING_MESSAGE);
			servername="invalid";           
        }	
    }
	
	/**
	 * Method listens to the observable from the battleship class.
	 * 
	 * @throws IOException 
	 */
	public void listen() throws IOException 
	{		
		while(!(start = this.in.readLine()).equals("play"))
		{			
			System.out.println("start =" + start);
			if (start.equals("getmove"))
				break;
			else if (start.equals("opponent"))
			{
				Battleship.getPlayers(1).setUser(in.readLine());	
			}				
			System.out.println("Still in the loop.");
		}
		if (!start.equals("getmove"))			
		{
			Battleship.getPlayers(0).setMove(true);
			JOptionPane.showMessageDialog(null,Battleship.getPlayers(0).getUser() + " move.", "", JOptionPane.INFORMATION_MESSAGE);
		}
	}	
	
	/**
	 * Method handles the results of a shot fired.
	 */
	public void results()
	{
		String results = null;
		
		try
		{
			while(!(results = in.readLine()).equals("results"))
			{	
				
			}
			results = in.readLine();
			System.out.println("results:  " + results);			
		}
		catch (IOException e)
		{ 
			e.printStackTrace();
			System.out.println("Nothing's there.");
		}
		if (results.startsWith("miss"))
		{
			Battleship.getPlayers(1).setBboard(x,y,Color.blue);
			Battleship.getPlayers(0).setShots();
		}
		else if (results.startsWith("hit"))
		{
			Battleship.getPlayers(0).setHits();			
			results=results.substring(results.indexOf(" ") + 1);			
			if (results.startsWith("shipsunk"))
			{
				Battleship.getPlayers(1).setShipsLeft();
				results=results.substring(results.indexOf(" ")+1);
				if (!results.startsWith("Patrol"))
					JOptionPane.showMessageDialog(null,"You sank the " + results.substring(0,results.indexOf(" ")) + "!","Good Job!",				
					JOptionPane.INFORMATION_MESSAGE);
				else
				{
				  	JOptionPane.showMessageDialog(null,"You sank the Patrol Boa" + "t!","Good Job!", JOptionPane.INFORMATION_MESSAGE);
					results=results.substring(results.indexOf(" ")+1);
				}
				results = results.substring(results.indexOf(" ")+1);
				x = Integer.parseInt(results.substring(0,1));
				y = Integer.parseInt(results.substring(2,3));
				endp = Integer.parseInt(results.substring(6,7));				
				if (Integer.parseInt(results.substring(4,5)) == 0)
				{					
					for (i = x; i <= endp; i++)
					{
						Battleship.getPlayers(1).setBboard(i,y,Color.black);
					}
				}
				else
				{
					for (i = y; i <= endp; i++)
					{
						Battleship.getPlayers(1).setBboard(x,i,Color.black);
					}					
				}
			}			
			else
			{			
				Battleship.getPlayers(1).setBboard(x,y,Color.orange);
			}
			Battleship.getPlayers(0).setShots();			
		}
		else if (results.startsWith("theirshot"))
		{
			results = results.substring(results.indexOf(" ")+1);
			System.out.println("results:  "+results);
			x = Integer.parseInt(results.substring(0,1));
			y = Integer.parseInt(results.substring(2,3));
			if (!Battleship.getPlayers(0).getWhatShip(x,y).equals(" "))
			{
				Battleship.getPlayers(1).setHits();
				if (!Battleship.getPlayers(0).isSunk(x,y,Battleship.getPlayers(0).getWhatShip(x,y)))
				{
					Battleship.getPlayers(0).setBboard(x,y,Color.orange);
				}
			}
			else
			{
			  Battleship.getPlayers(0).setBboard(x,y,Color.blue);
			}
			Battleship.getPlayers(1).setShots();				
		}
		else if (results.startsWith("wastedturn"))
		{			
			System.out.println("Turn missed due to infraction");
		}		
		else if (results.startsWith("lostturn"))
		{
			JOptionPane.showMessageDialog(null, "You took too long!",
			"Lost Turn", JOptionPane.INFORMATION_MESSAGE);				
		}	
		try
		{
		  if (in.readLine().equals("gameover"))
		  {
		  		Battleship.setGameover(true);
				if (Battleship.getPlayers(0).getShipsLeft()!=0)
				{
					JOptionPane.showMessageDialog(null,"YOU WON!", "It's A Celebration!",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You Lost","Sorry!", JOptionPane.INFORMATION_MESSAGE);
				}
		  }
		}
		catch (IOException g)
		{
			g.printStackTrace();
			System.out.println("Problem with input out exception");
		}
		Player.isStatsOpen();
		System.out.println("results received");		
	}
	
	public String getServerName()
	{
		return servername;	
	}
		
	/**
	 * Method says where a shot was fired.
	 * 
	 * @param a, x coordinate from the shot fired
	 * @param b, y coordinate from the shot fired
	 */
	public static void fireShot(int a, int b)
	{
		x = a;
		y = b;		
		out.println(x);
		out.println(y);
		System.out.println("shot fired:  " + Battleship.getCletters(x + 1) + "," + Battleship.getCnumbers(y + 1));		
	}
	
	/**
	 * Method that checks for the fire shot.
	 */
	public static void fireShot()
	{
		out.println("wastedturn");		
	}
	
	/**
	 * Method sends the position of ships to the enemy grid.
	 */
	public void sendShips()
	{		
		out.println(Battleship.getPlayers(Battleship.getYou()).getUser());			
		for ( i = 0; i < 10; i++)
			for (j = 0; j < 10; j++)
				out.println(Battleship.getPlayers(Battleship.getYou()).getWhatShip(i,j));		
	}
>>>>>>> origin/master
}