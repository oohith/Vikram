package com.vikram.nwpexample;

import com.esri.arcgis.geoprocessing.tools.conversiontools.RasterToPoint;
import com.esri.arcgis.geoprocessing.tools.spatialanalysttools.ExtractByRectangle;
import com.esri.arcgis.geoprocessing.tools.spatialanalysttools.ExtractMultiValuesToPoints;

public class NWP_prediction {
	public void NWP_prediction_work(String png_file_location) {
		String inputRaster=png_file_location;
		String Rectangle_Boundaries="485373.0 917035.0 764930.0 1414399.0";
		String output_Raster="C:/Users/oohith/Documents/ArcGIS/Default.gdb/ExtractedNWP";
	
		try {
			ExtractByRectangle extr=new ExtractByRectangle(inputRaster, Rectangle_Boundaries, output_Raster);
			extr.setExtractionArea("INSIDE");
			String OutPointFeatures = "C:/Users/oohith/Documents/ArcGIS/Default.gdb/ExtractedR2P";
			RasterToPoint rp=new RasterToPoint(extr.getOutRaster(),OutPointFeatures );
			ExtractMultiValuesToPoints extractM2p=new ExtractMultiValuesToPoints(rp.getOutPointFeatures(), output_Raster);
			System.out.println("Extracted M2p "+extractM2p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	System.out.println("ended");
	}
		
}
