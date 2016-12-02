package com.naver.naverspeech.client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioWriterPCM {

	String path;
	String filename;
	FileOutputStream speechFile;
	
	public AudioWriterPCM(String path) {
		this.path = path;
	}

	public void open(String sessionId) {
		File directory = new File(path);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	
		filename = directory + "/" + sessionId + ".pcm";
		try {
			speechFile = new FileOutputStream(new File(filename));
		} catch (FileNotFoundException e) {
			System.err.println("Can't open file : " + filename);
			speechFile = null;
		}
	}

	public void close() {
		if (speechFile == null)
			return;
		
		try {
			speechFile.close();
		} catch (IOException e) {
			System.err.println("Can't close file : " + filename);
		}
	}

	public void write(short[] data) {
		if (speechFile == null)
			return;
		
		ByteBuffer buffer = ByteBuffer.allocate(data.length * 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		for(int i = 0; i < data.length; i++) {
			buffer.putShort(data[i]);
		}
		buffer.flip();
		
		try {
			speechFile.write(buffer.array());
		} catch (IOException e) {
			System.err.println("Can't write file : " + filename);
		}
	}
}
