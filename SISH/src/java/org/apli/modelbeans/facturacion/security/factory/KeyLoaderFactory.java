package org.apli.modelbeans.facturacion.security.factory;

import org.apli.modelbeans.facturacion.security.factory.KeyLoader;
import org.apli.modelbeans.facturacion.security.KeyLoaderEnumeration;
import org.apli.modelbeans.facturacion.security.PrivateKeyLoader;
import org.apli.modelbeans.facturacion.security.PublicKeyLoader;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Gerardo Aquino
 * Date: 4/06/13
 */
public class KeyLoaderFactory {


    public final static KeyLoader createInstance(KeyLoaderEnumeration keyLoaderEnumeration, String keyLocation, String ... keyPassword) {
        KeyLoader keyLoader = null;

        if(keyLoaderEnumeration == KeyLoaderEnumeration.PRIVATE_KEY_LOADER) {
            keyLoader = new PrivateKeyLoader(keyLocation, keyPassword == null ? null : keyPassword[0]);
        } else if (keyLoaderEnumeration == KeyLoaderEnumeration.PUBLIC_KEY_LOADER){
            keyLoader = new PublicKeyLoader(keyLocation);
        }

        return keyLoader;
    }


    public final static KeyLoader createInstance(KeyLoaderEnumeration keyLoaderEnumeration, InputStream keyInputStream, String ... keyPassword) {
        KeyLoader keyLoader = null;

        if(keyLoaderEnumeration == KeyLoaderEnumeration.PRIVATE_KEY_LOADER) {
            keyLoader = new PrivateKeyLoader(keyInputStream, keyPassword == null ? null : keyPassword[0]);
        } else if (keyLoaderEnumeration == KeyLoaderEnumeration.PUBLIC_KEY_LOADER){
            keyLoader = new PublicKeyLoader(keyInputStream);
        }

        return keyLoader;
    }
}
