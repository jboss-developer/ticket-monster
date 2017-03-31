package org.jboss.examples.ticketmonster.service;

import static org.jboss.examples.ticketmonster.model.MediaType.IMAGE;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.jboss.examples.ticketmonster.model.MediaItem;
import org.jboss.examples.ticketmonster.model.MediaType;
import org.jboss.examples.ticketmonster.util.Reflections;

/**
 * <p>
 * The media manager is responsible for taking a media item, and returning either the URL of the cached version (if the
 * application cannot load the item from the URL), or the original URL.
 * </p>
 * 
 * <p>
 * The media manager also transparently caches the media items on first load.
 * </p>
 * 
 * <p>
 * The computed URLs are cached for the duration of a request. This provides a good balance between consuming heap space, and
 * computational time.
 * </p>
 * 
 * @author Pete Muir
 * 
 */
@RequestScoped
public class MediaManager {

    /**
     * Locate the tmp directory for the machine
     */
    private static final File tmpDir;

    static {
        String dataDir = System.getenv("OPENSHIFT_DATA_DIR");
        String parentDir = dataDir != null ? dataDir : System.getProperty("java.io.tmpdir");
        tmpDir = new File(parentDir, "org.jboss.jdf.examples.ticket-monster");
        if (tmpDir.exists()) {
            if (tmpDir.isFile())
                throw new IllegalStateException(tmpDir.getAbsolutePath() + " already exists, and is a file. Remove it.");
        } else {
            tmpDir.mkdir();
        }
    }

    /**
     * A request scoped cache of computed URLs of media items.
     */
    private final Map<MediaItem, MediaPath> cache;

    public MediaManager() {

        this.cache = new HashMap<MediaItem, MediaPath>();
    }

    /**
     * Load a cached file by name
     * 
     * @param fileName
     * @return
     */
    public File getCachedFile(String fileName) {
        return new File(tmpDir, fileName);
    }

    /**
     * Obtain the URL of the media item. If the URL h has already been computed in this request, it will be looked up in the
     * request scoped cache, otherwise it will be computed, and placed in the request scoped cache.
     */
    public MediaPath getPath(MediaItem mediaItem) {
        if (cache.containsKey(mediaItem)) {
            return cache.get(mediaItem);
        } else {
            MediaPath mediaPath = createPath(mediaItem);
            cache.put(mediaItem, mediaPath);
            return mediaPath;
        }
    }

    /**
     * Compute the URL to a media item. If the media item is not cacheable, then, as long as the resource can be loaded, the
     * original URL is returned. If the resource is not available, then a placeholder image replaces it. If the media item is
     * cachable, it is first cached in the tmp directory, and then path to load it is returned.
     */
    private MediaPath createPath(MediaItem mediaItem) {
    	if(mediaItem == null) {
    		return createCachedMedia(Reflections.getResource("not_available.jpg").toExternalForm(), IMAGE);
    	} else if (!mediaItem.getMediaType().isCacheable()) {
            if (checkResourceAvailable(mediaItem)) {
                return new MediaPath(mediaItem.getUrl(), false, mediaItem.getMediaType());
            } else {
                return createCachedMedia(Reflections.getResource("not_available.jpg").toExternalForm(), IMAGE);
            }
        } else {
            return createCachedMedia(mediaItem);
        }
    }

    /**
     * Check if a media item can be loaded from it's URL, using the JDK URLConnection classes.
     */
    private boolean checkResourceAvailable(MediaItem mediaItem) {
        URL url = null;
        try {
            url = new URL(mediaItem.getUrl());
        } catch (MalformedURLException e) {
        }

        if (url != null) {
            try {
                URLConnection connection = url.openConnection();
                if (connection instanceof HttpURLConnection) {
                    return ((HttpURLConnection) connection).getResponseCode() == HttpURLConnection.HTTP_OK;
                } else {
                    return connection.getContentLength() > 0;
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * The cached file name is a base64 encoded version of the URL. This means we don't need to maintain a database of cached
     * files.
     */
    private String getCachedFileName(String url) {
        return Base64.getEncoder().encodeToString(url.getBytes());
    }

    /**
     * Check to see if the file is already cached.
     */
    private boolean alreadyCached(String cachedFileName) {
        File cache = getCachedFile(cachedFileName);
        if (cache.exists()) {
            if (cache.isDirectory()) {
                throw new IllegalStateException(cache.getAbsolutePath() + " already exists, and is a directory. Remove it.");
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * To cache a media item we first load it from the net, then write it to disk.
     */
    private MediaPath createCachedMedia(String url, MediaType mediaType) {
        String cachedFileName = getCachedFileName(url);
        if (!alreadyCached(cachedFileName)) {
            URL _url = null;
            try {
                _url = new URL(url);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Error reading URL " + url);
            }

            try {
                InputStream is = null;
                OutputStream os = null;
                try {
                    is = new BufferedInputStream(_url.openStream());
                    os = new BufferedOutputStream(getCachedOutputStream(cachedFileName));
                    while (true) {
                        int data = is.read();
                        if (data == -1)
                            break;
                        os.write(data);
                    }
                } finally {
                    if (is != null)
                        is.close();
                    if (os != null)
                        os.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Error caching " + mediaType.getDescription(), e);
            }
        }
        return new MediaPath(cachedFileName, true, mediaType);
    }

    private MediaPath createCachedMedia(MediaItem mediaItem) {
        return createCachedMedia(mediaItem.getUrl(), mediaItem.getMediaType());
    }

    private OutputStream getCachedOutputStream(String fileName) {
        try {
            return new FileOutputStream(getCachedFile(fileName));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Error creating cached file", e);
        }
    }

}
