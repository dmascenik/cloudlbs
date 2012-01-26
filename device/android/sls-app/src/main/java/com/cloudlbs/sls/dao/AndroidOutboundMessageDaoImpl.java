package com.cloudlbs.sls.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;

import com.google.protobuf.Message;

/**
 * Uses the Android API for reading/writing a file that is private to an app.<br/>
 * <br/>
 * The data file is arranged as alternating fields as follows:<br/>
 * <br/>
 * <code>len0|classname|len1|protobufbytes| etc...</code><br/>
 * <br/>
 * such that the <code>len0</code> is an <code><b>int</b></code> indicating the
 * length of the <code>byte[]</code> to use to extract the subsequent class name
 * string, and <code>len1</code> is an <code><b>int</b></code> indicating the
 * length of the <code>byte[]</code> to use to extract the subsequent Protobuf
 * bytes.
 * 
 * @author Dan Mascenik
 * 
 */
public class AndroidOutboundMessageDaoImpl implements OutboundMessageDao {

	private static final String DATA_FILE = "OutboundMessageData.dat";

	private Context context;

	public AndroidOutboundMessageDaoImpl(Context context) {
		this.context = context;
	}

	@Override
	public void saveOutboundMessageQueue(ConcurrentLinkedQueue<Message> queue)
			throws IOException {
		if (queue == null) {
			// nothing to do
			return;
		}

		FileOutputStream os = context.openFileOutput(DATA_FILE,
				Context.MODE_PRIVATE);
		DataOutputStream dos = new DataOutputStream(os);
		Message[] messages = getArray(queue);

		try {
			for (Message msg : messages) {
				String className = msg.getClass().getName();
				int len0 = className.length();
				byte[] bytes = msg.toByteArray();
				int len1 = bytes.length;
				dos.writeInt(len0);
				dos.writeBytes(className);
				dos.writeInt(len1);
				dos.write(bytes);
			}
		} finally {
			dos.close();
		}
	}

	@Override
	public ConcurrentLinkedQueue<Message> loadOutboundMessageQueue()
			throws IOException {

		ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();

		FileInputStream is;
		try {
			is = context.openFileInput(DATA_FILE);
		} catch (FileNotFoundException e) {
			// no data to load, return the empty queue
			return queue;
		}

		DataInputStream dis = new DataInputStream(is);
		try {
			while (true) {
				int len0 = dis.readInt();
				byte[] bytes = new byte[len0];
				int read = dis.read(bytes);
				if (read != len0) {
					throw new IOException("Unexpected EOF");
				}
				String className = new String(bytes);
				int len1 = dis.readInt();
				bytes = new byte[len1];
				read = dis.read(bytes);
				if (read != len1) {
					throw new IOException("Unexpected EOF");
				}
				try {
					Class<?> msgClz = Class.forName(className);
					Method parseFrom = msgClz.getMethod("parseFrom",
							byte[].class);
					Message msg = (Message) parseFrom.invoke(null, bytes);
					queue.add(msg);
				} catch (Exception e) {
					throw new IOException("Data corrupt");
				}
			}
		} catch (EOFException eof) {
			// Reached the end of the file
		} finally {
			dis.close();
		}
		return queue;
	}

	Message[] getArray(Queue<Message> queue) {
		return queue.toArray(new Message[0]);
	}
}
