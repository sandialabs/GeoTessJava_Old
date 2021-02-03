//- ****************************************************************************
//-
//- Copyright 2009 Sandia Corporation. Under the terms of Contract
//- DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
//- retains certain rights in this software.
//-
//- BSD Open Source License.
//- All rights reserved.
//-
//- Redistribution and use in source and binary forms, with or without
//- modification, are permitted provided that the following conditions are met:
//-
//-    * Redistributions of source code must retain the above copyright notice,
//-      this list of conditions and the following disclaimer.
//-    * Redistributions in binary form must reproduce the above copyright
//-      notice, this list of conditions and the following disclaimer in the
//-      documentation and/or other materials provided with the distribution.
//-    * Neither the name of Sandia National Laboratories nor the names of its
//-      contributors may be used to endorse or promote products derived from
//-      this software without specific prior written permission.
//-
//- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//- AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//- IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//- ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
//- LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//- CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//- INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//- ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//- POSSIBILITY OF SUCH DAMAGE.
//-
//- ****************************************************************************

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
