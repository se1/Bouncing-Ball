
package com.djoker.DEngine;


import javax.imageio.ImageIO;

import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class ResourceLoader
{
    private static final String DEFAULT_LOCALE = "pt_BR";

    private static URL home;
    private static HashMap<URL, Image> imageCache = new HashMap<URL, Image>();

    private static ClassLoader classLoader;
    private static List<URL> locations = new ArrayList<URL>();

    /**
     *
     */
    static
    {
        try
        {
            String self = ResourceLoader.class.getResource( "ResourceLoader.class" ).toString();

//            String pathProp = System.getProperty( "game.home" );
//
//            if ( pathProp != null && pathProp.trim().length() > 0 )
//            {
//                if ( pathProp.startsWith( "http:" ) )
//                {
//                    if ( !pathProp.endsWith( "/" ) )
//                    {
//                        pathProp += "/";
//                    }
//
//                    home = new URL( pathProp );
//                }
//
//                else
//                {
//                    if ( !pathProp.endsWith( File.separator ) )
//                    {
//                        pathProp += File.separator;
//                    }
//
//                    home = new URL( "file:" + pathProp );
//                }
//            }

            if ( self.startsWith( "jar:" ) )
            {
                home = new URL( self.substring( 0, self.lastIndexOf( "!/" ) + 2 ) );
            }

         }

        catch ( Exception e )
        {
  
        }
    }

    public static URL getHome()
    {
        return home;
    }

    public static void setHome( String path ) throws Exception
    {
        home = new URL( path );
    }

    /**
     * getClassLoader
     *
     * @return ClassLoader
     */
    public static ClassLoader getClassLoader()
    {
        return classLoader != null ? classLoader : ResourceLoader.class.getClassLoader();
    }

    /**
     * adds an URL as root for resource and class localization
     *
     * @param url URL
     */
    public static void addLocation( URL url )
    {
        if ( !locations.contains( url ) )
        {
            ClassLoader parent = classLoader;

            if ( parent == null )
            {
                parent = ResourceLoader.class.getClassLoader();
            }

            classLoader = new URLClassLoader( new URL[] { url }, parent );
        }
    }

    /**
     * @param name String
     * @return Class
     * @throws ClassNotFoundException
     */
    public static Class<? extends Object> classForName( String name ) throws ClassNotFoundException
    {
        return classLoader != null ? classLoader.loadClass( name ) : Class.forName( name );
    }

    /**
     * Only get <code>File</code> from outside <em>jar<em>.
     *
     * @param name String
     * @return File
     */
    public static File getFile( String name )
    {
        URL url = null;

        File f = null;

        try
        {
            url = getURL( name );

            if ( url != null )
            {
                // is much easier than rolling your own remove the %20 (where was white spaces) solution
                String fileName = URLDecoder.decode( url.getFile(), "UTF-8" );

                f = new File( fileName );
            }
        }

        catch ( Exception e )
        {

        }

        return f;
    }

    /**
     * @param name
     * @return
     */
    public static InputStream getStream( String name )
    {
        URL url = null;

        InputStream stream = null;

        name = checkSlash( name );

        try
        {
            url = getURL( name );

            if ( url != null )
            {
                stream = url.openStream(); //ResourceLoader.class.getResourceAsStream( name );

             }
        }

        catch ( Exception e )
        {
   
        }

        return stream;
    }

    /**
     * Return a valid URL, concatenating the home property with the path passes as parameter. If the
     * URL doesn't exists, then return null.
     *
     * @param path the relative path to a resource.
     * @return a valid URL or <code>null</code>.
     */
    public static URL getURL( String path )
    {
        URL url = null;

        try
        {
            if ( path.startsWith( "file:" ) )
            {
                return new URI( path ).toURL();
            }

            // prevent desnecessary HTTP request jar over http
            if ( path.startsWith( "jar:http:" ) || path.startsWith( "jar:https:" ) )
            {
                path = path.substring( path.lastIndexOf( "!/" ) + 1 );
            }

            // 1º tentative - try to obtain icon from local classpath
            if ( classLoader != null )
            {
                url = classLoader.getResource( path );
            }

            else
            {
                url = ResourceLoader.class.getResource( path );
            }

            if ( url == null )
            {
                // 2º tentative - try to obtain icon from global classpath
                if ( !path.startsWith( "/" ) )
                {
                    url = Class.class.getResource( "/" + path );
                }

                if ( url == null )
                {
                    // 3º tentative, load from absolute file in disk
                    File file = new File( path );
                    url = file.toURI().toURL();

                    if ( url == null )
                    {
                        // 4º tentative - try to obtain url from file system
                        if ( home != null )
                        {
                            url = new URL( home, path );
                        }
                    }
                }
            }
        }

        catch ( Exception e )
        {

        }

        return url;
    }

    /**
     * @param path String
     * @return URL
     */
    public static URL getExternURL( String path )
    {
        URL url = null;

        try
        {
            if ( path.startsWith( "http:" ) )
            {
                url = new URL( path );
            }

            // resolve relative folder path
            else
            {
                url = ResourceLoader.getURL( path );

                if ( url.getProtocol().equals( "jar" ) )
                {
                    String homeDirectory = System.getProperty( "game.home" );

                    if ( homeDirectory.startsWith( "http:" ) )
                    {
                        if ( !homeDirectory.endsWith( "/" ) )
                        {
                            homeDirectory += "/";
                        }

                        url = new URL( homeDirectory + path );
                    }

                    // assume local installation
                    else
                    {
                        File file = new File( homeDirectory, path );

                        url = file.toURI().toURL();
                    }
                }
            }
        }

        catch ( Exception e )
        {

        }

        return url;
    }

    /**
     * Get a Properties archive from a URL.
     *
     * @param name
     * @return
     */
    public static Properties getProperties( String name )
    {
        Properties props = new Properties();

        try
        {
            props.load( getURL( name ).openStream() );
        }

        catch ( Exception e )
        {

        }

        return props;
    }

    /**
     * @param name
     * @return
     */
    private static String checkSlash( String name )
    {
        if ( name.trim().charAt( 0 ) != '/' &&
             name.indexOf( '/' ) != -1 &&
             name.indexOf( "file:" ) == -1 &&
             name.indexOf( "url:" ) == -1 &&
             name.indexOf( "http:" ) == -1 )
       {
           name = "/" + name;
       }

        return name;
    }

    /**
     * getImage
     * 
     * @param name
     * @return Image
     */
    public static Image getImage( String name )
    {
        name = checkSlash( name );

        return getImageFromURL( name );

//        if ( name.startsWith( "/" ) || name.startsWith( "http:" ) || name.startsWith( "https:" )
//                || name.startsWith( "file:" ) )
//        {
//            return getImageFromURL( name );
//        }
//
//        else
//        {
//            return getImageFromURL( /*home +*/ name );
//        }
    }

    /**
     * 
     * @param name
     * @return
     */
    public static Image getLocalizedImage( String name )
    {
        return getLocalizedImage( name, System.getProperty( "game.localized", DEFAULT_LOCALE ) );
    }

    /**
     * 
     * @param name
     * @param flavour
     * @return
     */
    public static Image getLocalizedImage( String name, String flavour )
    {
        String path = "i18n/" + flavour + "/images/" + name;

        Image image = getImageFromURL( path );

        if ( image == null )
        {
            image = getImage( name );
            
          }

        return image;
    }

    /**
     *
     */
    public static void clearCache()
    {
        imageCache.clear();
    }

    /**
     * Return the number of <code>Image</code>s stored in cache.
     *
     * @return the number of <code>Image</code>s stored in cache.
     */
    public static int getCacheSize()
    {
        return imageCache.size();
    }

    /**
     * getImageFromURL
     * 
     * @param String path
     * @return Image
     */
    private static Image getImageFromURL( String path )
    {
        Image image = null;

        try
        {
            URL url = getURL( path );

            if ( url != null )
            {
                image = imageCache.get( url );

                if ( image == null )
                {
                    image = ImageIO.read( url );

                    if ( image != null )
                    {
                        // if ( image.getImageLoadStatus() == MediaTracker.COMPLETE )
                        {
                            imageCache.put( url, image );
                        }

                     }

                    else
                    {
                          image = null;
                    }
                }

//                else
//                {
//                    Log.log( ResourceLoader.class, "Image cache hit for '" + url.getFile() + "'", Log.LEVEL_LOW_PRIORITY );
//                }
            }
        }

        catch ( Exception e )
        {
         }

        return image;
    }
}