package com.vikram.config;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
@Sources({"file:G:/Vikram-Workspace/Vikram/src/com/vikram/config/serverconfig.properties",
			"classpath:Vikram"})
public interface serverconfig extends Config {
	String imd_link();
	String nwp_old_link();
	String nwp_new_link();
	String nwp_images_link();
	String storage_For_imd();
	String storage_For_nwp();
	String storage_For_nwp_od();
	String temp_folder_nwp();
	String temp_folder_imd();
	String jtdsDriver();
	String sqlUsername();
	String sqlPassword();
	String DownloadsFolder();
	String NDVILinkForPresentData();
	String NDVILinkForPreviousData();
	String NDVITextFileLocation();
	String NDVIStorage();
 
}
