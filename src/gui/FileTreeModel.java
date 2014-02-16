package gui;

import java.io.File;
import java.io.Serializable;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileTreeModel implements TreeModel, Serializable {

	private static final long serialVersionUID = 1L;
	private File root;
	
	public FileTreeModel(File rootFile) {
		root = rootFile;
	}

	/**
	 * getChild - returns a File object of the child of the given index
	 * @param parentNode	the node whose child you want
	 * @param idx     		the index of the child you want
	 */
	@Override
	public Object getChild(Object parentNode, int idx) {
		String[] childList = ((File) parentNode).list();
		if ((childList == null) || (idx >= childList.length)) return null;
        return new File((File) parentNode, childList[idx]);

	}

	/**
	 * getChildCount - returns the number of children nodes of the parent
	 * @param parentNode   the node whose number of children you want
	 */
	@Override
	public int getChildCount(Object parentNode) {
		String[] childList = ((File) parentNode).list();
		if (childList == null) return 0;
		return childList.length;

	}

	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		
		return 0;
	}
	
	/**
	 * getRoot - returns the root of the tree model
	 */
	@Override
	public Object getRoot() {
		return root;
	}

	/**
	 * isLeaf - if it's a file, it's a leaf.
	 * @param leaf  the object whose leafyness needs checking
	 */
	@Override
	public boolean isLeaf(Object leaf) {
		return ((File) leaf).isFile();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) { }
	
	@Override
	public void addTreeModelListener(TreeModelListener arg0) { }

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) { }

}
