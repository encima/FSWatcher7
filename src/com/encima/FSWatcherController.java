package com.encima;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FSWatcherController {

	public static void main(String[] args) {
		Path watchPath = FileSystems.getDefault().getPath("/Users/encima/Pictures/DG Corridor 1112/DG Corridor 2011-2012");
		FSWatcher fsWatcher = new FSWatcher(watchPath);
		Thread fsThread = new Thread(fsWatcher);
		fsThread.start();
	}
}
