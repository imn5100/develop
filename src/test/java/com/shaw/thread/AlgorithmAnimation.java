package com.shaw.thread;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AlgorithmAnimation {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new AnimationFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

class AnimationFrame extends JFrame {
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULTHEIGHT = 300;
	private static final long serialVersionUID = 1L;

	public AnimationFrame() {
		ArrayComponent comp = new ArrayComponent();
		add(comp, BorderLayout.CENTER);
		final Sorter sorter = new Sorter(comp);
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sorter.setRun();
			}
		});
		JButton stepButton = new JButton("Stop");
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sorter.setStep();
			}
		});
		JPanel buttons = new JPanel();
		buttons.add(runButton);
		buttons.add(stepButton);
		add(buttons, BorderLayout.NORTH);
		setSize(DEFAULT_WIDTH, DEFAULTHEIGHT);
		Thread t = new Thread(sorter);
		t.start();
	}
}

class Sorter implements Runnable {
	private Double[] values;
	private ArrayComponent compent;
	private Semaphore gate;
	private static final int DELAY = 1000;
	private volatile boolean run;
	private static final int VALUES_LENGTH = 30;

	public Sorter(ArrayComponent comp) {
		values = new Double[VALUES_LENGTH];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Double(Math.random());
		}
		this.compent = comp;
		this.gate = new Semaphore(1);
		this.run = false;
	}

	public void setRun() {
		run = true;
		gate.release();
	}

	public void setStep() {
		run = false;
		gate.release();
	}

	@Override
	public void run() {
		Comparator<Double> comp = new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				compent.setValues(values, o1, o2);
				try {
					if (run)
						Thread.sleep(DELAY);
					else
						gate.acquire();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return o1.compareTo(o2);
			}
		};
		Arrays.sort(values, comp);
		compent.setValues(values, null, null);
	}

}

class ArrayComponent extends JComponent {
	private static final long serialVersionUID = -3522218510126507778L;
	private Double marked1;
	private Double marked2;
	private Double[] values;

	public synchronized void setValues(Double[] values, Double marked1, Double marked2) {
		this.values = values;
		this.marked1 = marked1;
		this.marked2 = marked2;
		repaint();
	}

	public synchronized void paintComponent(Graphics g) {
		if (values == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		int width = getWidth() / values.length;
		for (int i = 0; i < values.length; i++) {
			double height = values[i] * getHeight();
			Rectangle2D bar = new Rectangle2D.Double(width * i, 0, width, height);
			if (values[i] == marked1 || values[i] == marked2)
				g2.fill(bar);
			else
				g2.draw(bar);
		}

	}

}