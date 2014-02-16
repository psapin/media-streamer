package gui;

import java.awt.Frame;

import javax.swing.JFrame;

import player.Player;

public class PlayerFrame extends JFrame {

	public PlayerFrame(Player player) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setContentPane(player.getMPC());
	}
}
