package com.bcbst.benefitchange.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bcbst.applogger.core.ApplicationSettings;
import com.bcbst.benefitchange.util.InvalidFileException;

public class PDFFileValidator {
	private static List<String> basePaths = new ArrayList<>();
	private static List<String> fileNamePrefixes = new ArrayList<>();
	private static List<String> fileExtensions = new ArrayList<>();
	
	private static final Logger logger = LogManager.getLogger(PDFFileValidator.class);

	public static boolean isValidFile(File file) throws InvalidFileException
	{
		if(basePaths.isEmpty())
		{
			String wasPDFOnDemandLoc = ApplicationSettings.getSettingValue(PDFGenerator.WASPDF_ON_DEMAND_LOC).trim();
			String wasPDFTransformLoc = ApplicationSettings.getSettingValue(PDFGenerator.WASPDF_TRANSFORM_LOC).trim();
			String onDemandDirectory = ApplicationSettings.getSettingValue(PDFGenerator.ON_DEMAND_LOC).trim();
			basePaths.add(wasPDFOnDemandLoc);
			basePaths.add(wasPDFTransformLoc);
			basePaths.add(onDemandDirectory);
		}
		
		if(fileNamePrefixes.isEmpty())
		{
			String onDemandFileNamePrefix = ApplicationSettings.getSettingValue(PDFGenerator.ON_DEMAND_FILE_NAME_PREFIX).trim();
			fileNamePrefixes.add(PDFGenerator.BNFTCHGFORM);
			fileNamePrefixes.add(onDemandFileNamePrefix);
		}

		if(fileExtensions.isEmpty())
		{
			fileExtensions.add("pdf");
			fileExtensions.add("OUT");
			fileExtensions.add("IND");
			fileExtensions.add("ARD");
		}
		
		return isValidFile(file, basePaths, fileNamePrefixes, fileExtensions);
	}
	
	public static boolean isValidFile(File file, String basePath, String fileNameContains, String fileExtension) throws InvalidFileException
	{
		List<String> basePathList = new ArrayList<>();
		basePathList.add(basePath);
		List<String> filePrefix = new ArrayList<>();
		filePrefix.add(fileNameContains);
		List<String> fileExtensions = new ArrayList<>();
		fileExtensions.add(fileExtension);
		return isValidFile(file, basePathList, filePrefix, fileExtensions);
	}
	
	public static boolean isValidFile(File file, List<String> basePaths, List<String> fileNamePrefixes, List<String> fileExtensions) throws InvalidFileException
	{
		String filePath;
		
		try {
			filePath = file.getCanonicalPath();
		if(filePath  == null || filePath.isEmpty())
			return false;
			boolean hasValidBasePath = basePaths.stream().filter(path -> filePath.startsWith(path)).collect(Collectors.toList()).size() > 0;
			if(!hasValidBasePath)
				throw new InvalidFileException("Invalid path for file " + filePath);
			boolean validFileName = fileNamePrefixes.stream().filter(prefix -> filePath.contains(prefix)).collect(Collectors.toList()).size() > 0;
			if(!validFileName)
				throw new InvalidFileException("File doesn't have correct name " + filePath);
			String requestedFileExt = FilenameUtils.getExtension(filePath);
			List<String> validExtensions = fileExtensions.stream().filter(fileExt -> requestedFileExt.equalsIgnoreCase(fileExt) || requestedFileExt.equalsIgnoreCase("." + fileExt)).collect(Collectors.toList());
			logger.debug("Matched Extensions: " + validExtensions.size());
			if(validExtensions.isEmpty())
			{
				logger.warn("Invalid Extension:{}-{}", StringEscapeUtils.escapeJava(requestedFileExt), StringEscapeUtils.escapeJava(filePath));
				throw new InvalidFileException("Invalid extension " + requestedFileExt);
			}
		} catch (IOException e) {
			throw new InvalidFileException(e.getMessage());
		}
		return true;
	}
}
