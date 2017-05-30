package com.vikram.nwpexample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.esri.arcgis.datasourcesGDB.SdeWorkspaceFactory;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.IFeatureWorkspace;
import com.esri.arcgis.geodatabase.IFields;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.geodatabase.IWorkspaceFactory;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IPropertySet;
import com.esri.arcgis.system.PropertySet;

public class ArcTest1 {

	public static void main(String[] args) {
		Map<String,String> districtsList=new HashMap<String,String>();
		districtsList=getdistrictslist(districtsList);
		Iterator<String> itr=districtsList.keySet().iterator();
		while (itr.hasNext()) {
			String key=itr.next().toString();
		System.out.println(key +"  "+districtsList.get(key) );
		}
	}

	private static Map<String, String> getdistrictslist(Map<String, String> districtsList) {
		try {
			IFeatureWorkspace featureWorkspace=(IFeatureWorkspace) GetWorkspace();
			IFeatureClass featureClass = featureWorkspace.openFeatureClass("IMD_Station");
			IQueryFilter query=new QueryFilter();
			IFeatureCursor featureCursor=featureClass.search(query, true);
			IFields fields=featureCursor.getFields();
			int iIndex = fields.findField("Station_Na");
			IFeature feature=null;
			while ((feature=featureCursor.nextFeature())!=null) {
				 String strTaluk = (feature.getValue(iIndex))+"";
                districtsList.put(strTaluk, "0");
				
			}
			
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return districtsList;
	}
	private static IWorkspace GetWorkspace() throws AutomationException, IOException
    {
        IPropertySet propertySet = new PropertySet();
        propertySet.setProperty("SERVER", "192.168.2.129");
        propertySet.setProperty("INSTANCE", "sde:sqlserver:192.168.2.129");
        propertySet.setProperty("DATABASE","Kerala" );
        propertySet.setProperty("USER", "sde");
        propertySet.setProperty("PASSWORD","Welcome@123" );
        propertySet.setProperty("VERSION","SDE.DEFAULT" );

        IWorkspaceFactory workspaceFactory = new SdeWorkspaceFactory();
        return workspaceFactory.open(propertySet, 0);
    }

}
