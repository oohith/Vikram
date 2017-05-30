package com.vikram.nwpexample;

import java.io.IOException;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.carto.MapServer;

public class EngineHelloWorld
{
    public static void main(String[] args)
    {
        try
        {
            //Step 1: Initialize the Java Componet Object Model (COM) Interop.
            EngineInitializer.initializeEngine();

            //Step 2: Initialize a valid license.
            new AoInitialize().initialize
                (esriLicenseProductCode.esriLicenseProductCodeEngine);

            //Step 3 : Invoke ArcObjects.
            MapServer mapServer = new MapServer();
            String ArcGISInstallDir = System.getenv("ARCGISHOME");
            mapServer.connect(ArcGISInstallDir + 
                "\\java\\samples\\data\\mxds\\brazil.mxd");
            String name = mapServer.getDefaultMapName();
            System.out.println("Hello, ArcObjects!: " + name);

        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            System.out.println("App failed.");
        }
        finally
        {
            try
            {
                //Step 4: Release the license.
                new AoInitialize().shutdown();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    } //End of method main.
} //End of class:EngineHelloWorld.