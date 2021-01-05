package gov.sandia.geotess.gui.tools;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckBoxList extends JList<JCheckBox>
{
   protected static Border noFocusBorder = new EmptyBorder(0, 0, 0, 0);

   public CheckBoxList()
   {
      this.setCellRenderer(new CellRenderer<JCheckBox>());
      this.addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent e)
            {
               int index = locationToIndex(e.getPoint());

               if (index != -1) {
                  JCheckBox checkbox = (JCheckBox)getModel().getElementAt(index);
                  checkbox.setSelected(!checkbox.isSelected());
                  repaint();
               }
            }
         }
      );

      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }

   protected class CellRenderer<T> implements ListCellRenderer<T>
   {
	  @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
      {
         JCheckBox checkbox = (JCheckBox) value;
         checkbox.setBackground(isSelected ? Color.RED : getBackground());
         checkbox.setEnabled(isEnabled());
         checkbox.setFont(getFont());
         checkbox.setFocusPainted(false);
         checkbox.setBorderPainted(true);
         checkbox.setBorder(isSelected ?
          UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
         return checkbox;
      }
   }
}