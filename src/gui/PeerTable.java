package gui;

import java.awt.Dimension;
import java.net.InetAddress;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PeerTable extends JTable {

	private DefaultTableModel tableModel;
	private boolean firstPeer = true;
	private HashMap<String, InetAddress> addrMap;
	
	/* A JTable to show the devices discovered with mDNS
	 * constructor sets some values and inits the tableModel with loading text
	 */
	public PeerTable() {
		super(new DefaultTableModel(new Object[]{"Name", "Type", "ID"}, 1));
		
		addrMap = new HashMap<String, InetAddress>();
		
		tableModel = (DefaultTableModel) this.getModel();
		this.setPreferredScrollableViewportSize(new Dimension(400, 150));
        this.setFillsViewportHeight(true);
        tableModel.addRow(new Object[]{"Please Wait...", "Discovering Services...", ""});
	}
	
	/* adds a new row of data to the chart of devices
	 * remove loading text if the first device to resolve
	 */
	public void addData(String dName, String dType, String sName, InetAddress addr) {
		if(firstPeer){
			tableModel.removeRow(1);
			tableModel.removeRow(0);
			firstPeer = false;
		}
		addrMap.put(sName, addr);
		tableModel.addRow(new Object[]{dName, dType, sName});
	}
	
	/* removes a row of data based on the given stream name
	 * loops through to find the row with the given value
	 */
	public void removeData(String sName) {
		for (int row = 0; row <= this.getRowCount() - 1; row++) {
			if (sName.equals(this.getValueAt(row, 2))) {
				tableModel.removeRow(row);
				break;
            }
		}
	}
	
	public InetAddress getInetAddress(String serviceName) {
		return addrMap.get(serviceName);
	}
	
}
