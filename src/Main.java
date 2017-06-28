import net.msdh.h3d.Home3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: TkachenkoAA
 * Date: 07.06.17
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
public class Main {

//    private static int width;
//    private int height;


    public static void main(String[] args) {

      JFrame myWindow = new JFrame("Пробное окно");
      myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myWindow.setSize(1024, 768);
      myWindow.setVisible(true);




      myWindow.setLayout(null);
      myWindow.setPreferredSize(new Dimension(410, 50));
      final Home3D home3D = new Home3D("F:\\dev\\projects\\lab\\dh\\console\\Resources\\Objects","F:\\dev\\projects\\lab\\dh\\console\\Resources\\Textures",700,500);
      home3D.setObject("myhouse");
      home3D.create();
      //final Home3D home3D = new Home3D("F:\\dev\\projects\\lab\\dh\\console\\Resources\\Objects\\woman.obj","F:\\dev\\projects\\lab\\dh\\console\\Resources\\Textures\\",700,500);
      //home3D.setTexturePatch();
      //home3D.setObjectPatch();
      //home3D.createShape("woman");

      myWindow.add(home3D.getCanvas3D());

      DefaultComboBoxModel objName = new DefaultComboBoxModel();



      Hashtable table = home3D.getObjectsName();

//      JCheckBox italicBox = new JCheckBox("Italic");
//      italicBox.addItemListener(new ItemListener() {
//        public void itemStateChanged(ItemEvent e) {
//
//        }
//      });

      int i=0;
      for(Enumeration e = table.keys() ; e.hasMoreElements() ;) {
        Object key = e.nextElement();
        //objName.addElement(key);
        JCheckBox shapeBox = new JCheckBox(key.toString());
        shapeBox.setName(key.toString());
        shapeBox.addItemListener(new ItemListener() {
           public void itemStateChanged(ItemEvent e) {
              System.out.println(e.getStateChange());
              if(e.getStateChange()==1){
                home3D.setUnvisible(((JCheckBox)e.getItem()).getName());
                System.out.println(((JCheckBox)e.getItem()).getName());
              }
              else{
                home3D.setVisible(((JCheckBox)e.getItem()).getName());
                System.out.println(((JCheckBox)e.getItem()).getName());
              }
           }
        });
        System.out.println(shapeBox.getName());
        shapeBox.setBounds(701,i,300,20);
        myWindow.add(shapeBox);
        myWindow.repaint();
        i=i+22;
      }

     // JComboBox visualObjects = new JComboBox(objName);
     // visualObjects.setSelectedIndex(0);
      i=i+22;
      final JLabel headerLabel = new JLabel("", JLabel.CENTER);
      headerLabel.setText("Control in action: JComboBox");
      headerLabel.setBounds(701,i,100,300);

    //  visualObjects.addActionListener(new ActionListener(){
//        public void actionPerformed(ActionEvent e){
//	      JComboBox jcmbType = (JComboBox) e.getSource();
//	      String cmbType = (String) jcmbType.getSelectedItem();
//          headerLabel.setText(cmbType);
//          home3D.setUnvisible(cmbType);
//        }
//      });

     // visualObjects.setBounds(701,0,300,20);
      myWindow.add(headerLabel);
     // myWindow.add(visualObjects);

      myWindow.repaint();


//      int width = 1024;
//      int height = 768;
//      setBounds(100, 100, width, height);
//      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      Home3D h3d = new Home3D();

    }

}
