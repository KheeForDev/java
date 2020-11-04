package com.khee.dev.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khee.dev.model.CsvDataDto;
import com.khee.dev.model.DataDto;
import com.khee.dev.model.Pixel;
import com.opencsv.CSVWriter;

@Component
public class DefaultComponent implements CommandLineRunner {
	private static List<File> fileList = new ArrayList<File>();

	@Override
	public void run(String... args) throws Exception {
		HashMap<Integer, List<CsvDataDto>> dmpDataHashmap = new HashMap<>();
		int dataCount = 0;

		listFiles("src/main/resources/input_raw/");
		System.out.println("Total file to process : " + fileList.size());

		if (fileList.size() > 0) {
			for (File file : fileList) {
				String content = readFileContent(file);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<DataDto> dataDtoList = mapper.readValue(content, new TypeReference<List<DataDto>>() {
				});

				System.out.println(file.getName() + " : " + dataDtoList.size());
				dataCount += dataDtoList.size();

				for (DataDto dto : dataDtoList) {
					for (Pixel pixel : dto.getPixels()) {
						int key = pixel.getCampaignId();

						CsvDataDto csvDataDto = new CsvDataDto();
						csvDataDto.setCampaignId(pixel.getCampaignId());
						csvDataDto.setPartnerUuid(pixel.getPartnerUuid());
						csvDataDto.setBkUuid(pixel.getBkUuid());
						csvDataDto.setDeliveryTime(UtcToTimestampWithTimezone(dto.getDeliveryTime()));

						if (dmpDataHashmap.containsKey(key)) {
							List<CsvDataDto> csvDataDtoList = dmpDataHashmap.get(key);
							csvDataDtoList.add(csvDataDto);
							dmpDataHashmap.put(key, csvDataDtoList);
						} else {
							List<CsvDataDto> csvDataDtoList = new ArrayList<>();
							csvDataDtoList.add(csvDataDto);
							dmpDataHashmap.put(key, csvDataDtoList);
						}

					}
				}
			}
		} else {
			System.out.println("No file available to be process");
		}

		System.out.println("Total data to process : " + dataCount);

		writeDataToCsv(dmpDataHashmap);
		writeDataToCsv(dmpDataHashmap, dataCount);
		System.exit(0);
	}

	private void listFiles(String path) {
		File folder = new File(path);

		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				fileList.add(file);
			} else if (file.isDirectory()) {
				listFiles(file.getAbsolutePath());
			}
		}
	}

	private String readFileContent(File file) {
		String content = null;

		try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				content = sCurrentLine;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		content = content.replace("}{", "},{");
		content = "[" + content + "]";

		return content;
	}

	private String UtcToTimestampWithTimezone(String timestamp) {
		if (timestamp != null) {
			Date date = null;
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(timestamp);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(date);
		} else {
			return "";
		}
	}

	private void writeDataToCsv(HashMap<Integer, List<CsvDataDto>> dmpDataHashmap) {
		// first create file object for file placed at location
		// specified by filepath
		File file = new File("dmp_flat_data_using_opencsv.csv");

		try {
			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// create a List which contains String array
			List<String[]> data = new ArrayList<String[]>();

			data.add(new String[] { "COL_1, COL_2, COL_3, COL_4" });

			for (Map.Entry me : dmpDataHashmap.entrySet()) {
				List<CsvDataDto> csvDataDtoList = (List<CsvDataDto>) me.getValue();

				if (csvDataDtoList.size() > 0) {
					for (CsvDataDto csvDataDto : csvDataDtoList)
						data.add(new String[] { csvDataDto.getCampaignId() + ", " + csvDataDto.getPartnerUuid() + ", "
								+ csvDataDto.getBkUuid() + ", " + csvDataDto.getDeliveryTime() });
				}
			}
			writer.writeAll(data);

			// closing writer connection
			writer.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void writeDataToCsv(HashMap<Integer, List<CsvDataDto>> dmpDataHashmap, Integer dataCount) {
		int rowIndex = 1;
		String[][] csvMatrix = new String[dataCount + 1][4];
		csvMatrix[0][0] = "COL_1";
		csvMatrix[0][1] = "COL_2";
		csvMatrix[0][2] = "COL_3";
		csvMatrix[0][3] = "COL_4";

		for (Map.Entry me : dmpDataHashmap.entrySet()) {
			List<CsvDataDto> csvDataDtoList = (List<CsvDataDto>) me.getValue();

			if (csvDataDtoList.size() > 0) {
				for (CsvDataDto csvDataDto : csvDataDtoList) {
					csvMatrix[rowIndex][0] = String.valueOf(csvDataDto.getCampaignId());
					csvMatrix[rowIndex][1] = csvDataDto.getPartnerUuid();
					csvMatrix[rowIndex][2] = csvDataDto.getBkUuid();
					csvMatrix[rowIndex][3] = csvDataDto.getDeliveryTime();
					rowIndex++;
				}
			}
		}

		ICsvListWriter csvWriter = null;
		try {
			csvWriter = new CsvListWriter(new FileWriter("dmp_flat_data_using_supercsv.csv"),
					CsvPreference.STANDARD_PREFERENCE);

			for (int i = 0; i < csvMatrix.length; i++) {
				csvWriter.write(csvMatrix[i]);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				csvWriter.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
