package hanabi.gui;

import hanabi.game.Card;
import hanabi.game.State;

import javax.swing.*;
import java.awt.*;
/*
public class FireworksPanel extends RefreshablePanel<State>
{
	JLabel blue,green,red,white,yellow,total;

	public FireworksPanel()
	{
		super("fireworks");
		blue = new JLabel();
		green = new JLabel();
		red = new JLabel();
		white = new JLabel();
		yellow = new JLabel();
		total = new JLabel();
	}

	@Override
	protected void afterChildrenInit() {

	}

	@Override
	protected void afterChildrenRefresh(State model) {

	}

	@Override
	protected void beforeChildrenInit() {
		setLayout(new GridLayout(5,1));
		setBackground(null);
		add(blue);
		add(green);
		add(red);
		add(white);
		add(yellow);
	}

	@Override
	protected void beforeChildrenRefresh(State model)
	{
		int t=0, f=0;
		f = model.getFirework("blue");
		t+=f;
		blue.setText("  Blu: "+f);
		blue.setForeground(Card.getAwtColor("blue"));

		f=model.getFirework("red");
		t+=f;
		red.setText("  Rosso: "+f);
		red.setForeground(Card.getAwtColor("red"));

		f=model.getFirework("white");
		t+=f;
		white.setText("  Bianco: "+f);
		white.setForeground(Card.getAwtColor("white"));

		f=model.getFirework("green");
		t+=f;
		green.setText("  Verde: "+f);
		green.setForeground(Card.getAwtColor("green"));

		f=model.getFirework("yellow");
		t+=f;
		yellow.setText("  Giallo: "+f);
		yellow.setForeground(Card.getAwtColor("yellow"));

		total.setText("  Totale: "+f);

	}
}
*/

public class FireworksPanel extends JPanel {
	JLabel blue, green, red, white, yellow, total;

	public FireworksPanel() {
		super();
		blue = new JLabel();
		green = new JLabel();
		red = new JLabel();
		white = new JLabel();
		yellow = new JLabel();
		total = new JLabel();

		setLayout(new GridLayout(6, 1));
		setBackground(null);
		add(blue);
		add(green);
		add(red);
		add(white);
		add(yellow);
		add(total);
	}

	public void setModel(State model) {
		int t = 0,f;
		f = model.getFirework("blue");
		t += f;
		blue.setText("  Blu: " + f);
		blue.setForeground(Card.getAwtColor("blue"));

		f = model.getFirework("red");
		t += f;
		red.setText("  Rosso: " + f);
		red.setForeground(Card.getAwtColor("red"));

		f = model.getFirework("white");
		t += f;
		white.setText("  Bianco: " + f);
		white.setForeground(Card.getAwtColor("white"));

		f = model.getFirework("green");
		t += f;
		green.setText("  Verde: " + f);
		green.setForeground(Card.getAwtColor("green"));

		f = model.getFirework("yellow");
		t += f;
		yellow.setText("  Giallo: " + f);
		yellow.setForeground(Card.getAwtColor("yellow"));

		total.setText("  Totale: " + t);

	}
}