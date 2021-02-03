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

/**
 * Created by dmdaily on 8/1/2014.
 */
public class ModelLayer {

    private int index;
    private String layerName;

    public ModelLayer(int index, String layerName)
    {
        this.index = index;
        this.layerName = layerName;
    }

    /**
     * This method takes the layer name from the model without being formatted.  For example upper_mantle would be
     * the name of a layer. This method capitalizes each word of the layer name and replaces the '_' with ' '
     * so that the name is displayed in a friendlier way.
     *
     * @param name the non formatted layer name
     * @return a readable formatted version of the layer name
     */
    private String makePretty(String name)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(name.charAt(0)));

        //Starts at 1 since the first letter has already been modified
        for(int i = 1; i < name.length(); i++)
        {
            if(name.charAt(i) == '_') {
                sb.append(" ");
                sb.append(Character.toUpperCase(name.charAt(++i)));
            }
            else sb.append(name.charAt(i));
        }
        return sb.toString();
    }

    public int getIndex()
    {
        return index;
    }

    @Override
    public String toString()
    {
        return index + " - " + makePretty(layerName);
    }
}
