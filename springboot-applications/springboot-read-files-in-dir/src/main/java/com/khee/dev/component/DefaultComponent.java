package com.khee.dev.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import com.khee.dev.model.DataDto;
import com.opencsv.CSVWriter;

@Component
public class DefaultComponent implements CommandLineRunner {
	private static List<File> fileList = new ArrayList<File>();

	@Override
	public void run(String... args) throws Exception {
		HashMap<String, Integer> uuidHashmap = new HashMap<>();
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
					String key = dto.getUuid();

					if (uuidHashmap.containsKey(key)) {
						uuidHashmap.put(key, uuidHashmap.get(key) + 1);
					} else {
						uuidHashmap.put(key, 1);
					}
				}
			}
		} else {
			System.out.println("No file available to be process");
		}

		System.out.println("Total data to process : " + dataCount);

		writeDataToCsv(uuidHashmap);
		writeDataToCsv(uuidHashmap, dataCount);
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

		return content;
	}

	private void writeDataToCsv(HashMap<String, Integer> uuidHashmap) {
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

			data.add(new String[] { "UUID, COUNT" });

			for (Map.Entry me : uuidHashmap.entrySet()) {
				data.add(new String[] { me.getKey() + ", " + me.getValue() });
			}

			writer.writeAll(data);

			// closing writer connection
			writer.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void writeDataToCsv(HashMap<String, Integer> uuidHashmap, Integer dataCount) {
		int rowIndex = 1;
		String[][] csvMatrix = new String[dataCount + 1][4];
		csvMatrix[0][0] = "UUID";
		csvMatrix[0][1] = "COUNT";

		for (Map.Entry me : uuidHashmap.entrySet()) {
			csvMatrix[rowIndex][0] = (String) me.getKey();
			csvMatrix[rowIndex][1] = String.valueOf(me.getValue());
			rowIndex++;
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
