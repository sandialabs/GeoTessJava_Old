package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.ParameterHelp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dmdaily on 7/29/2014.
 */
public class DeepestShallowestComponents {

    private JComboBox<ModelLayer> deepest;
    private JComboBox<ModelLayer> shallowest;
    private JLabel deepLabel;
    private JLabel shallowLabel;
    private ModelLayer[] layers;

    public DeepestShallowestComponents(GeoTessModel model) {
        this.layers = makeLayerList(model);
        this.deepLabel = new PopupLabel("Deepest Layer: ", ParameterHelp.DEEPEST_LAYER);
        this.shallowLabel = new PopupLabel("Shallowest Layer: ", ParameterHelp.SHALLOWEST_LAYER);
        this.deepest = makeDeepest();
        this.shallowest = new JComboBox<>();
        updateShallowestBox();
    }

    private ModelLayer[] makeLayerList(GeoTessModel model)
    {
        List<ModelLayer> modelLayers = new ArrayList<>();
        for(String s : model.getMetaData().getLayerNames())
        {
            ModelLayer l = new ModelLayer(model.getMetaData().getLayerIndex(s), s);
            modelLayers.add(l);
        }
        Collections.reverse(modelLayers);
        return modelLayers.toArray(new ModelLayer[modelLayers.size()]);
    }

    private JComboBox<ModelLayer> makeDeepest()
    {
        JComboBox<ModelLayer> combo = new JComboBox<>(layers);
        combo.setSelectedIndex(layers.length - 1);
        combo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                updateShallowestBox();
            }
        });
        return combo;
    }

    private void updateShallowestBox()
    {
        int deepestIndex = getDeepestIndex();
        shallowest.removeAllItems();
        for(ModelLayer ml : layers)
        {
            if(ml.getIndex() > deepestIndex)
                shallowest.addItem(ml);
        }
    }

    public int getDeepestIndex()
    {
        return ((ModelLayer) deepest.getSelectedItem()).getIndex();
    }

    public int getShallowestIndex()
    {
        return ((ModelLayer) shallowest.getSelectedItem()).getIndex();
    }

    public JLabel getDeepLabel()
    {
        return deepLabel;
    }

    public JLabel getShallowLabel()
    {
        return shallowLabel;
    }

    public JComboBox<ModelLayer> getDeepBox()
    {
        return deepest;
    }

    public JComboBox<ModelLayer> getShallowBox()
    {
        return shallowest;
    }
}
