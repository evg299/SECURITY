package ru.evg299.example.xmlsearch;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class FileIO {

	public static String readFile(String filePath, String encoding) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(filePath), encoding);
		StringBuffer sb = new StringBuffer();
		int data = reader.read();
		while (data != -1) {
			char dataChar = (char) data;
			sb.append(dataChar);
			data = reader.read();
		}
		reader.close();

		return sb.toString().trim();
	}

	public static void writeFile(String filePath, String encoding, String content) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
		out.write(content);
		out.close();
	}
}
