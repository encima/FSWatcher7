package com.encima;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Vector;

public class FSWatcher implements Runnable{
	private Path path;
	private WatchKey wk;
	Vector<File> created;
	
	public FSWatcher(Path path) {
		this.path = path;
		created = new Vector<File>();
	}
//	Events fired are dealt with by this method, passing the event
	public void handleEvent(WatchEvent<?> ev) {
		Path evPath = (Path) ev.context();
		if(ev.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
//			For some strange reason, this is the recommended way to get the absolute path, using the normal way appends the file to the code directory. Weird.
			Path dir = (Path)wk.watchable();
			System.out.println("CREATED: " + dir.resolve(evPath) + " Added to vector, will run when three are loaded. " + created.size());
			created.add(dir.resolve(evPath).toFile());
			if(created.size() == 3) {
				try {
					Process p = Runtime.getRuntime().exec("/Users/encima/Dropbox/PhD/Projects/Opencv/Opencv/imset /Users/encima/Pictures/DG Corridor 1112/DG Corridor 2011-2012 3");
					BufferedReader in = new BufferedReader(  
                            new InputStreamReader(p.getInputStream()));  
					String line = null;  
        			while ((line = in.readLine()) != null) {  
        				System.out.println(line);  
        			}  
					created.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(ev.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
			System.out.println("DELETED: " + evPath.toFile().getAbsolutePath());
		}else if(ev.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
			System.out.println("MODIFIED: " + evPath.toFile().getAbsolutePath());
		}
	}

	@Override
	public void run() {
		WatchService ws;
		try {
			ws = path.getFileSystem().newWatchService();
//			Register for which services to watch
			path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, 
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
			
			while(true) {
				wk = ws.take();
				
				for(final WatchEvent<?> ev : wk.pollEvents()) {
					handleEvent(ev);
				}
				
				if (!wk.reset()) {
		            System.out.println("No longer valid");
		            wk.cancel();
		            ws.close();
	            	break;
		         }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
}
