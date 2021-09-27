package cs523.utils;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import cs523.streaming.kafka.MyKafkaProducer;


public class DataListener {
 
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
 
    /**
     * Creates a WatchService and registers the given directory
     */
    DataListener(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
 
        walkAndRegisterDirectories(dir);
    }
 
    /**
     * Register the given directory with the WatchService; This function will be called by FileVisitor
     */
    private void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     */
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
 
    /**
     * Process all events for keys queued to the watcher
     * @throws URISyntaxException 
     */
    void processEvents() throws URISyntaxException {
        for (;;) {
 
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
 
            boolean flag = false;
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();
 
                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
 
                // ignore modify & delete event
                if ((kind == ENTRY_MODIFY && event.count() > 1) && kind == ENTRY_DELETE)
                	continue;
                
                if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
                    System.out.format("%s: %s\n", event.kind().name(), child);
                	producer(child.toString());
                }
 
                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                        }
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }                    
                }
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
    
    private void producer(String filePath) throws URISyntaxException {        
		MyKafkaProducer kafkaProducer = new MyKafkaProducer();
	    kafkaProducer.PublishMessages(filePath);
	    System.out.println("Producing job completed");
  	}   
 
    public static void main(String[] args) throws IOException, URISyntaxException {
        Path dir = Paths.get(System.getProperty("user.dir") + "/input");
        new DataListener(dir).processEvents();
    }
}