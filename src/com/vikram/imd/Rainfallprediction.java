package com.vikram.imd;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
	public class Rainfallprediction extends RainfallPdf {

		@SuppressWarnings("deprecation")
		public void rainfall(String date) throws IOException, SAXException, TikaException{
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			FileInputStream inputstream = new FileInputStream(config.storage_For_imd()+"/"+date+"/"+date+".pdf");
			PDFParser pdfparser=new PDFParser();
			pdfparser.parse(inputstream, handler, metadata);
			new RainfallData().PdfData(handler.toString());	 
	}
}
