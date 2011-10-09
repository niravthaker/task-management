package name.nirav.tasks.core.dao.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import name.nirav.tasks.core.eventsourcing.Event;
import name.nirav.tasks.core.eventsourcing.EventListener;

public class TransactionLog<E extends Event<? extends Serializable, ?>> implements EventListener {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private final BlockingQueue<E> queue = new ArrayBlockingQueue<E>(1000, true/*fair FIFO*/);
	private final ExecutorService pool = Executors.newSingleThreadExecutor();
	
	private final class TxLogWriter implements Runnable{
		private final BlockingQueue<E> queue;
		private final BufferedWriter logFileWriter;

		TxLogWriter(BlockingQueue<E> queue, BufferedWriter logFileWriter) {
			this.queue = queue;
			this.logFileWriter = logFileWriter;
		}

		public void run() {
			while(true){
				try {
					E nextEvent = queue.take();
					logFileWriter.append(nextEvent.serialize());
					logFileWriter.newLine();
				} catch (InterruptedException e) {
					Thread.interrupted();
					logger.log(Level.SEVERE, "Interrupted while writing log record", e);
				} catch (IOException e) {
					logger.log(Level.SEVERE, "IO exception writing log record", e);
				}
			}
		}
	};
	
	public TransactionLog(File logFile) {
		pool.execute(new TxLogWriter(queue, initLogWriter(logFile)));
	}

	private BufferedWriter initLogWriter(File logFile) {
		try{
			if(!logFile.createNewFile())
				if(!logFile.canRead() && !logFile.canWrite())
					throw new RuntimeException(String.format("Unable to create/read/write file: %s", logFile));
			return new BufferedWriter(new FileWriter(logFile));
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void handle(Event<? extends Serializable, ?> event) {
		try {
			queue.put((E) event);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Interrupted while putting event in queue", e);
		}
	}
}
